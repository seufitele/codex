package com.github.detentor.codex.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.github.detentor.codex.function.Function1;
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
	public static <A, B> Function1<A, B> lift(final Class<A> fromClass, final String methodName)
	{
		final Method theMethod = ensureNotEmpty(getMethodFromName(fromClass, methodName));

		return new Function1<A, B>()
		{
			@Override
			public B apply(final A param)
			{
				return invokeSafe(fromClass, theMethod, (Object[]) null);
			}
		};
	}

	/**
	 * Transforma um método estático de uma classe numa função.
	 * @param fromClass A classe onde o método estático existe
	 * @param methodName O nome do método a ser transformado em função
	 * @return Uma função que representa o método definido pela classe
	 */
	public static <A, B, C> Function1<B, C> liftStatic(final Class<A> fromClass, final String methodName)
	{
		final Method theMethod = ensureNotEmpty(getMethodFromName(fromClass, methodName));

		return new Function1<B, C>()
		{
			@Override
			public C apply(final B param)
			{
				return invokeSafe(fromClass, theMethod, param);
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
	 * Retorna o método de uma classe a partir de seu nome.
	 * @param fromClass A classe onde o método será procurado
	 * @param methodName O nome do método a ser retornado
	 * @return Uma instância de Option que conterá o método, se ele existir
	 */
	public static <A> Option<Method> getMethodFromName(final Class<A> fromClass, final String methodName)
	{
		Method theMethod = null;
		
		for (final Method curMethod : fromClass.getDeclaredMethods())
		{
			if (curMethod.getName().equals(methodName))
			{
				theMethod = curMethod;
				break;
			}
		}
		return Option.from(theMethod);
	}

	/**
	 * Invoca o método de uma classe, pulando as verificações de exceção.
	 * @param fromClass
	 * @param method
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <A, B> B invokeSafe(final Class<A> fromClass, final Method method, final Object ... params)
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
