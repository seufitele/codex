package com.github.detentor.codex.cat.Functors;

import com.github.detentor.codex.cat.Functor;
import com.github.detentor.codex.function.Function1;

/**
 * Um Bifunctor é um functor onde o primeiro e o segundo argumentos são covariantes.
 */
public interface Bifunctor<A, B> extends Functor<B>
{
	/**
	 * Constrói um novo functor a partir da aplicação da função passada como parâmetro em cada elemento deste functor. <br/>
	 * A ordem é preservada, se ela estiver bem-definida.
	 * @param <B> O tipo da novo functor.
	 * @param function Uma função que recebe um elemento deste functor, e retorna um elemento de (potencialmente) outro tipo.
	 * @return Uma novo functor, a partir da aplicação da função para cada elemento.
	 */
    public <C, D> Bifunctor<C, D> bimap(final Function1<? super A, C> function1, final Function1<? super B, D> function2);

    public <C> Bifunctor<C, B> mapFirst(final Function1<? super A, C> function);
    
    public <C> Bifunctor<A, C> map(final Function1<? super B, C> function);
}
