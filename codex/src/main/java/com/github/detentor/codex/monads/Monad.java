package com.github.detentor.codex.monads;

import com.github.detentor.codex.function.Function1;

public interface Monad<A>
{
	Monad<A> unit(A theValue);

	<B> Monad<B> bind(Function1<A, B> mapFunction);
}
