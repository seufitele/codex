package com.github.detentor.operations;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Function2;

/**
 * Essa classe provê funções comuns ao trabalhar com {@link Integer}.
 * 
 * @author Vinícius Seufitele Pinto
 * 
 */
public final class IntegerOps
{
	private IntegerOps()
	{
		// previne instanciação
	}

	public static final Function1<Integer, Boolean> greaterThan(final Integer num)
	{
		return new Function1<Integer, Boolean>()
		{
			@Override
			public Boolean apply(final Integer param)
			{
				return param > num;
			}
		};
	}

	public static final Function1<Integer, Boolean> lowerThan(final Integer num)
	{
		return new Function1<Integer, Boolean>()
		{
			@Override
			public Boolean apply(final Integer param)
			{
				return param < num;
			}
		};
	}

	public static final Function1<Integer, Integer> power2 = new Function1<Integer, Integer>()
	{
		@Override
		public Integer apply(final Integer param)
		{
			return param * param;
		}
	};

	public static final Function2<Integer, Integer, Integer> sum = new Function2<Integer, Integer, Integer>()
	{
		@Override
		public Integer apply(final Integer param1, final Integer param2)
		{
			return param1 + param2;
		}
	};

	public static final Function2<Integer, Integer, Integer> max = new Function2<Integer, Integer, Integer>()
	{
		@Override
		public Integer apply(final Integer param1, final Integer param2)
		{
			return param1.compareTo(param2) < 0 ? param2 : param1;
		}
	};
}
