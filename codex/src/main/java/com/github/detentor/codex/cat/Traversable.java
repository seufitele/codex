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
	/**
	 * Mapeia cada elemento desta estrutura para uma ação, executa cada
	 * uma destas ações da esquerda para a direita, e coleta os resultados.
	 * @param function
	 * @return
	 */
	public <B> Applicative<Foldable<B>> traverse(final Function1<? super A, Applicative<B>> function);
}
