package com.github.detentor.codex.collections;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import com.github.detentor.codex.collections.immutable.LazyList;
import com.github.detentor.codex.collections.mutable.LLSharp;
import com.github.detentor.codex.collections.mutable.ListSharp;
import com.github.detentor.codex.collections.mutable.MapSharp;
import com.github.detentor.codex.collections.mutable.SetSharp;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.FunctionN;
import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;
import com.github.detentor.codex.util.Reflections;
import com.github.detentor.operations.IntegerOps;
import com.github.detentor.operations.ObjectOps;


public class SharpCollectionTest
{
	// faltou (por depender da ordem do iterator):
	// foldLeft <- usualmente não é dependente de ordem, mas pode ser

	@Test
	public void testCat()
	{
		OptionTest.testOption();
		EitherTest.testEither();
		StateTest.testState();
	}
	
	@Test
	public void testSharpCollection()
	{
		// Os testes devem estar divididos em 3 partes:
		// 1 - Testes de GenericSharpCollection (imutável / mutável)
		// 2 - Testes de sequências (drop, tail, etc)
		// 3 - Testes de funções de ordem superior
		// 4 - Testes de sequências lineares e sequências indexadas (LinearSeq & IndexedSeq)
		// 5 - Testes de diversos tipos de mapas e sets.

		// Como observação, a ordem do iterator de GenericSharpCollection não é bem-definida (ex: Mapas e Sets).

		final List<Class<?>> collections = Arrays.<Class<?>> asList(ListSharp.class, SetSharp.class,
				com.github.detentor.codex.collections.immutable.ListSharp.class, LazyList.class, LLSharp.class,
				com.github.detentor.codex.collections.immutable.LLSharp.class);

		final List<ClassTest<Integer, String, Integer>> classTests = new ArrayList<ClassTest<Integer, String, Integer>>();

		for (Class<?> theClass : collections)
		{
			classTests.add(genClassTest(theClass));
		}

		try
		{
			for (final ClassTest<Integer, String, Integer> ele : classTests)
			{
				testSharpCollection(ele);
			}
			testSharpCollection(genMapTest());
		}
		catch (final AssertionError ae)
		{
			//System.out.println("Erro ao fazer o teste para a classe " + ele.getSharpCollection().getClass());
			throw new RuntimeException(ae);
		}
	}

	/**
	 * Faz um teste completo para uma SharpCollection (teste descontando mutabilidade). <br/>
	 * Para aplicar o teste a uma coleção, basta fazer uma coleção com os valores [1, 2, 3, 4, 5] <br/>
	 * 
	 * Obs: Espera-se um array de tamanho maior que 5, que tenha pelo menos 1 elemento repetido e que os elementos implementem a interface
	 * Comparable.
	 * 
	 * @param sharpCollection Uma coleção com os valores
	 * @param originalCollection Uma cópia da SharpCol (deep copy)
	 * @param emptyCollection Uma coleção vazia
	 * 
	 * @param filterFunc Uma função simples de filtro (que esteja válida para, pelo menos, 1 elemento)
	 * @param mapFunc Uma função simples de mapa (não será verificado o retorno dos elementos)
	 * @param fmapFunc Uma função de flatMap simples
	 * 
	 * @param eleNotInCol Elemento que não está contido na coleção
	 * @param elems Elementos que compõe a coleção. <br/>
	 */
	public <T, U, W> void testSharpCollection(final ClassTest<T, U, W> classTest)
	{
		final SharpCollection<T> sharpCol = classTest.getSharpCollection();
		final SharpCollection<T> oriCol = classTest.getOriginalCollection();
		final SharpCollection<T> emptyCol = classTest.getEmptyCollection();
		final Function1<? super T, Boolean> filterF = classTest.getFilterFunc();
		final Function1<? super T, U> mapF = classTest.getMapFunc();
		final Function1<? super T, ? extends Iterable<W>> fmapF = classTest.getFmapFunc();
		final Comparator<? super T> comparator = classTest.getTheComparator();
		final T eleNotInCol = classTest.getEleNotInCollection();
		final T[] elems = classTest.getElems();

		// Testes básicos deveria passar
		SharpCollectionBasic.testSharpCollectionBasic(sharpCol, oriCol, emptyCol, filterF, eleNotInCol, elems);

		// HOF
		SharpCollectionHOF.testSharpCollectionHOF(sharpCol, oriCol, emptyCol, filterF, mapF, fmapF, eleNotInCol, elems);

		// Se for sequencia, inicia os testes apropriados
		if (sharpCol instanceof Seq<?>)
		{
			SeqCollectionTest.testSeqCollection((Seq<T>) sharpCol, (Seq<T>) oriCol, (Seq<T>) emptyCol, filterF, eleNotInCol, elems);
		}
		
		if (sharpCol instanceof ComparableCollection<?>)
		{
			// Comparison
			SeqCollectionComparator.testSharpCollectionComparator((ComparableCollection<?>) sharpCol, (ComparableCollection<?>) oriCol, (ComparableCollection<?>) emptyCol, comparator, eleNotInCol, elems);
		}
	}

	@SuppressWarnings("unchecked")
	public ClassTest<Tuple2<String, Integer>, String, Tuple2<String, Integer>> genMapTest()
	{
		final Tuple2<String, Integer> ele1 = Tuple2.from("a", 1);
		final Tuple2<String, Integer> ele2 = Tuple2.from("b", 2);
		final Tuple2<String, Integer> ele3 = Tuple2.from("c", 3);
		final Tuple2<String, Integer> ele4 = Tuple2.from("d", 4);
		final Tuple2<String, Integer> ele5 = Tuple2.from("e", 5);
		final Tuple2<String, Integer> ele6 = Tuple2.from("b", 2);

		final List<Tuple2<String, Integer>> listaTupla = Arrays.asList(ele1, ele2, ele3, ele4, ele5, ele6);

		final SharpCollection<Tuple2<String, Integer>> mapaOri = MapSharp.from(listaTupla);
		final SharpCollection<Tuple2<String, Integer>> mapa = MapSharp.from(listaTupla);
		final SharpCollection<Tuple2<String, Integer>> mapaVazio = MapSharp.empty();

		final Function1<Tuple2<String, Integer>, List<Tuple2<String, Integer>>> fmap = new Function1<Tuple2<String, Integer>, List<Tuple2<String, Integer>>>()
		{
			@Override
			public List<Tuple2<String, Integer>> apply(final Tuple2<String, Integer> param)
			{
				final List<Tuple2<String, Integer>> retorno = new ArrayList<Tuple2<String, Integer>>();
				retorno.add(param);
				retorno.add(param);
				return retorno;
			}
		};

		final Comparator<Tuple2<String, Integer>> theComp = new Comparator<Tuple2<String, Integer>>()
		{
			@Override
			public int compare(final Tuple2<String, Integer> ob1, final Tuple2<String, Integer> ob2)
			{
				return ob1.getVal1().compareTo(ob2.getVal1());
			}
		};

		final Function1<Tuple2<String, Integer>, Boolean> fFilter = new Function1<Tuple2<String, Integer>, Boolean>()
		{
			@Override
			public Boolean apply(final Tuple2<String, Integer> param)
			{
				return param.getVal2() < 3;
			}
		};

		ClassTest<Tuple2<String, Integer>, String, Tuple2<String, Integer>> classTest = new ClassTest<Tuple2<String, Integer>, String, Tuple2<String, Integer>>();

		classTest.setSharpCollection(mapa);
		classTest.setOriginalCollection(mapaOri);
		classTest.setEmptyCollection(mapaVazio);
		classTest.setFilterFunc(fFilter);
		classTest.setMapFunc(ObjectOps.toString);
		classTest.setFmapFunc(fmap);
		classTest.setTheComparator(theComp);
		classTest.setEleNotInCollection(Tuple2.from("f", 6));
		classTest.setElems((Tuple2<String, Integer>[]) listaTupla.toArray());

		return classTest;
	}

	public ClassTest<Integer, String, Integer> genClassTest(final Class<?> theClass)
	{
		final Function1<Integer, List<Integer>> fmap = new FlatmapFunction();
		final Comparator<Integer> theComp = new ComparatorFunction();

		final Option<Method> func2 = Reflections.getMethodFromNameAndType(theClass, "empty", new Class<?>[0]);
		final FunctionN<Object, SharpCollection<Integer>> func = Reflections.liftStaticVarArgs(theClass, "from");

		final Integer[] elementos = new Integer[] { 2, 4, 3, 3, 1, 6, 5, 2 };

		final SharpCollection<Integer> listaOri = func.apply((Object[]) elementos);
		final SharpCollection<Integer> lista = func.apply((Object[]) elementos);
		final SharpCollection<Integer> listaVazia = Reflections.safeInvoke(theClass, func2.get());

		ClassTest<Integer, String, Integer> classTest = new ClassTest<Integer, String, Integer>();

		classTest.setSharpCollection(lista);
		classTest.setOriginalCollection(listaOri);
		classTest.setEmptyCollection(listaVazia);

		classTest.setFilterFunc(IntegerOps.lowerThan(5));
		classTest.setMapFunc(ObjectOps.toString);
		classTest.setFmapFunc(fmap);
		classTest.setTheComparator(theComp);
		classTest.setEleNotInCollection(10);
		classTest.setElems(elementos);

		return classTest;
	}
	
	private static final class ComparatorFunction implements Comparator<Integer>
	{
		@Override
		public int compare(final Integer ob1, final Integer ob2)
		{
			return ob2.compareTo(ob1);
		}
	}

	private static final class FlatmapFunction implements Function1<Integer, List<Integer>>
	{
		@Override
		public List<Integer> apply(final Integer param)
		{
			final List<Integer> retorno = new ArrayList<Integer>();
			retorno.add(param);
			retorno.add(param);
			return retorno;
		}
	}
}