package br.com.codex.collections.builders;

import java.util.HashSet;
import java.util.Set;

import br.com.codex.collections.Builder;
import br.com.codex.collections.SharpCollection;
import br.com.codex.collections.mutable.SetSharp;

/**
 * Essa classe é um builder para Set baseado em um SetSharp. <br/>
 * 
 * @author f9540702 Vinícius Seufitele Pinto
 *
 * @param <E> O tipo de dados armazenado no SetSharp.
 */
public class HashSetBuilder<E> implements Builder<E, SharpCollection<E>>
{
	private final Set<E> backingSet = new HashSet<E>();

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
