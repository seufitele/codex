package com.github.detentor.codex.monads;


/**
 * State Monad é uma mônade que carrega um estado do tipo A.
 * 
 * @author f9540702
 *
 * @param <A>
 * @param <B>
 * @param <C>
 */
public abstract class StateMonad<A, B> implements Monad<B>
{
	protected final A state;

	protected StateMonad(final A theState)
	{
		super();
		this.state = theState;
	}
}
