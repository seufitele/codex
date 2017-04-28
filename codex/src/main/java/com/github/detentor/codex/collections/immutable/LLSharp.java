package com.github.detentor.codex.collections.immutable;

import java.util.Comparator;

import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.LinearSeq;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.PartialFunction1;
import com.github.detentor.codex.product.Tuple2;

/**
 * Implementação imutável da lista encadeada. <br/> <br/>
 * 
 *  A lista encadeada possui vantagens e desvantagens:  <br/><br/>
 *  
 *  <b>Vantagens</b>: <br/><br/>
 *  
 *  1 - {@link #add(Object) add}, {@link #head() head} e {@link #tail() tail} são O(1) <br/>
 *  2 - A estrutura naturalmente recursiva da lista facilita a codificação dos métodos, 
 *  e é especialmente útil para criação de listas cujo tamanho não é conhecido previamente (por exemplo,
 *  listas criadas a partir de um iterator), ou, até mesmo, listas infinitas.
 *  
 *  <br/><br/>
 *  
 *  <b>Desvantagens</b>: <br/><br/>
 *  
 *  1 - Tem um maior custo de memória do que listas baseadas em Array. <br/>
 *  2 - Calcular o tamanho da lista ({@link #size() size}) tem um custo de O(n). <br/>
 *  3 - Acesso a elementos em uma posição aleatória 'n' tem custo O(n) <br/>
 *  4 - A adição de elementos representa uma pilha (FIFO), não uma fila (LILO)
 *  
 * @author Vinicius Seufitele
 *
 * @param <T> O tipo de dados guardado na lista
 */
public class LLSharp<T> implements LinearSeq<T>
{
	private T head;
	private LLSharp<T> tail;

	// O objeto vazio
	private static final LLSharp<Object> Nil = new LLSharp<Object>(null, null);

	/**
	 * Construtor privado. Instâncias devem ser criadas com o 'from'
	 */
	protected LLSharp(final T theHead, final LLSharp<T> theTail)
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
	public static <A> LLSharp<A> empty()
	{
		return (LLSharp<A>) Nil;
	}

	/**
	 * Cria uma nova LinkedListSharp, a partir do valor passado como parâmetro. <br/>
	 * 
	 * @param <A> O tipo de dados da LinkedListSharp a ser retornada.
	 * @param valor O valor da LinkedListSharp
	 * @return Uma nova LinkedListSharp, cujo elemento será o valor passado como parâmetro
	 */
	public static <T> LLSharp<T> from(final T valor)
	{
		return LLSharp.<T>empty().add(valor);
	}
	
	/**
	 * Cria uma nova LinkedListSharp, a partir dos valores passados como parâmetro. <br/>
	 * 
	 * @param <A> O tipo de dados da LinkedListSharp a ser retornada.
	 * @param valores A LinkedListSharp a ser criada, a partir dos valores
	 * @return Uma nova LinkedListSharp, cujos elementos são os elementos passados como parâmetro
	 */
	@SuppressWarnings("unchecked")
	public static <T> LLSharp<T> from(final T... valores)
	{
		LLSharp<T> retorno = LLSharp.empty();

		for (int i = valores.length - 1; i > -1; i--)
		{
			retorno = retorno.add(valores[i]);
		}
		return retorno;
	}

	/**
	 * Cria uma instância de LinkedListSharp a partir dos elementos existentes no iterable passado como parâmetro. 
	 * A ordem da adição dos elementos será a mesma ordem do iterable.
	 * 
	 * @param <T> O tipo de dados da lista
	 * @param theIterable O iterator que contém os elementos
	 * @return Uma lista criada a partir da adição de todos os elementos do iterador
	 */
	public static <T> LLSharp<T> from(final Iterable<T> theIterable)
	{
		final LinkedListBuilder<T> builder = new LinkedListBuilder<T>();
		
		for (final T ele : theIterable)
		{
			builder.add(ele);
		}
		return builder.result();
	}

	@Override
	public boolean isEmpty()
	{
		//Compare this to Nil:
		return this.head() == null && this.tail() == null; 
	}

	@Override
	public T head()
	{
		return head;
	}

	@Override
	public LLSharp<T> tail()
	{
		return tail;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <B, U extends SharpCollection<B>> Builder<B, U> builder()
	{
		//TODO: Tentar remover o cast e remover o LLBuilder pra classe builders
		return (Builder<B, U>) new LinkedListBuilder<B>();
	}

	/**
     * Adiciona o elemento passado como parâmetro nesta coleção. <br/><br/>
     * 
     * @param element O elemento a ser adicionado nesta coleção
     * @return A referência à uma nova coleção, com o elemento adicionado
     */
	public LLSharp<T> add(final T element)
	{
		return new LLSharp<T>(element, this);
	}

	/**
     * Adiciona todos os elementos da coleção especificada nesta coleção.
     *
     * @param col Coleção contendo elemento a serem adicionados nesta coleção
     * @return A referência à uma nova coleção, com o elemento adicionado
     */
	public LLSharp<T> addAll(final Iterable<? extends T> col)
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

	//Overrides obrigatórios
	
	@Override
	public <B> LLSharp<B> map(final Function1<? super T, B> function)
	{
		return (LLSharp<B>) LinearSeq.super.map(function);
	}

	@Override
	public <B> LLSharp<B> collect(final PartialFunction1<? super T, B> pFunction)
	{
		return (LLSharp<B>) LinearSeq.super.collect(pFunction);
	}

	@Override
	public <B> LLSharp<B> flatMap(final Function1<? super T, ? extends Iterable<B>> function)
	{
		return (LLSharp<B>) LinearSeq.super.flatMap(function);
	}

	@Override
	public LLSharp<Tuple2<T, Integer>> zipWithIndex()
	{
		return (LLSharp<Tuple2<T, Integer>>) LinearSeq.super.zipWithIndex();
	}

	/**
	 * Essa classe é um builder para SharpCollection baseado em um LinkedListSharp. IMUTÁVEL.
	 * Esse builder assegura que a ordem de inserção será preservada. 
	 * @param <E> O tipo de dados do ListSharp retornado
	 */
	private static final class LinkedListBuilder<E> implements Builder<E, LLSharp<E>>
	{
		private LLSharp<E> list = LLSharp.empty();
		private LLSharp<E> last;

		@Override
		public void add(final E element)
		{
			if (list.isEmpty())
			{
				list = LLSharp.from(element);
				last = list;
			}
			else
			{
				final LLSharp<E> novoEle = LLSharp.from(element);
				last.tail = novoEle;
				last = novoEle;
			}
		}

		@Override
		public LLSharp<E> result()
		{
			return list;
		}
	}

//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Override
//	public LLSharp<T> sorted()
//	{
//		return sorted(new DefaultComparator());
//	}

	@Override
	public LLSharp<T> sorted(Comparator<? super T> comparator)
	{
		//TODO: Reescrever  
		return from(ListSharp.from(this).sorted(comparator));
	}
}
