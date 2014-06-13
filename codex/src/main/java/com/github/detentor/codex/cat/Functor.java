package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;

/**
 * Em teoria das categorias, Functor é um mapeamento entre categorias, de forma a preservar a sua estrutura. <br/>
 * Em particular, um Functor é como uma função que opera em contâineres. Se uma função f : A -> B opera transformando elementos
 * A em elementos B, um Functor opera transformando uma estrutura que contém elementos do tipo A numa outra estrutura (Functor) contendo
 * elementos do tipo B. <br/>
 * 
 * @author Vinicius Seufitele Pinto
 *
 */
public interface Functor<A> 
{
	/**
	 * Mapeia esse functor para um outro functor
	 * @param func A função de mapeamento que transforma elementos do tipo 'A' em 'B'
	 * @return Um functor que contém elementos do tipo B
	 */
	<B> Functor<B> fmap(final Function1<A, B> func);
}
