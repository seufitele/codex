package com.github.detentor.codex.monoids;

import com.github.detentor.codex.cat.Monoid;

/**
 * Essa classe representa o dual de uma monóide, objetida a partir da inversão dos argumentos de {@link #reduce(Monoid)}
 *
 * @param <A> O tipo da monóide
 */
public class Dual<A> implements Monoid<A>
{
	private final Monoid<A> theMonoid;

	protected Dual(final Monoid<A> theMonoid)
	{
		super();
		this.theMonoid = theMonoid;
	}

	/**
	 * Cria o Dual do monóide passado como parâmetro
	 * 
	 * @param monoid O monóide cujo dual será criado
	 * @return O Dual do monóide passado como parâmetro
	 */
	public static <A> Dual<A> from(final Monoid<A> monoid)
	{
		return new Dual<>(monoid);
	}

	/**
	 * Retorna o dual deste monóide (ou seja, o monóide original)
	 * 
	 * @return O dual deste monóide
	 */
	public Monoid<A> getDual()
	{
		return theMonoid;
	}

	@Override
	public A empty()
	{
		return theMonoid.empty();
	}

	@Override
	public A reduce(final A param1, final A param2)
	{
		return theMonoid.reduce(param2, param1);
	}
}
