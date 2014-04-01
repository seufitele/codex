package com.github.detentor.codex.collections;

/**
 * Builder é uma interface que determina como as coleções serão criadas. <br/>
 * A partir do builder é possível, nos métodos de ordem superior, retornar instâncias de diferentes tipos
 * de coleções (set, mapas, etc). <br/> <br/>
 * 
 * Com builders é possível determinar exatamente o tipo de objeto que será criado quando uma função
 * de ordem superior for chamada. Por exemplo, é possível passar um builder de LinkedHashSet, que assegurará
 * a ordem de inclusão dos elementos. <br/><br/>
 * 
 * Se coleções forem vistas como mônades, builders são o 'unit' ou 'return': eles fornecem uma interface comum
 * para a criação destas mônades. 
 * 
 * @author Vinícius Seufitele Pinto
 *
 * @param <From> O tipo de elementos que serão alimentados no builder
 * @param <To> O tipo de coleção a ser retornada pelo builder
 */
public interface Builder<From, To extends Iterable<From>>
{
	/**
	 * Adiciona o elemento passado como parâmetro no Builder
	 * @param element O elemento a ser adicionado no builder
	 */
	void add(final From element);
	
	/**
	 * Adiciona todos os elemento passados como parâmetro no Builder
	 * @param elements Os elementos a serem adicionados no builder
	 */
	default void addAll(final Iterable<From> elements)
	{
		for (From ele : elements)
		{
			add(ele);
		}
	}

	/**
	 * Retorna a coleção criada pelo Builder.
	 * @return A coleção criada pelo Builder
	 */
	To result();
}
