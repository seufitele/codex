package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function2;

/**
 * Um semigrupo é apenas uma estrutura que possui uma operação binária associativa. <br/>
 * Ela é mais geral que a {@link Monoid}, no sentido de que não precisa ter o elemento identidade.

 * @param <A> O tipo dos elementos do semigrupo
 */
public interface Semigroup<A>
{
    /**
	 * Uma função binária associativa que transforma dois elementos em um. <br/>
	 * 
	 * @param func A função binária que transforma dois elementos em um
	 * @return Um valor A, a partir da aplicação da função
	 */
	A reduce(final Function2<A, A, A> func);

}
