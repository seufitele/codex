package br.com.codex.collections.builders.java;

import java.util.HashSet;
import java.util.Set;

import br.com.codex.collections.Builder;

/**
 * Essa classe é um builder para Set baseado em um HashSet. <br/>
 * 
 * @author Vinícius Seufitele Pinto
 *
 * @param <E> O tipo de dados armazenado no HashSet.
 */
public class HashSetBuilder<E> implements Builder<E, Set<E>>
{
	private final Set<E> backingSet = new HashSet<E>();

	@Override
	public void add(final E element)
	{
		backingSet.add(element);
	}

	@Override
	public Set<E> result()
	{
		return backingSet;
	}

}
