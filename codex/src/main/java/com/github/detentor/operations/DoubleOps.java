package com.github.detentor.operations;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Function2;
import com.github.detentor.codex.monads.Option;

/**
 * Essa classe provê funções comuns ao trabalhar com {@link Integer}.
 * 
 * @author Vinícius Seufitele Pinto
 * 
 */
public final class DoubleOps
{
	private DoubleOps()
	{
		// previne instanciação
	}

	public static final Function1<Double, Boolean> greaterThan(final Double num)
	{
		return new Function1<Double, Boolean>()
		{
			@Override
			public Boolean apply(final Double param)
			{
				return param > num;
			}
		};
	}
	
	public static final Function1<Double, Boolean> equal(final Double num)
	{
		return new Function1<Double, Boolean>()
		{
			@Override
			public Boolean apply(final Double param)
			{
				return Option.from(num).equals(Option.from(param));
			}
		};
	}

	public static final Function1<Double, Boolean> lowerThan(final Double num)
	{
		return new Function1<Double, Boolean>()
		{
			@Override
			public Boolean apply(final Double param)
			{
				return param < num;
			}
		};
	}

	public static final Function2<Double, Double, Double> sum = new Function2<Double, Double, Double>()
	{
		@Override
		public Double apply(final Double param1, final Double param2)
		{
			return param1 + param2;
		}
	};
	
	public static final Function2<Double, Double, Double> max = new Function2<Double, Double, Double>()
	{
		@Override
		public Double apply(final Double param1, final Double param2)
		{
			return param1.compareTo(param2) < 0 ? param2 : param1;
		}
	};
	
}
