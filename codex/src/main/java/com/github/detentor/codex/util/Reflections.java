package com.github.detentor.codex.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NoSuchElementException;

import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.FunctionN;
import com.github.detentor.codex.monads.Option;

/**
 * Essa classe provê métodos helper para trabalhar com Reflection.
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public final class Reflections
{
	private Reflections()
	{
		//Previne instanciação
	}
	
	/**
	 * Transforma um método de uma classe em uma seta. <br/>
	 * Funciona tanto para métodos estáticos como métodos de instância. <br/>
	 * O método retornado será o primeiro método que tenha o nome passado como parâmetro.
	 * 
	 * @param fromClass A classe a partir da qual o método será 'promovido'
	 * @param methodName O nome do método a ser transformado em função. É necessário que o método não receba
	 * parâmetros, tenha visibilidade public e um retorno diferente de void.
	 * @return Uma função que representa o método definido pela classe.
	 */
	public static <A, B> Function1<A, B> lift(final Class<A> fromClass, final String methodName)
	{
		final Method theMethod = ensureNotEmpty(getMethodFromName(fromClass, methodName));
		return param -> invokeSafe(param, theMethod, (Object[]) null);
	}
	
	/**
	 * Transforma um método estático de uma classe numa seta.
	 * @param fromClass A classe onde o método estático existe.
	 * @param methodName O nome do método a ser transformado em seta
	 * @return Uma seta que representa o método definido pela classe
	 */
	public static <A, B, C> Function1<B, C> liftStatic(final Class<A> fromClass, final String methodName)
	{
		final Method theMethod = ensureNotEmpty(getMethodFromName(fromClass, methodName));
		return param -> invokeSafe(fromClass, theMethod, param);
	}
	
	/**
	 * Transforma um método de uma classe em uma {@link Function0}, amarrando os valores
	 * passados como parâmetro na chamada do método, de forma que aplicar a função 
	 * seja equivalente a chamar o método com os parâmetros passados.
	 * 
	 * @param fromInstance A instância a partir da qual o método será promovido
	 * @param methodName O nome do método a ser transformado
	 * @param params Os parâmetros a serem "amarrados" ao método. Para métodos que não recebem parâmetro pode-se passar
	 * um array de tamanho 0.
	 * @return Uma {@link Function0} que, ao ser aplicada, seja equivalente à chamada do método com os parâmetros passados
	 */
	public static <B> Function0<B> liftBind(final Object fromInstance, final String methodName, final Object... params)
	{
		final Class<?>[] types = new Class[params.length]; 

		for (int i = 0; i < params.length; i++)
		{
			types[i] = params[i].getClass();
		}
		final Method theMethod = ensureNotEmpty(getMethodFromNameAndType(fromInstance.getClass(), methodName, types));
		return () -> invokeSafe(fromInstance, theMethod, params);
	}

	/**
	 * Transforma um método estático de uma classe numa seta.
	 * @param fromClass A classe onde o método estático existe
	 * @param methodName O nome do método a ser transformado em seta
	 * @return Uma seta que representa o método definido pela classe
	 */
	public static <A, B, C> FunctionN<B, C> liftStaticVarArgs(final Class<A> fromClass, final String methodName)
	{
		final Method theMethod = ensureNotEmpty(
									getMethodFromNameAndType(fromClass, methodName, new Class<?>[]{Object[].class}));
		return params -> invokeSafe(fromClass, theMethod, new Object[] { params });
	}

	/**
	 * Valida que a Option contém elementos, disparando uma exceção se não tiver.
	 * @param theOption A Option a ser verificada
	 * @return O elemento contido na Option.
	 * @throws NoSuchElementException Se a Option não contiver elementos 
	 */
	private static Method ensureNotEmpty(final Option<Method> theOption)
	{
		if (theOption.isEmpty())
		{
			throw new NoSuchElementException("The named method doesn't exist");
		}
		return theOption.get();
	}

	/**
	 * Retorna o primeiro método de uma classe que possui o nome passado como parâmetro. <br/>
	 * @param fromClass A classe onde o método será procurado
	 * @param methodName O nome do método a ser retornado
	 * @return Uma instância de Option que conterá o método, se ele existir
	 */
	public static <A> Option<Method> getMethodFromName(final Class<A> fromClass, final String methodName)
	{
		for (final Method curMethod : fromClass.getDeclaredMethods())
		{
			if (curMethod.getName().equals(methodName))
			{
				return Option.from(curMethod);
			}
		}

		final Class<? super A> superClass = fromClass.getSuperclass();
		return superClass == null ? Option.<Method>empty() : getMethodFromName(superClass, methodName);
	}
	
	/**
	 * Retorna o método de uma classe a partir de seu nome e da sua lista de parâmetros. <br/> 
	 * Passar um array vazio significa sem parâmetros.
	 * @param fromClass A classe onde o método será procurado
	 * @param methodName O nome do método a ser retornado
	 * @param parameterType Um array com as classes que representam a assinatura do método
	 * @return Uma instância de Option que conterá o método, se ele existir
	 */
	public static <A> Option<Method> getMethodFromNameAndType(final Class<A> fromClass, 
															  final String methodName, 
															  final Class<?>[] parameterType)
	{
		for (final Method curMethod : fromClass.getDeclaredMethods())
		{
			if (curMethod.getName().equals(methodName) && Arrays.equals(curMethod.getParameterTypes(), parameterType))
			{
				return Option.from(curMethod);
			}
		}

		final Class<? super A> superClass = fromClass.getSuperclass();
		return superClass == null ? Option.<Method>empty() : getMethodFromNameAndType(superClass, methodName, parameterType);
	}

	/**
	 * Retorna a referência a um campo de uma classe, a partir de seu nome.<br/>
	 * @param <A> O tipo da classe a ser procurado o campo
	 * @param fromClass A classe onde o campo será procurado
	 * @param fieldName O nome do campo a ser procurado
	 * @return Uma option que conterá o campo, se ele existir
	 */
	public static <A> Option<Field> fieldFromName(final Class<A> fromClass, final String fieldName)
	{
		for (final Field curField : fromClass.getDeclaredFields())
		{
			if (curField.getName().equals(fieldName))
			{
				return Option.from(curField);
			}
		}

		final Class<? super A> superClass = fromClass.getSuperclass();
		return superClass == null ? Option.<Field>empty() : fieldFromName(superClass, fieldName);
	}

	/**
	 * Invoca o método de uma classe, pulando as verificações de exceção.
	 * @param fromClass A instância da classe onde o método será chamado
	 * @param method O método a ser chamado
	 * @param params A lista de parâmetros esperada pela classe
	 * @return O valor retornado pelo método
	 */
	@SuppressWarnings("unchecked")
	public static <A, B> B invokeSafe(final Object fromClass, final Method method, final Object ... params)
	{
		try
		{
			return (B) method.invoke(fromClass, params);
		}
		catch (final IllegalAccessException | InvocationTargetException e)
		{
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * Cria, via reflection, uma nova instancia da classe informada. <br/>
	 * A classe deve possuir construtor público que não recebe parâmetros. <br/>
	 * Esse método equivale a chamar {@link #newInstance(Class, Object...)} passando o segundo parâmetro nulo.
	 * 
	 * @param <T> o tipo da instancia desejada
	 * @param type o tipo da instancia desejada
	 * @return Uma nova instância da classe informada
	 * @throws IllegalArgumentException caso aconteça erro ao se instanciar, por exemplo não ter construtor público
	 */
	public static <T> T newInstance(final Class<T> type) throws IllegalArgumentException
	{
		return newInstance(type, (Object[]) null);
	}
	
	/**
	 * Cria, via reflection, uma nova instância da classe informada, passando os parâmetros informados. <br/>
	 * O construtor da classe deve ser público. Se params for nulo ou de tamanho 0, então será invocado
	 * o construtor padrão da classe.
	 * 
	 * @param <T> o tipo da instancia desejada
	 * @param type o tipo da instancia desejada
	 * @param params Os parâmetros a serem passados para o construtor
	 * @return Uma nova instância da classe informada
	 * @throws IllegalArgumentException caso aconteça erro ao se instanciar, por exemplo não ter construtor público
	 */
	public static <T> T newInstance(final Class<T> type, final Object... params) throws IllegalArgumentException
	{
		try
		{
			if (params == null || params.length == 0)
			{
				return type.newInstance();
			}

			final Class<?>[] paramTypes = new Class<?>[params.length];

			for (int i = 0; i < params.length; i++)
			{
				paramTypes[i] = params[i].getClass();
			}
			
			return type.getConstructor(paramTypes).newInstance(params);
		}
		catch (IllegalAccessException | InstantiationException | SecurityException | 
			   InvocationTargetException | NoSuchMethodException e)
		{
			throw new IllegalArgumentException("Erro ao instanciar a classe " + type, e);
		}
	}
}
