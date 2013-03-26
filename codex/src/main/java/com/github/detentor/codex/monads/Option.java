package com.github.detentor.codex.monads;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.github.detentor.codex.collections.AbstractGenericCollection;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.collections.builders.OptionBuilder;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.product.Tuple2;

/**
 * Option é uma mônade que representa uma operação que pode ou não retornar um valor. <br/>
 * <br/>
 * 
 * Option pode ser vista como uma lista, que estará vazia se a operação não foi bem-sucedida, ou conterá um elemento se ela foi.
 * 
 * @author Vinícius Seufitele Pinto
 * 
 * @param <T> O tipo de dados a ser guardado no option
 */
public class Option<T> extends AbstractGenericCollection<T, SharpCollection<T>> implements Serializable
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
	 * Cria um option para uma operação que falhou (Option está vazio)
	 * 
	 * @param <T> O tipo de dados que option deveria conter
	 * @return Uma option que representa uma computação não sucedida
	 */
	@SuppressWarnings("unchecked")
	public static <T> Option<T> empty()
	{
		return (Option<T>) NONE;
	}

	/**
	 * Cria um option para uma operação que falhou (Option está vazio)
	 * 
	 * @param <T> O tipo de dados que option deveria conter
	 * @return Uma option que representa uma computação não sucedida
	 */
	@SuppressWarnings("unchecked")
	public static <T> Option<T> from(final T theValue)
	{
		return theValue == null ? (Option<T>) NONE : new Option<T>(theValue);
	}

	/**
	 * Retorna o valor T se ele existir nesta Option. Chamar esse método com Option sem elementos causará um erro.
	 * 
	 * @return O valor T guardado nesta Option
	 * @throws NoSuchElementException Se o Option estiver vazio
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
	 * Retorna o valor contido nesta Option, ou o valor determinado pela parte do else se ela não contém nenhum valor.
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
	public <B> Option<B> flatMap(final Function1<? super T, ? extends SharpCollection<B>> function)
	{
		return (Option<B>) super.flatMap(function);
	}

	@Override
	public Option<Tuple2<T, Integer>> zipWithIndex()
	{
		return (Option<Tuple2<T, Integer>>) super.zipWithIndex();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + value.hashCode();
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
		return value.equals(((Option) obj).value);
	}
}
