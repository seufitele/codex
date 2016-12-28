package com.github.detentor.codex.collections;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Functions;
import com.github.detentor.codex.function.PartialFunction1;
import com.github.detentor.codex.product.Tuple2;
import com.github.detentor.operations.ObjectOps;

import junit.framework.AssertionFailedError;

/**
 * Testes de métodos de ordem superior (high-order functions)
 *
 */
public class SharpCollectionHOF
{
	private SharpCollectionHOF()
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
	public static <T, U, W> void testSharpCollectionHOF(final SharpCollection<T> sharpCollection,
			final SharpCollection<T> originalCollection, final SharpCollection<T> emptyCollection,
			final Function1<? super T, Boolean> filterFunc, final Function1<? super T, U> mapFunc,
			final Function1<? super T, ? extends Iterable<W>> fmapFunc, final T eleNotInCollection, final T... elems)
	{
		// Premissas:
		assertTrue(elems.length > 5);
		assertTrue(new HashSet<Object>(Arrays.asList(elems)).size() != elems.length);

		// filter
		List<Object> listaFiltro = new ArrayList<Object>();
		for (int i = 0; i < elems.length; i++)
		{
			if (filterFunc.apply(elems[i]))
			{
				listaFiltro.add(elems[i]);
			}
		}
		assertTrue(emptyCollection.filter(filterFunc).isEmpty());
		assertTrue((sharpCollection.filter(filterFunc).size() == listaFiltro.size())
				|| (sharpCollection.filter(filterFunc).size() == new HashSet<Object>(listaFiltro).size()));

		// partition
		assertTrue(emptyCollection.partition(filterFunc).getVal1().isEmpty() && emptyCollection.partition(filterFunc).getVal2().isEmpty());
		final Tuple2<SharpCollection<T>, SharpCollection<T>> partResult = (Tuple2<SharpCollection<T>, SharpCollection<T>>) sharpCollection
				.partition(filterFunc);
		assertTrue((partResult.getVal1().size() + partResult.getVal2().size()) == originalCollection.size()); // A partição engloba todos os
																												// elementos
		assertTrue(partResult.getVal1().equals(sharpCollection.filter(filterFunc))); // O primeiro retorno da partição é igual ao filtro
		for (T obj : partResult.getVal2())
		{
			if (filterFunc.apply(obj))
			{
				throw new AssertionFailedError("A segunda coleção retornada pelo partition pertence ao filtro");
			}
		}

		// exists
		assertTrue(!emptyCollection.exists(filterFunc));
		assertTrue(sharpCollection.exists(filterFunc) == sharpCollection.filter(filterFunc).notEmpty());

		// forall
		assertTrue(emptyCollection.forall(filterFunc));
		assertTrue(sharpCollection.forall(filterFunc) == (sharpCollection.filter(filterFunc).size() == originalCollection.size()));

		// map
		final List<U> mapList = new ArrayList<U>();
		for (int i = 0; i < elems.length; i++)
		{
			mapList.add(mapFunc.apply(elems[i]));
		}
		assertTrue(emptyCollection.map(mapFunc).isEmpty()); // Map de lista vazia é vazio
		assertTrue(sharpCollection.map(mapFunc).size() == originalCollection.size()); // O tamanho das listas é igual
		assertTrue(sharpCollection.map(mapFunc).containsAll(mapList)); // Elas contém os mesmos elementos (weak equals)

		// collect <- basta verificar se ele é o mesmo resultado de filter com map
		final PartialFunction1<T, U> partFunc = Functions.createPartial(filterFunc, mapFunc);
		assertTrue(sharpCollection.collect(partFunc).equals(sharpCollection.filter(filterFunc).map(mapFunc)));
		assertTrue(emptyCollection.collect(partFunc).isEmpty());

		// flatMap
		final List<W> fmapList = new ArrayList<W>();
		
		for (T ele : originalCollection)
		{
			for (W curEle : fmapFunc.apply(ele))
			{
				fmapList.add(curEle);
			}
		}
		
//		for (int i = 0; i < elems.length; i++)
//		{
//			for (W curEle : fmapFunc.apply(elems[i]))
//			{
//				fmapList.add(curEle);
//			}
//		}

		assertTrue(emptyCollection.flatMap(fmapFunc).isEmpty()); // flatMap de lista vazia é vazio
		assertTrue((sharpCollection.flatMap(fmapFunc).size() == fmapList.size())
				|| (sharpCollection.flatMap(fmapFunc).size() == new HashSet<W>(fmapList).size())); // O tamanho das listas é igual
		assertTrue(sharpCollection.flatMap(fmapFunc).containsAll(fmapList)); // Elas contém os mesmos elementos (weak equals)

		// count
		int theCount = 0;
		for (T ele : originalCollection)
		{
			if (filterFunc.apply(ele))
			{
				theCount++;
			}
		}
		assertTrue(emptyCollection.count(filterFunc) == 0);
		assertTrue(sharpCollection.count(filterFunc) == theCount);

		// find
		assertTrue(emptyCollection.find(ObjectOps.isEquals(elems[0])).isEmpty());
		assertTrue(sharpCollection.find(ObjectOps.isEquals(elems[0])).notEmpty());
	}

}
