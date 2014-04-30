package com.github.detentor.codex.collections;

import com.github.detentor.codex.monads.Option;

/**
 * As classes que assinam esta interface possuem diversas funções que utilizam comparação. <br/>
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public interface ComparableCollection<T extends Comparable<? super T>> extends Seq<T>
{
	/**
	 * Retorna o valor mínimo desta coleção.
	 * Formalmente, retorna um valor T tal que não exista um elemento U onde a U.compareTo(T) < 0. <br/>
	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado.
	 * @return Uma Option que conterá o elemento com o menor valor na comparação, se ele existir
	 */
	default Option<T> min()
	{
		return min((param1, param2) -> param1.compareTo(param2));
	}

	/**
	 * Retorna o valor máximo desta coleção.
	 * Formalmente, retorna um valor T tal que não exista um elemento U onde a U.compareTo(T) > 0. <br/>
	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado.
	 * @return Uma Option que conterá o elemento com o maior valor na comparação, se ele existir
	 */
	default Option<T> max()
	{
		return min((param1, param2) -> param2.compareTo(param1));
	}

	/**
	 * Ordena esta coleção, de acordo com a ordenação natural de seus elementos. <br/>
	 * 
	 * @return A coleção com os elementos ordenados, se for mutável, ou uma nova coleção, se imutável.
	 */
	default Seq<T> sorted()
	{
		return sorted((param1, param2) -> param1.compareTo(param2));
	}

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
