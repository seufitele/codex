package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;

/**
 * Functores representam estruturas que podem ser mapeadas. <br/>
 * Em particular, o mapeamento não altera a forma da estrutura.
 *
 * @param <A>
 */
public interface Functor<A>
{
	/**
	 * Mapeia esse functor para um outro functor
	 * @param func A função de mapeamento que transforma elementos do tipo 'A' em 'B'
	 * @return Um functor que contém elementos do tipo B
	 */
    public <B> Functor<B> map(final Function1<? super A, B> function);
}
