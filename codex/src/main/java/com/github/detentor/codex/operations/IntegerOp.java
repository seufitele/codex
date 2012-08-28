package com.github.detentor.codex.operations;

import com.github.detentor.codex.function.Function1;

public class IntegerOp
{
	public static final Function1<Integer, Integer> square = new Square();
	
	
	private static final class Square implements Function1<Integer, Integer>
	{
		@Override
		public Integer apply(final Integer param)
		{
			return param * param;
		}
	}

}
