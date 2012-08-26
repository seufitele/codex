package br.com.codex.function;

/**
 * Interface que define uma função que não possui parâmetros, e retorna um valor do tipo T
 * @author f9540702 Vinícius Seufitele Pinto
 *
 * @param <T> O tipo a ser retornado pela função
 */
public interface Function0<T>
{
	/**
	 * Executa a função, retornando um valor do tipo T
	 * @return Um valor do tipo T
	 */
	public T apply();

}
