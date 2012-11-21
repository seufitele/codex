package com.github.detentor.codex.collections.mutable;

import java.util.Arrays;

/**
 * Criação própria do ArrayList. <br/>
 * Principal diferença: <br/>
 *
 * 1) Remoção de elementos no início da lista, o pior caso do ArrayList, são removidos em O(1)
 * 2) Remoção de intervalos começando no início da lista, ou que vão até o fim da lista 
 * 		são O(n), onde n é o tamanho do intervalo removido. <br/><br/>
 * 
 * Essa classe permitirá ser construída uma versão imutável com operação de tail em O(1), utilizando flyweight para
 * evitar desperdício de memória (da mesma forma que é feito com strings).
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public class FastList<E>
{
	Object[] data;

	int startIndex; //Guarda a posição nos dados onde começam os elementos (para permitir remover head com O(1))
	int endIndex; //Guarda a posição nos dados onde terminam os elementos
	
	/**
	 * Cria uma nova classe ArrayList, com a capacidade inicial definida
	 * @param capacity
	 */
	protected FastList(final E[] theData)
	{
		startIndex = 0;
		endIndex = theData.length - 1;
		data = theData;
	}

	public static <A> FastList<A> from(final A... params)
	{
		return new FastList<A>(params);
	}
	
	@SuppressWarnings("unchecked")
	public static <A> FastList<A> empty()
	{
		return new FastList<A>((A[]) new Object[0]);
	}

	/**
	 * Retorna a quantidade de elementos contida nesta lista
	 * @return
	 */
	public int size()
	{
		return (endIndex - startIndex) + 1;
	}

	/**
	 * Adiciona um elemento para a lista, retornando a referência para a mesma,
	 * após o elemento ser adicionado
	 * @param element
	 * @return
	 */
	public FastList<E> add(final E element)
	{
		ensureCapacity(this.size() + 1);
		addFast(element);
		return this;
	}

	@SuppressWarnings("unchecked")
	public FastList<E> subsequence(final int start, final int end)
	{
		//Alterar o método para retornar uma cópia imutável da lista
		return (FastList<E>) FastList.from(Arrays.copyOfRange(this.data, startIndex + start, startIndex + end));
	}

	/**
	 * Remove o elemento no índice especificado
	 * @param index
	 * @return
	 */
	public FastList<E> remove(final int index)
	{
		if (index == 0)
		{
			removeFirst();
		}
		else if (index == this.size() - 1)
		{
			removeLast();
		}
		else
		{
			throw new IllegalArgumentException("not implemented yet");
		}
		return this;
	}
	
	private void removeFirst()
	{
		data[startIndex++] = null;
	}
	
	private void removeLast()
	{
		data[endIndex--] = null;
	}

	/**
	 * Adiciona todos os elementos 
	 * @param elements
	 * @return
	 */
	public FastList<E> addAll(final Iterable<E> elements)
	{
		for (E element : elements)
		{
			add(element);
		}
		return this;
	}
	
	public FastList<E> addAll(final E... elements)
	{
		ensureCapacity(this.size() + elements.length);
		System.arraycopy(elements, 0, data, endIndex + 1, elements.length);
		endIndex += elements.length;
		return this;
	}

	/**
	 * Adiciona um elemento rapidamente, sem verificar
	 * se existe espaço ou não
	 */
	protected void addFast(final E element)
	{
		data[++endIndex] = element;
	}

	public void ensureCapacity(int minCapacity) 
    {
		int oldCapacity = data.length;
		if (minCapacity > oldCapacity) 
		{
		    Object[] oldData = data;
		    int newCapacity = Math.max(minCapacity, (oldCapacity * 3)/2 + 1);
		    //Alterar para System.arrayCopy, para liberar o espaço do primeiro elemento
		    data = Arrays.copyOf(oldData, newCapacity);
		}
    }

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();

		builder.append('[');
		
		for (int i = startIndex; i < this.endIndex; i++)
		{
			builder.append(data[i] + ", ");
		}

		builder.append(data[this.endIndex]);
		builder.append(']');

		return builder.toString();
	}

}
