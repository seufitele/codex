package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;

/**
 * Interface de estruturas que podem ser percorridas da esquerda
 * para a direita, executando uma ação sobre cada elemento
 * 
 * @param <A> O tipo de elemento contido na estrutura
 */
public interface Traversable<A> extends Functor<A>, Foldable<A>
{
	//Definição mínima: traverse | sequenceA
	
	//traverse :: Applicative f => (a -> f b) -> t a -> f (t b)
	//sequenceA :: Applicative f => t (f a) -> f (t a)
	
	/**
	 * Mapeia cada elemento desta estrutura para uma ação, executa cada
	 * uma destas ações da esquerda para a direita, e coleta os resultados.
	 * @param function
	 * @return
	 */
	public <B> Applicative<Foldable<B>> traverse(final Function1<? super A, Applicative<B>> function);
	
	
}
