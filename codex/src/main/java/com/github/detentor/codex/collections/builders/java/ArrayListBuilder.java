package com.github.detentor.codex.collections.builders.java;

import java.util.ArrayList;
import java.util.List;

import com.github.detentor.codex.collections.Builder;


/**
 * Essa classe é um builder para List baseado em um ArrayList.
 * 
 * @author Vinícius Seufitele Pinto
 *
 * @param <E> O tipo de dados do ArrayList retornado
 */
public class ArrayListBuilder<E> implements Builder<E, List<E>>
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
