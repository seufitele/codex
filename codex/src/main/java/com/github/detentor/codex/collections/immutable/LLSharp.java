package com.github.detentor.codex.collections.immutable;

import com.github.detentor.codex.collections.AbstractLinearSeq;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.PartialFunction;
import com.github.detentor.codex.product.Tuple2;

/**
 * Implementação mutável da lista encadeada. <br/> 
 * A mutabilidade da lista é limitada: ela permite adicionar e remover elementos 'inplace' (ou seja,
 * a estrutura interna da lista é modificada depois da adição ou remoção), mas não é possível setar,
 * diretamente, o valor do head ou do tail. <br/><br/>
 * 
 *  A lista encadeada é ideal para remoção do primeiro elemento, ou adição de elementos. Note-se que a 
 *  lista encadeada adiciona elementos como uma pilha (FILO). Além disso, é a estrutura ideal para 
 *  a criação de listas (potencialmente) infinitas.
 * 
 * @author Vinicius Seufitele
 *
 * @param <T>
 */
public class LLSharp<T> extends AbstractLinearSeq<T, LLSharp<T>>
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
	 * Esse método é uma forma mais compacta de se criar LinkedListSharp.
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
	 * Esse método é uma forma mais compacta de se criar LinkedListSharp.
	 * 
	 * @param <A> O tipo de dados da LinkedListSharp a ser retornada.
	 * @param valores A LinkedListSharp a ser criada, a partir dos valores
	 * @return Uma nova LinkedListSharp, cujos elementos são os elementos passados como parâmetro
	 */
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
	 * Cria uma instância de LinkedListSharp a partir dos elementos existentes no iterable passado como parâmetro. A ordem da adição dos
	 * elementos será a mesma ordem do iterable.
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

	@Override
	public <B> Builder<B, SharpCollection<B>> builder()
	{
		return new LinkedListBuilder<B>();
	}

	/**
	 * Cria uma LLSharp que representa a adição do elemento passado como parâmetro com esta lista
	 */
	public LLSharp<T> add(final T element)
	{
		return new LLSharp<T>(element, this);
	}

	public LLSharp<T> addAll(final Iterable<? extends T> col)
	{
		for (T ele : col)
		{
			this.add(ele);
		}
		return this;
	}
	
//	@Override
//	public LLSharp<T> remove(final T element)
//	{
//		if (this.isEmpty())
//		{
//			return this;
//		}
//
//		if (Option.from(this.head).equals(Option.from(element)))
//		{
//			this.head = this.tail.head;
//			this.tail = this.tail.tail;
//		}
//		else
//		{
//			this.tail = this.tail.remove(element);
//		}
//		return this;
//	}
//
//	@Override
//	public LLSharp<T> removeAll(final Iterable<T> col)
//	{
//		for (T ele : col)
//		{
//			this.remove(ele);
//		}
//		return this;
//	}

	@Override
	public String toString()
	{
		return mkString("[", ", ", "]");
	}
	
	//Overrides obrigatórios
	
	@Override
	public <B> LLSharp<B> map(Function1<? super T, B> function)
	{
		return (LLSharp<B>) super.map(function);
	}

	@Override
	public <B> LLSharp<B> collect(PartialFunction<? super T, B> pFunction)
	{
		return (LLSharp<B>) super.collect(pFunction);
	}

	@Override
	public <B> LLSharp<B> flatMap(Function1<? super T, ? extends SharpCollection<B>> function)
	{
		return (LLSharp<B>) super.flatMap(function);
	}

	@Override
	public LLSharp<Tuple2<T, Integer>> zipWithIndex()
	{
		return (LLSharp<Tuple2<T, Integer>>) super.zipWithIndex();
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
	
}
