package com.github.detentor.codex.function.arrow.impl;

import com.github.detentor.codex.function.PartialFunction0;

/**
 * StatePartialArrow representa uma seta que carrega um estado. Pela atualização
 * do estado, é possível à seta retornar valores diferentes para a mesma aplicação. <br/><br/>
 * É semelhante à State Monad, com a diferença que StateArrow não é funcionalmente pura.
 * 
 * @author Vinicius Seufitele Pinto
 *
 * @param <S> O tipo do estado a ser carregado
 * @param <A> O tipo a ser retornado pela seta
 */
public abstract class StatePartialArrow0<S, A> implements PartialFunction0<A>
{
	protected S state;

	public StatePartialArrow0(final S state)
	{
		super();
		this.state = state;
	}
}
