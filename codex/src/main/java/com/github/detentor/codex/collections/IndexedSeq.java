package com.github.detentor.codex.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Functions;
import com.github.detentor.codex.function.PartialFunction1;
import com.github.detentor.codex.product.Tuple2;


/**
 * Sequências indexadas são sequências que garantem tempo constante no acesso 
 * a elementos em posição aleatória e ao seu tamanho. <br/><br/>
 * 
 * Além disso, possuem capacidade de extrair rapidamente sub-sequências (tempo, no máximo, proporcional ao tamanho
 * da sub-sequência). <br/><br/>
 * 
 * Para criar uma coleção (imutável) com base nesta implementação, basta prover o código dos seguintes métodos: <br/>
 * <br/>
 * 
 * {@link SharpCollection#size() size()} , {@link #subsequence(int, int)} , {@link #apply(Integer)} , {@link SharpCollection#builder()
 * builder()} <br/>
 * <br/>
 * 
 * O {@link #equals(Object)} e o {@link #hashCode()} são padrão para todas as sequências indexadas. <br/>
 * <br/>
 * 
 * Para coleções mutáveis, veja {@link AbstractMutableIndexedSeq}. <br/>
 * <br/>
 * 
 * NOTA: Subclasses devem sempre dar override nos métodos {@link #map(Function1) map}, {@link #collect(PartialFunction1) collect},
 * {@link #flatMap(Function1) flatMap} e {@link #zipWithIndex() zipWithIndex}. Devido à incompetência do Java com relação a Generics,
 * isso é necessário para assegurar que o tipo de retorno seja o mesmo da coleção. A implementação padrão (chamado o método da super
 * classe é suficiente).
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public interface IndexedSeq<T> extends Seq<T>
{
	/**
	 * Retorna o elemento vinculado à posição especificada. <br/>
	 * Esta operação tem garantia de ser executada em tempo constante.
	 */
	T apply(final Integer pos);

	/**
	 * {@inheritDoc} <br/>
	 * Esta operação tem garantia de ser executada em tempo constante.
     * @return O número de elementos desta coleção
     */
    int size();

    /**
     * Retorna uma sub-sequência desta sequência, a partir do índice de início (inclusive) 
     * e fim (exclusive) passados como parâmetro
     * @param startIndex O índice onde a sub-sequência será iniciada
     * @param endIndex O índice onde a sub-sequência terminará (exclusive)
     * @return A sub-sequência desta sequência que representa o intervalo determinado
     */
    IndexedSeq<T> subsequence(final int startIndex, final int endIndex);
    
    /**
	 * Retorna a sub-sequência a partir do índice passado como parâmetro, até o fim dela. <br/>
	 * Este método equivale a chamar o método subsequence com startIndex, this.size().
	 * 
	 * @param startIndex O índice a partir do qual a sub-sequência será extraída
	 * @return A sub-sequência desta sequência, iniciada a partir do índice passado como parâmetro
	 */
	default IndexedSeq<T> subsequence(final int startIndex)
	{
		return subsequence(startIndex, this.size());
	}

	/**
	 * Retorna essa sequência na ordem inversa da ordem retornada pelo iterator.
	 * @return Uma nova sequência, onde o primeiro elemento é o último da sequência original, o 
	 * segundo é o penúltimo, e assim sucessivamente.
	 */
    default IndexedSeq<T> reverse()
    {
    	final Builder<T, IndexedSeq<T>> retorno = builder();

		for (int i = this.size() - 1; i > -1; i--)
		{
			retorno.add(this.apply(i));
		}
		return retorno.result();
    }
    
    /**
	 * Retorna o índice onde o predicado passado como parâmetro é satisfeito, se existir
	 * 
	 * @param pred O predicado a ser verificado
	 * @return O índice do elemento que satisfaz o predicado, ou -1 se ele não existir
	 */
	default int indexWhere(final Function1<? super T, Boolean> pred)
	{
		for (int i = 0; i < this.size(); i++)
		{
			if (pred.apply(this.apply(i)))
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Retorna o último índice onde o predicado passado como parâmetro é satisfeito, se existir
	 * 
	 * @param pred O predicado a ser verificado
	 * @return O índice do elemento que satisfaz o predicado, ou -1 se ele não existir
	 */
	default int lastIndexWhere(final Function1<? super T, Boolean> pred)
	{
		for (int i = this.size() - 1; i > -1; i--)
		{
			if (pred.apply(this.apply(i)))
			{
				return i;
			}
		}
		return -1;
	}
	
	@Override
	default IndexedSeq<T> tail()
	{
		if (this.isEmpty())
		{
			throw new NoSuchElementException("tail foi chamado para uma coleção vazia");
		}
		return this.drop(1);
	}
	
	
	default Iterator<T> iterator()
	{
		final int[] curIndex = new int[1];
		final int count = this.size();

		return new Iterator<T>()
		{
			@Override
			public boolean hasNext()
			{
				return curIndex[0] < count;
			}

			@Override
			public T next()
			{
				return apply(curIndex[0]++);
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException("Operação de remoção não suportada");
			}
		};
	}
	
	
	//Métodos reescritos por motivo de otimização
	
	@Override
	default boolean isEmpty()
	{
		return this.size() == 0;
	}
	
	@Override
	default T head()
	{
		if (this.isEmpty())
		{
			throw new NoSuchElementException("head foi chamado para uma coleção vazia");
		}
		return this.apply(0);
	}
	
	@Override
	default T last()
	{
		if (this.isEmpty())
		{
			throw new NoSuchElementException("last foi chamado para uma coleção vazia");
		}
		return this.apply(this.size() - 1);
	}
	
	@Override
	default IndexedSeq<T> take(final Integer num)
	{
		final int nEle = num < 0 ? 0 : num;
		return this.subsequence(0, nEle);
	}
	
	@Override
	default IndexedSeq<T> takeRight(final Integer num)
	{
		final int nEle = num < 0 ? 0 : num;
		return this.drop(Math.min(this.size() - nEle, this.size()));
	}
	
	@Override
	default IndexedSeq<T> drop(final Integer num)
	{
		final int nEle = num < 0 ? 0 : num;
		return this.subsequence(Math.min(this.size(), nEle));
	}
	
	@Override
	default IndexedSeq<T> dropRight(final Integer num)
	{
		final int nEle = num < 0 ? 0 : num;
		return this.take(this.size() - nEle);
	}
	
	@Override
	default IndexedSeq<T> dropWhile(final Function1<? super T, Boolean> pred)
	{
		final int theIndex = indexWhere(Functions.not(pred));
		return this.drop(theIndex == -1 ? this.size() : theIndex);
	}
	
	@Override
	default IndexedSeq<T> dropRightWhile(final Function1<? super T, Boolean> pred)
	{
		final int theIndex = lastIndexWhere(Functions.not(pred));
		return this.dropRight(theIndex == -1 ? this.size() : this.size() - (theIndex + 1));
	}
	
	@Override
	default IndexedSeq<T> takeWhile(final Function1<? super T, Boolean> pred)
	{
		final int theIndex = indexWhere(Functions.not(pred));
		return this.take(theIndex == -1 ? this.size() : theIndex);
	}

	@Override
	default IndexedSeq<T> takeRightWhile(final Function1<? super T, Boolean> pred)
	{
		final int index = lastIndexWhere(Functions.not(pred));
		return this.takeRight(index == -1 ? this.size() : this.size() - (index + 1));
	}
	
	@Override
	default Tuple2<IndexedSeq<T>, IndexedSeq<T>> splitAt(final Integer num)
	{
		return Tuple2.from(take(num), drop(num));
	}

	
	
//	@SuppressWarnings("unchecked")
//	@Override
//	public IndexedSeq<U> grouped(final Integer size)
//	{
//		if (size <= 0)
//		{
//			throw new IllegalArgumentException("size deve ser maior do que zero");
//		}
//
//		final Builder<U, SharpCollection<U>> retorno = builder();
//		int curIndex = 0;
//		
//		while (curIndex < this.size())
//		{
//			retorno.add((U) this.subsequence(curIndex, curIndex += size));
//		}
//		return (IndexedSeq<U>) retorno.result();
//	}
	
}
