package com.github.detentor.codex.alg;

/**
 * Monóides são estruturas algébricas que comportam uma operação binária associativa e possuem um elemento identidade.
 * 
 * @param <A> O tipo de dados que a estrutura monoidal contém
 */
public interface Monoid<A> extends Semigroup<A>
{
	/**
	 * Retorna o valor identidade deste monóide
	 * @return O valor identidade do monóide
	 */
    A empty();
    
    
}
