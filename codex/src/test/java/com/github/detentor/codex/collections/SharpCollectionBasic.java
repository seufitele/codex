package com.github.detentor.codex.collections;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.detentor.codex.function.Function1;


public class SharpCollectionBasic
{
	private SharpCollectionBasic()
	{
		
//		final SharpCollection<T> sharpCol,
//			final SharpCollection<T> oriSharpCol,
//			final SharpCollection<T> emptyCol,
//			final Function1<? super T, Boolean> filterFunc,
//			final Function1<? super T, U> mapFunc,
//			final Function1<? super T, ? extends Iterable<W>> fmapFunc,
//			final Comparator<? super T> theComparator,
//			final T eleNotInCol,
//			final T... elems
		
	}
	/**
	 * Faz um teste completo para uma SharpCollection, nos métodos básicos (teste descontando mutabilidade). <br/>
	 * Para aplicar o teste a uma coleção, basta fazer uma coleção com os valores [1, 2, 3, 4, 5] <br/>
	 * 
	 * Obs: Espera-se um array de tamanho maior que 5, que tenha pelo menos 1 elemento repetido
	 * e que os elementos implementem a interface Comparable.
	 * 
	 * @param sharpCollection Uma coleção com os valores
	 * @param originalCollection Uma cópia da coleção
	 * @param emptyCollection Uma coleção vazia
	 * 
	 * @param eleNotInCollection Elemento que não está contido na coleção
	 * @param elems Elementos que compõe a coleção. <br/>
	 */
	@SuppressWarnings("unchecked")
	public static <T, U, W> void testSharpCollectionBasic(final SharpCollection<T> sharpCollection, final SharpCollection<T> originalCollection,
			final SharpCollection<T> emptyCollection, final Function1<? super T, Boolean> filterFunc, final T eleNotInCollection, final T... elems)
	{
		// Premissas:
		assertTrue(elems.length > 5);
		assertTrue(new HashSet<Object>(Arrays.asList(elems)).size() != elems.length);

		final List<T> elemsList = Arrays.asList(elems);
		final List<T> singleEleList = Arrays.asList(eleNotInCollection);
		final Set<T> distinctSet = new HashSet<T>(Arrays.asList(elems));

		// equals
		assertTrue(sharpCollection.equals(originalCollection));
		assertTrue(emptyCollection.equals(emptyCollection));

		// hashCode
		assertTrue(sharpCollection.hashCode() == originalCollection.hashCode());
		assertTrue(emptyCollection.hashCode() == emptyCollection.hashCode());

		// size
		assertTrue(sharpCollection.size() == elems.length || sharpCollection.size() == distinctSet.size());

		// isEmpty
		assertTrue(sharpCollection.isEmpty() == false && sharpCollection.size() > 0);
		assertTrue(emptyCollection.isEmpty() == true && emptyCollection.size() == 0);

		// notEmpty
		assertTrue(sharpCollection.notEmpty() == true && sharpCollection.size() > 0);
		assertTrue(emptyCollection.notEmpty() == false && emptyCollection.size() == 0);

		// contains
		assertTrue(sharpCollection.contains(elems[0]));
		assertTrue(!sharpCollection.contains(eleNotInCollection));
		assertTrue(!emptyCollection.contains(elems[0]));

		// containsAll
		assertTrue(sharpCollection.containsAll(elemsList));
		assertTrue(!sharpCollection.containsAll(singleEleList));
		assertTrue(!emptyCollection.containsAll(elemsList));

		// containsAny
		assertTrue(sharpCollection.containsAny(elemsList));
		assertTrue(!sharpCollection.containsAny(singleEleList));
		assertTrue(!emptyCollection.containsAny(elemsList));
		
		//intersect
		assertTrue(sharpCollection.intersect(singleEleList).equals(emptyCollection));
		assertTrue(emptyCollection.intersect(elemsList).equals(emptyCollection));
		assertTrue(sharpCollection.intersect(elemsList).equals(originalCollection));
		
		//distinct
		assertTrue(sharpCollection.distinct().intersect(distinctSet).size() == distinctSet.size());
		assertTrue(emptyCollection.distinct().intersect(distinctSet).size() == 0);


		// Testes de chamadas de método - não verificam a validade da informação
		// Esses testes só garantem que chamar o método não vai causar erro, além de ter algumas garantias
		// óbvias (ex: tail diminui o tamanho da coleção em 1, etc.)

		// Iterator (valida que ele retorna todos os elementos)
		sharpCollection.iterator();

		final List<Object> eleList = new ArrayList<Object>(Arrays.asList(elems));
		final List<Object> dEleList = new ArrayList<Object>(distinctSet); //distintos
		for (Object ele : sharpCollection)
		{
			eleList.remove(ele);
			dEleList.remove(ele);
		}
		assertTrue(!emptyCollection.iterator().hasNext());
		//Verifica que os elementos distintos foram removidos, e o tamanho da lista que sobrou é equivalente 
		assertTrue(dEleList.isEmpty() && (eleList.isEmpty() || (eleList.size() == (elems.length - distinctSet.size())))); 

		// head & headOption
		sharpCollection.head();
		assertTrue(emptyCollection.headOption().isEmpty());
		assertTrue(sharpCollection.headOption().notEmpty());

		// last & lastOption
		sharpCollection.last();
		sharpCollection.lastOption();
		assertTrue(emptyCollection.lastOption().isEmpty());
		assertTrue(sharpCollection.lastOption().notEmpty());

		// tail
		sharpCollection.tail();
		assertTrue(sharpCollection.tail().size() == originalCollection.size() - 1);
		assertTrue(sharpCollection.tail().tail().size() == originalCollection.size() - 2);

		// take
		assertTrue(emptyCollection.take(-1).isEmpty());
		assertTrue(emptyCollection.take(0).isEmpty());
		assertTrue(emptyCollection.take(10).isEmpty());
		assertTrue(sharpCollection.take(-1).size() == 0);
		assertTrue(sharpCollection.take(0).size() == 0);
		assertTrue(sharpCollection.take(2).size() == Math.min(sharpCollection.size(), 2));
		assertTrue(sharpCollection.take(500).size() == Math.min(sharpCollection.size(), 500));

		// takeRight
		assertTrue(emptyCollection.takeRight(-1).isEmpty());
		assertTrue(emptyCollection.takeRight(0).isEmpty());
		assertTrue(emptyCollection.takeRight(10).isEmpty());
		assertTrue(sharpCollection.takeRight(-1).size() == 0);
		assertTrue(sharpCollection.takeRight(0).size() == 0);
		assertTrue(sharpCollection.takeRight(2).size() == Math.min(sharpCollection.size(), 2));
		assertTrue(sharpCollection.takeRight(500).size() == Math.min(sharpCollection.size(), 500));

		// drop
		assertTrue(emptyCollection.drop(-1).isEmpty());
		assertTrue(emptyCollection.drop(0).isEmpty());
		assertTrue(emptyCollection.drop(10).isEmpty());
		assertTrue(sharpCollection.drop(-1).size() == sharpCollection.size());
		assertTrue(sharpCollection.drop(0).size() == sharpCollection.size());
		assertTrue(sharpCollection.drop(2).size() == Math.max(sharpCollection.size() - 2, 0));
		assertTrue(sharpCollection.drop(500).size() == Math.max(sharpCollection.size() - 500, 0));

		// dropRight
		assertTrue(emptyCollection.dropRight(-1).isEmpty());
		assertTrue(emptyCollection.dropRight(0).isEmpty());
		assertTrue(emptyCollection.dropRight(10).isEmpty());
		assertTrue(sharpCollection.dropRight(-1).size() == sharpCollection.size());
		assertTrue(sharpCollection.dropRight(0).size() == sharpCollection.size());
		assertTrue(sharpCollection.dropRight(2).size() == Math.max(sharpCollection.size() - 2, 0));
		assertTrue(sharpCollection.dropRight(500).size() == Math.max(sharpCollection.size() - 500, 0));

		// grouped
		assertTrue(emptyCollection.grouped(2).isEmpty());
		assertTrue(sharpCollection.grouped(2).size() == Math.ceil(originalCollection.size() / 2.0d));

		// splitAt
		assertTrue(emptyCollection.splitAt(2).getVal1().isEmpty() && emptyCollection.splitAt(2).getVal2().isEmpty());
		assertTrue(sharpCollection.splitAt(2).getVal1().size() + sharpCollection.splitAt(2).getVal2().size() == originalCollection.size());

		//mkString <- assegura que chamar o método é seguro
		assertTrue(!sharpCollection.mkString().isEmpty());
		assertTrue(emptyCollection.mkString().isEmpty());
		
		
		//Testes somente de chamadas. Para coleções lineares será verificada a validade das informações
		sharpCollection.takeWhile(filterFunc);
		sharpCollection.takeRightWhile(filterFunc);
		sharpCollection.dropWhile(filterFunc);
		sharpCollection.dropRightWhile(filterFunc);
		sharpCollection.zipWithIndex();
	}

}
