//package com.github.detentor.codex.monads;
//
//import com.github.detentor.codex.function.Function1;
//
///**
// * Uma mônade é um container para um determinado tipo de dados, que, ao seguir três leis básicas, é capaz de abstrair composições de
// * diferentes tipos de operações. <br/>
// * <br/>
// * 
// * A motivação para mônades é ser capaz de realizar operações sem se importar com dado contido na mônade. <br/>
// * Alguns exemplos de mônades: <br/>
// * <br/>
// * 
// * Option -> É uma mônade que representa um valor que pode ou não existir <br/>
// * State -> É uma mônade que 'carrega' um estado <br/>
// * Continuation -> Uma mônade que abstrai computações, permitindo reexecutar uma operação diversas vezes. <br/>
// * Parser -> Mônades que fazem sequenciamento de operações de parser <br/>
// * <br/>
// * 
// * Para ser considerada uma mônade, deve-se seguir 3 leis básicas: <br/>
// * <br/>
// * 
// * 1) identidade à direita : unit(A) === M(A) <br/>
// * 2) identidade à esquerda: unit(A) bind f === f(A) <br/>
// * 3) associatividade : (m bind f) bind g === m bind (f bind g) <br/>
// * <br/>
// * 
// * O número 1) é resolvido pelo método {@link #unit(Object)} <br/>
// * O número 2) é um tanto óbvio, pois a aplicação de uma função em uma mônade implica em aplicar a função para o seu elemento <br/>
// * O número 3) é a associação básica de funções, e assegura que pode-se 'resolver' mônades concatenadas em uma ordem arbitrária.
// * 
// * @author Vinicius Seufitele Pinto
// * 
// * @param <A> O tipo de dados guardado pela mônade
// */
//public interface Monad<A>
//{
//	/**
//	 * Função de criação de mônades a partir de um valor do tipo A. <br/>
//	 * Embora esse método seja equivalente ao construtor de uma classe monádica, ele ser um método de instância significa que será
//	 * possível construir mônades sem conhecer qual o tipo concreto dela.
//	 * 
//	 * @param theValue O valor a ser guardado pela mônade
//	 * @return Uma mônade que contém o valor do elemento A
//	 */
//	Monad<A> unit(final A theValue);
//
//	/**
//	 * Função de 'amarração' monádica. Bind possibilita criar novas mônades a partir da transformação de seus valores. É o 'bloco'
//	 * principal da mônade
//	 * 
//	 * @param mapFunction A função que transforma um tipo de dados A em um tipo B
//	 * @return Uma mônade que contém o valor do tipo B
//	 */
//	<B> Monad<B> bind(final Function1<A, B> mapFunction);
//}
