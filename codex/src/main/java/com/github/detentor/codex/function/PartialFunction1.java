package com.github.detentor.codex.function;


/**
 * Uma função parcial é uma função que pode não estar definida para todos os valores de entrada. <br/><br/>
 * Métodos que esperam funções parciais devem, antes de aplicar a função, chamar o método {@link #isDefinedAt}, 
 * que retorna true caso a função esteja definida para o argumento passado como parâmetro. <br/><br/>
 * O comportamento de uma função, quando chamada para um argumento que ela não está definida, não é especificado. <br/><br/>
 * É importante observar que é responsabilidade do implementador assegurar que retorno do método {@link #isDefinedAt}
 * condiz com o comportamento esperado pelo método {@link Function1#apply(Object) apply} da função. 
 * 
 * @author Vinícius Seufitele Pinto
 * 
 * @param <A> O tipo de dados de entrada
 * @param <B> O tipo de dados da saída
 */
public interface PartialFunction1<A, B> extends Function1<A, B>
{
	/**
	 * Retorna <tt>true</tt> se, e somente se, essa função possui um retorno válido para o argumento passado como parâmetro.
	 * 
	 * @param forValue O valor onde a função será testada
	 * @return <tt>true</tt> se a função está definida para o argumento, ou <tt>false</tt> do contrário
	 */
	boolean isDefinedAt(final A forValue);

	/**
	 * Faz a composição desta função parcial com a função passada como parâmetro. <br/>
	 * A função parcial retornada estará definida onde esta função está definida.
	 * 
	 * @param func A função a ser composta com essa função parcial
	 */
	default public <C> PartialFunction1<A, C> compose(final Function1<? super B, C> func)
	{
		return new PartialFunction1<A, C>()
		{
			@Override
			public C apply(final A param)
			{
				return func.apply(PartialFunction1.this.apply(param));
			}

			@Override
			public boolean isDefinedAt(final A forValue)
			{
				return PartialFunction1.this.isDefinedAt(forValue);
			}
		};
	}
	
	/**
	 * Faz a composição desta função parcial com a função parcial passada como parâmetro. <br/>
	 * A função parcial retornada está definida apenas nos para os parâmetros que esta e a função passada
	 * como parâmetro estão definidas.
	 * 
	 * @param func A função parcial a ser composta com essa função
	 * 
	 * @return Uma função parcial que representa a composição desta função e da função parcial passada como parâmetro. 
	 * O domínio da função parcial será a interseção dos domínios desta função e da função passada como parâmetro
	 */
	default public <C> PartialFunction1<A, C> compose(final PartialFunction1<? super B, C> func)
	{
		return new PartialFunction1<A, C>()
		{
			@Override
			public C apply(final A param)
			{
				return func.apply(PartialFunction1.this.apply(param));
			}

			@Override
			public boolean isDefinedAt(final A forValue)
			{
				return PartialFunction1.this.isDefinedAt(forValue) && func.isDefinedAt(PartialFunction1.this.apply(forValue));
			}
		};
	}
}
