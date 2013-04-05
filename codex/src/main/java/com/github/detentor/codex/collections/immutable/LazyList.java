package com.github.detentor.codex.collections.immutable;

import static com.github.detentor.codex.function.Functions.compose;
import static com.github.detentor.codex.function.Functions.createPartial;

import java.io.Serializable;
import java.util.Iterator;

import com.github.detentor.codex.collections.AbstractLinearSeq;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.PartialFunction;
import com.github.detentor.codex.product.Tuple2;

/**
 * Implementação de uma lista 'Lazy': a lista não computa o resultado dos elementos até que seja necessário. <br/>
 * 
 * Note-se que esta lista utiliza uma lista encadeada para guardar os elementos. <br/>
 * 
 * -> Criação com iterable não consumirá imediatamente o iterable. -> Todas as operações de ordem superior não são executadas
 * imediatamente
 * 
 * -> As operações só são executadas quando necessário, o que geralmente acontece quando o método {@link #size()} ou
 * {@link #apply(Integer)} são chamados. Note-se que {@link #size()} só gerará a criação da lista caso ela tenha passado por alguma
 * transformação que possa, potencialmente, alterar o seu tamanho.
 * 
 * @author Vinicius Seufitele
 * 
 * @param <T>
 */
public class LazyList<T> extends AbstractLinearSeq<T, LazyList<T>> implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected T head;
	protected LazyList<T> tail;

	// O objeto vazio
	private static final LazyList<Object> Nil = new LazyList<Object>(null, null);

	protected LazyList()
	{
		this(null, null);
	}

	/**
	 * Construtor privado. Instâncias devem ser criadas com o 'from'
	 */
	protected LazyList(final T theHead, final LazyList<T> theTail)
	{
		head = theHead;
		tail = theTail;
	}

	/**
	 * Retorna uma instância de LinkedListSharp vazia
	 * 
	 * @param <A> O tipo de dados da instância
	 * @return Uma instância de LLSharp vazia.
	 */
	@SuppressWarnings("unchecked")
	public static <A> LazyList<A> empty()
	{
		return (LazyList<A>) Nil;
	}

	/**
	 * Cria uma nova LinkedListSharp, a partir do valor passado como parâmetro. <br/>
	 * Esse método é uma forma mais compacta de se criar LinkedListSharp.
	 * 
	 * @param <A> O tipo de dados da LinkedListSharp a ser retornada.
	 * @param valor O valor da LinkedListSharp
	 * @return Uma nova LinkedListSharp, cujo elemento será o valor passado como parâmetro
	 */
	public static <T> LazyList<T> from(final T valor)
	{
		return LazyList.<T> empty().add(valor);
	}

	/**
	 * Cria uma nova LinkedListSharp, a partir dos valores passados como parâmetro. <br/>
	 * Esse método é uma forma mais compacta de se criar LinkedListSharp.
	 * 
	 * @param <A> O tipo de dados da LinkedListSharp a ser retornada.
	 * @param valores A LinkedListSharp a ser criada, a partir dos valores
	 * @return Uma nova LinkedListSharp, cujos elementos são os elementos passados como parâmetro
	 */
	public static <T> LazyList<T> from(final T... valores)
	{
		LazyList<T> retorno = LazyList.empty();

		for (int i = valores.length - 1; i > -1; i--)
		{
			retorno = retorno.add(valores[i]);
		}
		return retorno;
	}

	/**
	 * Cria uma instância de LinkedListSharp a partir dos elementos existentes no iterable passado como parâmetro. A ordem da adição
	 * dos elementos será a mesma ordem do iterable.
	 * 
	 * @param <T> O tipo de dados da lista
	 * @param theIterable O iterator que contém os elementos
	 * @return Uma lista criada a partir da adição de todos os elementos do iterador
	 */
	public static <T> LazyList<T> from(final Iterable<T> theIterable)
	{
		return new LazyListIterator<T>(theIterable.iterator());
	}

	@Override
	public boolean isEmpty()
	{
		// Compare this to Nil:
		return this.head() == null && this.tail() == null;
	}

	@Override
	public T head()
	{
		ensureNotEmpty("head foi chamado para uma coleção vazia");
		return head;
	}

	@Override
	public LazyList<T> tail()
	{
		ensureNotEmpty("tail foi chamado para uma coleção vazia");
		return tail;
	}

	@Override
	public <B> Builder<B, SharpCollection<B>> builder()
	{
		return new LinkedListBuilder<B>();
	}

	/**
	 * Cria uma LazyList que representa a adição do elemento passado como parâmetro com esta lista
	 */
	public LazyList<T> add(final T element)
	{
		return new LazyList<T>(element, this);
	}

	public LazyList<T> addAll(final Iterable<? extends T> col)
	{
		for (T ele : col)
		{
			this.add(ele);
		}
		return this;
	}

	@Override
	public String toString()
	{
		return mkString("[", ", ", "]");
	}

	// Overrides obrigatórios

	@Override
	public <B> LazyList<B> map(Function1<? super T, B> function)
	{
		return (LazyList<B>) new MapMonadic<T, B>(this, function);
	}

	@Override
	public LazyList<T> filter(Function1<? super T, Boolean> pred)
	{
		return new FilterMonadic<T>(this, pred);
	}

	@Override
	public <B> LazyList<B> collect(PartialFunction<? super T, B> pFunction)
	{
		return (LazyList<B>) new FMapMonadic<T, B>(this, pFunction);
	}

	@Override
	public <B> LazyList<B> flatMap(Function1<? super T, ? extends SharpCollection<B>> function)
	{
		return (LazyList<B>) super.flatMap(function);
	}

	@Override
	public LazyList<Tuple2<T, Integer>> zipWithIndex()
	{
		return (LazyList<Tuple2<T, Integer>>) super.zipWithIndex();
	}

	/**
	 * Essa classe é um builder para SharpCollection baseado em um LinkedListSharp.
	 * 
	 * @author Vinícius Seufitele Pinto
	 * 
	 * @param <E> O tipo de dados do ListSharp retornado
	 */
	private static final class LinkedListBuilder<E> implements Builder<E, SharpCollection<E>>
	{
		private LazyList<E> list = LazyList.empty();
		private LazyList<E> last;

		@Override
		public void add(final E element)
		{
			if (list.isEmpty())
			{
				list = LazyList.from(element);
				last = list;
			}
			else
			{
				final LazyList<E> novoEle = LazyList.from(element);
				last.tail = novoEle;
				last = novoEle;
			}
		}

		@Override
		public LazyList<E> result()
		{
			return list;
		}
	}
	
	/**
	 * Função base para a construção de subclasses monádicas (que permitem encadeamento) 
	 */
	private static class LazyMonadic<T> extends LazyList<T>
	{
		private static final long serialVersionUID = 1L;

		protected boolean loadedHead = false;
		protected boolean loadedTail = false;
	}

	/**
	 * Cria uma lista Lazy a partir de um iterator
	 */
	private static class LazyListIterator<T> extends LazyMonadic<T>
	{
		private static final long serialVersionUID = 1L;
		private final Iterator<T> iterator;
		
		protected boolean loadedHead = false;
		protected boolean loadedTail = false;

		private LazyListIterator(final Iterator<T> iterator)
		{
			super();
			this.iterator = iterator;
		}

		@Override
		public T head()
		{
			if (!loadedHead)
			{
				loadedHead = true;
				head = iterator.next();
			}
			return head;
		}

		@SuppressWarnings("unchecked")
		@Override
		public LazyList<T> tail()
		{
			if (!loadedTail)
			{
				loadedTail = true;
				head();
				tail = iterator.hasNext() ? new LazyListIterator<T>(iterator) : (LazyList<T>) LazyList.empty();
			}
			return tail;
		}
	}

	/**
	 * Criação de uma lazy list monádica, que representa uma operação de map. <br/>
	 * Essa operação merece uma classe especial porque é possível saber que só temos 'maps' encadeados caso o tipo da classe seja este.
	 * Isso também garante que o tamanho da lista não está sendo modificado, somente o tipo de retorno de seus elementos.
	 */
	private static final class MapMonadic<A, B> extends LazyMonadic<B>
	{
		private static final long serialVersionUID = -7126502809258140442L;

		private final LazyList<A> originalList;
		private final Function1<? super A, B> mappingFunction;

		protected MapMonadic(final LazyList<A> originalList, final Function1<? super A, B> mappingFunction)
		{
			super();
			this.originalList = originalList;
			this.mappingFunction = mappingFunction;
		}

		@Override
		public B head()
		{
			if (!loadedHead)
			{
				loadedHead = true;

				if (originalList.isEmpty())
				{
					loadedTail = true;
					head = null;
					tail = null;
					ensureNotEmpty("head foi chamado para uma coleção vazia");
				}
				else
				{
					head = mappingFunction.apply(originalList.head());
				}
			}
			return head;
		}

		@Override
		public LazyList<B> tail()
		{
			if (!loadedTail)
			{
				loadedTail = true;
				tail = new MapMonadic<A, B>(originalList.tail(), mappingFunction);
			}
			return tail;
		}

		@Override
		public <C> LazyList<C> map(final Function1<? super B, C> function)
		{
			return new MapMonadic<A, C>(originalList, compose(mappingFunction, function));
		}

		@Override
		public LazyList<B> filter(final Function1<? super B, Boolean> pred)
		{
			return new FMapMonadic<A, B>(originalList, new PartialFunction<A, B>()
			{
				@Override
				public B apply(final A param)
				{
					return mappingFunction.apply(param);
				}

				@Override
				public boolean isDefinedAt(final A forValue)
				{
					return pred.apply(mappingFunction.apply(forValue));
				}
			});
		}

		@Override
		public <C> LazyList<C> collect(final PartialFunction<? super B, C> pFunction)
		{
			return new FMapMonadic<A, C>(originalList, compose(mappingFunction, pFunction));
		}
	}

	/**
	 * A criação de um filtro com map monádico, para permitir o encadeamento sequencial. <br/>
	 * <br/>
	 * 
	 * O filtro map monádico representa um mapeamento que, potencialmente, irá alterar o tamanho da sequência (reduzindo ou
	 * aumentando).
	 * 
	 * @author Vinicius Seufitele Pinto
	 * 
	 * @param <A>
	 * @param <B>
	 */
	private static final class FMapMonadic<A, B> extends LazyMonadic<B>
	{
		private static final long serialVersionUID = 7562646182915871262L;

		private final LazyList<A> originalList;
		private final PartialFunction<? super A, B> pFunction;

		protected FMapMonadic(final LazyList<A> originalList, final PartialFunction<? super A, B> mappingFunction)
		{
			super();
			this.originalList = originalList;
			this.pFunction = mappingFunction;
		}

		@Override
		public B head()
		{
			if (!loadedHead)
			{
				loadedHead = true;
				loadedTail = true;

				// Encontra, se houver, um elemento onde a função de filtro está definida
				LazyList<A> curPos = originalList;

				while (curPos.notEmpty() && !pFunction.isDefinedAt(curPos.head()))
				{
					curPos = curPos.tail();
				}
				//

				// Se não houver, esta lista representa uma lista vazia
				if (curPos.isEmpty())
				{
					head = null;
					tail = null;
					ensureNotEmpty("head foi chamado para uma coleção vazia");
				}
				else
				{
					// Do contrário, o valor é o elemento filtrado e mapeado
					head = pFunction.apply(curPos.head());
					// Passa para o tail o restante do trabalho
					tail = new FMapMonadic<A, B>(curPos.tail(), pFunction);
				}
			}
			return head;
		}

		@Override
		public LazyList<B> tail()
		{
			if (!loadedTail)
			{
				head();
			}
			return tail;
		}

		@Override
		public <C> LazyList<C> map(final Function1<? super B, C> function)
		{
			return new FMapMonadic<A, C>(originalList, compose(pFunction, function));
		}

		@Override
		public LazyList<B> filter(final Function1<? super B, Boolean> pred)
		{
			return new FMapMonadic<A, B>(originalList, new PartialFunction<A, B>()
			{
				@Override
				public B apply(final A param)
				{
					return pFunction.apply(param);
				}

				@Override
				public boolean isDefinedAt(final A forValue)
				{
					return pFunction.isDefinedAt(forValue) && pred.apply(pFunction.apply(forValue));
				}
			});
		}

		@Override
		public <C> LazyList<C> collect(final PartialFunction<? super B, C> pFunction)
		{
			return new FMapMonadic<A, C>(originalList, compose(this.pFunction, pFunction));
		}
	}

	/**
	 * A criação de um filtro monádico, para permitir o encadeamento sequencial. <br/>
	 * <br/>
	 * 
	 * O filtro monádico representa um mapeamento que, potencialmente, irá alterar o tamanho da sequência (reduzindo ou aumentando).
	 * 
	 * @author Vinicius Seufitele Pinto
	 * 
	 * @param <A>
	 * @param <B>
	 */
	private static final class FilterMonadic<A> extends LazyMonadic<A>
	{
		private static final long serialVersionUID = 7562646182915871262L;

		private final LazyList<A> originalList;
		private final Function1<? super A, Boolean> pred;

		protected FilterMonadic(final LazyList<A> originalList, final Function1<? super A, Boolean> predicate)
		{
			super();
			this.originalList = originalList;
			this.pred = predicate;
		}

		@Override
		public A head()
		{
			if (!loadedHead)
			{
				loadedHead = true;
				loadedTail = true;

				// Encontra, se houver, um elemento onde a função de filtro está definida
				LazyList<A> curPos = originalList;

				while (curPos.notEmpty() && !pred.apply(curPos.head()))
				{
					curPos = curPos.tail();
				}
				//

				// Se não houver, esta lista representa uma lista vazia
				if (curPos.isEmpty())
				{
					head = null;
					tail = null;
					ensureNotEmpty("head foi chamado para uma coleção vazia");
				}
				else
				{
					// Do contrário, o valor é o elemento filtrado e mapeado
					head = curPos.head();
					// Passa para o tail o restante do trabalho
					tail = new FilterMonadic<A>(curPos.tail(), pred);
				}
			}
			return head;
		}

		@Override
		public LazyList<A> tail()
		{
			if (!loadedTail)
			{
				head();
			}
			return tail;
		}

		@Override
		public <C> LazyList<C> map(final Function1<? super A, C> function)
		{
			return new FMapMonadic<A, C>(originalList, createPartial(pred, function));
		}

		@Override
		public LazyList<A> filter(final Function1<? super A, Boolean> pred)
		{
			return new FilterMonadic<A>(originalList, new Function1<A, Boolean>()
			{
				@Override
				public Boolean apply(final A param)
				{
					return FilterMonadic.this.pred.apply(param) && pred.apply(param);
				}
			});
		}

		@Override
		public <C> LazyList<C> collect(final PartialFunction<? super A, C> pFunction)
		{
			return new FMapMonadic<A, C>(originalList, new PartialFunction<A, C>()
			{
				@Override
				public C apply(A param)
				{
					return pFunction.apply(param);
				}

				@Override
				public boolean isDefinedAt(A forValue)
				{
					return FilterMonadic.this.pred.apply(forValue) && pFunction.isDefinedAt(forValue);
				}
			});
		}
	}
}
