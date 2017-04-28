package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;

/**
 * Functores representam estruturas que podem ser mapeadas. </br>
 * Além disso, esse mapeamento garante que a estrutura não será modificada, ou seja, se o Functor seguir
 * as leis de funtores, o tipo da estrutura não vai ser modificado.
 *
 * @param <A> O tipo de dados que o functor contém
 */
public interface Functor<A>
{
	/**
	 * Constrói um novo functor a partir da aplicação da função passada como parâmetro em cada elemento deste functor. <br/>
	 * A ordem é preservada, se ela estiver bem-definida.
	 * @param <B> O tipo da novo functor.
	 * @param function Uma função que recebe um elemento deste functor, e retorna um elemento de (potencialmente) outro tipo.
	 * @return Uma novo functor, a partir da aplicação da função para cada elemento.
	 */
    public <B> Functor<B> map(final Function1<? super A, B> function);
}
