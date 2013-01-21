package com.github.detentor.codex.function.impl;

import java.util.HashMap;
import java.util.Map;

import com.github.detentor.codex.function.Function1;

/**
 * Função que guarda na memória valores privamente calculados, utilizando um HashMap
 * 
 * @author f9540702
 *
 * @param <A>
 * @param <B>
 */
public abstract class MemoizedFunction1<A, B> implements Function1<A, B>
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
	 * Esse método será chamado somente quando não for possível contrar o valor
	 * na tabela de memoização
	 * @param param O parâmetro a ser passado para a função
	 * @return O valor, após a aplicação da função
	 */
	protected abstract B doApply(final A param);

}
