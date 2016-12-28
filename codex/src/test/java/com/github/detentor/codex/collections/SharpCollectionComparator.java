package com.github.detentor.codex.collections;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

/**
 * Faz a verificação para os métodos que utilizam algum tipo de comparação
 *
 */
public class SharpCollectionComparator
{
	private SharpCollectionComparator()
	{

	}

	/**
	 * Faz um teste completo para uma SharpCollection, nos métodos básicos (teste descontando mutabilidade). <br/>
	 * Para aplicar o teste a uma coleção, basta fazer uma coleção com os valores [1, 2, 3, 4, 5] <br/>
	 * 
	 * Obs: Espera-se um array de tamanho maior que 5, que tenha pelo menos 1 elemento repetido e que os elementos implementem a interface
	 * Comparable.
	 * 
	 * @param sharpCollection Uma coleção com os valores
	 * @param originalCollection Uma cópia da coleção
	 * @param emptyCollection Uma coleção vazia
	 * 
	 * @param eleNotInCollection Elemento que não está contido na coleção
	 * @param elems Elementos que compõe a coleção. <br/>
	 */
	@SuppressWarnings("unchecked")
	public static <T, U, W> void testSharpCollectionComparator(final SharpCollection<T> sharpCollection,
			final SharpCollection<T> originalCollection, final SharpCollection<T> emptyCollection,
			final Comparator<? super T> theComparator, final T eleNotInCollection, final T... elems)
	{
		// Premissas:
		assertTrue(elems.length > 5);
		assertTrue(new HashSet<Object>(Arrays.asList(elems)).size() != elems.length);

		final List<T> elemsList = Arrays.asList(elems);
		final TreeSet<T> sortedSet = new TreeSet<T>(elemsList);

		final TreeSet<T> ssComparator = new TreeSet<T>(theComparator);
		ssComparator.addAll(elemsList);

		// Métodos que dependem de Comparator
		// ATENÇÃO: SORTED, no caso do ListSharp (mutable), altera INPLACE os elementos
		// PORTANTO FOI MOVIDO PARA O FINAL, POR CAUSA DO SIDE-EFFECT

		// min & minOption
		assertTrue(sharpCollection.min().equals(sortedSet.first()));
		assertTrue(sharpCollection.minOption().get().equals(sortedSet.first()));
		assertTrue(emptyCollection.minOption().isEmpty());

		// minWith
		assertTrue(sharpCollection.min(theComparator).equals(ssComparator.first()));

		// max & maxOption
		assertTrue(sharpCollection.max().equals(sortedSet.last()));
		assertTrue(sharpCollection.maxOption().get().equals(sortedSet.last()));
		assertTrue(emptyCollection.maxOption().isEmpty());

		// maxWith
		assertTrue(sharpCollection.max(theComparator).equals(ssComparator.last()));

		// sorted
		assertTrue(emptyCollection.sorted().equals(emptyCollection)); // Chamar o sorted sem elementos não dá erro
		
		//Verifica que o primeiro elemento, depois de ordenado, será menor que todos os outros
		Comparable<Object> sPrevEle = (Comparable<Object>) sharpCollection.sorted().iterator().next();
		for (Object curObj : sharpCollection.sorted())
		{
			assertTrue(sPrevEle.compareTo((Comparable<Object>) curObj) <= 0);
			sPrevEle = (Comparable<Object>) curObj;
		}

		// sorted (comparator)
		//Verifica que o primeiro elemento, depois de ordenado, será menor que todos os outros
		assertTrue(emptyCollection.sorted(theComparator).equals(emptyCollection)); // Chamar o sorted sem elementos não dá erro
		T scPrevEle = sharpCollection.sorted(theComparator).iterator().next();
		for (T curObj : sharpCollection.sorted(theComparator))
		{
			assertTrue(theComparator.compare(scPrevEle, curObj) <= 0);
			scPrevEle = curObj;
		}
	}

}
