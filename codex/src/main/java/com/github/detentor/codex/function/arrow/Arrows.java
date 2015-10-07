package com.github.detentor.codex.function.arrow;

import com.github.detentor.codex.function.Function1;


/**
 * Classe que possui métodos helpers para Setas
 * 
 * @author Vinicius Seufitele Pinto
 *
 */
public final class Arrows
{
	private Arrows()
	{
		//Previne instanciação
	}
	
	/**
	 * Promove a função passada como parâmetro a seta.
	 * 
	 * @param func A função a ser transformada em seta
	 * @return Uma seta, cujo comportamento será o mesmo da função passada como parâmetro
	 */
	public static <C, D> Arrow1<C, D> lift(final Function1<C, D> func)
	{
		return new Arrow1<C, D>()
		{
			@Override
			public D apply(final C param)
			{
				return func.apply(param);
			}
		};
	}

	/**
	 * Faz a composição de duas setas passadas como parâmetro. <br/>
	 * Formalmente, seja
	 * 
	 * <pre>
	 * f: A -> B e g: B -> C. Então, será retornado um h: A -> C.
	 * </pre>
	 * 
	 * @param <A> O tipo de dados de entrada da primeira seta
	 * @param <B> O tipo de dados de retorno da primeira seta, e de entrada da segunda
	 * @param <C> O tipo de dados de retorno da segunda seta
	 * @param arrow1 Uma seta <b>f: A -> B</b>, a ser feita a composição.
	 * @param arrow2 Uma seta <b>g: B -> C</b>, a ser feita a composição.
	 * @return Uma seta <b>h: A -> C</b>, que representa a composição das duas setas.
	 */
	public static <A, B, C> Arrow1<A, C> compose(final Arrow1<A, B> arrow1, final Arrow1<B, C> arrow2)
	{
		return new Arrow1<A, C>()
		{
			@Override
			public C apply(final A param)
			{
				return arrow2.apply(arrow1.apply(param));
			}
		};
	}
}
