package com.github.detentor.codex.function.arrow;

import com.github.detentor.codex.function.Function2;

public abstract class Arrow2<A, B, C> implements Function2<A, B, C>, Arrow
{
	/**
	 * Retorna uma seta de aridade 1, a partir da aplicação do primeiro parâmetro. <br/>
	 * Essa técnica é também conhecida como Currying.
	 * 
	 * @param param1 O parâmetro a ser aplicado parcialmente nesta seta
	 * @return Uma seta de aridade 1, que representa esta seta com o parâmetro passado fixado
	 */
	public Arrow1<B, C> applyPartial(final A param1)
	{
		return new Arrow1<B, C>()
		{
			@Override
			public C apply(final B param2)
			{
				return Arrow2.this.apply(param1, param2);
			}
		};
	}

	@Override
	public int getArity()
	{
		return 2;
	}
	
}
