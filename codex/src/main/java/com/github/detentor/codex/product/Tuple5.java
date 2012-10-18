package com.github.detentor.codex.product;

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
 * @param <D> O objeto do quarto tipo
 * @param <E> O objeto do quinto tipo
 */
public final class Tuple5<A, B, C, D, E>
{
	private A val1;
	private B val2;
	private C val3;
	private D val4;
	private E val5;

	private Tuple5(final A valor1, final B valor2, final C valor3, final D valor4, final E valor5)
	{
		this.val1 = valor1;
		this.val2 = valor2;
		this.val3 = valor3;
		this.val4 = valor4;
		this.val5 = valor5;
	}

	/**
	 * Cria uma tuple 5 a partir dos valores passados como parâmetro
	 * 
	 * @param <E> O tipo de dados do primeiro valor
	 * @param <F> O tipo de dados do segundo valor
	 * @param <G> O tipo de dados do terceiro valor
	 * @param <H> O tipo de dados do quarto valor
	 * @param <I> O tipo de dados do quinto valor
	 * @param valor1 O valor do primeiro elemento
	 * @param valor2 O valor do segundo elemento
	 * @param valor3 O valor do terceiro elemento
	 * @param valor4 O valor do quarto elemento
	 * @param valor5 O valor do quinto elemento
	 * @return Uma tuple5 com os valores passados como parâmetro
	 */
	public static <E, F, G, H, I> Tuple5<E, F, G, H, I> from(final E valor1, final F valor2, final G valor3, final H valor4, final I valor5)
	{
		return new Tuple5<E, F, G, H, I>(valor1, valor2, valor3, valor4, valor5);
	}

	/**
	 * Cria uma tupla 5 inicializando os valores como null
	 * @return Uma tupla 5 onde o valor de cada elemento é null
	 */
	public static <E, F, G, H, I> Tuple5<E, F, G, H, I> empty()
	{
		return new Tuple5<E, F, G, H, I>(null, null, null, null, null);
	}

	public A getVal1()
	{
		return val1;
	}

	public void setVal1(final A theVal1)
	{
		this.val1 = theVal1;
	}

	public B getVal2()
	{
		return val2;
	}

	public void setVal2(final B theVal2)
	{
		this.val2 = theVal2;
	}

	public C getVal3()
	{
		return val3;
	}

	public void setVal3(final C theVal3)
	{
		this.val3 = theVal3;
	}

	public D getVal4()
	{
		return val4;
	}

	public void setVal4(final D theVal4)
	{
		this.val4 = theVal4;
	}

	public E getVal5()
	{
		return val5;
	}

	public void setVal5(final E theVal5)
	{
		this.val5 = theVal5;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (val1 == null ? 0 : val1.hashCode());
		result = prime * result + (val2 == null ? 0 : val2.hashCode());
		result = prime * result + (val3 == null ? 0 : val3.hashCode());
		result = prime * result + (val4 == null ? 0 : val4.hashCode());
		result = prime * result + (val5 == null ? 0 : val5.hashCode());
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
		final Tuple5 other = (Tuple5) obj;
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
		if (val4 == null)
		{
			if (other.val4 != null)
			{
				return false;
			}
		}
		else if (!val4.equals(other.val4))
		{
			return false;
		}
		if (val5 == null)
		{
			if (other.val5 != null)
			{
				return false;
			}
		}
		else if (!val5.equals(other.val5))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "Tuple(" + val1 + ", " + val2 + ", " + val3 + ", " + val4 + ", " + val5 + ")";
	}

}
