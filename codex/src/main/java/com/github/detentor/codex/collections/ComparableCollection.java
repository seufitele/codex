package com.github.detentor.codex.collections;

import com.github.detentor.codex.monads.Option;

/**
 * As classes que assinam esta interface fazem partes de sequências cujos elementos são comparáveis entre si. <br/>
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
}
