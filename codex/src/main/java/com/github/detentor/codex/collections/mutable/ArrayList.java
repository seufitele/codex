package com.github.detentor.codex.collections.mutable;

import java.util.Arrays;

/**
 * Criação própria do ArrayList. <br/>
 * Principal diferença: <br/>
 *
 * 1) Remoção ou último elementos são feitas em O(1) 
 * 2) Remoção de intervalos começando no início da lista, ou que vão até o fim da lista 
 * 		são O(n), onde n é o tamanho do intervalo removido. <br/><br/>
 * 
 * Essa classe permitirá ser construída uma versão imutável com operação de tail em O(1), utilizando flyweight para
 * evitar desperdício de memória (da mesma forma que é feito com strings).
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public class ArrayList<E>
{
	Object[] data;
	
	int startIndex; //Guarda a posição nos dados onde começam os elementos (para permitir remover head com O(1))
	int size; //Guarda o tamanho dos dados
	
	/**
	 * Cria uma nova classe ArrayList, com a capacidade inicial definida
	 * @param capacity
	 */
	protected ArrayList(final E[] theData )
	{
		startIndex = 0;
		size = theData.length;
		data = theData;
	}
	
	public static <A> ArrayList<A> from(final A... params)
	{
		return new ArrayList<A>(params);
	}
	
	
	/**
	 * Retorna a quantidade de elementos contida nesta lista
	 * @return
	 */
	public int size()
	{
		return size;
	}
	
	/**
	 * Adiciona um elemento para a lista, retornando a referência para a mesma,
	 * após o elemento ser adicionado
	 * @param element
	 * @return
	 */
	public ArrayList<E> add(final E element)
	{
		ensureCapacity(size + 1);
		addFast(element);
		return this;
	}
	
	/**
	 * Remove o elemento no índice especificado
	 * @param index
	 * @return
	 */
	public ArrayList<E> remove(final int index)
	{
		if (index == 0)
		{
			removeFirst();
		}
		else if (index == size - 1)
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
		size--;
	}
	
	private void removeLast()
	{
		data[--size - startIndex] = null;
	}
	
	/**
	 * Adiciona todos os elementos 
	 * @param elements
	 * @return
	 */
	public ArrayList<E> addAll(final Iterable<E> elements)
	{
		for (E element : elements)
		{
			add(element);
		}
		return this;
	}
	
	public ArrayList<E> addAll(final E[] elements)
	{
		ensureCapacity(size + elements.length);
		System.arraycopy(elements, 0, data, size - startIndex, elements.length);
		size += elements.length;
		return this;
	}

	/**
	 * Adiciona um elemento rapidamente, sem verificar
	 * se existe espaço ou não
	 */
	protected void addFast(final E element)
	{
		data[size++ - startIndex] = element;
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
		
		for (int i = startIndex; i < this.size - 1; i++)
		{
			builder.append(data[i] + ", ");
		}
		
		builder.append(data[startIndex + this.size - 1]);
		builder.append(']');
		
		return builder.toString();
	}

}
