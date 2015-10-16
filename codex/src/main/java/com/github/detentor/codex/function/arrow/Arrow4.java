package com.github.detentor.codex.function.arrow;

import com.github.detentor.codex.function.Function4;

public abstract class Arrow4<A, B, C, D, E> implements Function4<A, B, C, D, E>, Arrow
{
	@Override
	public int getArity()
	{
		return 4;
	}
	
	/**
	 * Retorna uma seta de aridade 3, a partir da aplicação do primeiro parâmetro. <br/>
	 * Essa técnica é também conhecida como Currying.
	 * 
	 * @param param1 O parâmetro a ser aplicado parcialmente nesta seta
	 * @return Uma seta de aridade 3, que representa esta seta com o parâmetro passado fixado
	 */
	public Arrow3<B, C, D, E> applyPartial(final A param1)
	{
		return new Arrow3<B, C, D, E>()
		{
			@Override
			public E apply(final B param2, final C param3, final D param4)
			{
				return Arrow4.this.apply(param1, param2, param3, param4);
			}
		};
	}
}
