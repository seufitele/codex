package com.github.detentor.codex.collections.builders;

import java.util.LinkedHashSet;
import java.util.Set;

import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.collections.mutable.SetSharp;


/**
 * Essa classe é um builder para Set baseado em um SetSharp. <br/>
 * 
 * @author f9540702 Vinícius Seufitele Pinto
 *
 * @param <E> O tipo de dados armazenado no SetSharp.
 */
public class LinkedHashSetBuilder<E> implements Builder<E, SharpCollection<E>>
{
	private final Set<E> backingSet = new LinkedHashSet<E>();

	@Override
	public void add(final E element)
	{
		backingSet.add(element);
	}

	@Override
	public SetSharp<E> result()
	{
		return SetSharp.from(backingSet);
	}

}
