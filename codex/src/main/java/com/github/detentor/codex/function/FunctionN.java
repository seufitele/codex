package com.github.detentor.codex.function;

/**
 * Interface que define uma função que recebe uma quantidade arbitrária de argumentos
 * do tipo A, e retorna um valor do tipo B
 * 
 * @author f9540702 Vinícius Seufitele Pinto
 *
 * @param <A> O tipo dos parâmetros da função
 * @param <B> O tipo de retorno da função
 */
public interface FunctionN<A, B>
{
	/**
	 * Executa a função, a partir da aplicação dos parâmetros passados.
	 * @param params Os parâmetros a serem passados para a função
	 * @return Um valor do tipo B, obtido após a aplicação da função para os parâmetros
	 */
	B apply(final A... params);
}
