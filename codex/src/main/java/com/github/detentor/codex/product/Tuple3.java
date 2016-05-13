package com.github.detentor.codex.product;

import com.github.detentor.codex.cat.Functor;
import com.github.detentor.codex.function.Function1;

/**
 * Na teoria dos conjuntos, uma n-tuple (enupla) é uma sequência de n elementos, onde n é um inteiro positivo.<br/>
 * Uma enupla é igual a outra se, e somente se, os seus valores são iguais.<br/>
 * <br/>
 * 
 * @author Vinícius Seufitele Pinto
 * 
 * @param <A> O objeto do primeiro tipo
 * @param <B> O objeto do segundo tipo
 * @param <C> O objeto do terceiro tipo
 */
public final class Tuple3<A, B, C> implements Product, Functor<A>
{
	private static final long serialVersionUID = 1L;

	private A val1;
	private B val2;
	private C val3;

	private Tuple3(final A valor1, final B valor2, final C valor3)
	{
		this.val1 = valor1;
		this.val2 = valor2;
		this.val3 = valor3;
	}

	/**
	 * Cria uma tuple 3 a partir dos valores passados como parâmetro 
	 * @param <D> O tipo de dados do primeiro valor
	 * @param <E> O tipo de dados do segundo valor
	 * @param <F> O tipo de dados do terceiro valor
	 * @param valor1 O valor do primeiro elemento
	 * @param valor2 O valor do segundo elemento
	 * @param valor3 O valor do terceiro elemento
	 * @return Uma tuple3 com os valores passados como parâmetro
	 */
	public static <D, E, F> Tuple3<D, E, F> from(final D valor1, final E valor2, final F valor3)
	{
		return new Tuple3<D, E, F>(valor1, valor2, valor3);
	}
	
	/**
	 * Cria uma tupla 3 inicializando os valores como null
	 * @return Uma tupla 3 onde o valor de cada elemento é null
	 */
	public static <D, E, F> Tuple3<D, E, F> empty()
	{
		return new Tuple3<D, E, F>(null, null, null);
	}
	
	@Override
	public <K> Tuple4<A, B, C, K> add(final K value)
	{
		return Tuple4.from(this.val1, this.val2, this.val3, value);
	}
	
	@Override
	public <U> Tuple3<U, B, C> map(final Function1<? super A, U> function)
	{
		return Tuple3.from(function.apply(val1), val2, val3);
	}

	public A getVal1()
	{
		return val1;
	}

	public B getVal2()
	{
		return val2;
	}

	public C getVal3()
	{
		return val3;
	}

	public void setVal1(final A theVal1)
	{
		this.val1 = theVal1;
	}

	public void setVal2(final B theVal2)
	{
		this.val2 = theVal2;
	}

	public void setVal3(final C theVal3)
	{
		this.val3 = theVal3;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((val1 == null) ? 0 : val1.hashCode());
		result = prime * result + ((val2 == null) ? 0 : val2.hashCode());
		result = prime * result + ((val3 == null) ? 0 : val3.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final Tuple3 other = (Tuple3) obj;
		if (val1 == null)
		{
			if (other.val1 != null)
			{
				return false;
			}
		}
		else if (!val1.equals(other.val1))
		{
			return false;
		}
		if (val2 == null)
		{
			if (other.val2 != null)
			{
				return false;
			}
		}
		else if (!val2.equals(other.val2))
		{
			return false;
		}
		if (val3 == null)
		{
			if (other.val3 != null)
			{
				return false;
			}
		}
		else if (!val3.equals(other.val3))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "Tuple(" + val1 + ", " + val2 + ", " + val3 + ")";
	}
}
