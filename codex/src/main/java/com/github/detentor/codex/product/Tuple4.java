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
 */
public final class Tuple4<A, B, C, D>
{
	private A val1;
	private B val2;
	private C val3;
	private D val4;

	private Tuple4(final A valor1, final B valor2, final C valor3, final D valor4)
	{
		this.val1 = valor1;
		this.val2 = valor2;
		this.val3 = valor3;
		this.val4 = valor4;
	}

	/**
	 * Cria uma tuple 4 a partir dos valores passados como parâmetro 
	 * @param <E> O tipo de dados do primeiro valor
	 * @param <F> O tipo de dados do segundo valor
	 * @param <G> O tipo de dados do terceiro valor
	 * @param <H> O tipo de dados do quarto valor
	 * @param valor1 O valor do primeiro elemento
	 * @param valor2 O valor do segundo elemento
	 * @param valor3 O valor do terceiro elemento
	 * @param valor4 O valor do quarto elemento
	 * @return Uma tuple4 com os valores passados como parâmetro
	 */
	public static <E, F, G, H> Tuple4<E, F, G, H> from(final E valor1, final F valor2, final G valor3, final H valor4)
	{
		return new Tuple4<E, F, G, H>(valor1, valor2, valor3, valor4);
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

	public D getVal4()
	{
		return val4;
	}

	public void setVal4(final D theVal4)
	{
		this.val4 = theVal4;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((val1 == null) ? 0 : val1.hashCode());
		result = prime * result + ((val2 == null) ? 0 : val2.hashCode());
		result = prime * result + ((val3 == null) ? 0 : val3.hashCode());
		result = prime * result + ((val4 == null) ? 0 : val4.hashCode());
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
		final Tuple4 other = (Tuple4) obj;
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
		return true;
	}

	@Override
	public String toString()
	{
		return "Tuple(" + val1 + ", " + val2 + ", " + val3 +  ", " + val4 + ")";
	}

}
