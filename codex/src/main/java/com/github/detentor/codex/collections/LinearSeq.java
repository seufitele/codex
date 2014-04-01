package com.github.detentor.codex.collections;

import java.util.Iterator;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.PartialFunction1;


/**
 * Sequências Lineares são sequências que garantem tempo constante para os métodos
 * {@link #head()} , {@link #tail()} , {@link #isEmpty()}, mas não para elementos em posições
 * arbitrárias.<br/><br/>
 * 
 * Classe que provê a implementação padrão de métodos de coleções lineares, para simplificar a criação de classes que os estenda. <br/>
 * <br/>
 * 
 * Para criar uma coleção (imutável) com base nesta implementação, basta prover o código dos seguintes métodos: <br/>
 * <br/>
 * 
 * {@link #head()}, {@link #tail()}, {@link #isEmpty()}, {@link SharpCollection#builder() builder()} <br/>
 * <br/>
 * 
 * O método {@link #size()} tem implementação baseada no iterator (complexidade O(n)). <br/>
 * <br/>
 * 
 * O {@link #equals(Object)} e o {@link #hashCode()} são padrão para todas as sequências lineares. <br/><br/>
 * 
 * Para coleções mutáveis, veja {@link AbstractMutableLinearSeq}. <br/>
 * <br/>
 * 
 * NOTA: Subclasses devem sempre dar override nos métodos {@link #map(Function1) map}, {@link #collect(PartialFunction1) collect},
 * {@link #flatMap(Function1) flatMap} e {@link #zipWithIndex() zipWithIndex}.  
 * Devido à incompetência do Java com relação a Generics, isso é necessário para assegurar que o
 * tipo de retorno seja o mesmo da coleção. A implementação padrão (chamado o método da super classe é suficiente).
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public interface LinearSeq<T> extends Seq<T>
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
    LinearSeq<T> tail();
    
    /**
     * {@inheritDoc}
     * <br/>Esta operação tem garantia de ser executada em tempo constante.
     */
    T head();

	@Override
	default T apply(final Integer param)
	{
		return param.equals(0) ? this.head() : this.tail().apply(param - 1);
	}
	
	@Override
	default int size()
	{
		int theSize = 0;
		LinearSeq<T> curEle = this;
		
		while (curEle.notEmpty())
		{
			curEle = curEle.tail();
			theSize++;
		}
		return theSize;
	}
    
	default Iterator<T> iterator()
	{
		@SuppressWarnings("unchecked")
		final LinearSeq<T>[] curEle = new LinearSeq[1];
		curEle[0] = this;

		return new Iterator<T>()
		{
			@Override
			public boolean hasNext()
			{
				return !curEle[0].isEmpty();
			}

			@Override
			public T next()
			{
				final T toReturn = curEle[0].head();
				curEle[0] = curEle[0].tail();
				return toReturn;
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException("Operação não suportada");
			}
		};
	}
	
}
