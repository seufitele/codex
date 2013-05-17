package com.github.detentor.codex.function.arrow;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.PartialFunction1;

/**
 * 
 * Uma seta parcial é uma seta que pode não estar definida para todos os valores de entrada. <br/>
 * <br/>
 * Métodos que esperam setas parciais devem, antes de aplicar a função, chamar o método {@link #isDefinedAt}, que retorna true caso a
 * seta esteja definida para o argumento passado como parâmetro. <br/>
 * <br/>
 * O comportamento de uma seta, quando chamada para um argumento que ela não está definida, não é especificado. <br/>
 * <br/>
 * É importante observar que é responsabilidade do implementador assegurar que retorno do método {@link #isDefinedAt} condiz com o
 * comportamento esperado pelo método {@link Function1#apply(Object) apply} da seta.
 * 
 * @author Vinícius Seufitele Pinto
 * 
 * @param <A> O tipo de dados de entrada
 * @param <B> O tipo de dados da saída
 */
public abstract class PartialArrow1<A, B> extends Arrow1<A, B> implements PartialFunction1<A, B>
{
	/**
	 * Retorna <tt>true</tt> se, e somente se, essa seta possui um retorno válido para o argumento passado como parâmetro.
	 * 
	 * @param forValue O valor onde a função será testada
	 * @return <tt>true</tt> se a função está definida para o argumento, ou <tt>false</tt> do contrário
	 */
	@Override
	public abstract boolean isDefinedAt(final A forValue);

	/**
	 * Faz a composição desta seta parcial com a seta passada como parâmetro. A seta
	 * parcial retornada estará definida onde esta seta está definida.
	 * 
	 * @param func A seta a ser composta com essa seta parcial
	 * 
	 */
	@Override
	public <C> PartialArrow1<A, C> and(final Function1<? super B, C> func)
	{
		return new PartialArrow1<A, C>()
		{
			@Override
			public C apply(final A param)
			{
				return func.apply(PartialArrow1.this.apply(param));
			}

			@Override
			public boolean isDefinedAt(final A forValue)
			{
				return PartialArrow1.this.isDefinedAt(forValue);
			}
		};
	}

}
