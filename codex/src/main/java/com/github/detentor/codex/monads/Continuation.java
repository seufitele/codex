package com.github.detentor.codex.monads;

import com.github.detentor.codex.cat.Applicative;
import com.github.detentor.codex.cat.Monad;
import com.github.detentor.codex.function.Function1;

public class Continuation<A, R> implements Monad<A>
{

	@Override
	public <U> Monad<U> pure(U value)
	{
		return null;
	}

	@Override
	public <U> Monad<U> map(Function1<? super A, U> function)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <B> Monad<B> ap(Applicative<Function1<A, B>> applicative)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <U> Monad<U> bind(Function1<? super A, ? extends Monad<U>> function)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
