package com.github.detentor.codex.util;

import com.github.detentor.codex.function.Function0;

/**
 * Essa classe possui métodos que facilitam a depuração de código
 * 
 * @author Vinicius Seufitele Pinto
 *
 */
public class Debugging
{
	private Debugging()
	{
		//Previne instanciaçãp
	}
	
	/**
	 * Calcula o tempo que demora para uma função ser chamada
	 * 
	 * @return Um valor long que representa a quantidade de milisegundos que a chamada da função
	 * demorou para ser executada
	 */
	public <A> long timeFunction(final Function0<A> function)
	{
		final long startTime = System.currentTimeMillis();
		function.apply();
		return System.currentTimeMillis() - startTime; 
	}
}
