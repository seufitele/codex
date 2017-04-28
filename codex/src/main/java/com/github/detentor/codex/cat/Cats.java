package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;

public class Cats
{
	private Cats()
	{
		// previne instanciação
	}

	/**
	 * Promove uma função que trabalha com valores para trabalhar com mônades
	 * 
	 * @param function A função a ser promovida
	 * @return Uma seta que é capaz de ser aplicada a mônades
	 */
	public static <A, B, T extends Monad<A>, U extends Monad<B>> Function1<T, U> liftM(final Function1<? super A, B> function)
	{
		return new Function1<T, U>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public U apply(final T param)
			{
				return (U) param.map(function);
			}
		};
	}

	/**
	 * Promove uma função que trabalha com valores para trabalhar com Applicatives
	 * 
	 * @param function A função a ser promovida
	 * @return Uma seta que é capaz de ser aplicada a mônades
	 */
	public static <A, B, T extends Applicative<A>, U extends Applicative<B>> Function1<T, U> liftA(final Function1<? super A, B> function)
	{
		return new Function1<T, U>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public U apply(final T param)
			{
				return (U) param.map(function);
			}
		};
	}

}
