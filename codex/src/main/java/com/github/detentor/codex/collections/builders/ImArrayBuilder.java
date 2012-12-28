package com.github.detentor.codex.collections.builders;

import java.util.ArrayList;
import java.util.List;

import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.collections.immutable.ListSharp;


/**
 * Essa classe é um builder para SharpCollection baseado em um ListSharp.
 * 
 * @author Vinícius Seufitele Pinto
 *
 * @param <E> O tipo de dados do ListSharp retornado
 */
public class ImArrayBuilder<E> implements Builder<E, SharpCollection<E>>
{
	private final List<E> list = new ArrayList<E>();

	@Override
	public void add(final E element)
	{
		list.add(element);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListSharp<E> result()
	{
		return (ListSharp<E>) ListSharp.from(list.toArray());
	}

}
