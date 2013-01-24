package com.github.detentor.codex.collections;

import java.util.Iterator;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Functions;
import com.github.detentor.codex.function.PartialFunction;

/**
 * Classe que provê a implementação padrão de métodos de coleções indexadas sequencialmente, para simplificar a 
 * criação de classes que os estenda. <br/> <br/>
 * 
 * Para criar uma coleção (imutável) com base nesta implementação, basta prover o código dos seguintes métodos: <br/> <br/>
 * 
 * {@link SharpCollection#size() size()} , 
 * {@link #subsequence(int, int)} , 
 * {@link #apply(Integer)} ,
 * {@link SharpCollection#builder() builder()} <br/> <br/>
 * 
 * O {@link #equals(Object)} e o {@link #hashCode()} são padrão para todas as sequências indexadas. <br/><br/>
 * 
 * Para coleções mutáveis, veja {@link AbstractMutableIndexedSeq}. <br/><br/>
 * 
 * NOTA: Subclasses devem sempre dar override nos métodos {@link #map(Function1) map}, {@link #collect(PartialFunction) collect} 
 * e {@link #flatMap(Function1) flatMap}. Devido à incompetência do Java com relação a Generics,
 * isso é necessário para assegurar que o tipo de retorno seja o mesmo da coleção. A implementação padrão (chamado o método da super
 * classe é suficiente).
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public abstract class AbstractIndexedSeq<T> extends AbstractSeq<T, IndexedSeq<T>> implements IndexedSeq<T>
{
	/**
	 * Retorna a sub-sequência a partir do índice passado como parâmetro, até o fim dela. <br/>
	 * Este método equivale a chamar o método subsequence com startIndex, this.size().
	 * 
	 * @param startIndex O índice a partir do qual a sub-sequência será extraída
	 * @return A sub-sequência desta sequência, iniciada a partir do índice passado como parâmetro
	 */
	public IndexedSeq<T> subsequence(final int startIndex)
	{
		return this.subsequence(startIndex, this.size());
	}

	/**
	 * Retorna o índice onde o predicado passado como parâmetro é satisfeito, se existir
	 * 
	 * @param pred O predicado a ser verificado
	 * @return O índice do elemento que satisfaz o predicado, ou -1 se ele não existir
	 */
	protected int indexWhere(final Function1<? super T, Boolean> pred)
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
	protected int lastIndexWhere(final Function1<? super T, Boolean> pred)
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
	public boolean isDefinedAt(final Integer forValue)
	{
		return forValue >= 0 && forValue < this.size();
	}

	@Override
	public boolean isEmpty()
	{
		return this.size() == 0;
	}

	@Override
	public T head()
	{
		ensureNotEmpty("head foi chamado para uma coleção vazia");
		return this.apply(0);
	}

	@Override
	public T last()
	{
		ensureNotEmpty("last foi chamado para uma coleção vazia");
		return this.apply(this.size() - 1);
	}

	@Override
	public IndexedSeq<T> tail()
	{
		ensureNotEmpty("tail foi chamado para uma coleção vazia");
		return this.drop(1);
	}

	@Override
	public IndexedSeq<T> take(final Integer num)
	{
		final int nEle = num < 0 ? 0 : num;
		return this.subsequence(0, nEle);
	}

	@Override
	public IndexedSeq<T> takeRight(final Integer num)
	{
		final int nEle = num < 0 ? 0 : num;
		return this.drop(Math.min(this.size() - nEle, this.size()));
	}

	@Override
	public IndexedSeq<T> drop(final Integer num)
	{
		final int nEle = num < 0 ? 0 : num;
		return this.subsequence(Math.min(this.size(), nEle));
	}

	@Override
	public IndexedSeq<T> dropRight(final Integer num)
	{
		final int nEle = num < 0 ? 0 : num;
		return this.take(this.size() - nEle);
	}

	@Override
	public IndexedSeq<T> dropWhile(final Function1<? super T, Boolean> pred)
	{
		final int theIndex = indexWhere(Functions.not(pred));
		return this.drop(theIndex == - 1 ? this.size() : theIndex);
	}

	@Override
	public IndexedSeq<T> dropRightWhile(final Function1<? super T, Boolean> pred)
	{
		final int theIndex = lastIndexWhere(Functions.not(pred));
		return this.dropRight(theIndex == -1 ? this.size() : this.size() - (theIndex + 1));
	}

	@Override
	public IndexedSeq<T> takeWhile(final Function1<? super T, Boolean> pred)
	{
		final int theIndex = indexWhere(Functions.not(pred));
		return this.take(theIndex == -1 ? this.size() : theIndex);
	}

	@Override
	public IndexedSeq<T> takeRightWhile(final Function1<? super T, Boolean> pred)
	{
		final int index = lastIndexWhere(Functions.not(pred));
		return this.takeRight(index == -1 ? this.size() : this.size() - (index + 1));
	}

	@Override
	public Iterator<T> iterator()
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
				return AbstractIndexedSeq.this.apply(curIndex[0]++);
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException("Operação de remoção não suportada");
			}
		};
	}
}
