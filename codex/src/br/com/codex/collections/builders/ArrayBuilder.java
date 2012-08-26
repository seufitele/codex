package br.com.codex.collections.builders;

import java.util.ArrayList;
import java.util.List;

import br.com.codex.collections.Builder;
import br.com.codex.collections.SharpCollection;
import br.com.codex.collections.mutable.ListSharp;

/**
 * Essa classe é um builder para SharpCollection baseado em um ListSharp.
 * 
 * @author f9540702 Vinícius Seufitele Pinto
 *
 * @param <E> O tipo de dados do ListSharp retornado
 */
public class ArrayBuilder<E> implements Builder<E, SharpCollection<E>>
{
	private final List<E> list = new ArrayList<E>();

	@Override
	public void add(final E element)
	{
		list.add(element);
	}

	@Override
	public ListSharp<E> result()
	{
		return ListSharp.from(list);
	}

}
