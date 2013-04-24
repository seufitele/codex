package com.github.detentor.codex.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import com.github.detentor.codex.function.arrow.Arrow1;
import com.github.detentor.codex.function.arrow.ArrowN;
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
		//Previne instanciaçãp
	}
	
	/**
	 * Transforma um método de uma classe em uma função. <br/>
	 * 
	 * @param fromClass A classe a partir da qual o método será 'promovido'
	 * @param methodName O nome do método a ser transformado em função. É necessário que o método não receba
	 * parâmetros, tenha visibilidade public e um retorno diferente de void.
	 * @return Uma função que representa o método definido pela classe.
	 */
	public static <A, B> Arrow1<A, B> lift(final Class<A> fromClass, final String methodName)
	{
		final Method theMethod = ensureNotEmpty(getMethodFromName(fromClass, methodName));

		return new Arrow1<A, B>()
		{
			@Override
			public B apply(final A param)
			{
				return invokeSafe(param, theMethod, (Object[]) null);
			}
		};
	}

	/**
	 * Transforma um método estático de uma classe numa função.
	 * @param fromClass A classe onde o método estático existe.
	 * @param methodName O nome do método a ser transformado em função
	 * @return Uma função que representa o método definido pela classe
	 */
	public static <A, B, C> Arrow1<B, C> liftStatic(final Class<A> fromClass, final String methodName)
	{
		final Method theMethod = ensureNotEmpty(getMethodFromName(fromClass, methodName));

		return new Arrow1<B, C>()
		{
			@Override
			public C apply(final B param)
			{
				return invokeSafe(fromClass, theMethod, param);
			}
		};
	}
	
	/**
	 * Transforma um método estático de uma classe numa função.
	 * @param fromClass A classe onde o método estático existe
	 * @param methodName O nome do método a ser transformado em função
	 * @return Uma função que representa o método definido pela classe
	 */
	public static <A, B, C> ArrowN<B, C> liftStaticVarArgs(final Class<A> fromClass, final String methodName)
	{
		final Method theMethod = ensureNotEmpty(
									getMethodFromNameAndType(fromClass, methodName, new Class<?>[]{Object[].class}));

		return new ArrowN<B, C>()
		{
			@Override
			public C apply(final B... params)
			{
				return invokeSafe(fromClass, theMethod, new Object[] { params });
			}
		};
	}

	/**
	 * Valida que a Option contém elementos, disparando uma exceção se não tiver.
	 * @param theOption A Option a ser verificada
	 * @return O elemento contido na Option.
	 * @throws IllegalArgumentException Se a Option não contiver elementos 
	 */
	private static Method ensureNotEmpty(final Option<Method> theOption)
	{
		if (theOption.isEmpty())
		{
			throw new IllegalArgumentException("The named method doesn't exist");
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
	 * Retorna o método de uma classe a partir de seu nome.
	 * @param fromClass A classe onde o método será procurado
	 * @param methodName O nome do método a ser retornado
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
		catch (final IllegalAccessException e)
		{
			throw new IllegalArgumentException(e);
		}
		catch (final InvocationTargetException e)
		{
			throw new IllegalArgumentException(e);
		}
	}
}
