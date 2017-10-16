package com.github.detentor.codex.cat.functors;

import com.github.detentor.codex.function.Function1;

/**
 * Representa um Functor contravariante, ou seja, um Functor que 
 * trabalha da direção inversa de um Functor.
 */
public interface Contrafunctor<B>
{
	/**
	 * Contrói um novo functor contravariante, a partir da função passada como parâmetro. <br/>
	 * Note a diferença da direção da seta. <br/> 
	 * A ordem é preservada, se ela estiver bem-definida.
	 * @param <A> O tipo do novo functor.
	 * @param function Uma função que recebe um tipo de elemento, e retorna o tipo de elemento deste functor.
	 * @return Um novo functor contravariante, a partir da aplicação da função para cada elemento.
	 */
    public <A> Contrafunctor<A> contramap(final Function1<? super A, B> function);
}
