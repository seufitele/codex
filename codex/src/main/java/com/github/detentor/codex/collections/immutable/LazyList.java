package com.github.detentor.codex.collections.immutable;

import java.util.Comparator;
import java.util.Iterator;

import com.github.detentor.codex.collections.AbstractLinearSeq;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.PartialFunction0;
import com.github.detentor.codex.function.PartialFunction1;
import com.github.detentor.codex.product.Tuple2;

/**
 * Lista encadeada Lazy. O principal benefício é poder executar
 * as funções
 * 
 * ATENÇÃO:  <br/>
 * 1 - Remover, após usar o iterator, a referência a ele, para evitar que ele fique com a referência 'presa'. <br/>
 * 2 - Analisar a possibilidade de remover os atributos de iterator e function, mergindo eles no valor do head, para
 * otimizar o gasto de memória. Como os valores são utilizados somente uma vez, então é possível criar uma classe
 * 'Uninitialized', que teria os atributos, e seria o valor inicial. 
 *   
 * @author Vinicius Seufitele
 *
 * @param <T>
 */
public class LazyList<T> extends AbstractLinearSeq<T, LazyList<T>>
{
	protected Object head = UNINITIALIZED;
	protected LazyList<T> tail;

	/**
	 * Esse objeto representa uma lista (genérica) vazia
	 */
	public static final LazyList<Object> Nil = new LazyList<Object>(null, null);

	//Valor ainda não carregado lazy
	private static final Object UNINITIALIZED = new Uninitialized();
	private static final class Uninitialized { }

	/**
	 * Construtor privado. Instâncias devem ser criadas com o 'from'
	 */
	protected LazyList(final T theHead, final LazyList<T> theTail)
	{
		head = theHead;
		tail = theTail;
	}

	/**
	 * Retorna uma instância de LazyList vazia. Como LazyList é imutável, retorna sempre {@link #Nil}.
	 * 
	 * @param <A> O tipo de dados da instância
	 * @return Uma instância de LazyList vazia.
	 */
	@SuppressWarnings("unchecked")
	public static <A> LazyList<A> empty()
	{
		return (LazyList<A>) Nil;
	}

	/**
	 * Cria uma nova LazyList, a partir do valor passado como parâmetro. <br/>
	 * Esse método é uma forma mais compacta de se criar LazyList.
	 * 
	 * @param <A> O tipo de dados da LazyList a ser retornada.
	 * @param valor O valor da LazyList
	 * @return Uma nova LazyList, cujo elemento será o valor passado como parâmetro
	 */
	@SuppressWarnings("unchecked")
	public static <T> LazyList<T> from(final T valor)
	{
		return new LazyList<T>(valor, (LazyList<T>) Nil);
	}
	
	/**
	 * Cria uma nova LazyList, a partir dos valores passados como parâmetro. <br/>
	 * Como os valores passados pertencem a um Array, então os elementos são "realizados" imediatamente.
	 * Esse método é uma forma mais compacta de se criar LazyList.
	 * 
	 * @param <A> O tipo de dados da LazyList a ser retornada.
	 * @param valores A LazyList a ser criada, a partir dos valores
	 * @return Uma nova LazyList, cujos elementos são os elementos passados como parâmetro
	 */
	public static <T> LazyList<T> from(final T... valores)
	{
		final LinkedListBuilder<T> builder = new LinkedListBuilder<T>();
		
		for (int i = 0; i < valores.length; i++)
		{
			builder.add(valores[i]);
		}
		return builder.result();
	}

	/**
	 * Cria uma instância de LazyList a partir dos elementos existentes no iterable passado como parâmetro. <br/>
	 * A ordem dos elementos será a mesma ordem do iterable, e o iterable só será consumido quando for necessário.
	 * 
	 * @param <T> O tipo de dados da lista
	 * @param theIterable O iterator que contém os elementos
	 * @return Uma lista criada a partir da adição de todos os elementos do iterador
	 */
	public static <T> LazyList<T> from(final Iterable<T> theIterable)
	{
		return new LazyListI<T>(theIterable.iterator());
	}

	/**
	 * Cria uma instância da LazyList a partir a função geradora. 
	 * Cada elemento da lista (potencialmente infinita) será definido pela chamada sucessiva à genFunction. <br/>
	 * ATENÇÃO: A lista só terá fim se em algum momento a função geradora retornar {@link #Nil}
	 * 
	 * @param genFunction A função que gerará os elementos da LazyList
	 * @return Uma lista infinita, cujos elementos só serão computados quando forem chamados
	 */
	public static <T> LazyList<T> unfold(final PartialFunction0<T> genFunction)
	{
		return new UnfoldedList<T>(genFunction);
	}
	
	@Override
	public boolean isEmpty()
	{
		//Compare this to Nil:
		return this.head() == null && this.tail() == null; 
	}

	@SuppressWarnings("unchecked")
	@Override
	public T head()
	{
		return (T) head;
	}

	@Override
	public LazyList<T> tail()
	{
		return tail;
	}
	
	/**
	 * {@inheritDoc} <br/>
	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
	 */
	@Override
	public int size()
	{
		return super.size();
	}

	@Override
	public <B> Builder<B, SharpCollection<B>> builder()
	{
		return new LinkedListBuilder<B>();
	}

	@Override
	public String toString()
	{
		if (this.isEmpty())
		{
			return "[]";
		}

		final StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("[" + this.head());

		LazyList<T> curTail = this.tail;

		while (curTail.head != UNINITIALIZED && curTail.notEmpty())
		{
			sBuilder.append(", " + curTail.head());
			curTail = curTail.tail;
		}

		if (! curTail.isEmpty())
		{
			sBuilder.append(", ?");
		}
		
		sBuilder.append(']');
		
		return sBuilder.toString();
	}
	
	//Overrides obrigatórios
	
	@Override
	public <B> LazyList<B> map(final Function1<? super T, B> function)
	{
		return (LazyList<B>) new MapMonadic<T, B>(this.iterator(), function);
	}
	
	@Override
	public LazyList<T> filter(final Function1<? super T, Boolean> pred)
	{
		return new FilterMonadic<T>(this.iterator(), pred);
	}

	@Override
	public <B> LazyList<B> collect(PartialFunction1<? super T, B> pFunction)
	{
		return (LazyList<B>) new FMapMonadic<T, B>(this.iterator(), pFunction);
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
	 * Essa classe é um builder para SharpCollection baseado em um LinkedListSharp. IMUTÁVEL. 
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public LazyList<T> sorted()
	{
		return sorted(new DefaultComparator());
	}

	@Override
	public LazyList<T> sorted(final Comparator<? super T> comparator)
	{
		return from(ListSharp.from(this).sorted(comparator));
	}
	
	/**
	 * Base para as classes {@link UnfoldedList}, {@link MapMonadic}, {@link FilterMonadic}, {@link FMapMonadic}.
	 */
	private static abstract class LazyMonadic<T> extends LazyList<T>
	{
		@SuppressWarnings("unchecked")
		protected LazyMonadic()
		{
			super((T) UNINITIALIZED, null);
		}
		
		/**
		 * Função que seta os valores do head e do tail
		 */
		protected abstract void extractValues();
		
		@Override
		public boolean isEmpty()
		{
			//Método sobrescrito para evitar a chamada ao método tail(), pois isso gera ciclo infinito
			return head() == null && tail == null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T head()
		{
			if (head == UNINITIALIZED)
			{
				extractValues();
			}
			return (T) head;
		}
		
		@Override
		public LazyList<T> tail()
		{
			//ensure chama isEmpty() -> que chama head(), que preenche o valor de tail
			ensureNotEmpty("tail foi chamado para uma coleção vazia");
			return tail;
		}
	}
	
	/**
	 * UnfoldedList são listas geradas a partir de uma função parcial geradora. <br/>
	 */
	private static final class UnfoldedList<T> extends LazyMonadic<T>
	{
		private final PartialFunction0<T> genFunction;
		
		protected UnfoldedList(final PartialFunction0<T> theGenFunction)
		{
			super();
			this.genFunction = theGenFunction;
		}

		@Override
		protected void extractValues()
		{
			if (genFunction.isDefined())
			{
				head = genFunction.apply();
				tail = new UnfoldedList<T>(genFunction);
			}
			else
			{
				//Finaliza as chamadas para a lista
				head = null;
				tail = null;
			}
		}
	}
	
	/**
	 * Lazy List criada a partir de um iterable, sem operação alguma definida. <br/>
	 * Base para as classes {@link MapMonadic}, {@link FilterMonadic}, {@link FMapMonadic}.
	 */
	private static class LazyListI<T> extends LazyMonadic<T>
	{
		private final Iterator<T> iterator;

		protected LazyListI(final Iterator<T> theIterator)
		{
			super();
			this.iterator = theIterator;
		}

		@Override
		protected void extractValues()
		{
			if (iterator.hasNext())
			{
				head = iterator.next();
				tail = new LazyListI<T>(iterator);
			}
			else
			{
				head = null;
				tail = null;
			}
		}
	}
	
	/**
	 * Classe responsável por definir o comportamento de operações monádicas que
	 * incluem somente Map
	 */
	private static final class MapMonadic<T, B> extends LazyMonadic<B>
	{
		private final Iterator<T> iterator;
		private final Function1<? super T, B> mappingFunction;

		protected MapMonadic(final Iterator<T> theIterator, final Function1<? super T, B> theMappingFunction)
		{
			super();
			this.iterator = theIterator;
			this.mappingFunction = theMappingFunction;
		}

		@Override
		protected void extractValues()
		{
			if (iterator.hasNext())
			{
				head = mappingFunction.apply(iterator.next());
				tail = new MapMonadic<T, B>(iterator, mappingFunction);
			}
			else
			{
				head = null;
				tail = null;
			}
		}
	}
	
	/**
	 * Classe responsável por definir o comportamento de operações monádicas que
	 * incluem somente Filter
	 */
	private static final class FilterMonadic<T> extends LazyMonadic<T>
	{
		private final Iterator<T> iterator;
		private final Function1<? super T, Boolean> filterFunction;

		protected FilterMonadic(final Iterator<T> theIterator, final Function1<? super T, Boolean> theFilterFunction)
		{
			super();
			this.iterator = theIterator;
			this.filterFunction = theFilterFunction;
		}

		@Override
		protected void extractValues()
		{
			while (iterator.hasNext())
			{
				final T curEle = iterator.next();
				
				if (filterFunction.apply(curEle))
				{
					head = curEle;
					tail = new FilterMonadic<T>(iterator, filterFunction);
					return;
				}
			}
			head = null;
			tail = null;
		}
	}
	
	/**
	 * Classe responsável por definir o comportamento de operações monádicas que
	 * incluem Map e Filter juntas (método collect).
	 */
	private static final class FMapMonadic<T, A> extends LazyMonadic<A>
	{
		private final Iterator<T> iterator;
		private final PartialFunction1<? super T, A> pFunction;

		protected FMapMonadic(final Iterator<T> theIterator, final PartialFunction1<? super T, A> thePartialFunction)
		{
			super();
			this.iterator = theIterator;
			this.pFunction = thePartialFunction;
		}

		@Override
		protected void extractValues()
		{
			while (iterator.hasNext())
			{
				final T curEle = iterator.next();
				
				if (pFunction.isDefinedAt(curEle))
				{
					head = pFunction.apply(curEle);
					tail = new FMapMonadic<T, A>(iterator, pFunction);
					return;
				}
			}
			head = null;
			tail = null;
		}
	}
}
