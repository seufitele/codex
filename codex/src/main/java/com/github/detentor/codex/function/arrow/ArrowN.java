package com.github.detentor.codex.function.arrow;

import com.github.detentor.codex.collections.mutable.LLSharp;
import com.github.detentor.codex.function.FunctionN;

/**
 * Interface que define uma seta que não recebe uma quantidade arbitrária de argumentos do tipo A, e retorna um valor do tipo B
 * 
 * @author f9540702 Vinícius Seufitele Pinto
 * 
 * @param <A> O tipo dos parâmetros da seta
 * @param <B> O tipo de retorno da seta
 */
public abstract class ArrowN<A, B> implements FunctionN<A, B>, Arrow
{
	/**
	 * Aplica, parcialmente, a seta, ou seja, guarda um parâmetro e retorna uma seta com aquele parâmetro setado
	 * 
	 * @param param1 O valor a ser aplicado à seta
	 * @return Uma nova seta, com o valor já aplicado
	 */
	public ArrowN<A, B> applyPartial(final A param1)
	{
		return new ArrowN<A, B>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public B apply(final A... params)
			{
				return ArrowN.this.apply((A[]) LLSharp.from(params).add(param1).toList().toArray());
			}
		};
	}

	@Override
	public int getArity()
	{
		return 0;
	}

}
