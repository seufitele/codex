package com.github.detentor.codex.collections.mutable;

import java.util.Iterator;

import com.github.detentor.codex.collections.AbstractMutableGenericCollection;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.collections.builders.LinkedListBuilder;
import com.github.detentor.codex.monads.Option;

public class LinkedListSharp<T> extends AbstractMutableGenericCollection<T, LinkedListSharp<T>>
{
	private T head;
	private LinkedListSharp<T> tail;

	// O objeto vazio
	private static final LinkedListSharp<Object> Nil = new LinkedListSharp<Object>(null, null);

	/**
	 * Construtor privado. Instâncias devem ser criadas com o 'from'
	 */
	protected LinkedListSharp()
	{
		this(null, null);
	}

	/**
	 * Construtor privado. Instâncias devem ser criadas com o 'from'
	 */
	@SuppressWarnings("unchecked")
	protected LinkedListSharp(final T theHead)
	{
		this(theHead, (LinkedListSharp<T>) Nil);
	}

	/**
	 * Construtor privado. Instâncias devem ser criadas com o 'from'
	 */
	protected LinkedListSharp(final T theHead, final LinkedListSharp<T> theTail)
	{
		head = theHead;
		tail = theTail;
	}

	/**
	 * Constrói uma instância de ListSharp vazia.
	 * 
	 * @param <A> O tipo de dados da instância
	 * @return Uma instância de ListSharp vazia.
	 */
	public static <A> LinkedListSharp<A> empty()
	{
		return new LinkedListSharp<A>();
	}

	/**
	 * Cria uma nova LinkedListSharp, a partir dos valores passados como parâmetro. <br/>
	 * Esse método é uma forma mais compacta de se criar LinkedListSharp.
	 * 
	 * @param <A> O tipo de dados da LinkedListSharp a ser retornada.
	 * @param collection A LinkedListSharp a ser criada, a partir dos valores
	 * @return Uma nova LinkedListSharp, cujos elementos são os elementos passados como parâmetro
	 */
	public static <T> LinkedListSharp<T> from(final T... valores)
	{
		final LinkedListSharp<T> retorno = LinkedListSharp.empty();

		for (final T ele : valores)
		{
			retorno.add(ele);
		}
		return retorno;
	}

	/**
	 * Cria uma instância de LinkedListSharp a partir dos elementos existentes no iterable passado como parâmetro. A ordem da adição dos
	 * elementos será a mesma ordem do iterable.
	 * 
	 * @param <T> O tipo de dados da lista
	 * @param theIterable O iterator que contém os elementos
	 * @return Uma lista criada a partir da adição de todos os elementos do iterador
	 */
	public static <T> LinkedListSharp<T> from(final Iterable<T> theIterable)
	{
		final LinkedListSharp<T> retorno = LinkedListSharp.empty();

		for (final T ele : theIterable)
		{
			retorno.add(ele);
		}
		return retorno;
	}

	@Override
	public boolean isEmpty()
	{
		return tail.equals(Nil);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> iterator()
	{
		final LinkedListSharp<T>[] curEle = new LinkedListSharp[1];
		curEle[0] = this;

		return new Iterator<T>()
		{
			@Override
			public boolean hasNext()
			{
				return ! Nil.equals(curEle[0]);
			}

			@Override
			public T next()
			{
				final T toReturn = curEle[0].head;
				curEle[0] = curEle[0].tail;
				return toReturn;
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException("Operação não suportada");
			}
		};
	}

	@Override
	public T head()
	{
		return head;
	}

	@Override
	public LinkedListSharp<T> tail()
	{
		return tail;
	}

	@Override
	public <B> Builder<B, SharpCollection<B>> builder()
	{
		return new LinkedListBuilder<B>();
	}

	/**
	 * {@inheritDoc}<br/>
	 * 
	 * 
	 */
	@Override
	public LinkedListSharp<T> add(final T element)
	{
		final LinkedListSharp<T> prevElement = new LinkedListSharp<T>(this.head, this.tail);
		this.head = element;
		this.tail = prevElement;
		return this;
	}

	@Override
	public LinkedListSharp<T> remove(final T element)
	{
		if (this.equals(Nil))
		{
			return this;
		}

		if (Option.from(this.head).equals(Option.from(element)))
		{
			this.head = this.tail.head;
			this.tail = this.tail.tail;
		}
		else
		{
			this.tail = this.tail.remove(element);
		}
		return this;
	}
	
	@Override
	public LinkedListSharp<T> addAll(final Iterable<? extends T> col)
	{
		for (T ele : col)
		{
			this.add(ele);
		}
		return this;
	}

	@Override
	public LinkedListSharp<T> removeAll(final Iterable<T> col)
	{
		for (T ele : col)
		{
			this.remove(ele);
		}
		return this;
	}

	@Override
	public LinkedListSharp<T> clear()
	{
		this.head = null;
		this.tail = null; 
		return this;
	}

	@Override
	public int size()
	{
		if (this.equals(Nil))
		{
			return 0;
		}
		return 1 + this.tail.size();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (head == null ? 0 : head.hashCode());
		result = prime * result + (tail == null ? 0 : tail.hashCode());
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
		final LinkedListSharp other = (LinkedListSharp) obj;
		if (head == null)
		{
			if (other.head != null)
			{
				return false;
			}
		}
		else if (!head.equals(other.head))
		{
			return false;
		}
		if (tail == null)
		{
			if (other.tail != null)
			{
				return false;
			}
		}
		else if (!tail.equals(other.tail))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return mkString("[", ", ", "]");
	}
}
