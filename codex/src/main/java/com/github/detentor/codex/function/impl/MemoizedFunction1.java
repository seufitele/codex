package com.github.detentor.codex.function.impl;

import java.util.HashMap;
import java.util.Map;

import com.github.detentor.codex.function.Function1;

/**
 * A representação de uma {@link Function1} que guarda na memória valores previamente calculados.
 * 
 * @author Vinicius Seufitele Pinto
 * 
 * @param <A> O tipo de dado de entrada da função
 * @param <B> O tipo de dado de saída da função
 */
public abstract class MemoizedFunction1<A, B> implements Function1<A, B>
{
	private final Map<A, B> memoValues = new HashMap<A, B>();

	@Override
	public final B apply(final A param)
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
	 * Esse método será chamado somente quando não for possível contrar o valor na tabela de memoização
	 * 
	 * @param param O parâmetro a ser passado para a função
	 * @return O valor, após a aplicação da função
	 */
	protected abstract B doApply(final A param);

}
