package com.github.detentor.codex.collections.immutable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.detentor.codex.collections.AbstractIndexedSeq;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.collections.builders.ImArrayBuilder;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Functions;
import com.github.detentor.codex.function.PartialFunction;
import com.github.detentor.codex.product.Tuple2;

/**
 * Implementação de uma lista 'Lazy': a lista não computa o resultado dos elementos até que seja necessário. <br/>
 *
 * ATENÇÃO: Embora esta classe tenha passado nos testes, deve-se utilizá-la com cuidado. Alguns métodos ainda não
 * estão lazy, e ela precisa ser melhor documentada.
 * 
 * @author Vinicius Seufitele
 *
 * @param <T>
 */
public class LazyList<T> extends AbstractIndexedSeq<T, LazyList<T>> implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final int startIndex;
	private final int theSize;

	private final Object[] data;

	// Singleton, pois como é imutável não faz sentido criar várias
	private static final LazyList<Object> EMPTY_LIST = new LazyList<Object>();

	/**
	 * Construtor privado. Instâncias devem ser criadas com o 'from'
	 */
	protected LazyList()
	{
		startIndex = 0;
		theSize = 0;
		data = new Object[0];
	}

	/**
	 * Construtor privado, que reutiliza o objeto theData passado.
	 */
	protected LazyList(final Object[] theData)
	{
		this(theData, 0, theData.length);
	}

	/**
	 * Construtor privado, que reutiliza o objeto theData passado.
	 * 
	 * @param theData
	 * @param theStart Representa onde vai começar o índice da lista
	 * @param theEnd Representa onde vai terminar a lista (exclusive)
	 */
	protected LazyList(final Object[] theData, final int theStart, final int theEnd)
	{
		startIndex = theStart;
		theSize = theEnd - theStart;
		data = theData;
	}

	/**
	 * Construtor privado, que reutiliza o objeto passado.
	 * 
	 * @param prevList
	 * @param theStart Representa onde vai começar o índice da lista
	 * @param theEnd Representa onde vai terminar a lista (exclusive)
	 */
	protected LazyList(final LazyList<T> prevList, final int theStart, final int theEnd)
	{
		startIndex = prevList.startIndex + theStart;
		theSize = theEnd - theStart;
		data = prevList.data;
	}

	/**
	 * Constrói uma instância de ListSharp vazia.
	 * 
	 * @param <A> O tipo de dados da instância
	 * @return Uma instância de ListSharp vazia.
	 */
	@SuppressWarnings("unchecked")
	public static <A> LazyList<A> empty()
	{
		// Retorna sempre a mesma lista - afinal, ela é imutável
		return (LazyList<A>) EMPTY_LIST;
	}

	/**
	 * Cria uma instância de ListSharp a partir dos elementos existentes no iterable passado como parâmetro. A ordem da adição dos
	 * elementos será a mesma ordem do iterable.
	 * 
	 * @param <T> O tipo de dados da lista
	 * @param theIterable O iterator que contém os elementos
	 * @return Uma lista criada a partir da adição de todos os elementos do iterador
	 */
	public static <T> LazyList<T> from(final Iterable<T> theIterable)
	{
		// por enquanto está bem porco, melhorar
		final List<T> listaRetorno = new ArrayList<T>();

		for (final T ele : theIterable)
		{
			listaRetorno.add(ele);
		}
		return new LazyList<T>(listaRetorno.toArray());
	}

	/**
	 * Cria uma nova ListSharp, a partir dos valores passados como parâmetro. <br/>
	 * Esse método é uma forma mais compacta de se criar ListSharp.
	 * 
	 * @param <A> O tipo de dados da ListSharp a ser retornada.
	 * @param collection A ListSharp a ser criada, a partir dos valores
	 * @return Uma nova ListSharp, cujos elementos são os elementos passados como parâmetro
	 */
	public static <T> LazyList<T> from(final T... valores)
	{
		return new LazyList<T>(Arrays.copyOf(valores, valores.length));
	}

	@Override
	public int size()
	{
		return theSize;
	}

	@Override
	public LazyList<T> subsequence(final int startIndex, final int endIndex)
	{
		return new LazyList<T>(this, Math.max(startIndex, 0), Math.min(endIndex, this.size()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public T apply(final Integer param)
	{
		return (T) data[startIndex + param];
	}

	@Override
	public <B> Builder<B, SharpCollection<B>> builder()
	{
		return new ImArrayBuilder<B>();
	}

	@Override
	public <B> LazyList<B> map(final Function1<? super T, B> function)
	{
		return new MapMonadic<T, B>(this, function);
	}

	@Override
	public LazyList<T> filter(final Function1<? super T, Boolean> pred)
	{
		return new FilterMonadic<T, T>(this, new PartialFunction<T, T>()
		{
			@Override
			public T apply(final T param)
			{
				return param;
			}

			@Override
			public boolean isDefinedAt(final T forValue)
			{
				return pred.apply(forValue);
			}
		});
	}

	@Override
	public <B> LazyList<B> collect(final PartialFunction<? super T, B> pFunction)
	{
		return (LazyList<B>) super.collect(pFunction);
	}

	@Override
	public <B> LazyList<B> flatMap(final Function1<? super T, ? extends SharpCollection<B>> function)
	{
		return (LazyList<B>) super.flatMap(function);
	}

	@Override
	public LazyList<Tuple2<T, Integer>> zipWithIndex()
	{
		return (LazyList<Tuple2<T, Integer>>) super.zipWithIndex();
	}

	@Override
	public String toString()
	{
		return mkString("[", ", ", "]");
	}
	
	/**
	 * ATENÇÃO: Não estou seguro se este método está retail. Deve-se fazer testes antes.
	 * {@inheritDoc}<br/>
	 * 
	 */
	@Override
	public LazyList<T> reverse()
	{
		return new LazyList<T>(this, this.startIndex, this.startIndex + this.size())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public T apply(final Integer param)
			{
				return super.apply(this.size() - 1 - param);
			}
		};
	}

	private static final class MapMonadic<A, B> extends LazyList<B>
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
		public B apply(final Integer param)
		{
			return mappingFunction.apply(originalList.apply(param));
		}

		@Override
		public <C> LazyList<C> map(final Function1<? super B, C> function)
		{
			return new MapMonadic<A, C>(originalList, Functions.compose(mappingFunction, function));
		}

		@Override
		public LazyList<B> filter(final Function1<? super B, Boolean> pred)
		{
			return new FilterMonadic<A, B>(originalList, new PartialFunction<A, B>()
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
		public int size()
		{
			return originalList.size();
		}

		@Override
		public LazyList<B> subsequence(final int startIndex, final int endIndex)
		{
			return new MapMonadic<A, B>(originalList.subsequence(startIndex, endIndex), mappingFunction);
		}
	}

	private static final class FilterMonadic<A, B> extends LazyList<B>
	{
		private static final long serialVersionUID = 7562646182915871262L;

		private final LazyList<A> originalList;
		private final PartialFunction<A, B> pFunction;
		private Object[] lazyValue = null;

		protected FilterMonadic(final LazyList<A> originalList, final PartialFunction<A, B> mappingFunction)
		{
			super();
			this.originalList = originalList;
			this.pFunction = mappingFunction;
		}

		@SuppressWarnings("unchecked")
		@Override
		public B apply(final Integer param)
		{
			return (B) getLazyValue()[param];
		}

		@Override
		public <C> LazyList<C> map(final Function1<? super B, C> function)
		{
			return new FilterMonadic<A, C>(originalList, Functions.compose(pFunction, function));
		}

		@Override
		public LazyList<B> filter(final Function1<? super B, Boolean> pred)
		{
			return new FilterMonadic<A, B>(originalList, new PartialFunction<A, B>()
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
		public int size()
		{
			return getLazyValue().length;
		}

		@Override
		public LazyList<B> subsequence(final int startIndex, final int endIndex)
		{
			return new FilterMonadic<A, B>(originalList.subsequence(startIndex, endIndex), pFunction);
		}

		/**
		 * Aciona a aplicação dos valores
		 * 
		 * @return
		 */
		private Object[] getLazyValue()
		{
			if (lazyValue == null)
			{
				System.out.println("Chamou");
				int curIndex = 0;
				lazyValue = new Object[originalList.size()];
				
				for (int i = 0; i < lazyValue.length; i++)
				{
					final A curValue = originalList.apply(i);
					if (pFunction.isDefinedAt(curValue))
					{
						lazyValue[curIndex++] = pFunction.apply(curValue);
					}
				}
				lazyValue = Arrays.copyOf(lazyValue, curIndex);
			}
			return lazyValue;
		}

	}
}
