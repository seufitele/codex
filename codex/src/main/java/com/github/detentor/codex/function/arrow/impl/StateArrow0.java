package com.github.detentor.codex.function.arrow.impl;

import com.github.detentor.codex.function.arrow.Arrow0;

/**
 * StateFunction representa uma função que carrega um estado. Pela atualização
 * do estado, é possível a função retornar valores diferentes para a mesma aplicação. <br/><br/>
 * É, funcionalmente, equivalente à State Monad.
 * 
 * @author Vinicius Seufitele Pinto
 *
 * @param <S>
 * @param <A>
 */
public abstract class StateArrow0<S, A> extends Arrow0<A>
{
	protected S state;

	public StateArrow0(final S state)
	{
		super();
		this.state = state;
	}
}
