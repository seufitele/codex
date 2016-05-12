package com.github.detentor.codex.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import com.github.detentor.codex.function.arrow.Arrow0;
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
		//Previne instanciação
	}
	
	/**
	 * Transforma um método de uma classe em uma seta. <br/>
	 * 
	 * @param fromClass A classe a partir da qual o método será 'promovido'
	 * @param methodName O nome do método a ser transformado em seta. É necessário que o método não receba
	 * parâmetros, tenha visibilidade public e um retorno diferente de void.
	 * @return Uma seta que representa o método definido pela classe.
	 */
	public static <A, B> Arrow1<A, B> lift(final Class<A> fromClass, final String methodName)
	{
		final Method theMethod = ensureNotEmpty(getMethodFromName(fromClass, methodName));

		return new Arrow1<A, B>()
		{
			@Override
			public B apply(final A param)
			{
				return safeInvoke(param, theMethod, (Object[]) null);
			}
		};
	}
	
	/**
     * Promove um método de uma classe que possui parâmetros para uma seta. <br/> <br/>
     * ATENÇÃO: O método retornado será o primeiro método encontrado da classe que possa ser chamado para os parâmetros. 
     * Isso significa que nem sempre o método com o tipo mais específico é encontrado, e sim um método que esteja definido para os parâmetros
     * passados. Para a maior parte dos casos, no entanto, isso é suficiente. <br/><br/>
     * Para um maior controle sobre os tipos dos parâmetros, use {@link #getMethodFromNameAndType(Class, String, Class[])}. 
     * @param fromClass A classe cujo método será transformado em seta.
     * @param methodName O nome do método a ser transformado
     * @return Uma seta que recebe a instância e os parâmetros para retornar um valor do tipo C
     */
    public static <A, C> ArrowN<Object, Arrow1<A, C>> liftArgs(final Class<? extends A> fromClass, final String methodName)
    {
        return new ArrowN<Object, Arrow1<A, C>>()
        {
            @Override
            public Arrow1<A, C> apply(final Object... params)
            {
                return new Arrow1<A, C>()
                {
                    @Override
                    public C apply(final A instance)
                    {
                        final Method method = ensureNotEmpty(getMethodFromNameAndAssignableType(fromClass, methodName, params));
                        return safeInvoke(instance, method, params);
                    }
                };
            }
        };
    }
    
	/**
     * Transforma um campo de uma classe em uma seta, de forma que chamar a seta com uma instância da classe
     * retornará o valor de seu campo. <br/>
     * 
     * @param fromClass A classe a partir da qual o campo será 'promovido' a método
     * @param fieldName O nome do campo a ser transformado em seta.
     * @return Uma {@link Option} que irá conter a seta que retorna o valor do campo, se o campo existir na classe.
     */
    public static <A, B> Option<Arrow1<A, B>> liftField(final Class<A> fromClass, final String fieldName)
    {
        final Option<Field> theField = fieldFromName(fromClass, fieldName);
        
        if (theField.isEmpty())
        {
            return Option.empty();
        }

        final Arrow1<A, B> arrowRetorno = new Arrow1<A, B>()
        {
            @Override
            public B apply(final A param)
            {
                return safeGet(param, theField.get());
            }
        };
        
        return Option.from(arrowRetorno);
    }
    
	/**
	 * Transforma um método de uma classe em uma {@link Arrow0}, amarrando os valores
	 * passados como parâmetro na chamada do método, de forma que aplicar a seta 
	 * seja equivalente a chamar o método com os parâmetros passados.
	 * 
	 * @param fromInstance A instância a partir da qual o método será promovido
	 * @param methodName O nome do método a ser transformado
	 * @param params Os parâmetros a serem "amarrados" ao método. Para métodos que não recebem parâmetro pode-se passar
	 * um array de tamanho 0.
	 * @return Uma {@link Arrow0} que, ao ser aplicada, seja equivalente à chamada do método com os parâmetros passados
	 */
	public static <B> Arrow0<B> liftBind(final Object fromInstance, final String methodName, final Object... params)
	{
		final Class<?>[] types = new Class[params.length]; 

		for (int i = 0; i < params.length; i++)
		{
			types[i] = params[i].getClass();
		}
		final Method theMethod = ensureNotEmpty(getMethodFromNameAndType(fromInstance.getClass(), methodName, types));

		return new Arrow0<B>()
		{
			@Override
			public B apply()
			{
				return safeInvoke(fromInstance, theMethod, params);
			}
		};
	}

	/**
	 * Transforma um método estático de uma classe numa seta.
	 * @param fromClass A classe onde o método estático existe.
	 * @param methodName O nome do método a ser transformado em seta
	 * @return Uma seta que representa o método definido pela classe
	 */
	public static <A, B, C> Arrow1<B, C> liftStatic(final Class<A> fromClass, final String methodName)
	{
		final Method theMethod = ensureNotEmpty(getMethodFromName(fromClass, methodName));

		return new Arrow1<B, C>()
		{
			@Override
			public C apply(final B param)
			{
				return safeInvoke(fromClass, theMethod, param);
			}
		};
	}
	
	/**
     * Transforma um método estático de uma classe que possui vários parâmetros numa seta. <br/>
     * O tipo do método chamado vai ser definido dinamicamente, a partir dos tipos passados como parâmetro. <br/>
     * Em outras palavras, será procurado um método que possui o nome passado como parâmetro e que seja possível 
     * ser invocado com os parâmetros dos tipos passados. <br/>
     * Note que o método encontrado será o primeiro método com essas características, e não o mais específico.
     *  
     * @param fromClass A classe onde o método estático existe.
     * @param methodName O nome do método a ser transformado em seta
     * @return Uma seta que representa o método definido pela classe
     */
    public static <A, B, C> ArrowN<Object, C> liftStaticArgs(final Class<A> fromClass, final String methodName)
    {
        return new ArrowN<Object, C>()
        {
            public C apply(final Object... params)
            {
                final Method method = ensureNotEmpty(getMethodFromNameAndAssignableType(fromClass, methodName, params));
                return safeInvoke(null, method, params);
            }
        };
    }
	
	/**
	 * Transforma um método estático de uma classe numa seta.
	 * @param fromClass A classe onde o método estático existe
	 * @param methodName O nome do método a ser transformado em seta
	 * @return Uma seta que representa o método definido pela classe
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
				return safeInvoke(fromClass, theMethod, new Object[] { params });
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
	 * Retorna o método de uma classe a partir de seu nome e da sua lista de parâmetros. 
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
     * Retorna o método de uma classe a partir de seu nome que possa ser chamado para os valores de parâmetros passados,
     * considerando inclusive subtipos. <br/>
     * Passar um array vazio significa sem parâmetros.
     * @param fromClass A classe onde o método será procurado
     * @param methodName O nome do método a ser retornado
     * @param paramValues Um array com os valores que o método retornado deve ser capaz de receber
     * @return Uma instância de Option que conterá o método, se ele existir
     */
    private static <A> Option<Method> getMethodFromNameAndAssignableType(final Class<A> fromClass, 
                                                                         final String methodName, 
                                                                         final Object[] paramValues)
    {
        for (final Method curMethod : fromClass.getDeclaredMethods())
        {
            if (curMethod.getName().equals(methodName) && paramValues.length == curMethod.getParameterTypes().length)
            {
                final Class<?>[] paramTypes = curMethod.getParameterTypes();
                boolean isAssignable = true;
                
                for (int i = 0; i < paramValues.length; i++)
                {
                    if (paramValues[i] != null && !paramTypes[i].isAssignableFrom(paramValues[i].getClass()))
                    {
                        isAssignable = false;
                        break;
                    }
                }
                
                if (isAssignable)
                {
                    return Option.from(curMethod); 
                }
            }
        }

        final Class<? super A> superClass = fromClass.getSuperclass();
        return superClass == null ? Option.<Method>empty() : getMethodFromNameAndAssignableType(superClass, methodName, paramValues);
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
	 * Invoca o método de uma classe, transformando as exceções checked em unchecked.
	 * @param fromClass A instância da classe onde o método será chamado
	 * @param method O método a ser chamado
	 * @param params A lista de parâmetros esperada pela classe
	 * @return O valor retornado pelo método
	 * @throws IllegalArgumentException Se ocorrer alguma exceção na chamada do método
	 */
	@SuppressWarnings("unchecked")
	public static <A, B> B safeInvoke(final Object fromClass, final Method method, final Object ... params)
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
	
	/**
	 * Retorna o valor de um determinado campo de uma classe, transformando as exceções checked em unchecked. <br/>
	 * @param theInstance A instância cujo valor do campo será retornado
	 * @param theField O campo cujo valor está sendo buscado
	 * @return O valor do campo na instância passada como parâmetro
	 * @throws IllegalArgumentException Se ocorrer alguma exceção na tentativa de acessar o campo
	 */
    @SuppressWarnings("unchecked")
    private static <T, A> A safeGet(final T theInstance, final Field theField)
    {
        boolean accessibleValue = theField.isAccessible();
        try
        {
            theField.setAccessible(true);
            return (A) theField.get(theInstance);
        }
        catch (IllegalAccessException e)
        {
            throw new IllegalArgumentException(e);
        }
        finally
        {
            //finally vai assegurar que isso será definitivamente executado
            theField.setAccessible(accessibleValue);
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
	 * @throws IllegalArgumentException caso aconteça erro ao se intanciar, por exemplo não ter construtor público
	 */
	public static <T> T newInstance(final Class<T> type) throws IllegalArgumentException
	{
		return newInstance(type, (Object[]) null);
	}
	
	/**
	 * Cria, via reflection, uma nova instancia da classe informada, passando os parâmetros informados. <br/>
	 * ATENÇÃO: O construtor da classe deve ser público. Se params for nulo ou de tamanho 0, então será invocado
	 * o construtor default da classe.
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
		catch (final InstantiationException instEx)
		{
			throw new IllegalArgumentException("A classe " + type + " não possui construtor público", instEx);
		}
		catch (final IllegalAccessException iae)
		{
			throw new IllegalArgumentException(iae);
		}
		catch (SecurityException e)
		{
			throw new IllegalArgumentException("A classe " + type + 
					" não possui um construtor público que receba os parâmetros informados", e);
		}
		catch (InvocationTargetException e)
		{
			throw new IllegalArgumentException("A classe " + type + 
					" não possui um construtor público que receba os parâmetros informados", e);
		}
		catch (NoSuchMethodException e)
		{
			throw new IllegalArgumentException("A classe " + type + 
					" não possui um construtor público que receba os parâmetros informados", e);
		}
	}
}
