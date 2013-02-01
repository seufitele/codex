package com.github.detentor.codex.monads;

/**
 * State Monad é uma mônade para um tipo A que carrega um estado do tipo S.
 * 
 * @author Vinicius Seufitele Pinto
 * 
 * @param <S> O tipo de dados do estado da mônade
 * @param <A> O tipo de dados guardado pela mônade
 */
public abstract class StateMonad<S, A> implements Monad<A>
{
	protected final S state;

	protected StateMonad(final S theState)
	{
		super();
		this.state = theState;
	}

}
