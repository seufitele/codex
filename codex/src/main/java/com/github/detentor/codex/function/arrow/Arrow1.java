package com.github.detentor.codex.function.arrow;

import com.github.detentor.codex.function.Function1;

/**
 * Setas são funções mais poderosas.
 * 
 * @author Vinicius Seufitele Pinto
 *
 * @param <A>
 * @param <B>
 */
public abstract class Arrow1<A, B> implements Function1<A, B>, Arrow
{
	public <C> Arrow1<A, C> and(final Function1<B, C> func)
	{
		return new Arrow1<A, C>()
		{
			@Override
			public C apply(A param)
			{
				return func.apply(Arrow1.this.apply(param));
			}
		};
	}
	
	public Arrow0<B> applyPartial(final A param1)
	{
		return new Arrow0<B>()
		{
			@Override
			public B apply()
			{
				return Arrow1.this.apply(param1);
			}
		};
	}
	
	@Override
	public int getArity()
	{
		return 1;
	}

}
