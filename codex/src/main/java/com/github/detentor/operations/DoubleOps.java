package com.github.detentor.operations;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Function2;

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
	public static final Function1<Double, Boolean> greaterThan(final Double num)
	{
		return param -> param > num;
	}
	
	/**
	 * Retorna uma seta que valida se o número passado como parâmetro é menor do que 'num'
	 * @param num O número a ser usado como comparador para a seta.
	 * @return Uma seta que valida se o número passado como parâmetro é menor do que 'num'
	 */
	public static final Function1<Double, Boolean> lowerThan(final Double num)
	{
		return param -> param < num;
	}

	/**
	 * Representa uma seta que retorna o valor da soma de dois números
	 */
	public static final Function2<Double, Double, Double> sum = (param1, param2) -> param1 + param2;
	
	/**
	 * Representa uma seta que retorna, dentre dois números, aquele de maior valor.
	 */
	public static final Function2<Double, Double, Double> max = Math::max;
}
