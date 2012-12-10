package com.github.detentor.codex.collections;

import java.util.Iterator;

import com.github.detentor.codex.function.PartialFunction;

/**
 * Sequências lineares representam sequências onde, assim como {@link Iterator iterators}, 
 * trata-se um elemento de cada vez.<br/><br/>
 * 
 * Sequências lineares garantem tempo constante para os métodos
 * {@link #head()} , {@link #tail()} , {@link #isEmpty()}.
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
