package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function2;

/**
 * Monóides são estruturas que comportam uma operação binária associativa e 
 * possuem um elemento identidade. <br/><br/>
 * 
 * <pre>
 * Ex: Números naturais, com o 0 sendo a identidade a operação de (+) 
 *     Números naturais, com o 1 sendo a identidade e a operação de (*) 
 *     Strings, com a string vazia sendo a identidade e operação de concatenação 
 *     Listas, com a lista vazia sendo a identidade e a operação de concatenação
 *     </pre>
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
	 * Uma função binária associativa que transforma dois elementos em um. <br/><br/>
	 * 
	 * @param param1 O primeiro elemento a ser combinado
	 * @param param2 O segundo elemento a ser combinado
	 * @return Um valor A, a partir da redução dos dois elementos em um só
	 */
	A reduce(final A param1, final A param2);
	
	/**
	 * Retorna esta monóide como uma {@link Function2}
	 * @return Uma {@link Function2} que representa esta monóide 
	 */
	default Function2<A, A, A> asFunction()
	{
		return (param1, param2) -> reduce(param1, param2);
	}
}
