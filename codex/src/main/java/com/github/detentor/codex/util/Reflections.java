package com.github.detentor.codex.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.github.detentor.codex.function.Function1;

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
		Method theMethod = null;

		for (final Method curMethod : fromClass.getDeclaredMethods())
		{
			if (curMethod.getName().equals(methodName))
			{
				theMethod = curMethod;
				break;
			}
		}

		if (theMethod == null)
		{
			throw new IllegalArgumentException("The named method doesn't exist");
		}

		final Method theMethod2 = theMethod;

		return new Function1<A, B>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public B apply(final A param)
			{
				try
				{
					return (B) theMethod2.invoke(param, (Object[]) null);
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
		};
	}

}
