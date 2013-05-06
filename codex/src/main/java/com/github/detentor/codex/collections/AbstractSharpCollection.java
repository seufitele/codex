package com.github.detentor.codex.collections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Function2;
import com.github.detentor.codex.function.PartialFunction;
import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;

/**
 * Classe que provê a implementação padrão de diversos métodos de coleções, para simplificar a criação de classes que os estenda. <br/>
 * <br/>
 * 
 * Para criar uma coleção (imutável) com base nesta implementação, basta prover o código dos seguintes métodos: <br/><br/>
 * 
 * {@link Iterable#iterator() iterator()}, {@link SharpCollection#size() size()} , {@link SharpCollection#builder() builder()} <br/><br/>
 * 
 * Não esquecer de sobrescrever o equals e o hashcode também. <br/><br/>
 * 
 * Para coleções mutáveis, veja {@link AbstractMutableCollection}. <br/><br/>
 * Subclasses que não possuam size() facilmente calculável devem sobrescrever o método isEmpty(). <br/> <br/>
 * 
 * NOTA: Subclasses devem sempre dar override nos métodos {@link #map(Function1) map}, {@link #collect(PartialFunction) collect},
 * {@link #flatMap(Function1) flatMap} e {@link #zipWithIndex() zipWithIndex}.   
 * Devido à incompetência do Java com relação a Generics, isso é necessário para assegurar que o tipo
 * de retorno seja o mesmo da coleção. A implementação padrão (chamado o método da super classe é suficiente).
 * 
 * @author Vinícius Seufitele Pinto
 * 
 */
public abstract class AbstractSharpCollection<T, U extends SharpCollection<T>> implements SharpCollection<T>, Convertable<T>,
		ComparisonFunctions<T>
{
	private static final String UNCHECKED = "unchecked";

	@Override
	public boolean isEmpty()
	{
		return this.size() == 0;
	}

	@Override
	public boolean notEmpty()
	{
		return !isEmpty();
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
			if (!this.contains(ele))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public T head()
	{
		ensureNotEmpty("head foi chamado para uma coleção vazia");
		return this.iterator().next();
	}

	@Override
	public Option<T> headOption()
	{
		return this.isEmpty() ? Option.<T> empty() : Option.from(this.head());
	}

	@Override
	public T last()
	{
		return takeRight(1).head();
	}

	@Override
	public Option<T> lastOption()
	{
		return this.isEmpty() ? Option.<T> empty() : Option.from(this.last());
	}

	@SuppressWarnings(UNCHECKED)
	@Override
	public U tail()
	{
		ensureNotEmpty("tail foi chamado para uma coleção vazia");

		final Builder<T, SharpCollection<T>> colecaoRetorno = this.builder();
		final Iterator<T> ite = this.iterator();

		ite.next(); // Pula o primeiro elemento

		while (ite.hasNext())
		{
			colecaoRetorno.add(ite.next());
		}
		return (U) colecaoRetorno.result();
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
	public Option<T> find(final Function1<? super T, Boolean> pred)
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
	public U filter(final Function1<? super T, Boolean> pred)
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
	
	@Override
	public Tuple2<? extends SharpCollection<T>, ? extends SharpCollection<T>> partition(final Function1<? super T, Boolean> pred)
	{
		final Builder<T, SharpCollection<T>> predTrue = builder();
		final Builder<T, SharpCollection<T>> predFalse = builder();
		
		for (final T ele : this)
		{
			if (pred.apply(ele))
			{
				predTrue.add(ele);
			}
			else
			{
				predFalse.add(ele);
			}
		}
		return Tuple2.from(predTrue.result(), predFalse.result());
	}

	@Override
	public boolean exists(final Function1<? super T, Boolean> pred)
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
	public boolean forall(final Function1<? super T, Boolean> pred)
	{
		for (final T ele : this)
		{
			if (!pred.apply(ele))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public Integer count(final Function1<? super T, Boolean> pred)
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
	public <B> B foldLeft(final B startValue, final Function2<B, ? super T, B> function)
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
	public T maxWith(final Comparator<? super T> comparator)
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
	public T minWith(final Comparator<? super T> comparator)
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

	@Override
	public Option<T> minOption()
	{
		return this.isEmpty() ? Option.<T>empty() : Option.from(min());
	}

	@Override
	public Option<T> maxOption()
	{
		return this.isEmpty() ? Option.<T>empty() : Option.from(max());
	}

	@SuppressWarnings(UNCHECKED)
	@Override
	public U intersect(final SharpCollection<T> withCollection)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = builder();

		for (final T ele : this)
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

		for (final T ele : this)
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

		for (final T ele : this)
		{
			colecaoRetorno.add(Tuple2.from(ele, ++curIndex));
		}
		return colecaoRetorno.result();
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
	
	/**
	 * Essa classe é um builder para Set baseado em um HashSet. <br/>
	 * @param <E> O tipo de dados armazenado no HashSet.
	 */
	private static final class HashSetBuilder<E> implements Builder<E, Set<E>>
	{
		private final Set<E> backingSet = new HashSet<E>();

		@Override
		public void add(final E element)
		{
			backingSet.add(element);
		}

		@Override
		public Set<E> result()
		{
			return backingSet;
		}
	}
	
	/**
	 * Essa classe é um builder para List baseado em um ArrayList.
	 * @param <E> O tipo de dados do ArrayList retornado
	 */
	private static final class ArrayListBuilder<E> implements Builder<E, List<E>>
	{
		private final List<E> list = new ArrayList<E>();

		@Override
		public void add(final E element)
		{
			list.add(element);
		}

		@Override
		public List<E> result()
		{
			return list;
		}
	}
}
