package com.github.detentor.codex.function;

/**
 * Interface de funções que recebem um parâmetro, e retornam um valor. (f : A -> B).
 * 
 * @author Vinícius Seufitele Pinto 
 *
 * @param <A> O tipo de dado de entrada da função
 * @param <B> O tipo de dado de saída da função
 */
public interface Function1<A, B>
{
	/**
	 * Executa a função, retornando um valor do tipo B
	 * @param param O parâmetro a ser passado para a função
	 * @return Um valor do tipo B
	 */
	B apply(final A param);
	
}
