package com.github.detentor.codex.collections;

import com.github.detentor.codex.function.PartialFunction;

/**
 * Sequências lineares representam sequências onde a ordem dos elementos retornados pelo 
 * iterator é estável (ex: Mapas e Sets não são sequências lineares, pois os itens 
 * retornados pelo iterador pode mudar). <br/><br/>
 * Além disso, sequências lineares garantem tempo constante para os métodos
 * {@link #head()} , {@link #tail()} , {@link #isEmpty()}.<br/><br/>
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public interface LinearSeq<A> extends PartialFunction<Integer, A>, SharpCollection<A>
{
	 /**
	 * {@inheritDoc}
	 * <br/>Esta operação tem garantia de ser executada em tempo constante.
	 */
    boolean isEmpty();

    /**
     * {@inheritDoc}
	 * <br/>Esta operação tem garantia de ser executada em tempo constante.
	 */
    IndexedSeq<A> tail();
    
    /**
     * {@inheritDoc}
     * <br/>Esta operação tem garantia de ser executada em tempo constante.
     */
    A head();
}
