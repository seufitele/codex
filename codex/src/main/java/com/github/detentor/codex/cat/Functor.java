package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;

/**
 * Functor representam estruturas que podem ser mapeadas
 *
 * @param <A>
 */
public interface Functor<A>
{
    public <B> Functor<B> map(final Function1<? super A, B> function);
}
