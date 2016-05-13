package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;

public class Monads
{
	private Monads()
	{
		//previne instanciação
	}
	
	/**
	 * Promove uma função que trabalha com valores para trabalhar com mônades
	 * @param function A função a ser promovida
	 * @return Uma função que é capaz de ser aplicadas a mônades
	 */
	public static <A, B> Function1<Monad<A>, Monad<B>> lift(final Function1<? super A, B> function)
	{
		return new Function1<Monad<A>, Monad<B>>()
		{
			@Override
			public Monad<B> apply(final Monad<A> param)
			{
				return param.map(function);
			}
		};
	}
}
