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
		//Evitando instanciação, para conformizar com o sonar.
	}
	
	/**
	 * Faz a composição de duas funções passadas como parâmetro. <br/>
	 * Formalmente, seja <pre>f: A -> B e g: B -> C. Então, será retornado um h: A -> C.</pre>
	 * 
	 * @param <A> O tipo de dados de entrada da primeira função
	 * @param <B> O tipo de dados de retorno da primeira função, e de entrada da segunda
	 * @param <C> O tipo de dados de retorno da segunda função
	 * @param function1 Uma função <b>f: A -> B</b>, a ser feita a composição.
	 * @param function2 Uma função <b>g: B -> C</b>, a ser feita a composição.
	 * @return Uma função <b>h: A -> C</b>, que representa a composição das duas funções.
	 */
	public static <A,B,C> Function1<A, C> compose(final Function1<A, B> function1, final Function1<B, C> function2)
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
}
