package com.github.detentor.codex.monads;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.github.detentor.codex.collections.AbstractSharpCollection;
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
public class Option<T> extends AbstractSharpCollection<T, SharpCollection<T>>
{
	private final List<T> value;

	protected Option()
	{
		value = new ArrayList<T>();
	}

	protected Option(final T theValue)
	{
		this();
		if (theValue != null)
		{
			value.add(theValue);
		}
	}

	/**
	 * Cria um option para uma operação que falhou (Option está vazio)
	 * 
	 * @param <T> O tipo de dados que option deveria conter
	 * @return Uma option que representa uma computação não sucedida
	 */
	public static <T> Option<T> empty()
	{
		return new Option<T>();
	}

	/**
	 * Cria um option para uma operação que falhou (Option está vazio)
	 * 
	 * @param <T> O tipo de dados que option deveria conter
	 * @return Uma option que representa uma computação não sucedida
	 */
	public static <T> Option<T> from(final T theValue)
	{
		return new Option<T>(theValue);
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
		return value.isEmpty() ? elsePart : value.get(0);
	}

	@Override
	public String toString()
	{
		return this.isEmpty() ? "None" : "Some(" + value.get(0).toString() + ")";
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
				return Option.this.value.get(0);
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
	public <B> Option<B> map(final Function1<T, B> function)
	{
		return (Option<B>) super.map(function);
	}

	@Override
	public <B> Option<B> flatMap(final Function1<T, ? extends SharpCollection<B>> function)
	{
		return (Option<B>) super.flatMap(function);
	}

	@Override
	public Option<Tuple2<T, Integer>> zipWithIndex()
	{
		return (Option<Tuple2<T, Integer>>) super.zipWithIndex();
	}

}
