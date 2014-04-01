package com.github.detentor.codex.function;

/**
 * Interface comum a todas as funções.
 * 
 * @author Vinicius Seufitele Pinto
 *
 */
public interface Function 
{
	/**
	 * Retorna a aridade da função (número de parâmetros que ela espera)
	 * @return O número de parâmetros desta função
	 */
	int getArity();
}
