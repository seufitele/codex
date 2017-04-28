package com.github.detentor.codex.function;

/**
 * Interface que define uma função que não possui parâmetros, e retorna um valor do tipo A
 * 
 * @author Vinícius Seufitele Pinto
 *
 * @param <A> O tipo a ser retornado pela função
 */
public interface Function0<A> 
{
	default int getArity()
	{
		return 0;
	}

	/**
	 * Executa a função, retornando um valor do tipo A
	 * @return Um valor do tipo A
	 */
	A apply();
}
