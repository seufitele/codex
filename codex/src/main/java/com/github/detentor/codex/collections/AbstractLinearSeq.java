package com.github.detentor.codex.collections;

import java.util.Iterator;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.PartialFunction;

/**
 * Classe que provê a implementação padrão de métodos de coleções lineares, para simplificar a criação de classes que os estenda. <br/>
 * <br/>
 * 
 * Para criar uma coleção (imutável) com base nesta implementação, basta prover o código dos seguintes métodos: <br/>
 * <br/>
 * 
 * {@link #head()}, {@link #tail()}, {@link #isEmpty()}, {@link SharpCollection#builder() builder()} <br/>
 * <br/>
 * 
 * O método {@link #size()} tem implementação baseada no iterator (complexidade O(n)). <br/>
 * <br/>
 * 
 * O {@link #equals(Object)} e o {@link #hashCode()} são padrão para todas as sequências lineares. <br/><br/>
 * 
 * Para coleções mutáveis, veja {@link AbstractMutableLinearSeq}. <br/>
 * <br/>
 * 
 * NOTA: Subclasses devem sempre dar override nos métodos {@link #map(Function1) map}, {@link #collect(PartialFunction) collect} e
 * {@link #flatMap(Function1) flatMap}. Devido à incompetência do Java com relação a Generics, isso é necessário para assegurar que o
 * tipo de retorno seja o mesmo da coleção. A implementação padrão (chamado o método da super classe é suficiente).
 * 
 * @author Vinícius Seufitele Pinto
 * 
 */
public abstract class AbstractLinearSeq<T, U extends SharpCollection<T>> extends AbstractSeq<T, LinearSeq<T>> implements LinearSeq<T>
{
	private static final String UNCHECKED = "unchecked";
	
	@Override
	public boolean isDefinedAt(final Integer forValue)
	{
		return forValue >= 0 && forValue < this.size();
	}

	@Override
	public T apply(final Integer param)
	{
		return param.equals(0) ? this.head() : this.tail().apply(param - 1);
	}

	@Override
	public T last()
	{
		ensureNotEmpty("last foi chamado para uma coleção vazia");
		return this.apply(this.size() - 1);
	}

	@SuppressWarnings(UNCHECKED)
	@Override
	public U take(final Integer num)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = this.builder();
		final Iterator<T> ite = this.iterator();
		int count = 0;

		while (count++ < num && ite.hasNext())
		{
			colecaoRetorno.add(ite.next());
		}
		return (U) colecaoRetorno.result();
	}

	@SuppressWarnings(UNCHECKED)
	@Override
	public U takeRight(final Integer num)
	{
		final int eleToSkip = Math.max(this.size() - num, 0);
		final Builder<T, SharpCollection<T>> colecaoRetorno = this.builder();
		final Iterator<T> ite = this.iterator();
		int curCount = 0;

		while (ite.hasNext() && curCount < eleToSkip)
		{
			ite.next();
			curCount++;
		}

		while (ite.hasNext())
		{
			colecaoRetorno.add(ite.next());
		}
		return (U) colecaoRetorno.result();
	}

	@SuppressWarnings(UNCHECKED)
	@Override
	public U drop(final Integer num)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = this.builder();
		final Iterator<T> ite = this.iterator();

		int count = 0;

		while (count++ < num && ite.hasNext())
		{
			ite.next();
		}

		while (ite.hasNext())
		{
			colecaoRetorno.add(ite.next());
		}

		return (U) colecaoRetorno.result();
	}

	@SuppressWarnings(UNCHECKED)
	@Override
	public U dropRight(final Integer num)
	{
		final int toAdd = Math.max(0, this.size() - num);
		final Builder<T, SharpCollection<T>> colecaoRetorno = this.builder();
		final Iterator<T> ite = this.iterator();

		int count = 0;

		while (ite.hasNext() && count++ < toAdd)
		{
			colecaoRetorno.add(ite.next());
		}

		return (U) colecaoRetorno.result();
	}
	
	@SuppressWarnings(UNCHECKED)
	@Override
	public U dropWhile(final Function1<? super T, Boolean> pred)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = builder();
		final Iterator<T> ite = this.iterator();

		while (ite.hasNext())
		{
			final T curEle = ite.next();

			if (!pred.apply(curEle))
			{
				colecaoRetorno.add(curEle);
				break;
			}
		}

		while (ite.hasNext())
		{
			colecaoRetorno.add(ite.next());
		}

		return (U) colecaoRetorno.result();
	}

	@SuppressWarnings("unchecked")
	@Override
	public U dropRightWhile(final Function1<? super T, Boolean> pred)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = builder();
		final Iterator<T> ite = this.iterator();

		Builder<T, SharpCollection<T>> tempCollection = builder();

		while (ite.hasNext())
		{
			final T curEle = ite.next();

			if (pred.apply(curEle))
			{
				// Esse predicado pode ser o último
				tempCollection.add(curEle);
			}
			else
			{
				// Adiciona os elementos que seriam descartados
				for (final T ele : tempCollection.result())
				{
					colecaoRetorno.add(ele);
				}
				// Adiciona o elemento atual
				colecaoRetorno.add(curEle);
				tempCollection = builder(); // reseta o builder
			}
		}
		return (U) colecaoRetorno.result();
	}

	@SuppressWarnings(UNCHECKED)
	@Override
	public U takeWhile(final Function1<? super T, Boolean> pred)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = builder();
		final Iterator<T> ite = this.iterator();

		while (ite.hasNext())
		{
			final T curEle = ite.next();

			if (pred.apply(curEle))
			{
				colecaoRetorno.add(curEle);
			}
			else
			{
				break;
			}
		}
		return (U) colecaoRetorno.result();
	}

	@SuppressWarnings("unchecked")
	@Override
	public U takeRightWhile(final Function1<? super T, Boolean> pred)
	{
		Builder<T, SharpCollection<T>> colecaoRetorno = builder();
		final Iterator<T> ite = this.iterator();

		while (ite.hasNext())
		{
			final T curEle = ite.next();

			if (pred.apply(curEle))
			{
				// Coleta os elementos que satisfazem o predicado
				colecaoRetorno.add(curEle);
			}
			else
			{
				colecaoRetorno = builder(); // reseta o builder
			}
		}
		return (U) colecaoRetorno.result();
	}
	
	@Override
	public int size()
	{
		int theSize = 0;
		LinearSeq<T> curEle = this;
		
		while (curEle.notEmpty())
		{
			curEle = curEle.tail();
			theSize++;
		}
		return theSize;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> iterator()
	{
		final LinearSeq<T>[] curEle = new LinearSeq[1];
		curEle[0] = this;

		return new Iterator<T>()
		{
			@Override
			public boolean hasNext()
			{
				return !curEle[0].isEmpty();
			}

			@Override
			public T next()
			{
				final T toReturn = curEle[0].head();
				curEle[0] = curEle[0].tail();
				return toReturn;
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException("Operação não suportada");
			}
		};
	}
}
