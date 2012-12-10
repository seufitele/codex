package com.github.detentor.codex.collections;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.github.detentor.codex.collections.builders.java.ArrayListBuilder;
import com.github.detentor.codex.collections.builders.java.HashSetBuilder;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Function2;
import com.github.detentor.codex.function.PartialFunction;
import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;


public abstract class AbstractSharpCollectionNew<T, U extends SharpCollection<T>> implements SharpCollection<T>, Convertable<T>, ComparisonFunctions<T>
{
	private static final String UNCHECKED = "unchecked";

	@Override
	public Option<T> headOption()
	{
		return this.isEmpty() ? Option.<T>empty() : Option.from(this.head());
	}
	
	@Override
	public Option<T> lastOption()
	{
		return this.isEmpty() ? Option.<T>empty() : Option.from(this.last());
	}

	@Override
	public boolean notEmpty()
	{
		return ! isEmpty();
	}

	@Override
	public boolean contains(final T element)
	{
		for (final T ele : this)
		{
			if (ele.equals(element))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(final SharpCollection<T> col)
	{
		for (final T ele : col)
		{
			if (! this.contains(ele))
			{
				return false;
			}
		}
		return true;
	}
	
	
	@Override
	public String mkString()
	{
		return mkString("", "", "");
	}

	@Override
	public String mkString(final String separator)
	{
		return mkString("", separator, "");
	}
	
	@Override
	public String mkString(final String start, final String separator, final String end)
	{
		final StringBuilder sBuilder = new StringBuilder();
		final Iterator<T> ite = this.iterator();

		sBuilder.append(start);

		while (ite.hasNext())
		{
			sBuilder.append(ite.next());
			if (ite.hasNext())
			{
				sBuilder.append(separator);
			}
		}
		sBuilder.append(end);
		return sBuilder.toString();
	}
	
	
	
	@Override
	public Option<T> find(final Function1<T, Boolean> pred)
	{
		final Iterator<T> ite = this.iterator();
		
		while (ite.hasNext())
		{
			final T curEle = ite.next();

			if (pred.apply(curEle))
			{
				return Option.from(curEle);
			}
		}
		return Option.empty();
	}
	
	@SuppressWarnings(UNCHECKED)
	@Override
	public U filter(final Function1<T, Boolean> pred)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = builder();
		
		for (final T ele : this)
		{
			if (pred.apply(ele))
			{
				colecaoRetorno.add(ele);
			}
		}
		return (U) colecaoRetorno.result();
	}
	
	@SuppressWarnings(UNCHECKED)
	@Override
	public U filterNot(final Function1<T, Boolean> pred)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = builder();
		
		for (final T ele : this)
		{
			if (!pred.apply(ele))
			{
				colecaoRetorno.add(ele);
			}
		}
		return (U) colecaoRetorno.result();
	}
	
	
	@Override
	public boolean exists(final Function1<T, Boolean> pred)
	{
		for (final T ele : this)
		{
			if (pred.apply(ele))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean forall(final Function1<T, Boolean> pred)
	{
		for (final T ele : this)
		{
			if (! pred.apply(ele))
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public Integer count(final Function1<T, Boolean> pred)
	{
		int numElementos = 0;
		
		for (final T ele : this)
		{
			if (pred.apply(ele))
			{
				numElementos++;
			}
		}
		return numElementos;
	}
	
	@Override
	public <B> B foldLeft(final B startValue, final Function2<B, T, B> function)
	{
		B accumulator = startValue;

		for (final T ele : this)
		{
			accumulator = function.apply(accumulator, ele);
		}
		return accumulator;
	}
	
	@Override
	public <B> SharpCollection<B> map(final Function1<? super T, B> function)
	{
		final Builder<B, SharpCollection<B>> colecaoRetorno = builder();

		for (final T ele : this)
		{
			colecaoRetorno.add(function.apply(ele));
		}
		return colecaoRetorno.result();
	}
	
	@Override
	public <B> SharpCollection<B> collect(final PartialFunction<? super T, B> pFunction)
	{
		final Builder<B, SharpCollection<B>> colecaoRetorno = builder();

		for (final T ele : this)
		{
			if (pFunction.isDefinedAt(ele))
			{
				colecaoRetorno.add(pFunction.apply(ele));
			}
		}
		return colecaoRetorno.result();
	}

	@Override
	public <B> SharpCollection<B> flatMap(final Function1<? super T, ? extends SharpCollection<B>> function)
	{
		final Builder<B, SharpCollection<B>> colecaoRetorno = builder();

		for (final T ele : this)
		{
			for (final B mappedEle : function.apply(ele))
			{
				colecaoRetorno.add(mappedEle);
			}
		}
		return colecaoRetorno.result();
	}
	
	
	@Override
	public T maxWith(final Comparator<T> comparator)
	{
		ensureNotEmpty();
		
		final Iterator<T> ite = this.iterator();
		T maxValue = ite.next();
		
		while (ite.hasNext())
		{
			final T curEle = ite.next();
			if (comparator.compare(curEle, maxValue) > 0)
			{
				maxValue = curEle;
			}
		}
		return maxValue;
	}
	
	@Override
	public T minWith(final Comparator<T> comparator)
	{
		ensureNotEmpty();

		final Iterator<T> ite = this.iterator();
		T minValue = ite.next();
		
		while (ite.hasNext())
		{
			final T curEle = ite.next();
			if (comparator.compare(curEle, minValue) < 0)
			{
				minValue = curEle;
			}
		}
		return minValue;
	}
	
	@SuppressWarnings({ UNCHECKED, "rawtypes" })
	@Override
	public T min()
	{
		return minWith(new DefaultComparator());
	}

	@SuppressWarnings({ UNCHECKED, "rawtypes" })
	@Override
	public T max()
	{
		return maxWith(new DefaultComparator());
	}
	
	@SuppressWarnings(UNCHECKED)
	@Override
	public U intersect(final SharpCollection<T> withCollection)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = builder();
		
		for (T ele : this)
		{
			if (withCollection.contains(ele))
			{
				colecaoRetorno.add(ele);
			}
		}
		return (U) colecaoRetorno.result();
	}

	@SuppressWarnings(UNCHECKED)
	@Override
	public U distinct()
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = builder();
		int count = -1;
		
		for (T ele : this)
		{
			if (!this.take(++count).contains(ele))
			{
				colecaoRetorno.add(ele);
			}
		}
		return (U) colecaoRetorno.result();
	}
	
	@Override
	public SharpCollection<Tuple2<T, Integer>> zipWithIndex()
	{
		final Builder<Tuple2<T, Integer>, SharpCollection<Tuple2<T, Integer>>> colecaoRetorno = builder();
		int curIndex = -1;
		
		for (T ele : this)
		{
			colecaoRetorno.add(Tuple2.from(ele, ++curIndex));
		}
		return (SharpCollection<Tuple2<T, Integer>>) colecaoRetorno.result();
	}
	
	/**
	 * Método protegido, para métodos que precisam assegurar que a lista contenha elementos
	 */
	protected void ensureNotEmpty(final String message)
	{
		if (this.isEmpty())
		{
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * Método protegido, para métodos que precisam assegurar que a lista contenha elementos
	 */
	protected void ensureNotEmpty()
	{
		ensureNotEmpty("Método não definido para coleções vazias");
	}

	/**
     * Classe com a implementação default do comparator
     */
	private static final class DefaultComparator<A extends Comparable<A>> implements Comparator<A>, Serializable
	{
		private static final long serialVersionUID = 6163897449143010763L;

		@Override
		public int compare(final A ob1, final A ob2)
		{
			return ob1.compareTo(ob2);
		}
	}
	
	@Override
	public List<T> toList()
	{
		return toList(new ArrayListBuilder<T>());
	}

	@Override
	public List<T> toList(final Builder<T, List<T>> builder)
	{
		for (final T ele : this)
		{
			builder.add(ele);
		}
		return builder.result();
	}

	@Override
	public Set<T> toSet()
	{
		return toSet(new HashSetBuilder<T>());
	}

	@Override
	public Set<T> toSet(final Builder<T, Set<T>> builder)
	{
		for (final T ele : this)
		{
			builder.add(ele);
		}
		return builder.result();
	}

	
	
}