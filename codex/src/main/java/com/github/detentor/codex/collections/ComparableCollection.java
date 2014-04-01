package com.github.detentor.codex.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.github.detentor.codex.monads.Option;

/**
 * As classes que assinam esta interface possuem diversas funções que utilizam comparação. <br/>
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public interface ComparableCollection<T extends Comparable<? super T>> extends SharpCollection<T>
{
	/**
	 * Retorna o valor mínimo desta coleção.
	 * Formalmente, retorna um valor T tal que não exista um elemento U onde a U.compareTo(T) < 0. <br/>
	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado.
	 * @return O elemento com o menor valor na comparação
	 * @throws NoSuchElementException Caso a coleção esteja vazia
	 */
//	 *@throws ClassCastException Caso os elementos desta classe não sejam do tipo Comparator <- NÃO DEVERIA ACONTECER MAIS!!!
	default T min()
	{
		if (this.isEmpty())
		{
			throw new NoSuchElementException("min foi chamado para uma coleção vazia");
		}

		final Iterator<T> ite = this.iterator();
		T minValue = ite.next();

		while (ite.hasNext())
		{
			final T curEle = ite.next();
			if (curEle.compareTo(minValue) < 0)
			{
				minValue = curEle;
			}
		}
		return minValue;
	}
	
	/**
	 * Retorna o valor mínimo, a partir da função de comparação passada como parâmetro. <br/>
	 * Formalmente, retorna um valor T tal que não exista um elemento U onde comparator.compare(T, U) < 0. <br/>
	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado. 
	 * @param comparator A função de comparação entre os elementos.
	 * @return O elemento com o menor valor na comparação
	 * @throws NoSuchElementException Caso a coleção esteja vazia
	 */
	default T min(final Comparator<? super T> comparator)
	{
		if (this.isEmpty())
		{
			throw new NoSuchElementException("min foi chamado para uma coleção vazia");
		}

		final Iterator<T> ite = this.iterator();
		T minValue = ite.next();

		while (ite.hasNext())
		{
			final T curEle = ite.next();
			if (comparator.compare(curEle, minValue) < 0)
			{
				minValue = curEle;
			}
		}
		return minValue;
	}
	
	/**
	 * Retorna uma Option contendo o valor retornado por {@link #min()}, se esta coleção possuir
	 * elementos, ou uma Option vazia, se esta coleção estiver vazia.
	 * @return Uma Option que conterá o valor mínimo, caso esta coleção não esteja vazia
	 */
//	 * @throws ClassCastException Caso os elementos desta classe não sejam do tipo Comparator
	default Option<T> minOption()
	{
		return this.isEmpty() ? Option.<T>empty() : Option.from(min());
	}

	/**
	 * Retorna o valor máximo desta coleção.
	 * Formalmente, retorna um valor T tal que não exista um elemento U onde a U.compareTo(T) > 0. <br/>
	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado.
	 * @return O elemento com o maior valor na comparação
	 * @throws NoSuchElementException Caso a coleção esteja vazia
	 */
//	 * @throws ClassCastException Caso os elementos desta classe não sejam do tipo Comparator
	default T max()
	{
		if (this.isEmpty())
		{
			throw new NoSuchElementException("max foi chamado para uma coleção vazia");
		}

		final Iterator<T> ite = this.iterator();
		T maxValue = ite.next();

		while (ite.hasNext())
		{
			final T curEle = ite.next();
			if (curEle.compareTo(maxValue) > 0)
			{
				maxValue = curEle;
			}
		}
		return maxValue;
	}
	
	/**
	 * Retorna o valor máximo, a partir da função de comparação passada como parâmetro. <br/>
	 * Formalmente, retorna um valor T tal que não exista um elemento U onde a comparator.compare(T, U) > 0. <br/>
	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado. 
	 * @param comparator A função de comparação entre os elementos.
	 * @return O elemento com o maior valor na comparação
	 * @throws NoSuchElementException Caso a coleção esteja vazia
	 */
	default T max(final Comparator<? super T> comparator)
	{
		if (this.isEmpty())
		{
			throw new NoSuchElementException("max foi chamado para uma coleção vazia");
		}

		final Iterator<T> ite = this.iterator();
		T maxValue = ite.next();

		while (ite.hasNext())
		{
			final T curEle = ite.next();
			if (comparator.compare(curEle, maxValue) > 0)
			{
				maxValue = curEle;
			}
		}
		return maxValue;
	}
	
	/**
	 * Retorna uma Option contendo o valor retornado por {@link #max()}, se esta coleção possuir
	 * elementos, ou uma Option vazia, se esta coleção estiver vazia.
	 * @return Uma Option que conterá o valor máximo, caso esta coleção não esteja vazia
	 */
//	 * @throws ClassCastException Caso os elementos desta classe não sejam do tipo Comparator
	default Option<T> maxOption()
	{
		return this.isEmpty() ? Option.<T>empty() : Option.from(max());
	}

	/**
	 * Ordena esta coleção, de acordo com a ordenação natural de seus elementos. <br/>
	 * 
	 * @return A coleção com os elementos ordenados, se for mutável, ou uma nova coleção, se imutável.
	 */
	SharpCollection<T> sorted();
	
	
//	/**
//	 * Retorna o valor mínimo, a partir da função de mapeamento passada como parâmetro. <br/>
//	 * @param func
//	 * @return
//	 */
//	default <U extends Comparable<? super U>> T min(final Function1<T, U> func)
//	{
//		return min( (obj1, obj2) -> func.apply(obj1).compareTo(func.apply(obj2)));  
//	}
	
//	/**
//	 * Retorna o valor máximo, a partir da função de mapeamento que seja comparável. <br/>
//	 * Formalmente, retorna um valor T tal que não exista um elemento U onde a U.compareTo(T) > 0. <br/>
//	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado. 
//	 * @param mapFunction A função de mapeamento que retorna o tipo a ser utilizado para prover a comparação.
//	 * @return O elemento com o maior valor na comparação
//	 * @throws IllegalArgumentException Caso a coleção esteja vazia
//	 */
//	<K extends Comparable<? super K>> T maxWith(final Function1<? super T, K> mapFunction);
	
//	/**
//	 * Retorna o valor mínimo, a partir da função de mapeamento que seja comparável. <br/>
//	 * Formalmente, retorna um valor T tal que não exista um elemento U onde a U.compareTo(T) < 0. <br/>
//	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado. 
//	 * @param mapFunction A função de mapeamento que retorna o tipo a ser utilizado para prover a comparação.
//	 * @return O elemento com o menor valor na comparação
//	 * @throws IllegalArgumentException Caso a coleção esteja vazia
//	 */
//	<K extends Comparable<? super K>> T minWith(final Function1<? super T, K> mapFunction);

}
