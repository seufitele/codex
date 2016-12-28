package com.github.detentor.codex.monads;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.github.detentor.codex.cat.Applicative;
import com.github.detentor.codex.cat.Monad;
import com.github.detentor.codex.collections.AbstractSharpCollection;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.arrow.Arrow1;
import com.github.detentor.codex.product.Tuple2;

/**
 * Option é uma mônade que representa um container que pode ou não conter um elemento. <br/>
 * <br/>
 * Option também pode ser vista como uma lista que pode ter, no máximo, um elemento. O valor informado na criação da Option determinará se
 * ela estará vazia ou não (a passagem do parâmetro com valor nulo cria uma Option vazia). <br/>
 * <br/>
 * 
 * Classe imutável.
 * 
 * @author Vinícius Seufitele Pinto
 * 
 * @param <T> O tipo do dado a ser guardado na Option
 */
@SuppressWarnings("rawtypes")
public class Option<T> extends AbstractSharpCollection<T, SharpCollection<T>> implements Serializable, Monad<T>
{
	private static final long serialVersionUID = 1L;

	private static final Option<Object> NONE = new Option<Object>();
	private final List<T> value = new ArrayList<T>(1);

	protected Option()
	{
		super();
	}

	protected Option(final T theValue)
	{
		this();
		value.add(theValue);
	}

	/**
	 * Retorna uma Option vazia.
	 * 
	 * @param <T> O tipo do dado que a Option deveria conter
	 * @return Uma Option vazia
	 */
	@SuppressWarnings("unchecked")
	public static <T> Option<T> empty()
	{
		return (Option<T>) NONE;
	}

	/**
	 * Cria uma Option a partir do valor passado como parâmetro. <br/>
	 * Se o valor estiver nulo, a Option retornada estará vazia. Do contrário, ela conterá o valor.
	 * 
	 * @param <T> O tipo do dado que a Option poderá conter
	 * @return Uma Option que conterá theValue, se ele for não-nulo
	 */
	@SuppressWarnings("unchecked")
	public static <T> Option<T> from(final T theValue)
	{
		return theValue == null ? (Option<T>) NONE : new Option<T>(theValue);
	}

	/**
	 * Retorna o valor T se ele existir nesta Option. Chamar esse método para uma Option vazia disparará uma exceção.
	 * 
	 * @return O valor T guardado nesta Option (se ele existir)
	 * @throws NoSuchElementException Se a Option estiver vazio
	 */
	public T get()
	{
		if (value.isEmpty())
		{
			throw new NoSuchElementException("option não possui elementos");
		}
		return value.get(0);
	}

	/**
	 * Retorna o valor contido nesta Option, ou o valor determinado pela parte do else se ela estiver vazia.
	 * 
	 * @param elsePart O valor a ser retornado, caso esta Option esteja vazia
	 * @return Um valor T, que será o valor desta Option, ou a parte do else.
	 */
	public T getOrElse(final T elsePart)
	{
		return value.isEmpty() ? elsePart : get();
	}

	@Override
	public String toString()
	{
		return this.isEmpty() ? "None" : "Some(" + get().toString() + ")";
	}

	@Override
	public int size()
	{
		return value.isEmpty() ? 0 : 1;
	}

	@Override
	public Iterator<T> iterator()
	{
		return new Iterator<T>()
		{
			boolean readValue = false;

			@Override
			public boolean hasNext()
			{
				return notEmpty() && !readValue;
			}

			@Override
			public T next()
			{
				if (readValue || isEmpty())
				{
					throw new NoSuchElementException("o iterator não possui mais elementos");
				}
				readValue = true;
				return Option.this.get();
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException("operação não suportada");
			}
		};
	}

	@Override
	public <B> Builder<B, SharpCollection<B>> builder()
	{
		return new OptionBuilder<B>();
	}

	@Override
	public <B> Option<B> map(final Function1<? super T, B> function)
	{
		return (Option<B>) super.map(function);
	}

	@Override
	public <B> Option<B> flatMap(final Function1<? super T, ? extends Iterable<B>> function)
	{
		return (Option<B>) super.flatMap(function);
	}

	@Override
	public Option<T> filter(final Function1<? super T, Boolean> pred)
	{
		return (Option<T>) super.filter(pred);
	}

	@Override
	public Option<Tuple2<T, Integer>> zipWithIndex()
	{
		return (Option<Tuple2<T, Integer>>) super.zipWithIndex();
	}

	@Override
	public Option<T> sorted()
	{
		return this;
	}

	@Override
	public Option<T> sorted(final Comparator<? super T> comparator)
	{
		return this;
	}

	@Override
	public <B> Option<B> pure(final B value)
	{
		return Option.from(value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <B> Option<B> ap(final Applicative<Function1<? super T, B>> applicative)
	{
		final Option<Function1<? super T, B>> apOption = (Option<Function1<? super T, B>>) applicative;
		
		if (apOption.isEmpty())
		{
			return (Option<B>) this;
		}
		
		return this.map(apOption.get());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <B> Option<B> bind(final Function1<? super T, Monad<B>> function)
	{
		if (this.isEmpty())
		{
			return (Option<B>) this;
		}

		return (Option<B>) function.apply(this.get());
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + value.hashCode();
		return result;
	}

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
		return value.equals(((Option) obj).value);
	}

	/**
	 * Essa classe é um builder para SharpCollection baseado em um Option.
	 * 
	 * @param <E> O tipo de dados do Option retornado
	 */
	private class OptionBuilder<E> implements Builder<E, SharpCollection<E>>
	{
		private boolean added = false;
		private E valor = null;

		@Override
		public void add(final E element)
		{
			if (added)
			{
				throw new IllegalStateException("tentou-se adicionar mais de um elemento no builder do option");
			}
			added = true;
			valor = element;
		}

		@Override
		public Option<E> result()
		{
			return Option.from(valor);
		}
	}

	/**
	 * Transforma uma função que (potencialmente) retorna null para uma seta null-safe
	 * 
	 * @param func A função que será transformada
	 * @return Uma seta que garante que o resultado da função será null-safe
	 */
	public static <A, B> Arrow1<A, Option<B>> lift(final Function1<A, B> func)
	{
		return new Arrow1<A, Option<B>>()
		{
			@Override
			public Option<B> apply(final A param)
			{
				return Option.from(func.apply(param));
			}
		};
	}
}
