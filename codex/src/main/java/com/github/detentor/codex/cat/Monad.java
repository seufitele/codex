package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;

/**
 * Mônades são abstrações sobre containeres, de forma que o valor contido nesse container seja encapsulado num 'contexto'. <br/> 
 * Note que dependendo da mônade não é possível recuperar o valor contido internamente, apenas executar operações sobre ele. <br/><br/>
 * 
 * Ex: A mônade {@link Option} encapsula um valor num contexto de incerteza (ele pode existir ou não). <br/><br/>
 * 
 * ATENÇÃO: Todas as classes que estenderem essa interface devem especializar o retorno dos métodos.
 * 
 * @param <C> O type constructor (a classe) representada por essa Mônade
 * @param <T> O tipo de dados que a Mônade guarda
 */
public interface Monad<T> extends Applicative<T>
{
	/**
	 * Põe um valor num contexto monádico 'puro'.<br/>  Note que isso equivale ao constructor da classe.
	 * 
	 * @param value O valor a ser colocado no contexto monádico.
	 * 
	 * @return Uma mônade que contém o valor
	 */
	public <U> Monad<U> pure(final U value);
	
	@Override
	public <U> Monad<U> map(final Function1<? super T, U> function);
	
	@Override
	<B> Monad<B> ap(Applicative<Function1<? super T, B>> applicative);

	/**
	 * Composição monádica, equivalente à composição de funções. <br/>
	 * Bind pega o valor contido na mônade, e aplica a função passada como parâmetro. <br/><br/>
	 * 
	 * ATENÇÃO: A função function1 : A -> Monad deve SEMPRE transformar o valor A na mesma 
	 * instância dessa Mônade. <br/>
	 * Se o retorno for diferente será disparado {@link ClassCastException}.
	 *  
	 * @param function A função a ser composta com essa mônade.
	 * @return Uma mônade representada pela composição dessas duas funções
	 */
	public <U> Monad<U> bind(final Function1<? super T, Monad<U>> function);
}
