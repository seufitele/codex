package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.monads.Option;

/**
 * Applicative representa uma estrutura entre o {@link Functor} e a {@link Monad}. <br/>
 * O principal benefício do Applicative é poder executar funções dentro de um contexto. <br/><br/>
 * O tipo de Applicative representa a abstração deste contexto. <br/> 
 * Ex: Para a {@link Option}, o contexto seria uma computação que pode ou não ter um resultado <br/><br/>
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
	 * Faz a aplicação da função contida no Applicative passado como parâmetro neste Applicative
	 * @param applicative O Applicative que possui a função a ser executada
	 * @return Um Applicative obtido a partir da aplicação da função contida no Applicative passado como parâmetro
	 */
	public <B> Applicative<B> ap(final Applicative<Function1<? super A, B>> applicative);

	public <B> Applicative<B> map(final Function1<? super A, B> function);

}
