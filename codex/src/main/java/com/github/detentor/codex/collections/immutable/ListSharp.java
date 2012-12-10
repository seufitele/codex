package com.github.detentor.codex.collections.immutable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.detentor.codex.collections.AbstractIndexedSeq;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.collections.builders.ArrayBuilder;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.PartialFunction;

public class ListSharp<T> extends AbstractIndexedSeq<T>
{
	private final List<T> backingList;

	// Singleton, pois como é imutável não faz sentido criar várias
	private static final ListSharp<Object> EMPTY_LIST = new ListSharp<Object>();

	/**
	 * Construtor privado. Instâncias devem ser criadas com o 'from'
	 */
	protected ListSharp()
	{
		backingList = new ArrayList<T>(0);
	}

	protected ListSharp(final List<T> backList)
	{
		this.backingList = backList;
	}

	/**
	 * Constrói uma instância de ListSharp vazia.
	 * 
	 * @param <A> O tipo de dados da instância
	 * @return Uma instância de ListSharp vazia.
	 */
	@SuppressWarnings("unchecked")
	public static <A> ListSharp<A> empty()
	{
		return (ListSharp<A>) EMPTY_LIST;
	}

	/**
	 * Cria uma instância de ListSharp a partir da lista passada como parâmetro. <br/>
	 * ATENÇÃO: Mudanças estruturais na lista original também afetam esta lista.
	 * 
	 * @param <T> O tipo de dados guardado pela lista
	 * @param theList A lista a partir da qual esta ListSharp será criada
	 * @return Uma listSharp que contém a referência à lista passada como parâmetro
	 */
	public static <T> ListSharp<T> from(final List<T> theList)
	{
		return new ListSharp<T>(theList);
	}

	/**
	 * Cria uma instância de ListSharp a partir dos elementos existentes no iterable passado como parâmetro. A ordem da adição dos elementos
	 * será a mesma ordem do iterable.
	 * 
	 * @param <T> O tipo de dados da lista
	 * @param theIterable O iterator que contém os elementos
	 * @return Uma lista criada a partir da adição de todos os elementos do iterador
	 */
	public static <T> ListSharp<T> from(final Iterable<T> theIterable)
	{
		final List<T> listaRetorno = new ArrayList<T>();

		for (final T ele : theIterable)
		{
			listaRetorno.add(ele);
		}
		return new ListSharp<T>(listaRetorno);
	}

	/**
	 * Cria uma nova ListSharp, a partir dos valores passados como parâmetro. <br/>
	 * Esse método é uma forma mais compacta de se criar ListSharp.
	 * 
	 * @param <A> O tipo de dados da ListSharp a ser retornada.
	 * @param collection A ListSharp a ser criada, a partir dos valores
	 * @return Uma nova ListSharp, cujos elementos são os elementos passados como parâmetro
	 */
	public static <T> ListSharp<T> from(final T... valores)
	{
		final List<T> listaRetorno = new ArrayList<T>();

		for (final T ele : valores)
		{
			listaRetorno.add(ele);
		}
		return new ListSharp<T>(listaRetorno);
	}

	@Override
	public int size()
	{
		return backingList.size();
	}

	@Override
	public ListSharp<T> subsequence(final int startIndex, final int endIndex)
	{
		return ListSharp.from(backingList.subList(Math.max(startIndex, 0), Math.min(endIndex, this.size())));
	}

	@Override
	public T apply(final Integer param)
	{
		return backingList.get(param);
	}

	@Override
	public Iterator<T> iterator()
	{
		/**
		 * ATENÇÃO: REESCREVER O ITERATOR PARA NÃO PERMITIR EXCLUSÕES
		 */
		return backingList.iterator();
	}

	@Override
	public <B> Builder<B, SharpCollection<B>> builder()
	{
		return new ArrayBuilder<B>();
	}

	@Override
	public <B> ListSharp<B> map(final Function1<? super T, B> function)
	{
		return (ListSharp<B>) super.map(function);
	}

	@Override
	public <B> ListSharp<B> collect(final PartialFunction<? super T, B> pFunction)
	{
		return (ListSharp<B>) super.collect(pFunction);
	}

	@Override
	public <B> ListSharp<B> flatMap(final Function1<? super T, ? extends SharpCollection<B>> function)
	{
		return (ListSharp<B>) super.flatMap(function);
	}

	@Override
	public String toString()
	{
		return mkString("[", ", ", "]");
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (backingList == null ? 0 : backingList.hashCode());
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
		final ListSharp other = (ListSharp) obj;
		if (backingList == null)
		{
			if (other.backingList != null)
			{
				return false;
			}
		}
		else if (!backingList.equals(other.backingList))
		{
			return false;
		}
		return true;
	}
}
