package com.github.detentor.codex.collections;

import static com.github.detentor.operations.IntegerOps.lowerThan;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.AssertionFailedError;

import org.junit.Test;

import com.github.detentor.codex.cat.monads.Option;
import com.github.detentor.codex.collections.immutable.LazyList;
import com.github.detentor.codex.collections.mutable.LLSharp;
import com.github.detentor.codex.collections.mutable.ListSharp;
import com.github.detentor.codex.collections.mutable.MapSharp;
import com.github.detentor.codex.collections.mutable.SetSharp;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.FunctionN;
import com.github.detentor.codex.function.Functions;
import com.github.detentor.codex.function.PartialFunction1;
import com.github.detentor.codex.product.Tuple2;
import com.github.detentor.codex.util.Reflections;
import com.github.detentor.operations.IntegerOps;
import com.github.detentor.operations.ObjectOps;


//Genérico 

	//size
	//isEmpty
	//notEmpty
	//contains
	//containsAll
	//intersect
	//distinct
	//sorted
	//sorted(comparator)
	//mkString (só a chamada mesmo, pra assegurar que não há erro)
	//min
	//minOption
	//max
	//maxOption
	//maxWith
	//minWith
	//filter
	//partition
	//exists
	//forall
	//map
	//collect
	//flatMap
	//count
	
	//Dependem de ordem
	
	//head
	//headOption
	//last
	//lastOption
	//tail
	//take
	//takeWhile
	//takeRight
	//takeRightWhile
	//drop
	//dropWhile
	//dropRight
	//dropRightWhile
	//splitAt
	//grouped
	//zipWithIndex
	//iterator <- depende de ordem
	//find
	
	//Funções a serem verificadas 'na unha', por ter que verificar resultados específicos:

	//map
	//flatMap
	//foldLeft <- usualmente não é dependente de ordem, mas pode ser
//


public class SharpCollectionTest2
{
	@SuppressWarnings("unchecked")
	@Test
	public void testSharpCollection() 
	{
		//Os testes devem estar divididos em 3 partes:
		//1 - Testes de GenericSharpCollection (imutável / mutável)
		//2 - Testes de sequências (drop, tail, etc)
		//3 - Testes de funções de ordem superior
		//4 - Testes de sequências lineares e sequências indexadas (LinearSeq & IndexedSeq)
		//5 - Testes de diversos tipos de mapas e sets.
		
		//Como observação, a ordem do iterator de GenericSharpCollection não é bem-definida (ex: Mapas e Sets).

		ListSharp<Class<?>> collections = ListSharp.<Class<?>>from(
//											MapSharp.class,
											SetSharp.class,
											LazyList.class,
											ListSharp.class, 
											com.github.detentor.codex.collections.immutable.ListSharp.class,
											LLSharp.class,
											com.github.detentor.codex.collections.immutable.LLSharp.class);

		for(Class<?> ele : collections)
		{
			try
			{
				testCollection((Class<SharpCollection<?>>) ele);
			}
			catch (AssertionError ae)
			{
				System.out.println("Erro ao fazer o teste para a classe " + ele);
				throw new RuntimeException(ae);
			}
		}
	}
	
	public void testCollection(final Class<SharpCollection<?>> theClass) 
	{
		if (theClass.equals(MapSharp.class))
		{
//			final Tuple2<String, Integer> ele1 = Tuple2.from("a", 1);
//			final Tuple2<String, Integer> ele2 = Tuple2.from("b", 2);
//			final Tuple2<String, Integer> ele3 = Tuple2.from("c", 3);
//			final Tuple2<String, Integer> ele4 = Tuple2.from("d", 4);
//			final Tuple2<String, Integer> ele5 = Tuple2.from("e", 5);
//
//			final List<Tuple2<String, Integer>> listaTupla = new ArrayList<Tuple2<String,Integer>>();
//			listaTupla.add(ele1);
//			listaTupla.add(ele2);
//			listaTupla.add(ele3);
//			listaTupla.add(ele4);
//			listaTupla.add(ele5);
//			
//			final SharpCollection<Tuple2<String, Integer>> listaOri = MapSharp.from(listaTupla);
//			final SharpCollection<Tuple2<String, Integer>> lista = MapSharp.from(listaTupla);
//			final SharpCollection<Tuple2<String, Integer>> listaVazia = MapSharp.empty();
//			
//			final Function1<Tuple2<String, Integer>, List<Tuple2<String, Integer>>> fmap = 
//			new Function1<Tuple2<String, Integer>, List<Tuple2<String, Integer>>>()
//			{
//				@Override
//				public List<Tuple2<String, Integer>> apply(final Tuple2<String, Integer> param)
//				{
//					final List<Tuple2<String, Integer>> retorno = new ArrayList<Tuple2<String, Integer>>();
//					retorno.add(param);
//					retorno.add(param);
//					return retorno;
//				}
//			};
//			
//			final Comparator<Tuple2<String, Integer>> theComp = new Comparator<Tuple2<String, Integer>>()
//			{
//				@Override
//				public int compare(final Tuple2<String, Integer> ob1, final Tuple2<String, Integer> ob2)
//				{
//					return ob1.getVal1().compareTo(ob2.getVal1());
//				}
//			};
//			
//			final Function1<Tuple2<String, Integer>, Boolean> fFilter = 
//			new Function1<Tuple2<String, Integer>, Boolean>()
//			{
//				@Override
//				public Boolean apply(final Tuple2<String, Integer> param)
//				{
//					return param.getVal2() < 3;
//				}
//			};
//			
//			testGenSetCollection(listaOri, lista, listaVazia, fFilter, ObjectOps.toString, fmap, theComp, Tuple2.from("f", 10), 
//					ele1, ele2, ele3, ele4, ele5, ele4);
			return;
		}
		
		final Function1<Integer, List<Integer>> fmap = new Function1<Integer, List<Integer>>()
		{
			@Override
			public List<Integer> apply(final Integer param)
			{
				final List<Integer> retorno = new ArrayList<Integer>();
				retorno.add(param);
				retorno.add(param);
				return retorno;
			}
		};

		final Comparator<Integer> theComp = new Comparator<Integer>()
		{
			@Override
			public int compare(final Integer ob1, final Integer ob2)
			{
				return ob2.compareTo(ob1);
			}
		};
		
		final Option<Method> func2 = Reflections.getMethodFromNameAndType(theClass, "empty", new Class<?>[0]);
		final FunctionN<Object, SharpCollection<Integer>> func = Reflections.liftStaticVarArgs(theClass, "from");
		
		final Integer[] elementos = new Integer[] {2, 4, 3, 3, 1, 6, 5, 2}; 
		
		final SharpCollection<Integer> listaOri = func.apply((Object[]) elementos);
		final SharpCollection<Integer> lista = func.apply((Object[]) elementos);
		final SharpCollection<Integer> listaVazia = Reflections.safeInvoke(theClass, func2.get());

		if (theClass.equals(SetSharp.class))
		{
			testGenSetCollection(listaOri, lista, listaVazia, lowerThan(5), ObjectOps.toString, fmap, theComp, 10, elementos);
		}
		else
		{
			testGenSharpCollection(listaOri, lista, listaVazia, lowerThan(5), ObjectOps.toString, fmap, theComp, 10, elementos);
		}

		if (lista instanceof Seq<?>)
		{
			testSeq(lista, IntegerOps.lowerThan(5), elementos);
		}
	}
	
//	public void testMapCollection() 
//	{
//		final Option<Method> func2 = Reflections.getMethodFromNameAndType(MapSharp.class, "empty", new Class<?>[0]);
//		
//		final Tuple2<String, Integer> ele1 = Tuple2.from("a", 1);
//		final Tuple2<String, Integer> ele2 = Tuple2.from("b", 2);
//		final Tuple2<String, Integer> ele3 = Tuple2.from("c", 3);
//		final Tuple2<String, Integer> ele4 = Tuple2.from("d", 4);
//		final Tuple2<String, Integer> ele5 = Tuple2.from("e", 5);
//		
//		final Tuple2<String, Integer>[] elements = new Tuple2[] {ele1, ele2, ele3, ele4, ele5}; 
//		
//		SharpCollection<Tuple2<String, Integer>> listaOri = MapSharp.<String, Integer>empty(MapSharpType.LINKED_HASH_MAP).addAll(elements);
//	
//		SharpCollection<Tuple2<String, Integer>> lista = MapSharp.<String, Integer>empty(MapSharpType.LINKED_HASH_MAP).addAll(elements);
//		SharpCollection<Integer> listaVazia = Reflections.invokeSafe(MapSharp.class, func2.get());
//		
//		SharpCollection<Tuple2<String, Integer>> listaDrop1 = MapSharp.<String, Integer>empty(MapSharpType.LINKED_HASH_MAP).addAll(ele2, ele3, ele4, ele5);
//		SharpCollection<Tuple2<String, Integer>> listaDrop2 = MapSharp.<String, Integer>empty(MapSharpType.LINKED_HASH_MAP).addAll(ele3, ele4, ele5); 
//		
//		SharpCollection<Tuple2<String, Integer>> listaTake1 = MapSharp.<String, Integer>empty(MapSharpType.LINKED_HASH_MAP).addAll(ele1);
//		SharpCollection<Tuple2<String, Integer>> listaTake2 = MapSharp.<String, Integer>empty(MapSharpType.LINKED_HASH_MAP).addAll(ele1, ele2);
//		
//		SharpCollection<Tuple2<String, Integer>> listaTakeRight1 = MapSharp.<String, Integer>empty(MapSharpType.LINKED_HASH_MAP).addAll(ele5);
//		SharpCollection<Tuple2<String, Integer>> listaTakeRight2 = MapSharp.<String, Integer>empty(MapSharpType.LINKED_HASH_MAP).addAll(ele4, ele5); 
//		
//		SharpCollection<Tuple2<String, Integer>> listaDropRight1 = MapSharp.<String, Integer>empty(MapSharpType.LINKED_HASH_MAP).addAll(ele1, ele2, ele3, ele4);
//		SharpCollection<Tuple2<String, Integer>> listaDropRight2 = MapSharp.<String, Integer>empty(MapSharpType.LINKED_HASH_MAP).addAll(ele1, ele2, ele3);
//		
////		testGenSharpCollection(lista, listaOri, 
////							listaDrop1, listaDrop2, 
////							listaTake1, listaTake2, 
////							listaTakeRight1, listaTakeRight2, 
////							listaDropRight1, listaDropRight2, listaVazia, Tuple2.from("f", "6"), new Object[] { ele1, ele2, ele3, ele4, ele5} );
//	}
	
	
	
	
	/**
	 * Faz um teste completo para uma SharpCollection (teste descontando mutabilidade). <br/>
	 * Para aplicar o teste a uma coleção, basta fazer uma coleção com os valores [1, 2, 3, 4, 5] <br/>
	 * 
	 * Obs: Espera-se um array de tamanho maior que 5, que tenha pelo menos 1 elemento repetido
	 * e que os elementos implementem a interface Comparable.
	 * 
	 * @param sharpCol Uma coleção com os valores
	 * @param oriSharpCol Uma cópia da SharpCol (deep copy)
	 * @param emptyCol Uma coleção vazia
	 * 
	 * @param filterFunc Uma função simples de filtro (que esteja válida para, pelo menos, 1 elemento)
	 * @param mapFunc Uma função simples de mapa (não será verificado o retorno dos elementos)
	 * @param fmapFunc Uma função de flatMap simples
	 * 
	 * @param eleNotInCol Elemento que não está contido na coleção
	 * @param elems Elementos que compõe a coleção. <br/>
	 */
	@SuppressWarnings("unchecked")
	public <T, U, W> void  testGenSharpCollection( final SharpCollection<T> sharpCol,
							  			final SharpCollection<T> oriSharpCol,
							  			final SharpCollection<T> emptyCol,
							  			final Function1<? super T, Boolean> filterFunc,
							  			final Function1<? super T, U> mapFunc,
							  			final Function1<? super T, ? extends Iterable<W>> fmapFunc,
							  			final Comparator<? super T> theComparator,
							  			final T eleNotInCol,
							  			final T... elems) 
	{
		//Premissas:
		assertTrue(elems.length > 5);
		assertTrue(new HashSet<Object>(Arrays.asList(elems)).size() != elems.length);
		
		final List<T> elemsList = Arrays.asList(elems);
		final List<T> singleEleList = Arrays.asList(eleNotInCol);
		final Set<T> distinctSet = new HashSet<T>(Arrays.asList(elems));
		final TreeSet<T> sortedSet = new TreeSet<T>(elemsList);
		final TreeSet<T> ssComparator = new TreeSet<T>(theComparator);
		ssComparator.addAll(elemsList);
		
		//equals
		assertTrue(sharpCol.equals(oriSharpCol));
		assertTrue(emptyCol.equals(emptyCol));
		
		//hashCode
		assertTrue(sharpCol.hashCode() == oriSharpCol.hashCode());
		assertTrue(emptyCol.hashCode() == emptyCol.hashCode());
		
		//size
		assertTrue(sharpCol.size() == elems.length);
		
		//isEmpty
		assertTrue(sharpCol.isEmpty() == false && sharpCol.size() > 0);
		assertTrue(emptyCol.isEmpty() == true && emptyCol.size() == 0);
		
		//notEmpty
		assertTrue(sharpCol.notEmpty() == true && sharpCol.size() > 0);
		assertTrue(emptyCol.notEmpty() == false && emptyCol.size() == 0);
		
		//contains
		assertTrue(sharpCol.contains(elems[0]));
		assertTrue(! sharpCol.contains(eleNotInCol));
		assertTrue(! emptyCol.contains(elems[0]));
		
		//containsAll
		assertTrue(sharpCol.containsAll(elemsList));
		assertTrue(! sharpCol.containsAll(singleEleList));
		assertTrue(!emptyCol.containsAll(elemsList));

		//containsAny
		assertTrue(sharpCol.containsAny(elemsList));
		assertTrue(! sharpCol.containsAny(singleEleList));
		assertTrue(! emptyCol.containsAny(elemsList));
		
		//intersect
		assertTrue(sharpCol.intersect(elemsList).equals(oriSharpCol));
		assertTrue(sharpCol.intersect(singleEleList).equals(emptyCol));
		assertTrue(emptyCol.intersect(elemsList).equals(emptyCol));
		
		//distinct
		assertTrue(sharpCol.distinct().intersect(distinctSet).size() == distinctSet.size());
		assertTrue(emptyCol.distinct().intersect(distinctSet).size() == 0);
		
		//mkString <- assegura que chamar o método é seguro
		assertTrue(!sharpCol.mkString().isEmpty());
		assertTrue(emptyCol.mkString().isEmpty());

		//min & minOption
		assertTrue(sharpCol.min().equals(sortedSet.first()));
		assertTrue(sharpCol.minOption().get().equals(sortedSet.first()));
		assertTrue(emptyCol.minOption().isEmpty());
		
		//minWith
		assertTrue(sharpCol.min(theComparator).equals(ssComparator.first()));
		
		//max & maxOption
		assertTrue(sharpCol.max().equals(sortedSet.last()));
		assertTrue(sharpCol.maxOption().get().equals(sortedSet.last()));
		assertTrue(emptyCol.maxOption().isEmpty());
		
		//maxWith
		assertTrue(sharpCol.max(theComparator).equals(ssComparator.last()));
		
		//filter
		List<Object> listaFiltro = new ArrayList<Object>();
		for (int i = 0; i < elems.length; i++)
		{
			if (filterFunc.apply(elems[i]))
			{
				listaFiltro.add(elems[i]);
			}
		}
		assertTrue(emptyCol.filter(filterFunc).isEmpty());
		assertTrue(sharpCol.filter(filterFunc).size() == listaFiltro.size());

		//partition
		assertTrue(emptyCol.partition(filterFunc).getVal1().isEmpty() && emptyCol.partition(filterFunc).getVal2().isEmpty());
		final Tuple2<SharpCollection<T>, SharpCollection<T>> partResult = (Tuple2<SharpCollection<T>, SharpCollection<T>>) sharpCol.partition(filterFunc);
		assertTrue((partResult.getVal1().size() + partResult.getVal2().size()) == elems.length); //A partição engloba todos os elementos
		assertTrue(partResult.getVal1().equals(sharpCol.filter(filterFunc))); //O primeiro retorno da partição é igual ao filtro
		for (T obj : partResult.getVal2())
		{
			if (filterFunc.apply(obj))
			{
				throw new AssertionFailedError("A segunda coleção retornada pelo partition pertence ao filtro");
			}
		}

		//exists
		assertTrue(!emptyCol.exists(filterFunc));
		assertTrue(sharpCol.exists(filterFunc) == sharpCol.filter(filterFunc).notEmpty());
		
		//forall
		assertTrue(emptyCol.forall(filterFunc));
		assertTrue(sharpCol.forall(filterFunc) == (sharpCol.filter(filterFunc).size() == elems.length));
		
		//map
		final List<U> mapList = new ArrayList<U>();
		for (int i = 0; i < elems.length; i++)
		{
			mapList.add(mapFunc.apply(elems[i]));
		}
		assertTrue(emptyCol.map(mapFunc).isEmpty()); //Map de lista vazia é vazio
		assertTrue(sharpCol.map(mapFunc).size() == elems.length); //O tamanho das listas é igual
		assertTrue(sharpCol.map(mapFunc).containsAll(mapList)); //Elas contém os mesmos elementos (weak equals)

		//collect <- basta verificar se ele é o mesmo resultado de filter com map
		final PartialFunction1<T, U> partFunc = Functions.createPartial(filterFunc, mapFunc);
		assertTrue(sharpCol.collect(partFunc).equals(sharpCol.filter(filterFunc).map(mapFunc)));
		assertTrue(emptyCol.collect(partFunc).isEmpty());
		
		//flatMap
		final List<W> fmapList = new ArrayList<W>();
		for (int i = 0; i < elems.length; i++)
		{
			for (W curEle : fmapFunc.apply(elems[i]))
			{
				fmapList.add(curEle);
			}
		}
		assertTrue(emptyCol.flatMap(fmapFunc).isEmpty()); //flatMap de lista vazia é vazio
		assertTrue(sharpCol.flatMap(fmapFunc).size() == fmapList.size()); //O tamanho das listas é igual
		assertTrue(sharpCol.flatMap(fmapFunc).containsAll(fmapList)); //Elas contém os mesmos elementos (weak equals)
		
		//count
		int theCount = 0;
		for (int i = 0; i < elems.length; i++)
		{
			if (filterFunc.apply(elems[i]))
			{
				theCount++;
			}
		}
		assertTrue(emptyCol.count(filterFunc) == 0);
		assertTrue(sharpCol.count(filterFunc) == theCount);
		
		//find
		assertTrue(emptyCol.find(ObjectOps.isEquals(elems[0])).isEmpty());
		assertTrue(sharpCol.find(ObjectOps.isEquals(elems[0])).notEmpty());
		
		//iterator (valida que ele retorna todos os elementos)
		final List<Object> eleList = new ArrayList<Object>(Arrays.asList(elems));
		for (Object ele : sharpCol)
		{
			eleList.remove(ele);
		}
		assertTrue(!emptyCol.iterator().hasNext());
		assertTrue(eleList.isEmpty());
		
		//Testes de chamadas de método - não verificam a validade da informação
		
		//head & headOption
		sharpCol.head();
		assertTrue(emptyCol.headOption().isEmpty());
		assertTrue(sharpCol.headOption().notEmpty());
		
		//last & lastOption
		sharpCol.last();
		assertTrue(emptyCol.lastOption().isEmpty());
		assertTrue(sharpCol.lastOption().notEmpty());
		
		//tail
		sharpCol.tail();
//		assertTrue(emptyCol.tail().isEmpty()); causa erro
		assertTrue(sharpCol.tail().size() == Math.max(elems.length - 1, 0));
		assertTrue(sharpCol.tail().tail().size() == Math.max(elems.length - 2, 0));
		
		//take
		assertTrue(emptyCol.take(-1).isEmpty());
		assertTrue(emptyCol.take(0).isEmpty());
		assertTrue(emptyCol.take(10).isEmpty());
		assertTrue(sharpCol.take(-1).size() == 0);
		assertTrue(sharpCol.take(0).size() == 0);
		assertTrue(sharpCol.take(2).size() == Math.min(sharpCol.size(), 2));
		assertTrue(sharpCol.take(500).size() == Math.min(sharpCol.size(), 500));
		
		//takeRight
		assertTrue(emptyCol.takeRight(-1).isEmpty());
		assertTrue(emptyCol.takeRight(0).isEmpty());
		assertTrue(emptyCol.takeRight(10).isEmpty());
		assertTrue(sharpCol.takeRight(-1).size() == 0);
		assertTrue(sharpCol.takeRight(0).size() == 0);
		assertTrue(sharpCol.takeRight(2).size() == Math.min(sharpCol.size(), 2));
		assertTrue(sharpCol.takeRight(500).size() == Math.min(sharpCol.size(), 500));
		
		//drop
		assertTrue(emptyCol.drop(-1).isEmpty());
		assertTrue(emptyCol.drop(0).isEmpty());
		assertTrue(emptyCol.drop(10).isEmpty());
		assertTrue(sharpCol.drop(-1).size() == sharpCol.size());
		assertTrue(sharpCol.drop(0).size() == sharpCol.size());
		assertTrue(sharpCol.drop(2).size() == Math.max(sharpCol.size() - 2, 0));
		assertTrue(sharpCol.drop(500).size() == Math.max(sharpCol.size() - 500, 0));
		
		//dropRight
		assertTrue(emptyCol.dropRight(-1).isEmpty());
		assertTrue(emptyCol.dropRight(0).isEmpty());
		assertTrue(emptyCol.dropRight(10).isEmpty());
		assertTrue(sharpCol.dropRight(-1).size() == sharpCol.size());
		assertTrue(sharpCol.dropRight(0).size() == sharpCol.size());
		assertTrue(sharpCol.dropRight(2).size() == Math.max(sharpCol.size() - 2, 0));
		assertTrue(sharpCol.dropRight(500).size() == Math.max(sharpCol.size() - 500, 0));
		
		//grouped
		assertTrue(emptyCol.grouped(2).isEmpty());
		assertTrue(sharpCol.grouped(2).size() == Math.floor(elems.length / 2.0d));
		
		//splitAt
		assertTrue(emptyCol.splitAt(2).getVal1().isEmpty() && emptyCol.splitAt(2).getVal2().isEmpty());
		assertTrue(sharpCol.splitAt(2).getVal1().size() + sharpCol.splitAt(2).getVal2().size() == elems.length);

		
		//ATENÇÃO: SORTED, no caso do ListSharp (mutable), altera INPLACE os elementos
		//PORTANTO FOI MOVIDO PARA O FINAL, POR CAUSA DO SIDE-EFFECT
		
		//sorted 
		assertTrue(emptyCol.sorted().equals(emptyCol)); //Chamar o sorted sem elementos não dá erro
		Comparable<Object> sPrevEle = (Comparable<Object>) sharpCol.sorted().iterator().next();
		for (Object curObj : sharpCol.sorted())
		{
			assertTrue(sPrevEle.compareTo((Comparable<Object>) curObj) <= 0);
			sPrevEle = (Comparable<Object>) curObj;
		}
		
		//sorted  (comparator)
		assertTrue(emptyCol.sorted(theComparator).equals(emptyCol)); //Chamar o sorted sem elementos não dá erro
		T scPrevEle = sharpCol.sorted(theComparator).iterator().next();
		for (T curObj : sharpCol.sorted(theComparator))
		{
			assertTrue(theComparator.compare(scPrevEle, curObj) <= 0);
			scPrevEle = curObj;
		}
		
		//faltou (por depender da ordem do iterator):
		//takeWhile
		//takeRightWhile
		//dropWhile		
		//dropRightWhile
		//zipWithIndex
		//foldLeft <- usualmente não é dependente de ordem, mas pode ser
	}
	
	/**
	 * Teste executado para sequências, ou seja, coleções que garantam a ordem do iterator. <br/>
	 * Assume-se que os testes para sequências genéricas já foram executados. <br/>
	 * 
	 * @param sharpCol A coleção a ser testada
	 * @param filterFunc
	 * @param elems
	 */
	public <T> void testSeq(final SharpCollection<T> sharpCol, final Function1<? super T, Boolean> filterFunc, final T... elems) 
	{
		//Premissas:
		assertTrue(elems.length > 5);
		assertTrue(new HashSet<Object>(Arrays.asList(elems)).size() != elems.length);
		assertTrue(sharpCol instanceof Seq<?>);

		//head & headOption
		assertTrue(sharpCol.head().equals(elems[0]));
		assertTrue(sharpCol.headOption().get().equals(elems[0]));

		//last & lastOption
		assertTrue(sharpCol.last().equals(elems[elems.length - 1]));
		assertTrue(sharpCol.lastOption().get().equals(elems[elems.length - 1]));

		//tail
		assertTrue(sharpCol.tail().head().equals(elems[1]));
		assertTrue(sharpCol.tail().tail().head().equals(elems[2]));

		//take
		assertTrue(sharpCol.take(4).containsAll(Arrays.asList(Arrays.copyOf(elems, 4))));

		//takeRight
		assertTrue(sharpCol.takeRight(4).containsAll(Arrays.asList(Arrays.copyOfRange(elems, elems.length - 4, elems.length))));

		//takeWhile
		final List<T> twList = new ArrayList<T>();
		for (int i = 0; i < elems.length; i++)
		{
			if (filterFunc.apply(elems[i])) twList.add(elems[i]); else break;
		}
		assertTrue(sharpCol.takeWhile(filterFunc).size() == twList.size() && 
				   sharpCol.takeWhile(filterFunc).containsAll(twList));
		
		//takeRightWhile
		final List<T> trwList = new ArrayList<T>();
		for (int i = elems.length - 1; i > -1; i--)
		{
			if (filterFunc.apply(elems[i])) trwList.add(elems[i]); else break;
		}
		assertTrue(sharpCol.takeRightWhile(filterFunc).size() == trwList.size() && 
				   sharpCol.takeRightWhile(filterFunc).containsAll(trwList));

		//drop <- drop == takeRight
		assertTrue(sharpCol.drop(2).equals(sharpCol.takeRight(sharpCol.size() - 2)));
		
		//dropRight <- dropRight == take
		assertTrue(sharpCol.dropRight(2).equals(sharpCol.take(sharpCol.size() - 2)));
		
		//dropWhile
		final List<T> dwList = new ArrayList<T>();
		boolean dwToAdd = false;
		for (int i = 0; i < elems.length; i++)
		{
			if (dwToAdd) dwList.add(elems[i]);
			else if (!filterFunc.apply(elems[i]))
			{
				dwList.add(elems[i]);
				dwToAdd = true;
			}
		}
		assertTrue(sharpCol.dropWhile(filterFunc).size() == dwList.size() && 
				   sharpCol.dropWhile(filterFunc).containsAll(dwList));
		
		//dropRightWhile
		final List<T> drwList = new ArrayList<T>();
		boolean drwToAdd = false;
		for (int i = elems.length - 1; i > -1; i--)
		{
			if (drwToAdd) drwList.add(elems[i]);
			else if (!filterFunc.apply(elems[i]))
			{
				drwList.add(elems[i]);
				drwToAdd = true;
			}
		}
		assertTrue(sharpCol.dropRightWhile(filterFunc).size() == drwList.size() && 
				   sharpCol.dropRightWhile(filterFunc).containsAll(drwList));
		
		//splitAt
		assertTrue(sharpCol.splitAt(2).getVal1().containsAll(Arrays.asList(Arrays.copyOf(elems, 2))));
		assertTrue(sharpCol.splitAt(2).getVal2().containsAll(Arrays.asList(Arrays.copyOfRange(elems, 2, elems.length))));
		
		//grouped
		final List<SharpCollection<T>> groupList = new ArrayList<SharpCollection<T>>();
		int groupIndex = 0;
		while (groupIndex < sharpCol.size())
		{
			groupList.add(sharpCol.drop(groupIndex).take(2));
			groupIndex += 2;
		}
		groupIndex = 0;
		for (SharpCollection<T> curCol : sharpCol.grouped(2))
		{
			assertTrue(curCol.size() == groupList.get(groupIndex).size());
			assertTrue(curCol.containsAll(groupList.get(groupIndex++)));
		}
		
		//zipWithIndex
		SharpCollection<Tuple2<T, Integer>> zwiCol = sharpCol.zipWithIndex();
		for (int i = 0; i < elems.length; i++)
		{
			assertTrue(zwiCol.head().getVal1().equals(elems[i]) && zwiCol.head().getVal2() == i);
			zwiCol = zwiCol.tail(); //iterator pobre
		}
		
		//iterator
		final Iterator<T> iteCol = sharpCol.iterator();
		for (int i = 0; i < elems.length; i++)
		{
			assertTrue(iteCol.next().equals(elems[i]));
		}

		//find
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
		assertTrue(sharpCol.find(filterFunc).get().equals(theEle));
	}
	
	
	//TODO: ARRUMAR O TESTE
	//ATENÇÃO: ESSES TESTES SÃO FEITOS À MÃO, MAS DEVERIAM SER GENERALIZADOS
	@SuppressWarnings("unchecked")
	public <T, U, W> void  testGenSetCollection( final SharpCollection<T> sharpCol,
							  			final SharpCollection<T> oriSharpCol,
							  			final SharpCollection<T> emptyCol,
							  			final Function1<? super T, Boolean> filterFunc,
							  			final Function1<? super T, U> mapFunc,
							  			final Function1<? super T, ? extends Iterable<W>> fmapFunc,
							  			final Comparator<? super T> theComparator,
							  			final T eleNotInCol,
							  			final T... elems) 
	{
		final List<T> elemsList = Arrays.asList(elems);
		final List<T> singleEleList = Arrays.asList(eleNotInCol);
		final Set<T> distinctSet = new HashSet<T>(Arrays.asList(elems));
		final TreeSet<T> sortedSet = (elems[0] instanceof Comparable<?>) ? new TreeSet<T>(elemsList) : new TreeSet<T>(theComparator);
		if (sortedSet.isEmpty()) sortedSet.addAll(elemsList);
		final TreeSet<T> ssComparator = new TreeSet<T>(theComparator);
		ssComparator.addAll(elemsList);
		
		//Premissas:
		assertTrue(elems.length > 5);
		assertTrue(new HashSet<Object>(Arrays.asList(elems)).size() == oriSharpCol.size());
		
		//equals
		assertTrue(sharpCol.equals(oriSharpCol));
		assertTrue(emptyCol.equals(emptyCol));
		
		//hashCode
		assertTrue(sharpCol.hashCode() == oriSharpCol.hashCode());
		assertTrue(emptyCol.hashCode() == emptyCol.hashCode());
		
		//size
		assertTrue(sharpCol.size() == distinctSet.size());
		
		//isEmpty
		assertTrue(sharpCol.isEmpty() == false && sharpCol.size() > 0);
		assertTrue(emptyCol.isEmpty() == true && emptyCol.size() == 0);
		
		//notEmpty
		assertTrue(sharpCol.notEmpty() == true && sharpCol.size() > 0);
		assertTrue(emptyCol.notEmpty() == false && emptyCol.size() == 0);
		
		//contains
		assertTrue(sharpCol.contains(elems[0]));
		assertTrue(! sharpCol.contains(eleNotInCol));
		assertTrue(! emptyCol.contains(elems[0]));
		
		//containsAll
		assertTrue(sharpCol.containsAll(elemsList));
		assertTrue(! sharpCol.containsAll(singleEleList));
		assertTrue(!emptyCol.containsAll(elemsList));

		//containsAny
		assertTrue(sharpCol.containsAny(elemsList));
		assertTrue(! sharpCol.containsAny(singleEleList));
		assertTrue(! emptyCol.containsAny(elemsList));
		
		//intersect
		assertTrue(sharpCol.intersect(elemsList).equals(oriSharpCol));
		assertTrue(sharpCol.intersect(singleEleList).equals(emptyCol));
		assertTrue(emptyCol.intersect(elemsList).equals(emptyCol));
		
		//distinct
		assertTrue(sharpCol.distinct().intersect(distinctSet).size() == distinctSet.size());
		assertTrue(emptyCol.distinct().intersect(distinctSet).size() == 0);
		
		//mkString <- assegura que chamar o método é seguro
		assertTrue(!sharpCol.mkString().isEmpty());
		assertTrue(emptyCol.mkString().isEmpty());

		//Comparison
		if ((oriSharpCol.head() instanceof Comparable<?>))
		{
			//min & minOption
			assertTrue(sharpCol.min().equals(sortedSet.first()));
			assertTrue(sharpCol.minOption().get().equals(sortedSet.first()));
			assertTrue(emptyCol.minOption().isEmpty());
	
			//minWith
			assertTrue(sharpCol.min(theComparator).equals(ssComparator.first()));
			
			//max & maxOption
			assertTrue(sharpCol.max().equals(sortedSet.last()));
			assertTrue(sharpCol.maxOption().get().equals(sortedSet.last()));
			assertTrue(emptyCol.maxOption().isEmpty());
			
			//maxWith
			assertTrue(sharpCol.max(theComparator).equals(ssComparator.last()));
		}
		else
		{
			System.out.println("WARNING: elementos da lista não são instância de comparable. Alguns testes foram pulados");
		}
		
		//filter
		List<Object> listaFiltro = new ArrayList<Object>();
		for (T ele : distinctSet)
		{
			if (filterFunc.apply(ele))
			{
				listaFiltro.add(ele);
			}
		}
		assertTrue(emptyCol.filter(filterFunc).isEmpty());
		assertTrue(sharpCol.filter(filterFunc).size() == listaFiltro.size());

		//partition
		assertTrue(emptyCol.partition(filterFunc).getVal1().isEmpty() && emptyCol.partition(filterFunc).getVal2().isEmpty());
		final Tuple2<SharpCollection<T>, SharpCollection<T>> partResult = (Tuple2<SharpCollection<T>, SharpCollection<T>>) sharpCol.partition(filterFunc);
		assertTrue((partResult.getVal1().size() + partResult.getVal2().size()) == oriSharpCol.size()); //A partição engloba todos os elementos
		assertTrue(partResult.getVal1().equals(sharpCol.filter(filterFunc))); //O primeiro retorno da partição é igual ao filtro
		
		for (T obj : partResult.getVal2())
		{
			if (filterFunc.apply(obj))
			{
				throw new AssertionFailedError("A segunda coleção retornada pelo partition pertence ao filtro");
			}
		}

		//exists
		assertTrue(!emptyCol.exists(filterFunc));
		assertTrue(sharpCol.exists(filterFunc) == sharpCol.filter(filterFunc).notEmpty());
		
		//forall
		assertTrue(emptyCol.forall(filterFunc));
		assertTrue(sharpCol.forall(filterFunc) == (sharpCol.filter(filterFunc).size() == oriSharpCol.size()));
		
		//map
		final List<U> mapList = new ArrayList<U>();
		for (T ele : distinctSet)
		{
			mapList.add(mapFunc.apply(ele));
		}
		assertTrue(emptyCol.map(mapFunc).isEmpty()); //Map de lista vazia é vazio
		assertTrue(sharpCol.map(mapFunc).size() == oriSharpCol.size()); //O tamanho das listas é igual
		assertTrue(sharpCol.map(mapFunc).containsAll(mapList)); //Elas contém os mesmos elementos (weak equals)

		//collect <- basta verificar se ele é o mesmo resultado de filter com map
		final PartialFunction1<T, U> partFunc = Functions.createPartial(filterFunc, mapFunc);
		assertTrue(sharpCol.collect(partFunc).equals(sharpCol.filter(filterFunc).map(mapFunc)));
		assertTrue(emptyCol.collect(partFunc).isEmpty());
		
		//flatMap
		final Set<W> fmapList = new HashSet<W>();
		for (T ele : distinctSet)
		{
			for (W curEle : fmapFunc.apply(ele))
			{
				fmapList.add(curEle);
			}
		}
		assertTrue(emptyCol.flatMap(fmapFunc).isEmpty()); //flatMap de lista vazia é vazio
		final SharpCollection<W> flatMap = sharpCol.flatMap(fmapFunc);
		assertTrue(flatMap.size() == fmapList.size()); //O tamanho das listas é igual
		assertTrue(sharpCol.flatMap(fmapFunc).containsAll(fmapList)); //Elas contém os mesmos elementos (weak equals)
		
		//count
		int theCount = 0;
		for (T ele : distinctSet)
		{
			if (filterFunc.apply(ele))
			{
				theCount++;
			}
		}
		assertTrue(emptyCol.count(filterFunc) == 0);
		assertTrue(sharpCol.count(filterFunc) == theCount);
		
		//find
		assertTrue(emptyCol.find(ObjectOps.isEquals(elems[0])).isEmpty());
		assertTrue(sharpCol.find(ObjectOps.isEquals(elems[0])).notEmpty());
		
		//iterator (valida que ele retorna todos os elementos)
		final List<Object> eleList = new ArrayList<Object>(distinctSet);
		for (Object ele : sharpCol)
		{
			eleList.remove(ele);
		}
		assertTrue(!emptyCol.iterator().hasNext());
		assertTrue(eleList.isEmpty());
		
		//Testes de chamadas de método - não verificam a validade da informação
		
		//head & headOption
		sharpCol.head();
		assertTrue(emptyCol.headOption().isEmpty());
		assertTrue(sharpCol.headOption().notEmpty());
		
		//last & lastOption
		sharpCol.last();
		assertTrue(emptyCol.lastOption().isEmpty());
		assertTrue(sharpCol.lastOption().notEmpty());
		
		//tail
		sharpCol.tail();
//		assertTrue(emptyCol.tail().isEmpty()); causa erro
		assertTrue(sharpCol.tail().size() == Math.max(oriSharpCol.size() - 1, 0));
		assertTrue(sharpCol.tail().tail().size() == Math.max(oriSharpCol.size() - 2, 0));
		
		//take
		assertTrue(emptyCol.take(-1).isEmpty());
		assertTrue(emptyCol.take(0).isEmpty());
		assertTrue(emptyCol.take(10).isEmpty());
		assertTrue(sharpCol.take(-1).size() == 0);
		assertTrue(sharpCol.take(0).size() == 0);
		assertTrue(sharpCol.take(2).size() == Math.min(sharpCol.size(), 2));
		assertTrue(sharpCol.take(500).size() == Math.min(sharpCol.size(), 500));
		
		//takeRight
		assertTrue(emptyCol.takeRight(-1).isEmpty());
		assertTrue(emptyCol.takeRight(0).isEmpty());
		assertTrue(emptyCol.takeRight(10).isEmpty());
		assertTrue(sharpCol.takeRight(-1).size() == 0);
		assertTrue(sharpCol.takeRight(0).size() == 0);
		assertTrue(sharpCol.takeRight(2).size() == Math.min(sharpCol.size(), 2));
		assertTrue(sharpCol.takeRight(500).size() == Math.min(sharpCol.size(), 500));
		
		//drop
		assertTrue(emptyCol.drop(-1).isEmpty());
		assertTrue(emptyCol.drop(0).isEmpty());
		assertTrue(emptyCol.drop(10).isEmpty());
		assertTrue(sharpCol.drop(-1).size() == sharpCol.size());
		assertTrue(sharpCol.drop(0).size() == sharpCol.size());
		assertTrue(sharpCol.drop(2).size() == Math.max(sharpCol.size() - 2, 0));
		assertTrue(sharpCol.drop(500).size() == Math.max(sharpCol.size() - 500, 0));
		
		//dropRight
		assertTrue(emptyCol.dropRight(-1).isEmpty());
		assertTrue(emptyCol.dropRight(0).isEmpty());
		assertTrue(emptyCol.dropRight(10).isEmpty());
		assertTrue(sharpCol.dropRight(-1).size() == sharpCol.size());
		assertTrue(sharpCol.dropRight(0).size() == sharpCol.size());
		assertTrue(sharpCol.dropRight(2).size() == Math.max(sharpCol.size() - 2, 0));
		assertTrue(sharpCol.dropRight(500).size() == Math.max(sharpCol.size() - 500, 0));
		
		//grouped
		assertTrue(emptyCol.grouped(2).isEmpty());
		assertTrue(sharpCol.grouped(2).size() == Math.floor(oriSharpCol.size() / 2.0d));
		
		//splitAt
		assertTrue(emptyCol.splitAt(2).getVal1().isEmpty() && emptyCol.splitAt(2).getVal2().isEmpty());
		assertTrue(sharpCol.splitAt(2).getVal1().size() + sharpCol.splitAt(2).getVal2().size() == oriSharpCol.size());

		
		//ATENÇÃO: SORTED, no caso do ListSharp (mutable), altera INPLACE os elementos
		//PORTANTO FOI MOVIDO PARA O FINAL, POR CAUSA DO SIDE-EFFECT
		
		//sorted 
		assertTrue(emptyCol.sorted().equals(emptyCol)); //Chamar o sorted sem elementos não dá erro
		Comparable<Object> sPrevEle = (Comparable<Object>) sharpCol.sorted().iterator().next();
		for (Object curObj : sharpCol.sorted())
		{
			assertTrue(sPrevEle.compareTo((Comparable<Object>) curObj) <= 0);
			sPrevEle = (Comparable<Object>) curObj;
		}
		
		//sorted  (comparator)
		assertTrue(emptyCol.sorted(theComparator).equals(emptyCol)); //Chamar o sorted sem elementos não dá erro
		T scPrevEle = sharpCol.sorted(theComparator).iterator().next();
		for (T curObj : sharpCol.sorted(theComparator))
		{
			assertTrue(theComparator.compare(scPrevEle, curObj) <= 0);
			scPrevEle = curObj;
		}
		
		//faltou (por depender da ordem do iterator):
		//takeWhile
		//takeRightWhile
		//dropWhile		
		//dropRightWhile
		//zipWithIndex
		//foldLeft <- usualmente não é dependente de ordem, mas pode ser
	}
}
