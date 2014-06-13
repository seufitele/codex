package com.github.detentor.codex.cat;

import java.util.Date;

import com.github.detentor.codex.collections.mutable.ListSharp;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Function2;
import com.github.detentor.codex.product.Tuple2;

public class JavaTeste 
{
	
	public static void main2(String[] args)
	{
		JavaTeste jt = new JavaTeste();
	    int count = Integer.MAX_VALUE;
	    long beg = (new Date()).getTime();
	    for (int i = 0; i < count; i++) {
	    	jt.doNothing();
	    }
	    System.out.println("priv : " + new Long((new Date()).getTime() - beg).toString());

	    beg = (new Date()).getTime();
	    for (int i = 0; i < count; i++) {
	        doNothingStatic();
	    }
	    System.out.println("privstat : " + new Long((new Date()).getTime() - beg).toString());
	}

	public void doNothing() {
	    int i = 0;
	}
	
	private static void doNothingStatic() {
	    int i = 0;
	}

	public static void main(String[] args) 
	{
		main2(null);
//		State<Integer, Integer> sMonad = state -> Tuple2.from(state * state, state + 1);
//		State<Integer, String> mState = state -> Tuple2.from(String.valueOf(state * state), state + 1);
//		Function1<Integer, State<Integer, String>> sFunc = param -> mState; 
//		
//		System.out.println(sMonad.bind(sFunc).apply(2));
//		
//		
//		System.out.println(cata(1, (x, y) -> x * y, ListSharp.from(1, 2, 3, 4, 5)));
//		System.out.println(cata(ListSharp.<Integer>empty(), (x, y) -> y.add(x) , ListSharp.from(1, 2, 3, 4, 5)));
//		
//		System.out.println(ana(10, x -> x == 0 ? Tuple2.from(0, ListSharp.empty()) : Tuple2.from(x - 1, ListSharp.from(x))));
	}
	
	/**
	 * Um catamorfismo sobre um tipo T é uma função do tipo T -> U que destrói um objeto
	 * do tipo T de acordo com a estrutura de T, chamando-se recursivamente para quaisquer
	 * componentes de T que também possuem o tipo T, combinando os resultados para o tipo final U. <br/>
	 * <br/>
	 * Um exemplo de função catamórfica é a função fold. <br/>
	 * 
	 * Um catamorfismo sobre listas é composto de duas partes: (a, f), para cada tipo de lista diferente:
	 * a é um mapeamento de uma lista vazia em U (simplesmente uma constante a), e
	 * f é uma função binária que combina o elemento extraído do head da lista, com a aplicação recursiva da função
	 * para o restante da lista.  <br/><br/>
	 * 
	 * Em geral um catamorfismo transforma cada construtor do tipo T em um valor do tipo U. No caso da lista,
	 * ela aceita dois construtores: o construtor vazio e o construtor com elementos. Se houvessem mais casos (ou menos), por exemplo,
	 * a função catamórfica deveria ter um valor especificado para cada tipo de construtor.
	 * 
	 * 
	 */
	public static <A, B> B cata(final B start, final Function2<A, B, B> func, ListSharp<A> list)
	{
		if (list.isEmpty())
		{
			return start;
		}
		return func.apply(list.head(), cata(start, func, list.tail()));
	}

	/**
	 * Anamorfismno (dual de catamorfismo) <br/>
	 * Um anamorfismo sobre um tipo T é uma função do tipo U -> T que destrói um tipo U em
	 * algum número de componentes. Sobre os componentes que são do tipo U, ele aplica recursão para
	 * convertê-los para o tipo T. Depois disso ele combina os resultados recursivos com os outros componentes
	 * para um T, através de funções construtoras de T. <br/> <br/>
	 * 
	 * Voltando ao exemplo da lista, precisa-se de uma função u para indicar quando deve-se mapear o valor
	 * para uma lista vazia, ou quando deve-se criar uma lista com um determinado valor.
	 */
	public static <U, T> ListSharp<T> ana(final U toDestruct, final Function1<U, Tuple2<U, ListSharp<T>>> func)
	{
		Tuple2<U, ListSharp<T>> toReturn = func.apply(toDestruct);
		
		if (toReturn.getVal2().isEmpty())
		{
			return toReturn.getVal2();
		}
		return toReturn.getVal2().addAll(ana(toReturn.getVal1(), func) );
	}

}

