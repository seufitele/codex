package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;

/**
 * Applicative é uma classe que fica entre Functor e Monad. 
 * Ela é menos restrita que a Monad, mas é mais útil que Functor. 
 *
 * @param <A>
 */
public interface Applicative<A> extends Functor<A> 
{
	
	/**
	 * Transforma um valor em uma instância de Applicative
	 * @param value
	 * @return
	 */
	<U> Applicative<U> pure(final U value);
	
	/**
	 * Combina o applicative passado como parâmetro  
	 * @param app
	 * @return
	 */
	@Override
	<B> Applicative<B> fmap(final Function1<A, B> app);
}
