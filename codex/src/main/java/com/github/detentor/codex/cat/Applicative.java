package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;

/**
 * Applicative representa uma estrutura entre o {@link Functor} e a {@link Monad}. <br/>
 * Em particular, o Applicative possui a capacidade de colocar valores em contexto e fazer sequenciamento de computações e combinar seus
 * resultados.
 *
 * <A> O tipo de dados contido no Applicative
 */
public interface Applicative<A> extends Functor<A>
{
	/**
	 * Põe um valor num contexto 'puro'.<br/>
	 * Note que isso equivale ao constructor da classe.
	 * 
	 * @param value O valor a ser colocado no contexto puro.
	 * 
	 * @return Uma instância de Applicative que contém o valor passado como parâmetro
	 */
	public <U> Applicative<U> pure(final U value);
	
	/**
	 * Faz a aplicação deste Applicative a partir
	 * @param appl
	 * @return
	 */
	public <B> Applicative<B> ap(final Applicative<Function1<? super A, B>> appl);

	public <B> Applicative<B> map(final Function1<? super A, B> function);

}
