package com.github.detentor.codex.collections;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;

import com.github.detentor.codex.collections.mutable.LLSharp;
import com.github.detentor.codex.collections.mutable.ListSharp;
import com.github.detentor.codex.collections.mutable.MapSharp;
import com.github.detentor.codex.collections.mutable.SetSharp;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.FunctionN;
import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.util.Reflections;
import com.github.detentor.operations.IntegerOps;
import com.github.detentor.operations.ObjectOps;

public class SharpCollectionTest
{
	@SuppressWarnings("unchecked")
	@Test
	public void testListSharp() 
	{
		ListSharp<Class<?>> listas = 
				//Recolocar: SetSharp
				//SetSharp está com erro porque provavelmente o iterator está retornando diferente
				//FastList.class
				ListSharp.<Class<?>>from(
						ListSharp.class,
						LLSharp.class,
						SetSharp.class,
//						MapSharp.class,
						com.github.detentor.codex.collections.immutable.ListSharp.class);
		
		for(Class<?> ele : listas)
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
	
	public void testCollection(Class<SharpCollection<?>> theClass) 
	{
		final FunctionN<Object, SharpCollection<Integer>> func = Reflections.liftStaticVarArgs(theClass, "from");
		final Option<Method> func2 = Reflections.getMethodFromName(theClass, "empty");
		
		SharpCollection<Integer> listaOri = func.apply(1, 2, 3, 4, 5);
		SharpCollection<Integer> lista = func.apply(1, 2, 3, 4, 5);
		SharpCollection<Integer> listaVazia = Reflections.invokeSafe(theClass, func2.get());
		
		SharpCollection<Integer> listaDrop1 = func.apply(2, 3, 4, 5);
		SharpCollection<Integer> listaDrop2 = func.apply(3, 4, 5); 
		
		SharpCollection<Integer> listaTake1 = func.apply(1);
		SharpCollection<Integer> listaTake2 = func.apply(1, 2);
		
		SharpCollection<Integer> listaTakeRight1 = func.apply(5);
		SharpCollection<Integer> listaTakeRight2 = func.apply(4, 5); 
		
		SharpCollection<Integer> listaDropRight1 = func.apply(1, 2, 3, 4);
		SharpCollection<Integer> listaDropRight2 = func.apply(1, 2, 3);
		
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
			assertTrue(sharpCol.equals(oriSharpCol));
			assertTrue(emptyCol.equals(emptyCol));
		
		//IsEmpty() geral
			assertTrue(emptyCol.notEmpty() == false);
			assertTrue(emptyCol.isEmpty());

		//Head - geral
			assertTrue(sharpCol.head() == 1);
		
		//HeadOption
			assertTrue(sharpCol.headOption().notEmpty());
			assertTrue(emptyCol.headOption().isEmpty());
		
		//Tail - geral
			assertTrue(sharpCol.tail().equals(colDrop1));
			assertTrue(sharpCol.tail().head() == 2);
		
		//Last - geral
			assertTrue(sharpCol.last() == 5);
		
		//LastOption - geral
			assertTrue(sharpCol.lastOption().notEmpty());
			assertTrue(emptyCol.lastOption().isEmpty());
			
		//Take - geral
			assertTrue(sharpCol.take(-2).isEmpty());
			assertTrue(sharpCol.take(0).isEmpty());
			
			assertTrue(sharpCol.take(1).equals(colTake1));
			assertTrue(sharpCol.take(2).equals(colTake2));
			assertTrue(sharpCol.take(5).equals(oriSharpCol));
			assertTrue(sharpCol.take(10).equals(oriSharpCol));
		//

		//TakeRight - geral
			assertTrue(sharpCol.takeRight(-2).isEmpty());
			assertTrue(sharpCol.takeRight(0).isEmpty());
			assertTrue(sharpCol.takeRight(1).equals(colTakeRight1));
			assertTrue(sharpCol.takeRight(2).equals(colTakeRight2));
			assertTrue(sharpCol.takeRight(5).equals(oriSharpCol));
			assertTrue(sharpCol.takeRight(10).equals(oriSharpCol));
		//

		//TakeWhile - geral
			assertTrue(sharpCol.takeWhile(IntegerOps.greaterThan(100)).equals(emptyCol));
			assertTrue(sharpCol.takeWhile(IntegerOps.greaterThan(1)).equals(emptyCol));
			assertTrue(sharpCol.takeWhile(IntegerOps.lowerThan(2)).equals(colTake1));
			assertTrue(sharpCol.takeWhile(IntegerOps.lowerThan(3)).equals(colTake2));
			assertTrue(sharpCol.takeWhile(IntegerOps.lowerThan(10)).equals(oriSharpCol));
		//

		//TakeRightWhile - geral
			assertTrue(sharpCol.takeRightWhile(IntegerOps.greaterThan(100)).isEmpty());
			assertTrue(sharpCol.takeRightWhile(IntegerOps.greaterThan(5)).isEmpty());
			assertTrue(sharpCol.takeRightWhile(IntegerOps.greaterThan(4)).equals(colTakeRight1));
			assertTrue(sharpCol.takeRightWhile(IntegerOps.greaterThan(3)).equals(colTakeRight2));
			assertTrue(sharpCol.takeRightWhile(IntegerOps.lowerThan(10)).equals(oriSharpCol));
		//


		//Drop - geral
			assertTrue(sharpCol.drop(-2).equals(oriSharpCol));
			assertTrue(sharpCol.drop(0).equals(oriSharpCol));
			assertTrue(sharpCol.drop(1).equals(colDrop1));
			assertTrue(sharpCol.drop(2).equals(colDrop2));
			assertTrue(sharpCol.drop(5).isEmpty());
			assertTrue(sharpCol.drop(10).isEmpty());
		//

		//DropRight - geral
			assertTrue(sharpCol.dropRight(-2).equals(oriSharpCol));
			assertTrue(sharpCol.dropRight(0).equals(oriSharpCol));
			assertTrue(sharpCol.dropRight(1).equals(colDropRight1));
			assertTrue(sharpCol.dropRight(2).equals(colDropRight2));
			assertTrue(sharpCol.dropRight(5).isEmpty());
			assertTrue(sharpCol.dropRight(10).isEmpty());
			assertTrue(sharpCol.dropRight(sharpCol.size()).isEmpty());
		//

		//DropWhile - geral
			assertTrue(sharpCol.dropWhile(IntegerOps.greaterThan(100)).equals(oriSharpCol));
			assertTrue(sharpCol.dropWhile(IntegerOps.greaterThan(1)).equals(oriSharpCol));
			assertTrue(sharpCol.dropWhile(IntegerOps.lowerThan(2)).equals(colDrop1));
			assertTrue(sharpCol.dropWhile(IntegerOps.lowerThan(3)).equals(colDrop2));
			assertTrue(sharpCol.dropWhile(IntegerOps.lowerThan(10)).isEmpty());
		//
			
		//DropRightWhile - geral
			System.out.println(sharpCol);
			assertTrue(sharpCol.dropRightWhile(IntegerOps.greaterThan(100)).equals(oriSharpCol));
			assertTrue(sharpCol.dropRightWhile(IntegerOps.greaterThan(5)).equals(oriSharpCol));
			assertTrue(sharpCol.dropRightWhile(IntegerOps.greaterThan(4)).equals(colDropRight1));
			assertTrue(sharpCol.dropRightWhile(IntegerOps.greaterThan(3)).equals(colDropRight2));
			assertTrue(sharpCol.dropRightWhile(IntegerOps.lowerThan(10)).isEmpty());
		//
			
		//Contains
			assertTrue(sharpCol.contains(10) == false);
			assertTrue(sharpCol.contains(1) == true);
			assertTrue(sharpCol.contains(5) == true);
		
		//High-order functions
			
			//Find
			assertTrue(sharpCol.find(IntegerOps.equal(5)).notEmpty());
			assertTrue(sharpCol.find(IntegerOps.equal(0)).isEmpty());
			
			//Filter
				assertTrue(sharpCol.filter(IntegerOps.lowerThan(5)).equals(colDropRight1));
				assertTrue(sharpCol.filter(IntegerOps.lowerThan(10)).equals(sharpCol));
				assertTrue(sharpCol.filter(IntegerOps.lowerThan(0)).equals(emptyCol));
	
			//Exists
				assertTrue(sharpCol.exists(IntegerOps.equal(5)) == true);
				assertTrue(sharpCol.exists(IntegerOps.equal(0)) == false);
			
			//Forall
				assertTrue(sharpCol.forall(IntegerOps.lowerThan(5)) == false);
				assertTrue(sharpCol.forall(IntegerOps.lowerThan(10)) == true);
			
			//Count
				assertTrue(sharpCol.count(IntegerOps.lowerThan(5)) == 4);
				assertTrue(sharpCol.count(IntegerOps.lowerThan(10)) == 5);
				assertTrue(sharpCol.count(IntegerOps.lowerThan(0)) == 0);
				
			//FoldLeft
				assertTrue(sharpCol.foldLeft(0, IntegerOps.sum) == 15);
				assertTrue(sharpCol.foldLeft(0, IntegerOps.sum) != 14);
			
			//Map
				assertTrue(sharpCol.mkString().equals("12345"));

			//Collect DEFINIR MÉTODO DE TESTE
	}
	
	public void testIndexedSeq(final IndexedSeq<Integer> sharpCol)
	{
		//Is defined at -> funciona somente para indexed seqs
		assertTrue(sharpCol.isDefinedAt(0));
		assertTrue(sharpCol.isDefinedAt(2));
		assertTrue(sharpCol.isDefinedAt(sharpCol.size() - 1));
		assertTrue(sharpCol.isDefinedAt(-1) == false);
		assertTrue(sharpCol.isDefinedAt(sharpCol.size()) == false);
	}
	
}
