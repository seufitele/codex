package com.github.detentor.codex.cat.functors;

import com.github.detentor.codex.cat.Functor;
import com.github.detentor.codex.function.Function1;

/**
 * Um Profunctor é um Functor que é contravariante no primeiro argumento e covariante no segundo
 */
public interface Profunctor<A, B> extends Contrafunctor<A>, Functor<B>
{
	/**
	 * Constrói um novo functor a partir da aplicação da função passada como parâmetro em cada elemento deste functor. <br/>
	 * A ordem é preservada, se ela estiver bem-definida.
	 * @param <B> O tipo da novo functor.
	 * @param function Uma função que recebe um elemento deste functor, e retorna um elemento de (potencialmente) outro tipo.
	 * @return Uma novo functor, a partir da aplicação da função para cada elemento.
	 */
    public <C, D> Profunctor<C, D> dimap(final Function1<? super C, A> function1, final Function1<? super B, D> function2);
    
    public <C> Profunctor<C, B> contramap(final Function1<? super C, A> function);
    
    public <C> Profunctor<A, C> map(final Function1<? super B, C> function);
}
