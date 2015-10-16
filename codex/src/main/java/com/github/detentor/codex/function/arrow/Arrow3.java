package com.github.detentor.codex.function.arrow;

import com.github.detentor.codex.function.Function3;

public abstract class Arrow3<A, B, C, D> implements Function3<A, B, C, D>, Arrow
{
	@Override
	public int getArity()
	{
		return 3;
	}
	
	/**
	 * Retorna uma seta de aridade 2, a partir da aplicação do primeiro parâmetro. <br/>
	 * Essa técnica é também conhecida como Currying.
	 * 
	 * @param param1 O parâmetro a ser aplicado parcialmente nesta seta
	 * @return Uma seta de aridade 2, que representa esta seta com o parâmetro passado fixado
	 */
	public Arrow2<B, C, D> applyPartial(final A param1)
	{
		return new Arrow2<B, C, D>()
		{
			@Override
			public D apply(final B param2, final C param3)
			{
				return Arrow3.this.apply(param1, param2, param3);
			}
		};
	}
}
