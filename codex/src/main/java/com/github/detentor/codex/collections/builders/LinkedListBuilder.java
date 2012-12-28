package com.github.detentor.codex.collections.builders;

import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.collections.mutable.LinkedListSharp;


/**
 * Essa classe é um builder para SharpCollection baseado em um ListSharp.
 * 
 * @author Vinícius Seufitele Pinto
 *
 * @param <E> O tipo de dados do ListSharp retornado
 */
public class LinkedListBuilder<E> implements Builder<E, SharpCollection<E>>
{
	private final LinkedListSharp<E> list = LinkedListSharp.empty();

	@Override
	public void add(final E element)
	{
		list.add(element);
	}

	@Override
	public LinkedListSharp<E> result()
	{
		return list;
	}

}
