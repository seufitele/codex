package com.github.detentor.codex.collections.mutable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.github.detentor.codex.collections.AbstractMutableIndexedSeq;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.IndexedSeq;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.collections.mutable.MapSharp.MapSharpType;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.PartialFunction1;
import com.github.detentor.codex.product.Tuple2;

/**
 * Essa classe representa uma lista mutável, cujos elementos são armazenados num ArrayList usando composição. <br/>
 * 
 * Os métodos desta classe são utilizados para favorecer a programação funcional, e possuem a mesma nomeclatura encontrada no Scala. Os
 * métodos desta classe são extremamente poderosos, e simplificam a codificação de estruturas mais complexas.<br/>
 * <br/>
 * 
 * Coleções como funções parciais: <br/>
 * Coleções podem ser vistas como funções parciais. ListSharp, por exemplo, é uma função que faz corresponder, para cada inteiro, um
 * elemento do tipo T.
 * 
 * @author Vinícius Seufitele Pinto
 */
public class ListSharp<T> extends AbstractMutableIndexedSeq<T, ListSharp<T>> implements PartialFunction1<Integer, T>, Serializable
{
	private static final long serialVersionUID = 1L;

	private final List<T> backingList;

	/**
	 * Construtor privado. Instâncias devem ser criadas com o 'from'
	 */
	protected ListSharp()
	{
		this(new ArrayList<T>());
	}
	
	protected ListSharp(final List<T> fromList)
	{
		super();
		backingList = fromList;
	}

	/**
	 * Constrói uma instância de ListSharp vazia.
	 * 
	 * @param <A> O tipo de dados da instância
	 * @return Uma instância de ListSharp vazia.
	 */
	public static <A> ListSharp<A> empty()
	{
		return new ListSharp<A>();
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
		final ListSharp<T> retorno = new ListSharp<T>();
		for (final T ele : theIterable)
		{
			retorno.add(ele);
		}
		return retorno;
	}

	/**
	 * Cria uma nova ListSharp, a partir dos valores passados como parâmetro. <br/>
	 * Esse método é uma forma mais compacta de se criar ListSharp.
	 * 
	 * @param <A> O tipo de dados da ListSharp a ser retornada.
	 * @param valores A ListSharp a ser criada, a partir dos valores
	 * @return Uma nova ListSharp, cujos elementos são os elementos passados como parâmetro
	 */
	public static <T> ListSharp<T> from(final T... valores)
	{
		final ListSharp<T> retorno = new ListSharp<T>();

		for (final T ele : valores)
		{
			retorno.add(ele);
		}
		return retorno;
	}

	@Override
	public int size()
	{
		return backingList.size();
	}

	@Override
	public boolean contains(final T element)
	{
		return backingList.contains(element);
	}

	@Override
	public ListSharp<T> add(final T element)
	{
		backingList.add(element);
		return this;
	}

	@Override
	public ListSharp<T> remove(final T element)
	{
		backingList.remove(element);
		return this;
	}

	@Override
	public ListSharp<T> clear()
	{
		backingList.clear();
		return this;
	}

	@Override
	public <B> Builder<B, SharpCollection<B>> builder()
	{
		return new ArrayBuilder<B>();
	}

	@Override
	public String toString()
	{
		return mkString("[", ", ", "]");
	}
	
	@Override
	public <B> ListSharp<B> collect(final PartialFunction1<? super T, B> pFunction)
	{
		return (ListSharp<B>) super.collect(pFunction);
	}

	@Override
	public <B> ListSharp<B> map(final Function1<? super T, B> function)
	{
		return (ListSharp<B>) super.map(function);
	}

	@Override
	public <B> ListSharp<B> flatMap(final Function1<? super T, ? extends Iterable<B>> function)
	{
		return (ListSharp<B>) super.flatMap(function);
	}
	
	@Override
	public ListSharp<Tuple2<T, Integer>> zipWithIndex()
	{
		return (ListSharp<Tuple2<T, Integer>>) super.zipWithIndex();
	}

	/**
	 * Transforma esta coleção em um mapa de coleções de acordo com uma função discriminadora. </br> 
	 * Em outras palavras, aplica a função passada como parâmetro a cada elemento desta coleção, 
	 * criando um mapa onde a chave é o resultado da função aplicada, e o valor é uma coleção de 
	 * elementos desta coleção que retornam aquele valor à função. <br/>
	 * O o mapa retornado será criado a partir de um HashMap.
	 * 
	 * @param <B> O tipo de retorno da função
	 * @param funcao Uma função que transforma um item desta coleção em outro tipo
	 * @return Um mapa, onde a chave é o resultado da função, e os valores uma coleção 
	 * de elementos cujo resultado da função aplicada seja o mesmo.
	 */
	public <A> MapSharp<A, ListSharp<T>> groupBy(final Function1<? super T, A> function)
	{
		return groupBy(function, MapSharpType.HASH_MAP);
	}
	
	/**
	 * Transforma esta coleção em um mapa de coleções de acordo com uma função discriminadora. </br> 
	 * Em outras palavras, aplica a função passada como parâmetro a cada elemento desta coleção, 
	 * criando um mapa onde a chave é o resultado da função aplicada, e o valor é uma coleção de 
	 * elementos desta coleção que retornam aquele valor à função.
	 * 
	 * @param <B> O tipo de retorno da função
	 * @param funcao Uma função que transforma um item desta coleção em outro tipo
	 * @param mapType O tipo de mapa a ser criado pelo groupBy.
	 * @return Um mapa, onde a chave é o resultado da função, e os valores uma coleção 
	 * de elementos cujo resultado da função aplicada seja o mesmo.
	 */
	public <A> MapSharp<A, ListSharp<T>> groupBy(final Function1<? super T, A> function, final MapSharpType mapType)
	{
		final MapSharp<A, ListSharp<T>> mapaRetorno = MapSharp.empty(mapType);

		for (final T curEle : this)
		{
			final A value = function.apply(curEle);
			mapaRetorno.add(value, mapaRetorno.getOrElse(value, ListSharp.<T> empty()).add(curEle));
		}
		return mapaRetorno;
	}

	/**
	 * Transforma esta coleção em um mapa de acordo com uma função discriminadora. </br> Em outras palavras, aplica a função passada como
	 * parâmetro a cada elemento desta coleção, criando um mapa onde a chave é o resultado da função aplicada, e o valor é o elemento. <br/>
	 * Se esta coleção possuir elementos repetidos, então o elemento do mapa será o último a ser retornado pelo iterador desta coleção.
	 * 
	 * @param <B> O tipo de retorno da função
	 * @param funcao Uma função que transforma um item desta coleção em outro tipo
	 * @return Um mapa, onde a chave é o resultado da função, e o valor é o elemento usado para gerar a chave
	 */
	public <A> MapSharp<A, T> mapped(final Function1<? super T, A> function)
	{
		final MapSharp<A, T> mapaRetorno = MapSharp.empty();

		for (final T curEle : this)
		{
			mapaRetorno.add(Tuple2.from(function.apply(curEle), curEle));
		}
		return mapaRetorno;
	}

	/**
	 * Retorna esta lista, após a ordenação de seus elementos. <br/>
	 * Esse método não está definido quando os elementos contidos nesta lista não são instâncias de {@link Comparable} ou {@link Comparator}
	 * .
	 * 
	 * @return Esta lista com os elementos ordenados
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ListSharp<T> sorted()
	{
		return sorted(new DefaultComparator());
	}

	/**
	 * Retorna esta lista, após a ordenação de seus elementos de acordo com a função de comparação passada como parâmetro. <br/>
	 * 
	 * @return Esta lista após a ordenação dos elementos ordenados
	 */
	public ListSharp<T> sorted(final Comparator<? super T> comparator)
	{
		if (this.isEmpty())
		{
			return this;
		}
		Collections.sort(backingList, comparator);
		return this;
	}

	@Override
	public IndexedSeq<T> subsequence(int startIndex, int endIndex)
	{
		return ListSharp.from(backingList.subList(Math.max(startIndex, 0), Math.min(endIndex, this.size())));
	}

	/**
	 * {@inheritDoc} <br/>
	 * No caso desta lista, retorna o elemento na posição "param". Equivale ao {@link List#get(int) get} da List.
	 */
	@Override
	public T apply(final Integer param)
	{
		return backingList.get(param);
	}

	/**
	 * Retorna a referência à lista que contém os elementos desta coleção. <br/>
	 * ATENÇÃO: Mudanças estruturais na lista retornada também afetam esta coleção.
	 * 
	 * @return A referência à lista que serve como container de dados desta coleção
	 */
	@Override
	public List<T> toList()
	{
		return backingList;
	}

	/**
	 * Classe com a implementação default do comparator
	 */
	private static final class DefaultComparator<A extends Comparable<A>> implements Comparator<A>, Serializable
	{
		private static final long serialVersionUID = 4989261028786246998L;

		@Override
		public int compare(final A ob1, final A ob2)
		{
			return ob1.compareTo(ob2);
		}
	}
	
	/**
	 * Essa classe é um builder para SharpCollection baseado em um ListSharp.
	 * @param <E> O tipo de dados do ListSharp retornado
	 */
	protected final static class ArrayBuilder<E> implements Builder<E, SharpCollection<E>>
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
			return new ListSharp<E>(list);
		}
	}
}
