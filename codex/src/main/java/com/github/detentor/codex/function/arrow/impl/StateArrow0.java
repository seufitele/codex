package com.github.detentor.codex.function.arrow.impl;

import com.github.detentor.codex.function.arrow.Arrow0;

/**
 * StateFunction representa uma seta que carrega um estado. Pela atualização
 * do estado, é possível à seta retornar valores diferentes para a mesma aplicação. <br/><br/>
 * Funcionalmente, equivalente à State Monad.
 * 
 * @author Vinicius Seufitele Pinto
 *
 * @param <S> O tipo do estado a ser carregado
 * @param <A> O tipo a ser retornado pela seta
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
