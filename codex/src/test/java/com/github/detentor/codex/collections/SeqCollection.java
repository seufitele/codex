package com.github.detentor.codex.collections;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.product.Tuple2;

/**
 * Teste para sequências
 */
public class SeqCollection
{
	private SeqCollection()
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
	public static <T, U, W> void testSeqCollection(final Seq<T> sharpCollection, final Seq<T> originalCollection,
			final Seq<T> emptyCollection, final Function1<? super T, Boolean> filterFunc, final T eleNotInCollection,
			final T... elems)
	{
		// Premissas:
		assertTrue(elems.length > 5);
		assertTrue(new HashSet<Object>(Arrays.asList(elems)).size() != elems.length);

		// Premissas:
		assertTrue(elems.length > 5);
		assertTrue(new HashSet<Object>(Arrays.asList(elems)).size() != elems.length);
		assertTrue(sharpCollection instanceof Seq<?>);

		// head & headOption
		assertTrue(sharpCollection.head().equals(elems[0]));
		assertTrue(sharpCollection.headOption().get().equals(elems[0]));

		// last & lastOption
		assertTrue(sharpCollection.last().equals(elems[elems.length - 1]));
		assertTrue(sharpCollection.lastOption().get().equals(elems[elems.length - 1]));

		// tail
		assertTrue(sharpCollection.tail().head().equals(elems[1]));
		assertTrue(sharpCollection.tail().tail().head().equals(elems[2]));

		// take
		assertTrue(sharpCollection.take(4).containsAll(Arrays.asList(Arrays.copyOf(elems, 4))));

		// takeRight
		assertTrue(sharpCollection.takeRight(4).containsAll(Arrays.asList(Arrays.copyOfRange(elems, elems.length - 4, elems.length))));

		// takeWhile
		final List<T> twList = new ArrayList<T>();
		for (int i = 0; i < elems.length; i++)
		{
			if (filterFunc.apply(elems[i]))
			{
				twList.add(elems[i]);
			}
			else
			{
				break;
			}
		}
		assertTrue(
				sharpCollection.takeWhile(filterFunc).size() == twList.size() && sharpCollection.takeWhile(filterFunc).containsAll(twList));

		// takeRightWhile
		final List<T> trwList = new ArrayList<T>();
		for (int i = elems.length - 1; i > -1; i--)
		{
			if (filterFunc.apply(elems[i]))
			{
				trwList.add(elems[i]);
			}
			else
			{
				break;
			}
		}
		assertTrue(sharpCollection.takeRightWhile(filterFunc).size() == trwList.size()
				&& sharpCollection.takeRightWhile(filterFunc).containsAll(trwList));

		// drop <- drop == takeRight
		assertTrue(sharpCollection.drop(2).equals(sharpCollection.takeRight(sharpCollection.size() - 2)));

		// dropRight <- dropRight == take
		assertTrue(sharpCollection.dropRight(2).equals(sharpCollection.take(sharpCollection.size() - 2)));

		// dropWhile
		final List<T> dwList = new ArrayList<T>();
		boolean dwToAdd = false;
		for (int i = 0; i < elems.length; i++)
		{
			if (dwToAdd)
			{
				dwList.add(elems[i]);
			}
			else if (!filterFunc.apply(elems[i]))
			{
				dwList.add(elems[i]);
				dwToAdd = true;
			}
		}
		assertTrue(
				sharpCollection.dropWhile(filterFunc).size() == dwList.size() && sharpCollection.dropWhile(filterFunc).containsAll(dwList));

		// dropRightWhile
		final List<T> drwList = new ArrayList<T>();
		boolean drwToAdd = false;
		for (int i = elems.length - 1; i > -1; i--)
		{
			if (drwToAdd)
			{
				drwList.add(elems[i]);
			}
			else if (!filterFunc.apply(elems[i]))
			{
				drwList.add(elems[i]);
				drwToAdd = true;
			}
		}
		assertTrue(sharpCollection.dropRightWhile(filterFunc).size() == drwList.size()
				&& sharpCollection.dropRightWhile(filterFunc).containsAll(drwList));

		// splitAt
		assertTrue(sharpCollection.splitAt(2).getVal1().containsAll(Arrays.asList(Arrays.copyOf(elems, 2))));
		assertTrue(sharpCollection.splitAt(2).getVal2().containsAll(Arrays.asList(Arrays.copyOfRange(elems, 2, elems.length))));

		// grouped
		final List<SharpCollection<T>> groupList = new ArrayList<SharpCollection<T>>();
		int groupIndex = 0;
		while (groupIndex < sharpCollection.size())
		{
			groupList.add(sharpCollection.drop(groupIndex).take(2));
			groupIndex += 2;
		}
		groupIndex = 0;
		for (final SharpCollection<T> curCol : sharpCollection.grouped(2))
		{
			assertTrue(curCol.size() == groupList.get(groupIndex).size());
			assertTrue(curCol.containsAll(groupList.get(groupIndex++)));
		}

		// zipWithIndex
		SharpCollection<Tuple2<T, Integer>> zwiCol = sharpCollection.zipWithIndex();
		for (int i = 0; i < elems.length; i++)
		{
			assertTrue(zwiCol.head().getVal1().equals(elems[i]) && zwiCol.head().getVal2() == i);
			zwiCol = zwiCol.tail(); // iterator pobre
		}

		// iterator
		final Iterator<T> iteCol = sharpCollection.iterator();
		for (int i = 0; i < elems.length; i++)
		{
			assertTrue(iteCol.next().equals(elems[i]));
		}

		// find
		Object theEle = null;
		for (int i = 0; i < elems.length; i++)
		{
			if (filterFunc.apply(elems[i]))
			{
				theEle = elems[i];
				break;
			}
		}
		assertTrue(theEle != null);
		assertTrue(sharpCollection.find(filterFunc).get().equals(theEle));
	}

}
