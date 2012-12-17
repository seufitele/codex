package com.github.detentor.codex.collections;

import java.lang.reflect.Method;

import org.junit.Test;

import com.github.detentor.codex.collections.mutable.FastList;
import com.github.detentor.codex.collections.mutable.LinkedListSharp;
import com.github.detentor.codex.collections.mutable.ListSharp;
import com.github.detentor.codex.collections.mutable.SetSharp;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.util.Reflections;
import com.github.detentor.operations.IntegerOps;


public class SharpCollectionTest
{
	@SuppressWarnings("unchecked")
	@Test
	public void testListSharp() 
	{
		ListSharp<Class<?>> listas = 
				ListSharp.<Class<?>>from(SetSharp.class, ListSharp.class, LinkedListSharp.class, FastList.class);
		
		for(Class<?> ele : listas)
		{
			testCollection((Class<SharpCollection<?>>) ele);
		}
	}
	
	public void testCollection(Class<SharpCollection<?>> theClass) 
	{
		final Function1<Object[], SharpCollection<Integer>> func = Reflections.liftStaticVarArgs(theClass, "from");
		final Option<Method> func2 = Reflections.getMethodFromName(theClass, "empty");
		
		SharpCollection<Integer> listaOri = func.apply(new Object[] {new Object[] { 1, 2, 3, 4, 5} });
		SharpCollection<Integer> lista = func.apply(new Object[] {new Object[] { 1, 2, 3, 4, 5} });
		SharpCollection<Integer> listaVazia = Reflections.invokeSafe(theClass, func2.get());
		
		SharpCollection<Integer> listaDrop1 = func.apply(new Object[] {new Object[] {2, 3, 4, 5} });
		SharpCollection<Integer> listaDrop2 = func.apply(new Object[] {new Object[] {3, 4, 5} }); 
		
		SharpCollection<Integer> listaTake1 = func.apply(new Object[] {new Object[] {1}});
		SharpCollection<Integer> listaTake2 = func.apply(new Object[] {new Object[] {1, 2}});
		
		SharpCollection<Integer> listaTakeRight1 = func.apply(new Object[] {new Object[] {5}});
		SharpCollection<Integer> listaTakeRight2 = func.apply(new Object[] {new Object[] {4, 5}}); 
		
		SharpCollection<Integer> listaDropRight1 = func.apply(new Object[] {new Object[] { 1, 2, 3, 4} });
		SharpCollection<Integer> listaDropRight2 = func.apply(new Object[] {new Object[] { 1, 2, 3} });
		
		if (lista instanceof IndexedSeq<?>)
		{
			testIndexedSeq((IndexedSeq<Integer>) lista);
		}

		testSharpCollection(lista, listaOri, 
							listaDrop1, listaDrop2, 
							listaTake1, listaTake2, 
							listaTakeRight1, listaTakeRight2, 
							listaDropRight1, listaDropRight2, listaVazia);
	}
	
	/**
	 * Faz um teste completo para uma SharpCollection (teste descontando mutabilidade). <br/>
	 * Para aplicar o teste a uma coleção, basta fazer uma coleção com os valores [1, 2, 3, 4, 5] <br/>
	 * 
	 * 
	 * @param sharpCol Uma coleção com os valores [1, 2, 3, 4, 5]
	 * @param oriSharpCol Uma cópia da SharpCol (deep copy)
	 * @param colDrop1 A coleção com o primeiro item dropado 
	 * @param colDrop2 A coleção com os dois primeiro itens dropado 
	 * @param colTake1 A coleção com o primeiro item pego
	 * @param colTake2 A coleção com os dois primeiros itens pegos
	 * @param colTakeRight1 A coleção com o primeiro item à direita pego
	 * @param colTakeRight2 A coleção com os dois primeiros itens à direita pego
	 * @param colDropRight1 A coleção com o último elemento dropado
	 * @param colDropRight2 A coleção com os dois últimos elementos dropados
	 * @param emptyCol Uma coleção vazia
	 */
	public void testSharpCollection(final SharpCollection<Integer> sharpCol,
						  			final SharpCollection<Integer> oriSharpCol,
						  			SharpCollection<Integer> colDrop1,
						  			SharpCollection<Integer> colDrop2,
						  			SharpCollection<Integer> colTake1,
						  			SharpCollection<Integer> colTake2,
						  			SharpCollection<Integer> colTakeRight1,
						  			SharpCollection<Integer> colTakeRight2,
						  			SharpCollection<Integer> colDropRight1,
						  			SharpCollection<Integer> colDropRight2,
						  			final SharpCollection<Integer> emptyCol
						  			) 
	{
		//Igualdade de listas
			assert(sharpCol.equals(oriSharpCol));
			assert(emptyCol.equals(emptyCol));
		
		//IsEmpty() geral
			assert(emptyCol.notEmpty() == false);
			assert(emptyCol.isEmpty());

		//Head - geral
			assert(sharpCol.head() == 1);
		
		//HeadOption
			assert(sharpCol.headOption().notEmpty());
			assert(emptyCol.headOption().isEmpty());
		
		//Tail - geral
			assert(sharpCol.tail().equals(colDrop1));
			assert(sharpCol.tail().head() == 2);
		
		//Last - geral
			assert(sharpCol.last() == 5);
		
		//LastOption - geral
			assert(sharpCol.lastOption().notEmpty());
			assert(emptyCol.lastOption().isEmpty());
			
		//Take - geral
			assert(sharpCol.take(-2).isEmpty());
			assert(sharpCol.take(0).isEmpty());
			
			assert(sharpCol.take(1).equals(colTake1));
			assert(sharpCol.take(2).equals(colTake2));
			assert(sharpCol.take(5).equals(oriSharpCol));
			assert(sharpCol.take(10).equals(oriSharpCol));
		//

		//TakeRight - geral
			assert(sharpCol.takeRight(-2).isEmpty());
			assert(sharpCol.takeRight(0).isEmpty());
			assert(sharpCol.takeRight(1).equals(colTakeRight1));
			assert(sharpCol.takeRight(2).equals(colTakeRight2));
			assert(sharpCol.takeRight(5).equals(oriSharpCol));
			assert(sharpCol.takeRight(10).equals(oriSharpCol));
		//

		//TakeWhile - geral
			assert(sharpCol.takeWhile(IntegerOps.greaterThan(100)).equals(oriSharpCol));
			assert(sharpCol.takeWhile(IntegerOps.greaterThan(1)).equals(oriSharpCol));
			assert(sharpCol.takeWhile(IntegerOps.lowerThan(2)).equals(colTake1));
			assert(sharpCol.takeWhile(IntegerOps.lowerThan(3)).equals(colTake2));
			assert(sharpCol.takeWhile(IntegerOps.lowerThan(10)).equals(oriSharpCol));
		//

		//TakeRightWhile - geral
			assert(sharpCol.takeRightWhile(IntegerOps.greaterThan(100)).isEmpty());
			assert(sharpCol.takeRightWhile(IntegerOps.greaterThan(5)).isEmpty());
			assert(sharpCol.takeRightWhile(IntegerOps.greaterThan(4)).equals(colTakeRight1));
			assert(sharpCol.takeRightWhile(IntegerOps.greaterThan(3)).equals(colTakeRight2));
			assert(sharpCol.takeRightWhile(IntegerOps.lowerThan(10)).equals(oriSharpCol));
		//


		//Drop - geral
			assert(sharpCol.drop(-2).equals(oriSharpCol));
			assert(sharpCol.drop(0).equals(oriSharpCol));
			assert(sharpCol.drop(1).equals(colDrop1));
			assert(sharpCol.drop(2).equals(colDrop2));
			assert(sharpCol.drop(5).isEmpty());
			assert(sharpCol.drop(10).isEmpty());
		//

		//DropRight - geral
			assert(sharpCol.dropRight(-2).equals(oriSharpCol));
			assert(sharpCol.dropRight(0).equals(oriSharpCol));
			assert(sharpCol.dropRight(1).equals(colDropRight1));
			assert(sharpCol.dropRight(2).equals(colDropRight2));
			assert(sharpCol.dropRight(5).isEmpty());
			assert(sharpCol.dropRight(10).isEmpty());
		//

		//DropWhile - geral
			assert(sharpCol.dropWhile(IntegerOps.greaterThan(100)).equals(oriSharpCol));
			assert(sharpCol.dropWhile(IntegerOps.greaterThan(1)).equals(oriSharpCol));
			assert(sharpCol.dropWhile(IntegerOps.lowerThan(2)).equals(colDrop1));
			assert(sharpCol.dropWhile(IntegerOps.lowerThan(3)).equals(colDrop2));
			assert(sharpCol.dropWhile(IntegerOps.lowerThan(10)).isEmpty());
		//
			
		//DropRightWhile - geral
			assert(sharpCol.dropRightWhile(IntegerOps.greaterThan(100)).equals(oriSharpCol));
			assert(sharpCol.dropRightWhile(IntegerOps.greaterThan(5)).equals(oriSharpCol));
			assert(sharpCol.dropRightWhile(IntegerOps.greaterThan(4)).equals(colDropRight1));
			assert(sharpCol.dropRightWhile(IntegerOps.greaterThan(3)).equals(colDropRight2));
			assert(sharpCol.dropRightWhile(IntegerOps.lowerThan(10)).isEmpty());
		//

	}
	
	public void testIndexedSeq(final IndexedSeq<Integer> sharpCol)
	{
		//Is defined at -> funciona somente para indexed seqs
		assert(sharpCol.isDefinedAt(0));
		assert(sharpCol.isDefinedAt(2));
		assert(sharpCol.isDefinedAt(sharpCol.size() - 1));
		assert(sharpCol.isDefinedAt(-1) == false);
		assert(sharpCol.isDefinedAt(sharpCol.size()) == false);
	}
	
}
