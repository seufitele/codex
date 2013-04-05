package com.github.detentor.codex.function;

/**
 * Classe utilitária, com métodos a serem aplicados a funções.
 * 
 * @author Vinícius Seufitele Pinto
 * 
 */
public final class Functions
{
	private Functions()
	{
		// Evitando instanciação, para conformizar com o sonar.
	}

	/**
	 * Faz a composição de duas funções passadas como parâmetro. <br/>
	 * Formalmente, seja
	 * 
	 * <pre>
	 * f: A -> B e g: B -> C. Então, será retornado um h: A -> C.
	 * </pre>
	 * 
	 * @param <A> O tipo de dados de entrada da primeira função
	 * @param <B> O tipo de dados de retorno da primeira função, e de entrada da segunda
	 * @param <C> O tipo de dados de retorno da segunda função
	 * @param function1 Uma função <b>f: A -> B</b>, a ser feita a composição.
	 * @param function2 Uma função <b>g: B -> C</b>, a ser feita a composição.
	 * @return Uma função <b>h: A -> C</b>, que representa a composição das duas funções.
	 */
	public static <A, B, C> Function1<A, C> compose(final Function1<? super A, B> function1, final Function1<? super B, C> function2)
	{
		return new Function1<A, C>()
		{
			@Override
			public C apply(final A param)
			{
				return function2.apply(function1.apply(param));
			}
		};
	}
	
	/**
	 * Faz a composição de uma função parcial com uma função de mapeamento
	 */
	public static <A, B, C> PartialFunction<A, C> compose(final PartialFunction<? super A, B> function1, final Function1<? super B, C> function2)
	{
		return new PartialFunction<A, C>()
		{
			@Override
			public C apply(final A param)
			{
				return function2.apply(function1.apply(param));
			}

			@Override
			public boolean isDefinedAt(A forValue)
			{
				return function1.isDefinedAt(forValue);
			}
		};
	}
	
	/**
	 * Faz a composição de uma função de mapeamento com uma função parcial
	 */
	public static <A, B, C> PartialFunction<A, C> compose(final Function1<? super A, B> function1, final PartialFunction<? super B, C> function2)
	{
		return new PartialFunction<A, C>()
		{
			@Override
			public C apply(final A param)
			{
				return function2.apply(function1.apply(param));
			}

			@Override
			public boolean isDefinedAt(A forValue)
			{
				return function2.isDefinedAt(function1.apply(forValue));
			}
		};
	}

	/**
	 * Faz a composição de duas funções parciais. <br/>
	 * A função parcial retornada está definida apenas nos pontos em que as duas funções parciais 
	 * estão definidas.
	 * 
	 * @param function1
	 * @param function2
	 * 
	 * @return Uma função parcial que representa a composição das duas funções parciais passadas como
	 * parâmetro. O domínio da função parcial será a interseção dos domínios da function1 e function2
	 */
	public static <A, B, C> PartialFunction<A, C> compose(final PartialFunction<? super A, B> function1,
			final PartialFunction<? super B, C> function2)
	{
		return new PartialFunction<A, C>()
		{
			@Override
			public C apply(final A param)
			{
				return function2.apply(function1.apply(param));
			}

			@Override
			public boolean isDefinedAt(final A forValue)
			{
				return function1.isDefinedAt(forValue) && function2.isDefinedAt(function1.apply(forValue));
			}
		};
	}

	/**
	 * Retorna uma função predicado que inverte a lógica da função passada como parâmetro
	 * 
	 * @param theFunction A função cuja lógica booleana deverá ser invertida
	 * @return Uma função cujo resultado será o inverso do resultado da função passada como parâmetro
	 */
	public static <A> Function1<A, Boolean> not(final Function1<A, Boolean> theFunction)
	{
		return new Function1<A, Boolean>()
		{
			@Override
			public Boolean apply(final A param)
			{
				return !theFunction.apply(param);
			}
		};
	}

	/**
	 * Cria uma função parcial a partir de uma função predicado e uma função de transformação.
	 * 
	 * @param pred A função predicado, que irá definir os argumentos que a função parcial está definida
	 * @param transform A função de mapeamento, a ser usada quando a função estiver definida para o argumento
	 * @return Uma função parcial a partir da função predicado e de transformação passadas.
	 */
	public static <A, B> PartialFunction<A, B> createPartial(final Function1<? super A, Boolean> pred, final Function1<? super A, B> transform)
	{
		return new PartialCreation<A, B>(pred, transform);
	}

	private static final class PartialCreation<A, B> implements PartialFunction<A, B>
	{
		private final Function1<? super A, Boolean> predicate;
		private final Function1<? super A, B> mappingFunction;

		public PartialCreation(final Function1<? super A, Boolean> thePredicate, final Function1<? super A, B> theMappingFunction)
		{
			super();
			this.predicate = thePredicate;
			this.mappingFunction = theMappingFunction;
		}

		@Override
		public B apply(final A param)
		{
			return mappingFunction.apply(param);
		}

		@Override
		public boolean isDefinedAt(final A forValue)
		{
			return predicate.apply(forValue);
		}

	}

}
