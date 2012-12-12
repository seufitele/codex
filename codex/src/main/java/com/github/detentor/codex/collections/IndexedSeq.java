package com.github.detentor.codex.collections;

import com.github.detentor.codex.function.PartialFunction;

/**
 * Sequências indexadas são sequências que garantem tempo constante no acesso 
 * a elementos em posição aleatória e ao seu tamanho. <br/><br/>
 * 
 * Além disso, possuem capacidade de extrair rapidamente sub-sequências (tempo, no máximo, proporcional ao tamanho
 * da sub-sequência).
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public interface IndexedSeq<A> extends PartialFunction<Integer, A>, SharpCollection<A>
{
	/**
	 * Retorna o elemento vinculado à posição especificada
	 */
	A apply(Integer pos);

	/**
	 * Retorna o número de elementos desta coleção. <br/>
	 * Esta operação tem garantia de ser executada em tempo constante.
     * @return O número de elementos desta coleção
     */
    int size();

    /**
     * Retorna uma sub-sequência desta sequência, a partir do índice de início (inclusive) 
     * e fim (exclusive) passados como parâmetro
     * @param startIndex O índice onde a sub-sequência será iniciada
     * @param endIndex O índice onde a sub-sequência terminará (exclusive)
     * @return A sub-sequência desta sequência que representa o intervalo determinado
     */
    IndexedSeq<A> subsequence(int startIndex, int endIndex);


}
