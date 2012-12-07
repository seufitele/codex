package com.github.detentor.codex.collections;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Functions;

public abstract class AbstractIndexedSeq<T> extends AbstractSharpCollection<T, AbstractIndexedSeq<T>> implements IndexedSeq<T>
{
	/**
	 * Retorna a sub-sequência a partir do índice passado como parâmetro, até o fim dela. <br/>
	 * Este método equivale a chamar o método subsequence com startIndex, this.size() - 1.
	 * 
	 * @param startIndex O índice a partir do qual a sub-sequência será extraída
	 * @return A sub-sequência desta sequência, iniciada a partir do índice passado como parâmetro
	 */
	public IndexedSeq<T> subsequence(final int startIndex)
	{
		return this.subsequence(startIndex, this.size() - 1);
	}

	/**
	 * Retorna o índice onde o predicado passado como parâmetro é satisfeito, se existir
	 * 
	 * @param pred O predicado a ser verificado
	 * @return O índice do elemento que satisfaz o predicado, ou -1 se ele não existir
	 */
	protected int indexWhere(final Function1<T, Boolean> pred)
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
	protected int lastIndexWhere(final Function1<T, Boolean> pred)
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
		return this.size() > 0;
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
		return this.subsequence(1);
	}

	@Override
	public IndexedSeq<T> take(final Integer num)
	{
		return this.subsequence(0, num - 1);
	}

	@Override
	public IndexedSeq<T> takeRight(final Integer num)
	{
		return this.subsequence(this.size() - num + 1);
	}

	@Override
	public IndexedSeq<T> drop(final Integer num)
	{
		return this.subsequence(num + 1);
	}

	@Override
	public IndexedSeq<T> dropRight(final Integer num)
	{
		return this.subsequence(0, this.size() - num - 1);
	}

	@Override
	public IndexedSeq<T> dropWhile(final Function1<T, Boolean> pred)
	{
		return this.subsequence(indexWhere(Functions.not(pred)));
	}

	@Override
	public IndexedSeq<T> dropRightWhile(final Function1<T, Boolean> pred)
	{
		final int index = lastIndexWhere(Functions.not(pred));
		return this.subsequence(0, index == -1 ? this.size() - 1 : index);
	}

	@Override
	public IndexedSeq<T> takeWhile(final Function1<T, Boolean> pred)
	{
		return this.subsequence(indexWhere(Functions.not(pred)));
	}

	@Override
	public IndexedSeq<T> takeRightWhile(final Function1<T, Boolean> pred)
	{
		final int index = lastIndexWhere(Functions.not(pred));
		return this.subsequence(index == -1 ? this.size() : index);
	}

}
