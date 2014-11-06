package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function2;

/**
 * Monóides são estruturas algébricas que comportam uma operação binária associativa e possuem um elemento identidade.
 * 
 * @param <A> O tipo de dados que a estrutura monoidal contém
 */
public interface Monoid<A>
{
	/**
	 * Retorna o valor identidade deste monóide
	 * @return O valor identidade do monóide
	 */
	A empty();
	
	/**
	 * Uma função binária associativa que transforma dois elementos em um. <br/>
	 * 
	 * @param func A função binária que transforma dois elementos em um
	 * @return Um valor A, a partir da aplicação da função
	 */
	A reduce(final Function2<A, A, A> func);
}
