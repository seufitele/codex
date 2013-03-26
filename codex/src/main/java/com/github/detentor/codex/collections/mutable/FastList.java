//package com.github.detentor.codex.collections.mutable;
//
//import java.util.Arrays;
//import java.util.Iterator;
//
//import com.github.detentor.codex.collections.AbstractMutableIndexedSeq;
//import com.github.detentor.codex.collections.Builder;
//import com.github.detentor.codex.collections.SharpCollection;
//
///**
// * Criação própria do ArrayList. <br/>
// * Principal diferença: <br/>
// * 
// * 1) Remoção de elementos no início da lista, o pior caso do ArrayList, são removidos em O(1) 2) Remoção de intervalos começando no início
// * da lista, ou que vão até o fim da lista são O(n), onde n é o tamanho do intervalo removido. <br/>
// * <br/>
// * 
// * Essa classe permitirá ser construída uma versão imutável com operação de tail em O(1), utilizando flyweight para evitar desperdício de
// * memória (da mesma forma que é feito com strings).
// * 
// * @author Vinícius Seufitele Pinto
// * 
// */
//public class FastList<E> extends AbstractMutableIndexedSeq<E, FastList<E>>
//{
//	Object[] data;
//
//	int startIndex; // Guarda a posição nos dados onde começam os elementos (para permitir remover head com O(1))
//	int endIndex; // Guarda a posição nos dados onde terminam os elementos
//
//	/**
//	 * Cria uma nova classe ArrayList, com a capacidade inicial definida
//	 * 
//	 * @param capacity
//	 */
//	protected FastList(final E[] theData)
//	{
//		startIndex = 0;
//		endIndex = theData.length - 1;
//		data = theData;
//	}
//
//	public static <A> FastList<A> from(final A... params)
//	{
//		return new FastList<A>(params);
//	}
//
//	@SuppressWarnings("unchecked")
//	public static <A> FastList<A> empty()
//	{
//		return new FastList<A>((A[]) new Object[0]);
//	}
//
//	/**
//	 * Retorna a quantidade de elementos contida nesta lista
//	 * 
//	 * @return
//	 */
//	@Override
//	public int size()
//	{
//		return endIndex - startIndex + 1;
//	}
//
//	/**
//	 * Adiciona um elemento para a lista, retornando a referência para a mesma, após o elemento ser adicionado
//	 * 
//	 * @param element
//	 * @return
//	 */
//	@Override
//	public FastList<E> add(final E element)
//	{
//		ensureCapacity(this.size() + 1);
//		addFast(element);
//		return this;
//	}
//
//	@Override
//	@SuppressWarnings("unchecked")
//	public FastList<E> subsequence(final int start, final int end)
//	{
//		// Alterar o método para retornar uma cópia imutável da lista
//		return (FastList<E>) FastList.from(Arrays.copyOfRange(this.data, startIndex + start, startIndex + end));
//	}
//
//	/**
//	 * Remove o elemento no índice especificado
//	 * 
//	 * @param index
//	 * @return
//	 */
//	public FastList<E> remove(final int index)
//	{
//		if (index == 0)
//		{
//			removeFirst();
//		}
//		else if (index == this.size() - 1)
//		{
//			removeLast();
//		}
//		else
//		{
//			throw new IllegalArgumentException("not implemented yet");
//		}
//		return this;
//	}
//
//	private void removeFirst()
//	{
//		data[startIndex++] = null;
//	}
//
//	private void removeLast()
//	{
//		data[endIndex--] = null;
//	}
//
//	/**
//	 * Adiciona todos os elementos
//	 * 
//	 * @param elements
//	 * @return
//	 */
//	public FastList<E> addAll(final Iterable<E> elements)
//	{
//		for (final E element : elements)
//		{
//			add(element);
//		}
//		return this;
//	}
//
//	public FastList<E> addAll(final E... elements)
//	{
//		ensureCapacity(this.size() + elements.length);
//		System.arraycopy(elements, 0, data, endIndex + 1, elements.length);
//		endIndex += elements.length;
//		return this;
//	}
//
//	/**
//	 * Adiciona um elemento rapidamente, sem verificar se existe espaço ou não
//	 */
//	protected void addFast(final E element)
//	{
//		data[++endIndex] = element;
//	}
//
//	/**
//	 * Aumenta a capacidade desta instância, se necessário, para assegurar que
//	 * ela pode comportar pelo menos o número de elementos especificado no argumento
//	 * @param minCapacity A capacidade mínima que esta lista deve ter
//	 */
//	public void ensureCapacity(final int minCapacity)
//	{
//		final int oldCapacity = data.length;
//		if (minCapacity > oldCapacity)
//		{
//			final Object[] oldData = data;
//			final int newCapacity = Math.max(minCapacity, oldCapacity * 3 / 2 + 1);
//			// Alterar para System.arrayCopy, para liberar o espaço do primeiro elemento
//			data = Arrays.copyOf(oldData, newCapacity);
//		}
//	}
//
//	@Override
//	public String toString()
//	{
//		return mkString("[", ", ", "]");
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public E apply(final Integer pos)
//	{
//		return (E) data[startIndex + pos];
//	}
//
//	@Override
//	public Iterator<E> iterator()
//	{
//		final int[] curIndex = new int[1];
//		curIndex[0] = startIndex;
//		
//		return new Iterator<E>()
//		{
//			@Override
//			public boolean hasNext()
//			{
//				return curIndex[0] <= endIndex;
//			}
//
//			@SuppressWarnings("unchecked")
//			@Override
//			public E next()
//			{
//				return (E) FastList.this.data[curIndex[0]++];
//			}
//
//			@Override
//			public void remove()
//			{
//				throw new UnsupportedOperationException("operação não suportada");
//			}
//		};
//	}
//
//	@Override
//	public <B> Builder<B, SharpCollection<B>> builder()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public SharpCollection<E> remove(final E element)
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public SharpCollection<E> clear()
//	{
//		return null;
//	}
//
//}
