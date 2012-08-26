package br.com.codex.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import br.com.codex.collections.builders.java.ArrayListBuilder;
import br.com.codex.collections.builders.java.HashSetBuilder;
import br.com.codex.function.Function1;
import br.com.codex.function.Function2;
import br.com.codex.monads.Option;
import br.com.codex.product.Tuple2;

/**
 * Classe que provê a implementação padrão de diversos métodos de coleções, para simplificar a 
 * criação de classes que os estenda. <br/> <br/>
 * 
 * Para criar uma coleção (imutável) com base nesta implementação, basta prover o código dos seguintes métodos: <br/> <br/>
 * 
 * {@link Iterable#iterator() iterator()}, {@link SharpCollection#clear() clear()}, {@link SharpCollection#size() size()} , 
 * {@link SharpCollection#builder() builder()} <br/> <br/>
 * 
 * Não esquecer de sobrescrever o equals e o hashcode também. <br/><br/>
 * 
 * Para coleções mutáveis, veja {@link AbstractMutableCollection}. <br/>
 * Subclasses que não possuam size() facilmente calculável devem sobrescrever o método isEmpty(). <br/> <br/>
 * 
 * NOTA: Subclasses devem sempre dar override nos métodos Map e FlatMap. Devido à incompetência do Java com relação a Generics,
 * isso é necessário para assegurar que o tipo de retorno seja o mesmo da coleção. A implementação padrão (chamado o método da super
 * classe é suficiente).
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public abstract class AbstractSharpCollection<T, U extends SharpCollection<T>> 
									implements SharpCollection<T>, Convertable<T>, ComparisonFunctions<T>
{
	@Override
	public boolean isEmpty()
	{
		return this.size() == 0;
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
	public T head()
	{
		ensureNotEmpty("head foi chamado para uma coleção vazia");
		return this.iterator().next();
	}
	
	@Override
	public Option<T> headOption()
	{
		return this.isEmpty() ? Option.<T>empty() : Option.from(this.head());
	}

	@SuppressWarnings("unchecked")
	@Override
	public U tail()
	{
		ensureNotEmpty("tail foi chamado para uma coleção vazia");

		final Builder<T, SharpCollection<T>> colecaoRetorno = this.builder();
		final Iterator<T> ite = this.iterator();
		
		ite.next(); //Pula o primeiro elemento
		
		while (ite.hasNext())
		{
			colecaoRetorno.add(ite.next());
		}
		return (U) colecaoRetorno.result();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public U take(final Integer num)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = this.builder();
		final Iterator<T> ite = this.iterator();
		int count = 0;
		
		while (count < num && ite.hasNext())
		{
			colecaoRetorno.add(ite.next());
			count++;
		}
		return (U) colecaoRetorno.result();
	}

	@SuppressWarnings("unchecked")
	@Override
	public U drop(final Integer num)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = this.builder();
		final Iterator<T> ite = this.iterator();
		
		int count = 0;
		
		while (count < num && ite.hasNext())
		{
			ite.next();
			count++;
		}
		
		while (ite.hasNext())
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

	@SuppressWarnings("unchecked")
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
	public <B> SharpCollection<B> map(final Function1<T, B> function)
	{
		final Builder<B, SharpCollection<B>> colecaoRetorno = builder();

		for (final T ele : this)
		{
			colecaoRetorno.add(function.apply(ele));
		}
		return colecaoRetorno.result();
	}

	@Override
	public <B> SharpCollection<B> flatMap(final Function1<T, ? extends SharpCollection<B>> function)
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public T min()
	{
		return minWith(new ComparableToComparator());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public T max()
	{
		return maxWith(new ComparableToComparator());
	}
	
	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	@Override
	public U dropWhile(final Function1<T, Boolean> function)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = builder();
		final Iterator<T> ite = this.iterator();
		
		while (ite.hasNext())
		{
			final T curEle = ite.next();
			
			if (!function.apply(curEle))
			{
				colecaoRetorno.add(curEle);
				break;
			}
		}
		
		while(ite.hasNext())
		{
			colecaoRetorno.add(ite.next());
		}
		
		return (U) colecaoRetorno.result();
	}

	public SharpCollection<Tuple2<T, Integer>> zipWithIndex()
	{
		final Builder<Tuple2<T, Integer>, SharpCollection<Tuple2<T, Integer>>> colecaoRetorno = builder();
		int curIndex = 0;
		
		for (T ele : this)
		{
			colecaoRetorno.add(Tuple2.from(ele, curIndex++));
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
	
	
	//MÉTODOS PRIVADOS DESTA CLASSE
	
	/**
	 * Método privado, para métodos que precisam assegurar que a lista contenha elementos
	 */
	private void ensureNotEmpty(final String message)
	{
		if (this.isEmpty())
		{
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * Método privado, para métodos que precisam assegurar que a lista contenha elementos
	 */
	private void ensureNotEmpty()
	{
		ensureNotEmpty("Método não definido para coleções vazias");
	}
	
	/**
	 * Classe que converte um Comparable para Comparator
	 * @author f9540702 Vinícius Seufitele Pinto
	 *
	 * @param <A>
	 */
	private static final class ComparableToComparator<A extends Comparable<A>> implements Comparator<A>
	{
		@Override
		public int compare(final A ob1, final A ob2)
		{
			return ob1.compareTo(ob2);
		}
	}
	
}
