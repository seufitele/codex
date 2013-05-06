package com.github.detentor.codex.function.arrow.impl;

import java.util.HashMap;
import java.util.Map;

import com.github.detentor.codex.function.arrow.Arrow1;

/**
 * A representação de uma {@link Arrow1} que guarda na memória valores previamente calculados.
 * 
 * @author Vinícius Seufitele Pinto
 *
 * @param <A>
 * @param <B>
 */
public abstract class MemoizedArrow1<A, B> extends Arrow1<A, B>
{
	private Map<A, B> memoValues = new HashMap<A, B>();

	@Override
	public final B apply(A param)
	{
		B theVal = memoValues.get(param);
		
		if (theVal != null)
		{
			return theVal;
		}

		theVal = doApply(param);
		memoValues.put(param, theVal);
		return theVal;
	}

	/**
	 * Faz o efetivo cálculo da função, retornando o valor B. <br/>
	 * Esse método será chamado somente quando não for possível encontrar o valor na tabela de memoização
	 * @param param O parâmetro a ser passado para a função
	 * @return O valor, após a aplicação da função
	 */
	protected abstract B doApply(final A param);

}
