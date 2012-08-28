package com.github.detentor.codex.collections.mutable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.github.detentor.codex.collections.AbstractMutableCollection;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.collections.builders.HashSetBuilder;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.product.Tuple2;


public class SetSharp<T> extends AbstractMutableCollection<T, SetSharp<T>>
{
	private final Set<T> backingSet;

	/**
	 * Construtor privado. Instâncias devem ser criadas com o 'from'
	 */
	protected SetSharp()
	{
		super();
		backingSet = new HashSet<T>();
	}

	protected SetSharp(final Set<T> backSet)
	{
		super();
		this.backingSet = backSet;
	}

	/**
	 * Cria uma instância de SetSharp a partir do set passado como parâmetro. <br/>
	 * ATENÇÃO: Mudanças estruturais no set original também afetam este set.
	 * 
	 * @param <T> O tipo de dados guardado pelo set
	 * @param theSet O set a partir do qual este SetSharp será criado
	 * @return Uma setSharp que contém a referência ao set passado como parâmetro
	 */
	public static <T> SetSharp<T> from(final Set<T> theSet)
	{
		return new SetSharp<T>(theSet);
	}

	/**
	 * Cria uma instância de SetSharp a partir dos elementos existentes no iterable 
	 * passado como parâmetro. A ordem da adição dos elementos será a mesma ordem do iterable.
	 * 
	 * @param <T> O tipo de dados do set
	 * @param theIterable O iterator que contém os elementos
	 * @return Um set criado a partir da adição de todos os elementos do iterador
	 */
	public static <T> SetSharp<T> from(final Iterable<T> theIterable)
	{
		final SetSharp<T> retorno = new SetSharp<T>();

		for (final T ele : theIterable)
		{
			retorno.add(ele);
		}
		return retorno;
	}

	/**
	 * Cria um novo SetSharp, a partir dos valores passados como parâmetro. <br/>
	 * Esse método é uma forma mais compacta de se criar SetSharp.
	 * 
	 * @param <A> O tipo de dados do SetSharp a ser retornada.
	 * @param valores Os valores que irão compor o SetSharp
	 * @return Um novo SetSharp, cujos elementos são os elementos passados como parâmetro
	 */
	public static <T> SetSharp<T> from(final T... valores)
	{
		final SetSharp<T> retorno = new SetSharp<T>();

		for (final T ele : valores)
		{
			retorno.add(ele);
		}
		return retorno;
	}

	@Override
	public int size()
	{
		return backingSet.size();
	}

	@Override
	public Iterator<T> iterator()
	{
		return backingSet.iterator();
	}

	@Override
	public SetSharp<T> add(final T element)
	{
		backingSet.add(element);
		return this;
	}

	@Override
	public SetSharp<T> remove(final T element)
	{
		backingSet.remove(element);
		return this;
	}

	@Override
	public SetSharp<T> clear()
	{
		backingSet.clear();
		return this;
	}

	@Override
	public <B> Builder<B, SharpCollection<B>> builder()
	{
		return new HashSetBuilder<B>();
	}

	@Override
	public <B> SetSharp<B> map(final Function1<T, B> function)
	{
		return (SetSharp<B>) super.map(function);
	}

	@Override
	public <B> SetSharp<B> flatMap(final Function1<T, ? extends SharpCollection<B>> function)
	{
		return (SetSharp<B>) super.flatMap(function);
	}

	@Override
	public SetSharp<Tuple2<T, Integer>> zipWithIndex()
	{
		return (SetSharp<Tuple2<T, Integer>>) super.zipWithIndex();
	}

	@Override
	public SetSharp<Tuple2<T, Integer>> zipWithIndex(final Builder<Tuple2<T, Integer>, SharpCollection<Tuple2<T, Integer>>> cbf)
	{
		return (SetSharp<Tuple2<T, Integer>>) super.zipWithIndex(cbf);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (backingSet == null ? 0 : backingSet.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final SetSharp other = (SetSharp) obj;
		if (backingSet == null)
		{
			if (other.backingSet != null)
			{
				return false;
			}
		}
		else if (!backingSet.equals(other.backingSet))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return backingSet.toString();
	}

}
