package com.github.detentor.codex.collections;

import java.util.Iterator;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Function2;
import com.github.detentor.codex.function.PartialFunction;
import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;

/**
 * Implementação 'Lazy' de uma sequência linear. A principal característica é que todas as funções de ordem superior
 * são 'Lazy', ou seja, retornam uma outra instância de sequência lazy <br/>. Além disso, todos os métodos são escritos
 * tendo por base o foldLeft, para facilitar a sua implementação lazy. <br/>
 * 
 * Como observação, o builder é uma operação de unfold.
 * 
 * @author Vinícius Seufitele Pinto
 * 
 */
public abstract class AbstractLazyLinearSeq<T, U extends LinearSeq<T>> extends AbstractSeq<T, LinearSeq<T>> implements LinearSeq<T>
{
	private static final String UNCHECKED = "unchecked";
	
	@Override
	public boolean isDefinedAt(final Integer forValue)
	{
		return forValue >= 0 && forValue < this.size();
	}

	@Override
	public T apply(final Integer param)
	{
		return param.equals(0) ? this.head() : this.tail().apply(param - 1);
	}

	@Override
	public T last()
	{
		ensureNotEmpty("last foi chamado para uma coleção vazia");
		return this.apply(this.size() - 1);
	}
	
	@Override
	public <B> B foldLeft(B startValue, Function2<B, ? super T, B> function)
	{
//		B accumulator = startValue;
//
//		for (final T ele : this)
//		{
//			accumulator = function.apply(accumulator, ele);
//		}
//		return accumulator;
		
		
		
		return super.foldLeft(startValue, function);
	}
	
	

	//Operações Lazy:
	//public Iterator<T> iterator();
	//Option<T> headOption();
	//Option<T> lastOption();
	//SharpCollection<T> tail();
	//SharpCollection<T> take(final Integer num);
	//SharpCollection<T> takeRight(final Integer num);
	//SharpCollection<T> drop(final Integer num);
	//SharpCollection<T> dropRight(final Integer num);
	//SharpCollection<Tuple2<T, Integer>> zipWithIndex();
	//SharpCollection<T> intersect(final SharpCollection<T> withCollection);
	//SharpCollection<T> distinct();
	//SharpCollection<T> dropWhile(final Function1<? super T, Boolean> pred);
	//SharpCollection<T> dropRightWhile(final Function1<? super T, Boolean> pred);
	//SharpCollection<T> takeWhile(final Function1<? super T, Boolean> pred);
	//SharpCollection<T> takeRightWhile(final Function1<? super T, Boolean> pred);
	//Option<T> find(final Function1<? super T, Boolean> pred);
	//SharpCollection<T> filter(final Function1<? super T, Boolean> pred);
	//Tuple2<? extends SharpCollection<T>, ? extends SharpCollection<T>> partition(final Function1<? super T, Boolean> pred);
	//<B> SharpCollection<B> map(final Function1<? super T, B> function);
	//<B> SharpCollection<B> collect(final PartialFunction<? super T, B> pFunction);
	//<B> SharpCollection<B> flatMap(final Function1<? super T, ? extends SharpCollection<B>> function);
	
	
	
	
	//<B> B foldLeft(final B startValue, final Function2<B, ? super T, B> function);
	//<B> Builder<B, SharpCollection<B>> builder();
	
	@Override
	public int size()
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

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> iterator()
	{
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
