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
 */
public class Tuple2<A, B> implements Product
{
	private static final long serialVersionUID = 1L;

	private A val1;
	private B val2;

	private Tuple2(final A valor1, final B valor2)
	{
		this.val1 = valor1;
		this.val2 = valor2;
	}

	/**
	 * Cria uma tupla 2 a partir dos valores valor1 e valor2 passados como parâmetro.
	 * @param <C> O tipo de dados do primeiro item
	 * @param <D> O tipo de dados do segundo item
	 * @param valor1 O valor do primeiro item
	 * @param valor2 O valor do segundo item
	 * @return Uma tupla 2 com os valores passados como parâmetro
	 */
	public static <C, D> Tuple2<C, D> from(final C valor1, final D valor2)
	{
		return new Tuple2<C, D>(valor1, valor2);
	}
	
	/**
	 * Cria uma tupla 2 inicializando os valores como null
	 * @return Uma tupla 2 onde o valor de cada elemento é null
	 */
	public static <C, D> Tuple2<C, D> empty()
	{
		return new Tuple2<C, D>(null, null);
	}

	/**
	 * Retorna uma nova tupla, com os elementos em posições trocadas.
	 * @return Uma nova tupla, onde a posição dos elementos está invertida.
	 */
	public Tuple2<B, A> swap()
	{
		return Tuple2.from(this.getVal2(), this.getVal1());
	}
	
	@Override
	public <K> Tuple3<A, B, K> add(final K value)
	{
		return Tuple3.from(this.val1, this.val2, value);
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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((val1 == null) ? 0 : val1.hashCode());
		result = prime * result + ((val2 == null) ? 0 : val2.hashCode());
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
		final Tuple2 other = (Tuple2) obj;
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
		return true;
	}

	@Override
	public String toString()
	{
		return "Tuple(" + val1 + ", " + val2 + ")";
	}

}
