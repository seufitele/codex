package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function2;

/**
 * Interface de estruturas que podem ser 'dobradas'.
 *
 * @param <T>
 */
public interface Foldable<T> 
{
	/**
	 * 
	 * @param startValue
	 * @param func
	 * @return
	 */
	<B> B foldLeft(final B startValue, final Function2<? super B, ? super T, B> func);
	
	/**
	 * 
	 * @param startValue
	 * @param func
	 * @return
	 */
	<B> B foldRight(final B startValue, final Function2<? super T, ? super B, B> func);
}
