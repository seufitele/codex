package com.github.detentor.codex.collections;

import java.util.Comparator;

import com.github.detentor.codex.cat.monads.Option;
import com.github.detentor.codex.function.Function1;

/**
 * As classes que assinam esta interface possuem diversas funções que utilizam comparação. <br/>
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public interface ComparisonFunctions<T>
{
	/**
	 * Retorna o valor mínimo desta coleção, desde que esta coleção possua elementos que assinem
	 * a interface Comparator.
	 * @return O elemento com o menor valor na comparação
	 * @throws IllegalArgumentException Caso a coleção esteja vazia
	 * @throws ClassCastException Caso os elementos desta classe não sejam do tipo Comparator
	 */
	T min();
	
	/**
	 * Retorna uma Option contendo o valor retornado por {@link #min()}, se esta coleção possuir
	 * elementos, ou uma Option vazia, se esta coleção estiver vazia.
	 * @return Uma Option que conterá o valor mínimo, caso esta coleção não esteja vazia
	 * @throws ClassCastException Caso os elementos desta classe não sejam do tipo Comparator
	 */
	Option<T> minOption();
	
	/**
	 * Retorna o valor máximo desta coleção, desde que esta coleção possua elementos que assinem
	 * a interface Comparator.
	 * @return O elemento com o maior valor na comparação
	 * @throws IllegalArgumentException Caso a coleção esteja vazia
	 * @throws ClassCastException Caso os elementos desta classe não sejam do tipo Comparator
	 */
	T max();
	
	/**
	 * Retorna uma Option contendo o valor retornado por {@link #max()}, se esta coleção possuir
	 * elementos, ou uma Option vazia, se esta coleção estiver vazia.
	 * @return Uma Option que conterá o valor máximo, caso esta coleção não esteja vazia
	 * @throws ClassCastException Caso os elementos desta classe não sejam do tipo Comparator
	 */
	Option<T> maxOption();

	/**
	 * Retorna o valor máximo, a partir da função de comparação passada como parâmetro. <br/>
	 * Formalmente, retorna um valor T tal que não exista um elemento U onde a U.compareTo(T) > 0. <br/>
	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado. 
	 * @param comparator A função de comparação entre os elementos.
	 * @return O elemento com o maior valor na comparação
	 * @throws IllegalArgumentException Caso a coleção esteja vazia
	 */
	T max(final Comparator<? super T> comparator);
	
	/**
	 * Retorna o valor máximo, a partir da função de mapeamento que seja comparável. <br/>
	 * Formalmente, retorna um valor T tal que não exista um elemento U onde a U.compareTo(T) > 0. <br/>
	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado. 
	 * @param mapFunction A função de mapeamento que retorna o tipo a ser utilizado para prover a comparação.
	 * @return O elemento com o maior valor na comparação
	 * @throws IllegalArgumentException Caso a coleção esteja vazia
	 */
	<K extends Comparable<? super K>> T maxWith(final Function1<? super T, K> mapFunction);
	
	/**
	 * Retorna o valor mínimo, a partir da função de comparação passada como parâmetro. <br/>
	 * Formalmente, retorna um valor T tal que não exista um elemento U onde a U.compareTo(T) < 0. <br/>
	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado. 
	 * @param comparator A função de comparação entre os elementos.
	 * @return O elemento com o menor valor na comparação
	 * @throws IllegalArgumentException Caso a coleção esteja vazia
	 */
	T min(final Comparator<? super T> comparator);
	
	/**
	 * Retorna o valor mínimo, a partir da função de mapeamento que seja comparável. <br/>
	 * Formalmente, retorna um valor T tal que não exista um elemento U onde a U.compareTo(T) < 0. <br/>
	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado. 
	 * @param mapFunction A função de mapeamento que retorna o tipo a ser utilizado para prover a comparação.
	 * @return O elemento com o menor valor na comparação
	 * @throws IllegalArgumentException Caso a coleção esteja vazia
	 */
	<K extends Comparable<? super K>> T minWith(final Function1<? super T, K> mapFunction);

}
