package com.github.detentor.codex.function;

public interface Functions
{
	static <A, B, C> Function2<B, A, C> flip(final Function2<A, B, C> func)
	{
		return (param2, param1) -> func.apply(param1, param2);
	}
	
	static public <A, B, C> Function1<A, C> compose(final Function1<? super B, ? extends C> func2, final Function1<? super A, ? extends B> func1)
	{
		return param -> func2.apply(func1.apply(param));
	}

}
