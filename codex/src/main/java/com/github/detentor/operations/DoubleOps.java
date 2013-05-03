package com.github.detentor.operations;

import com.github.detentor.codex.function.arrow.Arrow1;
import com.github.detentor.codex.function.arrow.Arrow2;

/**
 * Essa classe provê funções comuns ao trabalhar com {@link Double}.
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

	/**
	 * Retorna uma seta que valida se o número passado como parâmetro é maior do que 'num'
	 * @param num O número a ser usado como comparador para a seta.
	 * @return Uma seta que valida se o número passado como parâmetro é maior do que 'num'
	 */
	public static final Arrow1<Double, Boolean> greaterThan(final Double num)
	{
		return new Arrow1<Double, Boolean>()
		{
			@Override
			public Boolean apply(final Double param)
			{
				return param > num;
			}
		};
	}
	
	/**
	 * Retorna uma seta que valida se o número passado como parâmetro é igual a 'num'
	 * @param num O número a ser usado como comparador para a seta.
	 * @return Uma seta que valida se o número passado como parâmetro é igual a 'num'
	 */
	public static final Arrow1<Double, Boolean> equal(final Double num)
	{
		return new Arrow1<Double, Boolean>()
		{
			@Override
			public Boolean apply(final Double param)
			{
				return ObjectOps.isEquals(num, param);
			}
		};
	}

	/**
	 * Retorna uma seta que valida se o número passado como parâmetro é menor do que 'num'
	 * @param num O número a ser usado como comparador para a seta.
	 * @return Uma seta que valida se o número passado como parâmetro é menor do que 'num'
	 */
	public static final Arrow1<Double, Boolean> lowerThan(final Double num)
	{
		return new Arrow1<Double, Boolean>()
		{
			@Override
			public Boolean apply(final Double param)
			{
				return param < num;
			}
		};
	}

	/**
	 * Representa uma seta que retorna o valor da soma de dois números
	 */
	public static final Arrow2<Double, Double, Double> sum = new Arrow2<Double, Double, Double>()
	{
		@Override
		public Double apply(final Double param1, final Double param2)
		{
			return param1 + param2;
		}
	};
	
	/**
	 * Representa uma seta que retorna, dentre dois números, aquele de maior valor.
	 */
	public static final Arrow2<Double, Double, Double> max = new Arrow2<Double, Double, Double>()
	{
		@Override
		public Double apply(final Double param1, final Double param2)
		{
			return param1.compareTo(param2) < 0 ? param2 : param1;
		}
	};
	
}
