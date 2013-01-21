package com.github.detentor.codex.collections;

import com.github.detentor.codex.function.PartialFunction;

/**
 * Sequências Lineares são sequências que garantem tempo constante para os métodos
 * {@link #head()} , {@link #tail()} , {@link #isEmpty()}, mas não para elementos em posições
 * arbitrárias.<br/><br/>
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public interface LinearSeq<A> extends PartialFunction<Integer, A>, SharpCollection<A>, Seq<A>
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
