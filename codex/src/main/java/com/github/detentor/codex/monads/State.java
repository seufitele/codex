package com.github.detentor.codex.monads;

import com.github.detentor.codex.cat.Applicative;
import com.github.detentor.codex.cat.Monad;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.product.Tuple;
import com.github.detentor.codex.product.Tuple2;

/**
 * State é uma mônade que representa uma computação que possui um estado. <br/>
 * É como uma função que depende de um estado interno para operar de maneira correta. <br/>
 * Ex: A função Random, que recebe um seed e retorna um inteiro. <br/>
 * <br/>
 * 
 * Para 'executar' a função, basta chamar o seu método apply.
 * 
 * @param <S> O tipo do estado guardado pela mônade
 * @param <A> O tipo de valor retornado pela mônade
 */
public abstract class State<S, A> implements Monad<A>, Function1<S, Tuple2<A, S>>
{
	@Override
	public <B> State<S, B> map(final Function1<? super A, B> function)
	{
		return new State<S, B>()
		{
			@Override
			public Tuple2<B, S> apply(final S param)
			{
				return State.this.apply(param).map(function);
			}
		};
	}

	@Override
	public <B> State<S, B> ap(final Applicative<Function1<A, B>> applicative)
	{
		@SuppressWarnings("unchecked")
		final State<S, Function1<A, B>> stateAp = (State<S, Function1<A, B>>) applicative;

		return new State<S, B>()
		{
			@Override
			public Tuple2<B, S> apply(final S param)
			{
				final Tuple2<A, S> ret = State.this.apply(param);
				final Tuple2<Function1<A, B>, S> res = stateAp.apply(ret.getVal2());
				return Tuple2.from(res.getVal1().apply(ret.getVal1()), res.getVal2());
			}
		};
	}

	/**
	 * Retorna um estado que ao ser executado simplesmente retorna o valor passado como parâmetro
	 */
	@Override
	public <U> Monad<U> pure(final U value)
	{
		return new State<S, U>()
		{
			@Override
			public Tuple2<U, S> apply(final S param)
			{
				return Tuple.from(value, param);
			}
		};
	}

	@Override
	public <U> State<S, U> bind(final Function1<? super A, Monad<U>> function)
	{
		return new State<S, U>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public Tuple2<U, S> apply(final S param)
			{
				final Tuple2<A, S> res1 = State.this.apply(param);
				return ((State<S, U>) function.apply(res1.getVal1())).apply(res1.getVal2());
			}
		};
	}
}
