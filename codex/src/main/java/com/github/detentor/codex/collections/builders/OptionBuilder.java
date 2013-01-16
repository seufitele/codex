package com.github.detentor.codex.collections.builders;

import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.monads.Option;


/**
 * Essa classe é um builder para SharpCollection baseado em um Option.
 * 
 * @author Vinícius Seufitele Pinto
 *
 * @param <E> O tipo de dados do Option retornado
 */
public class OptionBuilder<E> implements Builder<E, SharpCollection<E>>
{
	private boolean added = false;
	private E valor = null;

	@Override
	public void add(final E element)
	{
		if (added)
		{
			throw new IllegalStateException("tentou-se adicionar mais de um elemento no builder do option");
		}
		added = true;
		valor = element;
	}

	@Override
	public Option<E> result()
	{
		return Option.from(valor);
	}

}
