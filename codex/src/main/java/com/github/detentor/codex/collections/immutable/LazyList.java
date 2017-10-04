package com.github.detentor.codex.collections.immutable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.github.detentor.codex.cat.monads.Option;
import com.github.detentor.codex.collections.AbstractLinearSeq;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Function2;
import com.github.detentor.codex.function.Functions;
import com.github.detentor.codex.function.PartialFunction0;
import com.github.detentor.codex.function.PartialFunction1;
import com.github.detentor.codex.function.arrow.impl.StatePartialArrow0;
import com.github.detentor.codex.product.Tuple2;
import com.github.detentor.codex.util.RichIterator;

/**
 * Lista encadeada Lazy. Deve-se sempre optar por utilizar esta lista Lazy, por possibilitar a criação
 * de SharpCollections sem custo adicional de memória. <br/><br/>
 * 
 * Benefícios: <br/><br/>
 * 
 * 1) Serve de wrapper para qualquer iterator, possibilitando a criação instantânea de uma SharpCollection
 * sem custo adicional de memória (somente o custo do wrapper, que é negligível). <br/><br/>
 * 
 * 2) As funções de ordem superior que retornam coleções são lazy e permitem o encadeamento arbitrário de
 * operações. Além disso, a LazyList final será criada somente uma vez, ou seja, qualquer que seja o número
 * de funções encadeadas, o custo (de memória) será sempre O(n), onde n é o número de elementos do iterator usado na hora
 * de criar a LazyList.<br/><br/>
 * 
 * 3) As funções de ordem superior foram criadas de forma que não guardem uma referência ao 'head' da lista.
 * Isso significa que, no momento que algum elemento da lista não puder mais ser acessado, ele poderá ser coletado,
 * evitando gasto de memória desnecessário e potencial StackOverflow (comuns ao trabalhar com listas
 * infinitas. <br/><br/>
 * 
 * 4) Possibilidade de criação de listas infinitas com os métodos geradores. <br/><br/><br/>
 * 
 * Pode-se listar diversas maneiras de se criar funções geradoras para possibilitar a criação de estruturas lazy 
 * e listas infinitas. Listamos os mais comuns, com as suas forças e fraquezas: <br/><br/>
 * 
 * 1) Usar uma função parcial que carrega o estado (método escolhido): Tem o benefício de ser matematicamente
 * mais correto (a função para quando ela não estiver definida), mais otimizada (a mesma função é compartilhada
 * entre as instâncias, evitando o custo de criação e armazenamento) e não guarda referência à função ou lista anterior,
 * possibilitando o Garbage Collector limpar parte da lista que não é referenciada.
 *  
 * O lado ruim é que as implementações são mais complexas 
 * (uma função parcial com estado depende pesadamente do estado para definir o seu critério de parada),
 * dependem excessivamente de efeitos colaterais (a modificação direta do estado é uma resultante disso) e mais simples
 * de serem feitas de maneira errada (uma modificação incorreta no estado pode corromper toda a função). Como não é guardada
 * uma referência ao início da lista, é mais difícil implementar funções que precisam acessar o estado anterior da lista (como
 * exemplo, a função distinct requer uma inspeção no conteúdo da lista atual antes de adicionar alguma coisa).<br/><br/>
 * 
 * 2) Criar os métodos baseados no foldLeft: Tem o benefício de ter a implementação mais curta e simples: o código fica menor,
 * e é mais fácil intuir o comportamento da função a partir do código; é possível inspecionar sempre o estado anterior da lista,
 * visto que ele é sempre 'carregado' para a próxima execução. O lado ruim é que isso impossibilita a coleta da lista na maior parte
 * dos métodos. <br/><br/>
 * 
 * O custo, em memória, de cada LazyList é 24 bytes numa máquina de 64bits. 
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
	 * Cria uma LazyList a partir do valor passado como parâmetro. <br/>
	 * 
	 * @param <A> O tipo de dados da LazyList a ser retornada.
	 * @param valor O valor da LazyList
	 * @return Uma LazyList cujo elemento será o valor passado como parâmetro
	 */
	@SuppressWarnings("unchecked")
	public static <T> LazyList<T> from(final T valor)
	{
		return new LazyList<T>(valor, (LazyList<T>) Nil);
	}

	/**
	 * Cria uma LazyList a partir dos valores passados como parâmetro. <br/>
	 * Como os valores passados pertencem a um Array, então os elementos são "realizados" imediatamente.
	 * 
	 * @param <A> O tipo de dados da LazyList a ser retornada.
	 * @param valores A LazyList a ser criada, a partir dos valores
	 * @return Uma LazyList cujos elementos são os elementos passados como parâmetro
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
	 * @return Uma Lazy List criada a partir da adição de todos os elementos do iterador
	 */
	public static <T> LazyList<T> from(final Iterable<T> theIterable)
	{
		return new LazyListI<T>(theIterable.iterator());
	}

	/**
	 * Cria uma instância da LazyList a partir a função parcial geradora. 
	 * Cada elemento da lista (potencialmente infinita) será definido pela chamada sucessiva à genFunction. <br/>
	 * ATENÇÃO: A lista só terá fim se em algum momento a função geradora não estiver definida (isDefined retornar false).
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

		if (curTail.head instanceof Uninitialized || curTail.notEmpty())
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
	public <B> LazyList<B> collect(final PartialFunction1<? super T, B> pFunction)
	{
		return (LazyList<B>) new FMapMonadic<T, B>(this.iterator(), pFunction);
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

	@Override
	public LazyList<T> dropWhile(final Function1<? super T, Boolean> pred)
	{
		return unfold(new StatePartialArrow0<Tuple2<RichIterator<T>, Boolean>, T>(
				Tuple2.from(RichIterator.from(this.iterator()), true))
		{
			@Override
			public T apply()
			{
				return state.getVal1().next();
			}

			//Irá andar até que acabe o 'drop'
			private Iterator<T> consumeIterator()
			{
				while (state.getVal2() && state.getVal1().hasNext() && pred.apply(state.getVal1().peekNext().get()))
				{
					state.getVal1().next();
				}
				state.setVal2(false); //Já aplicou a função
				return state.getVal1();
			}

			@Override
			public boolean isDefined()
			{
				return consumeIterator().hasNext();
			}
		});
	}

	@Override
	public LazyList<T> takeWhile(final Function1<? super T, Boolean> pred)
	{
		return unfold(new StatePartialArrow0<Tuple2<RichIterator<T>, Boolean>, T>(
				Tuple2.from(RichIterator.from(this.iterator()), true))
		{
			@Override
			public T apply()
			{
				return state.getVal1().next();
			}

			@Override
			public boolean isDefined()
			{
				if (state.getVal2())
				{
					state.setVal2(state.getVal1().peekNext().notEmpty() && pred.apply(state.getVal1().peekNext().get()));
				}
				return state.getVal2();
			}
		});
	}

	@Override
	public Tuple2<LazyList<T>, LazyList<T>> partition(final Function1<? super T, Boolean> pred)
	{
		//Como filter é lazy, simplesmente cria um filter pra cada lista.
		//ATENÇÃO: ESSE CÓDIGO TEM COMPLEXIDADE N (2x). Se fosse feito o partition strict, ele teria complexidade N apenas.
		return Tuple2.from(this.filter(pred), this.filter(Functions.not(pred)));
	}

	/**
	 * {@inheritDoc} 
	 * ATENÇÃO: Se a coleção passada como parâmetro for infinita, este método pode não retornar.
	 */
	@Override
	public LazyList<T> intersect(final Iterable<T> withCollection)
	{
		//CÓDIGO usa um HashSet, o que acaba impactando a complexidade de memória
		return unfold(new StatePartialArrow0<Iterator<T>, T>(this.iterator())
		{
			private final Object UNINITIALIZED = new Uninitialized(null);
			private Object nextElement = UNINITIALIZED;
			private Set<T> eleSet = null;

			@SuppressWarnings("unchecked")
			@Override
			public T apply()
			{
				final T toReturn = (T) nextElement;
				nextElement = UNINITIALIZED;
				return toReturn;
			}

			private Object consumeIterator()
			{
				if (nextElement instanceof Uninitialized) //Tenta pegar o próximo elemento
				{
					T curEle = null;

					while (state.hasNext())
					{ 
						if (getEleSet().contains(curEle = state.next()))
						{
							nextElement = curEle;
							break;
						}
					}
				}
				return nextElement; //Devolve o valor original do nextElement
			}

			@Override
			public boolean isDefined()
			{
				return ! (consumeIterator() instanceof Uninitialized);
			}
			
			private Set<T> getEleSet()
			{
				if (eleSet == null)
				{
					eleSet = new HashSet<T>();

					for (T ele : withCollection)
					{
						eleSet.add(ele);
					}
				}
				return eleSet;
			}
		});
	}
	
	@Override
	public LazyList<T> distinct()
	{
		//Cria a lista
		final UnfoldedList<T> uList = new UnfoldedList<T>(null);

		//Cria a função, e passa a lista como parâmetro
		final StatePartialArrow0<Tuple2<Iterator<T>, LazyList<T>>, T> function = 
				new StatePartialArrow0<Tuple2<Iterator<T>, LazyList<T>>, T>(Tuple2.from(this.iterator(), (LazyList<T>) uList))
		{
			private final Object UNINITIALIZED = new Uninitialized(null);
			private Object nextElement = UNINITIALIZED;

			@SuppressWarnings("unchecked")
			@Override
			public T apply()
			{
				final T toReturn = (T) nextElement;
				nextElement = UNINITIALIZED;
				
				return toReturn;
			}

			private Object consumeIterator()
			{
				if (nextElement instanceof Uninitialized) //Tenta pegar o próximo elemento
				{
					T curEle = null;

					while (state.getVal1().hasNext())
					{ 
						if (! contains(state.getVal2(), curEle = state.getVal1().next()))
						{
							nextElement = curEle;
							break;
						}
					}
				}
				return nextElement; //Devolve o valor original do nextElement
			}
			
			//Contains privado que evita chamadas recursivas 
			private boolean contains(final LazyList<T> theList, final T element)
			{
				LazyList<T> curList = theList;
				
				while (! (curList.head instanceof Uninitialized))
				{
					if (curList.head.equals(element))
					{
						return true;
					}
					curList = curList.tail;
				}
				return false;
			}

			@Override
			public boolean isDefined()
			{
				return ! (consumeIterator() instanceof Uninitialized);
			}
		};

		//Seta a função no head
		uList.head = new Uninitialized(function);
		return uList;
	}

	@Override
	public <B> LazyList<B> flatMap(final Function1<? super T, ? extends Iterable<B>> function)
	{
		return unfold(new StatePartialArrow0<Iterator<T>, B>(this.iterator())
		{
			private final Object UNINITIALIZED = new Uninitialized(null);
			private Object nextElement = UNINITIALIZED;

			@SuppressWarnings("unchecked")
			@Override
			public B apply()
			{
				final B toReturn = ((Iterator<B>) nextElement).next(); //Extrai o elemento

				if (!((Iterator<B>) nextElement).hasNext()) //Reinicializa
				{
					nextElement = UNINITIALIZED; 
				}
				return toReturn;
			}

			private Object consumeIterator()
			{
				if (nextElement instanceof Uninitialized) //Tenta pegar o próximo elemento
				{
					Iterator<B> curEle = null;

					while (state.hasNext())
					{ 
						curEle = function.apply(state.next()).iterator();
						if (curEle.hasNext())
						{
							nextElement = curEle;
							break;
						}
					}
				}
				return nextElement; //Devolve o valor original do nextElement
			}

			@Override
			public boolean isDefined()
			{
				return ! (consumeIterator() instanceof Uninitialized);
			}
		});
	}

	@Override
	public Tuple2<LazyList<T>, LazyList<T>> splitAt(final Integer num)
	{
		//Como filter é lazy, simplesmente cria um filter pra cada lista.
		//ATENÇÃO: ESSE CÓDIGO TEM COMPLEXIDADE N (2x). Se fosse feito o partition strict, ele teria complexidade N apenas.
		return Tuple2.from(take(num), drop(num));
	}

	@Override
	public LazyList<LazyList<T>> grouped(final Integer size)
	{
		return unfold(new StatePartialArrow0<Iterator<T>, LazyList<T>>(this.iterator())
		{
			@Override
			public LazyList<T> apply()
			{
				LazyList<T> curEle = new LazyList<T>(null, null);
				final LazyList<T> first = curEle;
				
				int count = 0;

				while (count++ < size && state.hasNext())
				{
					curEle.head = state.next();
					curEle.tail = new LazyList<T>(null, null);
					curEle = curEle.tail;
				}
				return first;
			}

			@Override
			public boolean isDefined()
			{
				return state.hasNext();
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
	
	@Override
	public LazyList<Tuple2<T, Integer>> zipWithIndex()
	{
		return unfold(new StatePartialArrow0<Tuple2<Iterator<T>, Integer>, Tuple2<T, Integer>>(Tuple2.from(this.iterator(), 0))
		{
			@Override
			public Tuple2<T, Integer> apply()
			{
				final Integer curIndex = state.getVal2();
				state.setVal2(curIndex + 1); 
				return Tuple2.from(state.getVal1().next(), curIndex);
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

	//MÉTODOS REESCRITOS APENAS PELA DOCUMENTAÇÃO - PARA AVISAR QUE OS MÉTODOS LAZY NÃO 
	//IRÃO RETORNAR, CASO A LISTA SEJA INFINITA
	
	/**
	 * {@inheritDoc}
	 * ATENÇÃO: Para listas infinitas esse método não irá retornar.
	 */
	@Override
	public T last()
	{
		return super.last();
	}
	
	/**
	 * {@inheritDoc}
	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
	 */
	@Override
	public LazyList<T> takeRight(final Integer num)
	{
		return super.takeRight(num);
	}

	/**
	 * {@inheritDoc}
	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
	 */
	@Override
	public LazyList<T> dropRight(final Integer num)
	{
		return super.dropRight(num);
	}

	/**
	 * {@inheritDoc}
	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
	 */
	@Override
	public LazyList<T> dropRightWhile(final Function1<? super T, Boolean> pred)
	{
		return super.dropRightWhile(pred);
	}
	
	/**
	 * {@inheritDoc}
	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
	 */
	@Override
	public LazyList<T> takeRightWhile(Function1<? super T, Boolean> pred)
	{
		return super.takeRightWhile(pred);
	}
	
	/**
	 * {@inheritDoc}
	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
	 */
	@Override
	public Option<T> lastOption()
	{
		return super.lastOption();
	}

	/**
	 * {@inheritDoc}
	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
	 */
	@Override
	public String mkString()
	{
		return super.mkString();
	}

	/**
	 * {@inheritDoc}
	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
	 */
	@Override
	public String mkString(final String separator)
	{
		return super.mkString(separator);
	}

	/**
	 * {@inheritDoc}
	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
	 */
	@Override
	public String mkString(final String start, final String separator, final String end)
	{
		return super.mkString(start, separator, end);
	}

//	/**
//	 * {@inheritDoc}
//	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
//	 */
//	@Override
//	public T maxWith(final Comparator<? super T> comparator)
//	{
//		return super.maxWith(comparator);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
//	 */
//	@Override
//	public T minWith(final Comparator<? super T> comparator)
//	{
//		return super.minWith(comparator);
//	}

	/**
	 * {@inheritDoc}
	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
	 */
	@Override
	public T min()
	{
		return super.min();
	}

	/**
	 * {@inheritDoc}
	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
	 */
	@Override
	public T max()
	{
		return super.max();
	}

	/**
	 * {@inheritDoc}
	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
	 */
	@Override
	public Option<T> minOption()
	{
		return super.minOption();
	}

	/**
	 * {@inheritDoc}
	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
	 */
	@Override
	public Option<T> maxOption()
	{
		return super.maxOption();
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

	/**
	 * {@inheritDoc}
	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
	 */
	@Override
	public List<T> toList()
	{
		return super.toList();
	}

	/**
	 * {@inheritDoc}
	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
	 */
	@Override
	public List<T> toList(Builder<T, List<T>> builder)
	{
		return super.toList(builder);
	}

	/**
	 * {@inheritDoc}
	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
	 */
	@Override
	public Set<T> toSet()
	{
		return super.toSet();
	}

	/**
	 * {@inheritDoc}
	 * ATENÇÃO: Para listas infinitas essa função não irá retornar.
	 */
	@Override
	public Set<T> toSet(Builder<T, Set<T>> builder)
	{
		return super.toSet(builder);
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
	 * Classe responsável por definir o comportamento de operações monádicas que incluem somente Map
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
	 * Classe responsável por definir o comportamento de operações monádicas que incluem somente Filter
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
