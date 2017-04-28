package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.operations.ObjectOps;

/**
 * Interface de estruturas que podem ser percorridas da esquerda para a direita, executando uma ação sobre cada elemento. <br/><br/>
 * Implementação mínima: {@link #traverse(Function1) traverse} ou {@link #sequence(Traversable) sequence}.
 * 
 * @param <A> O tipo de elemento contido na estrutura
 */
public interface Traversable<A> extends Functor<A>, Foldable<A>
{
	// Definição mínima: traverse | sequenceA

	// traverse :: Applicative f => (a -> f b) -> t a -> f (t b)
	// sequenceA :: Applicative f => t (f a) -> f (t a)

	/**
	 * Mapeia cada elemento desta estrutura para uma ação, executa cada uma destas ações da esquerda para a direita, e coleta os resultados.
	 * 
	 * @param function
	 * @return
	 */
	default public <B> Applicative<Traversable<B>> traverse(final Function1<? super A, Applicative<B>> function)
	{
		return sequence(map(function));
	}

	/**
	 * Executa cada ação na estrutura da esquerda para a direita, coletando os resultados
	 * 
	 * @param traversable
	 * @return
	 */
	default public <C> Applicative<Traversable<C>> sequence(final Traversable<Applicative<C>> traversable)
	{
		return traverse(ObjectOps.identity());
	}

	@Override
	<B> Traversable<B> map(Function1<? super A, B> function);

}
