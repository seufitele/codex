package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function2;

/**
 * Foldable representa estruturas que podem ser 'dobradas' para
 * se resumir em um valor.
 * 
 * @param <A> O tipo de dados contido em Foldable
 */
public interface Foldable<A>
{
	
	<B> B foldRight(final B startValue, final Function2<B, ? super A, B> function);

}
