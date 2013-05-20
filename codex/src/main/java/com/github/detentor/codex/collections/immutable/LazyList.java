package com.github.detentor.codex.collections.immutable;

import java.util.Comparator;
import java.util.Iterator;

import com.github.detentor.codex.collections.AbstractLinearSeq;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Function2;
import com.github.detentor.codex.function.PartialFunction0;
import com.github.detentor.codex.function.PartialFunction1;
import com.github.detentor.codex.function.arrow.impl.StatePartialArrow0;
import com.github.detentor.codex.product.Tuple2;

/**
 * O custo, em memória, de cada LazyList é 24 bytes numa máquina de 64bits. 
 * 
 * Lista encadeada Lazy. O principal benefício é poder executar as funções
 * 
 * @author Vinicius Seufitele
 *
 * @param <T>
 */
public class LazyList<T> extends AbstractLinearSeq<T, LazyList<T>>
{
	protected Object head;
	protected LazyList<T> tail;

	/**
	 * Esse objeto representa uma lista (genérica) vazia
	 */
	public static final LazyList<Object> Nil = new LazyList<Object>(null, null);

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

		while ( !(curTail.head instanceof Uninitialized) && curTail.notEmpty())
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
	 * Classe que guarda objetos não inicializados
	 */
	private static final class Uninitialized 
	{ 
		protected final Object keptObject;

		private Uninitialized(Object keptObject)
		{
			super();
			this.keptObject = keptObject;
		}
	}
	

	@Override
	public LazyList<T> drop(final Integer num)
	{
		return unfold(new StatePartialArrow0<Tuple2<Iterator<T>, Integer>, T>(Tuple2.from(this.iterator(), num))
		{
			@Override
			public T apply()
			{
				if (! isDefined())
				{
					throw new IllegalArgumentException("Apply chamado para uma função não definida");
				}
				return state.getVal1().next();
			}

			//Anda as casas necessárias do iterator
			private Tuple2<Iterator<T>, Integer> consumeIterator()
			{
				int curCount = state.getVal2();

				while (curCount-- > 0 && state.getVal1().hasNext())
				{
					//Pula o elemento
					state.getVal1().next();
				}
				//Seta de volta o valor 0
				state.setVal2(curCount);
				return state;
			}

			@Override
			public boolean isDefined()
			{
				return consumeIterator().getVal1().hasNext();
			}
		});
	}
	
	@Override
	public LazyList<T> take(final Integer num)
	{
		return unfold(new StatePartialArrow0<Tuple2<Iterator<T>, Integer>, T>(Tuple2.from(this.iterator(), num))
		{
			@Override
			public T apply()
			{
				if (! isDefined())
				{
					throw new IllegalArgumentException("Apply chamado para uma função não definida");
				}
				state.setVal2(state.getVal2() - 1);
				return state.getVal1().next();
			}

			@Override
			public boolean isDefined()
			{
				return state.getVal2() > 0 && state.getVal1().hasNext();
			}
		});
	}
	
	/**
	 * Produz uma coleção contendo resultados cumulativos ao aplicar a função passada como parâmetro
	 * da esquerda para a direita. A coleção retornada será calculada de maneira lazy. <br/>
	 * Esse método é o {@link #foldLeft(Object, Function2) foldLeft} lazy. 
	 * 
	 * @param startValue O valor inicial da computação
	 * @param func A função a ser aplicada a cada passo da computação
	 * @return Uma coleção contendo os valores ao aplicar a função cumulativamente
	 */
	public <B> LazyList<B> scanLeft(final B startValue, final Function2<B, T, B> func)
	{
		return unfold(new StatePartialArrow0<Tuple2<Iterator<T>, B>, B>(Tuple2.from(this.iterator(), startValue))
		{
			@Override
			public B apply()
			{
				final B retorno = func.apply(state.getVal2(), state.getVal1().next());
				state.setVal2(retorno);
				return retorno;
			}

			@Override
			public boolean isDefined()
			{
				return state.getVal1().hasNext();
			}
		});
	}
	
	/**
	 * Força a realização de todos os elementos desta lista lazy
	 * 
	 * @return A referência a esta lista, após a realização de todos os elementos
	 */
	public LazyList<T> force()
	{
		if (this.isEmpty())
		{
			return this;
		}
		
		LazyList<T> curTail = this.tail();
		
		while (curTail.notEmpty())
		{
			curTail = curTail.tail();
		}
		
		return this;
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
		protected LazyMonadic(final Object valueToKeep)
		{
			super((T) new Uninitialized(valueToKeep), null);
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
			if (head instanceof Uninitialized)
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
		protected UnfoldedList(final PartialFunction0<T> theGenFunction)
		{
			super(theGenFunction);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void extractValues()
		{
			final PartialFunction0<T> genFunction = (PartialFunction0<T>) ((Uninitialized) head).keptObject;
			
			if (genFunction.isDefined())
			{
				head = genFunction.apply();
				tail = new UnfoldedList<T>(genFunction);
			}
			else
			{
				head = null; //Essa chamada libera qualquer elemento guardado em keptObject
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
		protected LazyListI(final Iterator<T> theIterator)
		{
			super(theIterator);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void extractValues()
		{
			final Iterator<T> iterator = (Iterator<T>) ((Uninitialized) head).keptObject;
			
			if (iterator.hasNext())
			{
				head = iterator.next();
				tail = new LazyListI<T>(iterator);
			}
			else
			{
				head = null; //Essa chamada libera qualquer elemento guardado em keptObject
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
		protected MapMonadic(final Iterator<T> theIterator, final Function1<? super T, B> theMappingFunction)
		{
			super(Tuple2.from(theIterator, theMappingFunction));
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void extractValues()
		{
			final Tuple2<Iterator<T>, Function1<? super T, B>> tuple = 
					(Tuple2<Iterator<T>, Function1<? super T, B>>) ((Uninitialized) head).keptObject;
			
			if (tuple.getVal1().hasNext())
			{
				head = tuple.getVal2().apply(tuple.getVal1().next());
				tail = new MapMonadic<T, B>(tuple.getVal1(), tuple.getVal2());
			}
			else
			{
				head = null; //Essa chamada libera qualquer elemento guardado em keptObject
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
		protected FilterMonadic(final Iterator<T> theIterator, final Function1<? super T, Boolean> theFilterFunction)
		{
			super(Tuple2.from(theIterator, theFilterFunction));
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void extractValues()
		{
			final Tuple2<Iterator<T>, Function1<? super T, Boolean>> tuple = 
					(Tuple2<Iterator<T>, Function1<? super T, Boolean>>) ((Uninitialized) head).keptObject;
			
			while (tuple.getVal1().hasNext())
			{
				final T curEle = tuple.getVal1().next();
				
				if (tuple.getVal2().apply(curEle))
				{
					head = curEle;
					tail = new FilterMonadic<T>(tuple.getVal1(), tuple.getVal2());
					return;
				}
			}
			head = null; //Essa chamada libera qualquer elemento guardado em keptObject
			tail = null;
		}
	}

	/**
	 * Classe responsável por definir o comportamento de operações monádicas que
	 * incluem Map e Filter juntas (método collect).
	 */
	private static final class FMapMonadic<T, A> extends LazyMonadic<A>
	{
		protected FMapMonadic(final Iterator<T> theIterator, final PartialFunction1<? super T, A> thePartialFunction)
		{
			super(Tuple2.from(theIterator, thePartialFunction));
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void extractValues()
		{
			final Tuple2<Iterator<T>, PartialFunction1<? super T, A>> tuple = 
					(Tuple2<Iterator<T>, PartialFunction1<? super T, A>>) ((Uninitialized) head).keptObject;
			
			while (tuple.getVal1().hasNext())
			{
				final T curEle = tuple.getVal1().next();
				
				if (tuple.getVal2().isDefinedAt(curEle))
				{
					head = tuple.getVal2().apply(curEle);
					tail = new FMapMonadic<T, A>(tuple.getVal1(), tuple.getVal2());
					return;
				}
			}
			
			head = null; //Essa chamada libera qualquer elemento guardado em keptObject
			tail = null;
		}
	}
}
