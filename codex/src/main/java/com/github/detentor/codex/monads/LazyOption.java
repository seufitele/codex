package com.github.detentor.codex.monads;

import java.io.Serializable;
import java.util.Iterator;

import com.github.detentor.codex.collections.AbstractGenericCollection;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.collections.builders.OptionBuilder;
import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.function.arrow.Arrow1;

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
public class LazyOption<T> extends AbstractGenericCollection<T, SharpCollection<T>> implements Serializable
{
	private static final long serialVersionUID = 1L;
	private final Function0<T> mapFunction;
	
	protected LazyOption(Function0<T> mapFunction)
	{
		super();
		this.mapFunction = mapFunction;
	}

	public T get()
	{
		return mapFunction.apply();
	}

	public <B> LazyOption<B> map(final Arrow1<? super T, B> function)
	{
		return new LazyOption<B>(new Function0<B>()
		{
			@Override
			public B apply()
			{
				return function.apply(LazyOption.this.get());
			}
		});
	}

	@Override
	public int size()
	{
		return Option.from(get()).size();
	}

	@Override
	public Iterator<T> iterator()
	{
		return Option.from(get()).iterator();
	}

	@Override
	public <B> Builder<B, SharpCollection<B>> builder()
	{
		return new OptionBuilder<B>();
	}

	@Override
	public String toString()
	{
		return this.isEmpty() ? "None" : "Some(" + get().toString() + ")";
	}
	
}
