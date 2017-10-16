package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;

/**
 * Representa o dual de uma monad. <br/>
 * Definição mínima: extract e (duplicate ou extend)
 */
public interface Comonad<A> extends Functor<A>
{

	// extract . fmap f = f . extract
	/**
	 * Extrai o valor contido nesta comonad
	 * 
	 * @return O valor contido nesta comonad
	 */
	A extract();

	// duplicate = extend id
	// fmap (fmap f) . duplicate = duplicate . fmap f
	/**
	 * O dual do {@link Cats#join(Monad) join} da Monad.
	 * 
	 * @return Uma comonad com dois níveis
	 */
	Comonad<Comonad<A>> duplicate();

	// extend f = fmap f . duplicate
	/**
	 * O dual do {@link Monad#bind(Function1) bind} da Monad.
	 * 
	 * @param function A função que transforma uma Comonad num valor do tipo B
	 * @return Uma Comonad do tipo B
	 */
	<B> Comonad<B> extend(final Function1<Comonad<A>, B> function);

	@Override
	<B> Comonad<B> map(Function1<? super A, B> function);
}
