package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.arrow.Arrow1;
import com.github.detentor.operations.ObjectOps;

public class Cats
{
	private Cats()
	{
		//previne instanciação
	}

	/**
	 * Promove uma função que trabalha com valores para trabalhar com mônades
	 * @param function A função a ser promovida
	 * @return Uma seta que é capaz de ser aplicada a mônades
	 */
	public static <A, B, T extends Monad<A>, U extends Monad<B>> Arrow1<T, U> liftM(final Function1<? super A, B> function)
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
	
	/**
	 * Promove uma função que trabalha com valores para trabalhar com Applicatives
	 * @param function A função a ser promovida
	 * @return Uma seta que é capaz de ser aplicada a mônades
	 */
	public static <A, B, T extends Applicative<A>, U extends Applicative<B>> Arrow1<T, U> liftA(final Function1<? super A, B> function)
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
	
	/**
	 * Combina dois níveis de monad em um só
	 * @param monad A monad que está em duas camadas
	 * @return
	 */
	public static <A> Monad<A> join(Monad<Monad<A>> monad)
	{
		return monad.bind(ObjectOps.<Monad<A>, Monad<A>>identity());
	}
}
