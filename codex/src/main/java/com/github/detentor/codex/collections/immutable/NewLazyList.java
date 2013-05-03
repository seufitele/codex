package com.github.detentor.codex.collections.immutable;

import java.util.Iterator;

import com.github.detentor.codex.collections.AbstractLinearSeq;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.LinearSeq;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Function2;
import com.github.detentor.codex.function.Functions;
import com.github.detentor.codex.function.arrow.Arrow2;
import com.github.detentor.codex.product.Tuple2;
import com.github.detentor.operations.IntegerOps;

/**
 * A lista lazy é composta por 3 tipos (por otimização): <br/> <br/>
 * 
 * 1 - WrapMonadic -> O tipo de lista mais simples, simplesmente envelopa um iterator, 
 * retornando os mesmos elementos que o iterator retornaria. <br/> <br/>
 * 
 * 2 - MapMonadic -> O tipo de lista que transforma os elementos de um iterator passado como parâmetro. <br/>
 * Observe que, como não há alteração do número de elementos, o iterator é compartilhado para a próxima função. <br/> <br/>
 * 
 * 3 - FilterMonadic -> O tipo de lista que transforma os elementos, removendo alguns de acordo com um predicado. <br/>
 * Como é possível que haja alteração dos elementos da lista, a composição monádica repassa o iterator da lista. <br/><br/>
 * 
 * ATENÇÃO: Para evitar prender o iterator por tempo maior do que o necessário, liberar a referência da memória após 
 * o iterator ter sido consumido.
 * 
 * @author Vinicius Seufitele Pinto
 *
 * @param <T>
 */
public abstract class NewLazyList<T> extends AbstractLinearSeq<T, NewLazyList<T>>
{
	protected T head;
	protected NewLazyList<T> tail;

	protected boolean loaded = false;
	
	public static final <T> NewLazyList<T> from(Iterable<T> iterable)
	{
		return new WrapMonadic<T>(iterable);
	}
	
	protected static final <T, U> NewLazyList<T> from(T value, final Function1<U, T> function)
	{
		return new SimpleList(value, new map);
	}
	
	protected static final <T, U> NewLazyList<T> from(T value, final Function1<U, T> function)
	{
		return new WrapMonadic<T>(iterable);
	}
	
	@Override
	public boolean isEmpty()
	{
		if (! loaded)
		{
			loadList();
		}
		return this.head == null && this.tail == null;
	}
	
	@Override
	public T head()
	{
		if (! loaded)
		{
			loadList();
			ensureNotEmpty();
		}
		return head;
	}

	@Override
	public LinearSeq<T> tail()
	{
		if (! loaded)
		{
			loadList();
			ensureNotEmpty();
		}
		return tail;
	}
	
	@Override
	public <B> NewLazyList<B> map(Function1<? super T, B> function)
	{
		return (NewLazyList<B>) super.map(function);
	}
	
	@Override
	public NewLazyList<T> filter(Function1<? super T, Boolean> pred)
	{
		return (NewLazyList<T>) super.filter(pred);
	}

	@Override
	public <B> Builder<B, SharpCollection<B>> builder()
	{
		return null;
	}
	
	@Override
	public String toString()
	{
		if (this.isEmpty())
		{
			return "[]";
		}
		
		final StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("[" + this.head);
		
		NewLazyList<T> curTail = this.tail;
		
		while (curTail.loaded)
		{
			sBuilder.append(", " + curTail.head);
			curTail = curTail.tail;
		}
		
		if (! curTail.isEmpty())
		{
			sBuilder.append(", ?");
		}
		
		sBuilder.append(']');
		
		return sBuilder.toString();
	}
	
	/**
	 * Carrega a lista
	 */
	protected abstract void loadList();
	
	
	
	
	
	
	@Override
	public NewLazyList<Tuple2<T, Integer>> zipWithIndex()
	{
		return this.isEmpty() ? this : 
	}



	protected static final class SimpleList<A> extends NewLazyList<A>
	{
		protected SimpleList(final A head, final NewLazyList<A> tail)
		{
			this.head = head;
			this.tail = tail;
		}

		@Override
		protected void loadList()
		{
			loaded = true;
		}

	}



	/**
	 * Lazy list criada somente um iterator
	 */
	protected static final class WrapMonadic<A> extends NewLazyList<A>
	{
		private final Iterator<A> iterator;
		
		protected WrapMonadic(final Iterable<A> theIterable)
		{
			this(theIterable.iterator());
		}

		protected WrapMonadic(final Iterator<A> theIterator)
		{
			this.iterator = theIterator;
		}

		@Override
		protected void loadList()
		{
			loaded = true;
			if (iterator.hasNext())
			{
				head = iterator.next();
				tail = new WrapMonadic<A>(iterator);
			}
			else
			{
				head = null;
				tail = null;
			}
		}

		@Override
		public <B> NewLazyList<B> map(Function1<? super A, B> function)
		{
			//Repassa este iterator, por otimização
			return new MapMonadic<A, B>(this.iterator, function);
		}

		@Override
		public NewLazyList<A> filter(Function1<? super A, Boolean> pred)
		{
			return new FilterMonadic<A>(this.iterator, pred);
		}
	}

	/**
	 * Lista monádica para operações de filtro.
	 * ATENÇÃO: É possível criar essa operação somente com MapMonadic. No entanto,
	 * por motivos de otimização, foi criada uma nova operação.
	 */
	protected static final class FilterMonadic<A> extends NewLazyList<A>
	{
		private final Iterator<A> iterator;
		private final Function1<? super A, Boolean> predicate;
		
		protected FilterMonadic(final Iterable<A> theIterable, Function1<? super A, Boolean> predicate)
		{
			this(theIterable.iterator(), predicate);
		}
		
		protected FilterMonadic(final Iterator<A> theIterator, Function1<? super A, Boolean> predicate)
		{
			this.iterator = theIterator;
			this.predicate = predicate;
		}

		/**
		 * Carrega a lista
		 */
		protected void loadList()
		{
			loaded = true;
			
			head = null;
			tail = null;

			A curEle;
			
			while (iterator.hasNext())
			{
				curEle = iterator.next();
				
				if (predicate.apply(curEle))
				{
					head = curEle;
					tail = new FilterMonadic<A>(iterator, predicate);
					break;
				}
			}
		}
		
		@Override
		public <B> NewLazyList<B> map(final Function1<? super A, B> function)
		{
			return new MapMonadic<A, B>(this, function);
		}

		@Override
		public NewLazyList<A> filter(final Function1<? super A, Boolean> pred)
		{
			return new FilterMonadic<A>(this.iterator, new Function1<A, Boolean>()
			{
				@Override
				public Boolean apply(A param)
				{
					return predicate.apply(param) && pred.apply(param);
				}
			});
		}
	}
	
	/**
	 * Lista lazy que utiliza uma função de mapeamento
	 */
	protected static final class MapMonadic<A, B> extends NewLazyList<B>
	{
		private final Iterator<A> iterator;
		private final Function1<? super A, B> function;
		
		protected MapMonadic(final Iterable<A> theIterable, final Function1<? super A, B> function)
		{
			this(theIterable.iterator(), function);
		}

		protected MapMonadic(final Iterator<A> theIterator, final Function1<? super A, B> function)
		{
			this.iterator = theIterator;
			this.function = function;
		}

		@Override
		protected void loadList()
		{
			loaded = true;
			if (iterator.hasNext())
			{
				head = function.apply(iterator.next());
				tail = new MapMonadic<A, B>(iterator, function);
			}
			else
			{
				head = null;
				tail = null;
			}
		}
		
		@Override
		public <C> NewLazyList<C> map(Function1<? super B, C> function)
		{
			return new MapMonadic<A, C>(this.iterator, Functions.compose(this.function, function));
		}

		@Override
		public NewLazyList<B> filter(final Function1<? super B, Boolean> pred)
		{
			return new FilterMonadic<B>(this, pred);
		}
	}
	
	
	
	public static void main(String[] args)
	{
		NewLazyList<Integer> lista = NewLazyList.from(ListSharp.from(1, 2, 3, 4))
										.filter(IntegerOps.lowerThan(80))
										.map(IntegerOps.square)
										.filter(IntegerOps.lowerThan(80))
										.map(IntegerOps.square)
										.filter(IntegerOps.lowerThan(80))
										;
		System.out.println(lista);
	}

}
