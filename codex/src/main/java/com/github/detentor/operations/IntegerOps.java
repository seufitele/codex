package com.github.detentor.operations;

import com.github.detentor.codex.function.arrow.Arrow1;
import com.github.detentor.codex.function.arrow.Arrow2;
import com.github.detentor.codex.monads.Option;

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

	/**
	 * Retorna uma função que verifica se o número aplicado é maior do que o número passado como parâmetro
	 * @param num O número a ser verificado, para cada número aplicado
	 * @return Uma função que verifica, para o número aplicado, se ele é maior que num
	 */
	public static final Arrow1<Integer, Boolean> greaterThan(final Integer num)
	{
		return new Arrow1<Integer, Boolean>()
		{
			@Override
			public Boolean apply(final Integer param)
			{
				return param > num;
			}
		};
	}
	
	public static final Arrow1<Integer, Boolean> equal(final Integer num)
	{
		return new Arrow1<Integer, Boolean>()
		{
			@Override
			public Boolean apply(final Integer param)
			{
				return Option.from(num).equals(Option.from(param));
			}
		};
	}

	public static final Arrow1<Integer, Boolean> lowerThan(final Integer num)
	{
		return new Arrow1<Integer, Boolean>()
		{
			@Override
			public Boolean apply(final Integer param)
			{
				return param < num;
			}
		};
	}

	public static final Arrow2<Integer, Integer, Integer> sum = new Arrow2<Integer, Integer, Integer>()
	{
		@Override
		public Integer apply(final Integer param1, final Integer param2)
		{
			return param1 + param2;
		}
	};
	
	public static final Arrow2<Integer, Integer, Integer> max = new Arrow2<Integer, Integer, Integer>()
	{
		@Override
		public Integer apply(final Integer param1, final Integer param2)
		{
			return param1.compareTo(param2) < 0 ? param2 : param1;
		}
	};
	
	public static final Arrow1<Integer, Integer> square = new Arrow1<Integer, Integer>()
	{
		@Override
		public Integer apply(final Integer param1)
		{
			return param1 * param1;
		}
	};

}
