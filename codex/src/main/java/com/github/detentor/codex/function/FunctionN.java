package com.github.detentor.codex.function;

import java.lang.reflect.Array;

/**
 * Interface que define uma função que recebe uma quantidade arbitrária de argumentos
 * do tipo A, e retorna um valor do tipo B
 * 
 * @author f9540702 Vinícius Seufitele Pinto
 *
 * @param <A> O tipo dos parâmetros da função
 * @param <B> O tipo de retorno da função
 */
public interface FunctionN<A, B> extends Function
{
	/**
	 * Executa a função, a partir da aplicação dos parâmetros passados.
	 * @param params Os parâmetros a serem passados para a função
	 * @return Um valor do tipo B, obtido após a aplicação da função para os parâmetros
	 */
	@SuppressWarnings("unchecked")
	B apply(final A... params);
	
	/**
	 * {@inheritDoc}<br/>
	 * Como essa função pode esperar um número qualquer de valores, a aridade
	 * retornada é -1.
	 */
	default int getArity()
	{
		return -1;
	}

	/**
	 * Aplica, parcialmente, a função, ou seja, guarda um parâmetro e retorna uma função com aquele parâmetro setado
	 * 
	 * @param param1 O valor a ser aplicado à função
	 * @return Uma nova função, com o valor já aplicado
	 */
	default public FunctionN<A, B> applyPartial(final A param1)
	{
		return params -> 
		{
			@SuppressWarnings("unchecked")
			final A[] todosEle = (A[]) Array.newInstance(param1.getClass(), params.length + 1);
			todosEle[0] = param1;
			System.arraycopy(params, 0, todosEle, 1, params.length);
			return FunctionN.this.apply(todosEle);
		};
	}
}
