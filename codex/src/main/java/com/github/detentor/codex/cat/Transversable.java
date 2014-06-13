package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;

/**
 * Funtores representando estruturas que podem ser percorridas da esquerda para a direita 
 *
 * @param <T>
 */
public interface Transversable<T extends Functor<T> & Foldable<T>> 
{

	//traverse :: Applicative f => (a -> f b) -> t a -> f (t b)
//	Applicative<Object> transverse(Function1<A, B>)

}
