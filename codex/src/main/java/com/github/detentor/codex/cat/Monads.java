package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.arrow.Arrow1;

public class Monads
{
	private Monads()
	{
		//previne instanciação
	}
	
	/**
	 * Promove uma função que trabalha com valores para trabalhar com mônades
	 * @param function A função a ser promovida
	 * @return Uma seta que é capaz de ser aplicada a mônades
	 */
	public static <A, B, T extends Monad<A>, U extends Monad<B>> Arrow1<T, U> lift(final Function1<? super A, B> function)
	{
		return new Arrow1<T, U>()
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
