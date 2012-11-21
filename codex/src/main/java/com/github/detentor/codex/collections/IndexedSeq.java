package com.github.detentor.codex.collections;

import com.github.detentor.codex.function.PartialFunction;

/**
 * Sequências indexadas são sequências que garantem tempo constante no acesso 
 * a elementos em posição aleatória ou ao seu tamanho. <br/><br/>
 * 
 * Além disso, possuem capacidade de extrair rapidamente sub-sequências (tempo, no máximo, proporcional ao tamanho
 * da sub-sequência).
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public interface IndexedSeq<A> extends PartialFunction<Integer, A>
{
	/**
	 * Retorna o número de elementos desta coleção.
     *
     * @return O número de elementos desta coleção
     */
    int size();
    
    /**
     * Retorna uma sub-sequência desta sequência, a partir 
     * @param startIndex
     * @param endIndex
     * @return
     */
//    IndexedSeq<A> subsequence(int startIndex, int endIndex);
	

}
