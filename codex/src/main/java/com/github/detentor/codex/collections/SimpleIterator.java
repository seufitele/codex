package com.github.detentor.codex.collections;

import java.util.Iterator;

/**
 * Essa classe é a base das coleções do codex. Ela representa um iterador simples (mais simples que o do Java). <br/>
 * Subclasses devem apenas sobrescrever os métodos {@link #hasNext()} e {@link #next()}. <br/>
 * Chamar o método {@link #remove()} irá disparar uma {@link UnsupportedOperationException}. <br/><br/>
 * O objetivo é simplificar a criação do {@link Iterator}, visto que essa é a classe ponte entre o codex e as coleções
 * do Java.
 * 
 * @author Vinícius Seufitele Pinto
 *
 * @param <E>
 */
public abstract class SimpleIterator<E> implements Iterator<E>
{
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("remove not supported by this iterator");
	}
}
