package com.github.detentor.codex.collections.immutable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.github.detentor.codex.collections.AbstractIndexedSeq;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.collections.mutable.MapSharp;
import com.github.detentor.codex.collections.mutable.MapSharp.MapSharpType;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.PartialFunction1;
import com.github.detentor.codex.product.Tuple2;

/**
 * Implementação de ListSharp imútável. <br/>
 * Sempre que possível, deve-se favorecer essa implementação em detrimento da mutável, pois apresenta os seguintes benefícios: <br/>
 * 
 * 1 - Totalmente thread-safe, por ser imutável. <br/>
 * 2 - Subsequence é O(1), o que torna quase todas as operações também O(1) (tail, take, takeRight, drop, dropRight, reverse, etc.).
 * 3 - Custo de memória constante nas operações O(1) (inclusive a operação map), 
 * pois a referência à lista original é compartilhada (padrão Flyweight). <br/>
 * 
 * @author f9540702 Vinícius Seufitele Pinto
 * 
 * @param <T>
 */
public class ListSharp<T> extends AbstractIndexedSeq<T, ListSharp<T>> implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final int startIndex;
	private final int theSize;

	private final Object[] data;

	// Singleton, pois como é imutável não faz sentido criar várias
	private static final ListSharp<Object> EMPTY_LIST = new ListSharp<Object>();

	/**
	 * Construtor privado. Instâncias devem ser criadas com o 'from'
	 */
	protected ListSharp()
	{
		startIndex = 0;
		theSize = 0;
		data = new Object[0];
	}

	/**
	 * Construtor privado, que reutiliza o objeto theData passado.
	 */
	protected ListSharp(final Object[] theData)
	{
		this(theData, 0, theData.length);
	}

	/**
	 * Construtor privado, que reutiliza o objeto theData passado.
	 * 
	 * @param theData
	 * @param theStart Representa onde vai começar o índice da lista
	 * @param theEnd Representa onde vai terminar a lista (exclusive)
	 */
	protected ListSharp(final Object[] theData, final int theStart, final int theEnd)
	{
		startIndex = theStart;
		theSize = theEnd - theStart;
		data = theData;
	}

	/**
	 * Construtor privado, que reutiliza o objeto passado.
	 * 
	 * @param prevList
	 * @param theStart Representa onde vai começar o índice da lista
	 * @param theEnd Representa onde vai terminar a lista (exclusive)
	 */
	protected ListSharp(final ListSharp<T> prevList, final int theStart, final int theEnd)
	{
		startIndex = prevList.startIndex + theStart;
		theSize = theEnd - theStart;
		data = prevList.data;
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
		// Retorna sempre a mesma lista - afinal, ela é imutável
		return (ListSharp<A>) EMPTY_LIST;
	}

	/**
	 * Cria uma instância de ListSharp a partir dos elementos existentes no iterable passado como parâmetro. A ordem da adição dos
	 * elementos será a mesma ordem do iterable.
	 * 
	 * @param <T> O tipo de dados da lista
	 * @param theIterable O iterator que contém os elementos
	 * @return Uma lista criada a partir da adição de todos os elementos do iterador
	 */
	public static <T> ListSharp<T> from(final Iterable<T> theIterable)
	{
		// por enquanto está bem porco, melhorar
		final List<T> listaRetorno = new ArrayList<T>();

		for (final T ele : theIterable)
		{
			listaRetorno.add(ele);
		}
		return new ListSharp<T>(listaRetorno.toArray());
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
		return new ListSharp<T>(Arrays.copyOf(valores, valores.length));
	}

	@Override
	public int size()
	{
		return theSize;
	}

	@Override
	public ListSharp<T> subsequence(final int startIndex, final int endIndex)
	{
		return new ListSharp<T>(this, Math.max(startIndex, 0), Math.min(endIndex, this.size()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public T apply(final Integer param)
	{
		return (T) data[startIndex + param];
	}

	@Override
	public <B> Builder<B, SharpCollection<B>> builder()
	{
		return new ImArrayBuilder<B>();
	}

	@Override
	public <B> ListSharp<B> map(final Function1<? super T, B> function)
	{
		return new ListSharp<B>(data, startIndex, this.startIndex + this.size())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public B apply(final Integer param)
			{
				return function.apply(ListSharp.this.apply(param));
			}
		};
	}

	@Override
	public <B> ListSharp<B> collect(final PartialFunction1<? super T, B> pFunction)
	{
		return (ListSharp<B>) super.collect(pFunction);
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

	@Override
	public String toString()
	{
		return mkString("[", ", ", "]");
	}

	/**
	 * {@inheritDoc}<br/>
	 * 
	 */
	@Override
	public ListSharp<T> reverse()
	{
		return new ListSharp<T>(this, this.startIndex, this.startIndex + this.size())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public T apply(final Integer param)
			{
				return super.apply(this.size() - 1 - param);
			}
		};
	}
	
	/**
	 * Retorna esta lista, após a ordenação de seus elementos. <br/>
	 * Esse método não está definido quando os elementos contidos nesta lista não são instâncias 
	 * de {@link Comparable} ou {@link Comparator}.
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
	@SuppressWarnings("unchecked")
	public ListSharp<T> sorted(final Comparator<? super T> comparator)
	{
		if (this.isEmpty())
		{
			return this;
		}

		final T[] novosDados = (T[]) Arrays.copyOf(data, data.length);
		Arrays.sort(novosDados, comparator);
		return new ListSharp<T>(novosDados, startIndex, startIndex + theSize);
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
		//ATENÇÃO: VERIFICAR SE O CÓDIGO NÃO PODE SER REESCRITO PARA SER MAIS OTIMIZADO
		final MapSharp<A, Builder<T, SharpCollection<T>>> mapaIntermediario = MapSharp.empty(mapType);

		for (final T curEle : this)
		{
			final A value = function.apply(curEle);
			Builder<T, SharpCollection<T>> builder = mapaIntermediario.get(value);
			
			if (builder == null)
			{
				builder = new ImArrayBuilder<T>();
				mapaIntermediario.add(value, builder);
			}
			builder.add(curEle);
		}
		
		final MapSharp<A, ListSharp<T>> mapaRetorno = MapSharp.empty(mapType);
		
		for (final A curEle : mapaIntermediario.keySet())
		{
			mapaRetorno.add(curEle, (ListSharp<T>) mapaIntermediario.get(curEle).result());
		}

		return mapaRetorno;
	}
	
	/**
	 * Essa classe é um builder para SharpCollection baseado em um ListSharp (imutável).
	 */
	private class ImArrayBuilder<E> implements Builder<E, SharpCollection<E>>
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
			return new ListSharp<E>(list.toArray());
		}
	}
}
