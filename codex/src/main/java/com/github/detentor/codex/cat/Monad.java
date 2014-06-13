package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;

/**
 * Mônades são construções de teoria das categorias
 */
public interface Monad<A> extends Functor<A> //extends Applicative<A> 
{
	
//	<U, V extends Monad<? extends Monad<U>>> Monad<U> join(final V monad);
//	
//	//Reescrevendo somente para arrumar o retorno
	<B> Monad<B> fmap(final Function1<A, B> func);

	/**
	 * Composição monádica
	 * @param func
	 * @return
	 */
	<B, C extends Monad<B>> Monad<B> bind(final Function1<A, C> func);
	
	/**
	 * Cria uma mônade a partir de um valor
	 * @param value
	 * @return
	 */
	<U> Monad<U> pure(U value); //representa o return
	
	
}
