package com.github.detentor.codex.function.arrow;

import com.github.detentor.codex.function.Function2;

public abstract class Arrow2<A, B, C> implements Function2<A, B, C>, Arrow
{
	
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
