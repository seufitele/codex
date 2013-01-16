package com.github.detentor.codex.function;

/**
 * Interface de funções que recebem dois parâmetros, e retornam um valor. (f : A -> B -> C).
 * 
 * @author Vinícius Seufitele Pinto 
 *
 * @param <A> O tipo de dado do primeiro parâmetro
 * @param <B> O tipo de dado do segundo parâmetro
 * @param <B> O tipo de dado de saída da função
 */
public interface Function2<A, B, C>
{
	/**
	 * Executa a função, a partir da aplicação dos dois parâmetros.
	 * @param param1 O primeiro parâmetro a ser passado para a função
	 * @param param2 O segundo parâmetro a ser passado para a função
	 * @return Um valor do tipo C, obtido após a aplicação da função para os parâmetros 
	 */
	C apply(final A param1, final B param2);
}
