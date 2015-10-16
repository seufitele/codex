package com.github.detentor.codex.function.arrow;

import com.github.detentor.codex.function.Function1;

/**
 * Setas são funções mais poderosas.
 * 
 * @author Vinicius Seufitele Pinto
 * 
 * @param <A>
 * @param <B>
 */
public abstract class Arrow1<A, B> implements Function1<A, B>, Arrow
{
	@Override
	public int getArity()
	{
		return 1;
	}

	/**
	 * Faz a composição desta seta com a seta passada como parâmetro. <br/>
	 * Formalmente, seja
	 * 
	 * <pre>
	 * f: A -> B e g: B -> C. Então, será retornado um h: A -> C.
	 * </pre>
	 * 
	 * @param <A> O tipo de dados de entrada da primeira seta
	 * @param <B> O tipo de dados de retorno da primeira seta, e de entrada da segunda (contravariante)
	 * @param <C> O tipo de dados de retorno da segunda seta
	 * @param func Uma função <b>g: B -> C</b>, a ser feita a composição.
	 * @return Uma seta <b>h: A -> C</b>, que representa a composição das duas setas.
	 */
	public <C> Arrow1<A, C> and(final Function1<? super B, C> func)
	{
		return new Arrow1<A, C>()
		{
			@Override
			public C apply(final A param)
			{
				return func.apply(Arrow1.this.apply(param));
			}
		};
	}

	/**
	 * Retorna uma seta de aridade 0, a partir da aplicação do primeiro parâmetro. <br/>
	 * Essa técnica é também conhecida como Currying.
	 * 
	 * @param param1 O parâmetro a ser aplicado parcialmente nesta seta
	 * @return Uma seta de aridade 0, que representa esta seta com o parâmetro passado fixado
	 */
	public Arrow0<B> applyPartial(final A param1)
	{
		return new Arrow0<B>()
		{
			@Override
			public B apply()
			{
				return Arrow1.this.apply(param1);
			}
		};
	}
}
