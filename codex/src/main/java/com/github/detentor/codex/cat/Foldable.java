package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Function2;
import com.github.detentor.codex.monoids.Endo;

/**
 * Foldable representa estruturas que podem ser 'dobradas' para se resumir em um valor. <br/><br/>
 * Implementação mínima: {@link #foldRight(Object, Function2) foldRight} ou {@link #foldMap(Function1, Monoid) foldMap}.
 * 
 * @param <A> O tipo de dados contido em Foldable
 */
public interface Foldable<A>
{
	// Definição mínima: foldr | foldMap

	// foldr :: (a -> b -> b) -> b -> t a -> b
	// foldMap :: Monoid m => (a -> m) -> t a -> m

	// foldr :: (a -> b -> b) -> b -> t a -> b
	// foldr f z t = appEndo (foldMap (Endo #. f) t) z
	/**
	 * Aplica uma transformação nos elementos desta coleção, a partir de um valor inicial e uma função associativa. <br/>
	 * A ordem que as operações são executadas é da direita para a esquerda. <br/>
	 * 
	 * @param startValue O valor inicial, a ser retornado caso esta coleção esteja vazia
	 * @param function Uma função de transformação associativa a ser aplicada nos elementos desta coleção
	 * @return Um elemento do tipo B, retornado a partir da transformação dos elementos desta coleção.
	 */
	default <B> B foldRight(final B startValue, final Function2<? super A, B, B> function)
	{
		final Function1<A, Function1<B, B>> f = param -> function.applyPartial(param);
		return foldMap(f, new Endo<B>()).apply(startValue);
	}

	/**
	 * Aplica uma transformação nos elementos desta coleção, a partir de um valor inicial e uma função associativa. <br/>
	 * A ordem que as operações são executadas é da esquerda para a direita. <br/>
	 * 
	 * @param startValue O valor inicial, a ser retornado caso esta coleção esteja vazia
	 * @param function Uma função de transformação associativa a ser aplicada nos elementos desta coleção
	 * @return Um elemento do tipo B, retornado a partir da transformação dos elementos desta coleção.
	 */
	default <B> B foldLeft(final B startValue, final Function2<? super A, B, B> function)
	{
		final Function1<A, Function1<B, B>> f = param -> function.applyPartial(param);
		return foldMap(f, new Endo<B>().getDual()).apply(startValue);
	}

	// foldMap f = foldr (mappend . f) mempty
	/**
	 * Transforma os elementos desta função a partir de uma função de mapeamento, e os combina utilizando uma monóide
	 * 
	 * @param func Uma função que transforma um elemento do tipo A para um elemento do tipo B
	 * @param monoid A monóide que será responsável por combinar os elementos
	 * 
	 * @return O elemento obtido a partir da transformação e combinação dos elementos desta estrutura
	 */
	default <B> B foldMap(final Function1<? super A, ? extends B> func, final Monoid<B> monoid)
	{
		return foldRight(monoid.empty(), (param1, param2) -> monoid.reduce(func.apply(param1), param2));
	}
	// return foldRight(null, (a, b) -> b.reduce(func.apply(a)));
}
