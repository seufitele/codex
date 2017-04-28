package com.github.detentor.codex.collections;

import java.util.Comparator;

import com.github.detentor.codex.function.Function1;

public class ClassTest<T, U, W>
{
	SharpCollection<T> sharpCollection;
	SharpCollection<T> originalCollection;
	SharpCollection<T> emptyCollection;
	Function1<? super T, Boolean> filterFunc;
	Function1<? super T, U> mapFunc;
	Function1<? super T, ? extends Iterable<W>> fmapFunc;
	Comparator<? super T> theComparator;
	T eleNotInCollection;
	T[] elems;

	public SharpCollection<T> getSharpCollection()
	{
		return sharpCollection;
	}

	public void setSharpCollection(SharpCollection<T> sharpCollection)
	{
		this.sharpCollection = sharpCollection;
	}

	public SharpCollection<T> getOriginalCollection()
	{
		return originalCollection;
	}

	public void setOriginalCollection(SharpCollection<T> originalCollection)
	{
		this.originalCollection = originalCollection;
	}

	public SharpCollection<T> getEmptyCollection()
	{
		return emptyCollection;
	}

	public void setEmptyCollection(SharpCollection<T> emptyCollection)
	{
		this.emptyCollection = emptyCollection;
	}

	public Function1<? super T, Boolean> getFilterFunc()
	{
		return filterFunc;
	}

	public void setFilterFunc(Function1<? super T, Boolean> filterFunc)
	{
		this.filterFunc = filterFunc;
	}

	public Function1<? super T, U> getMapFunc()
	{
		return mapFunc;
	}

	public void setMapFunc(Function1<? super T, U> mapFunc)
	{
		this.mapFunc = mapFunc;
	}

	public Function1<? super T, ? extends Iterable<W>> getFmapFunc()
	{
		return fmapFunc;
	}

	public void setFmapFunc(Function1<? super T, ? extends Iterable<W>> fmapFunc)
	{
		this.fmapFunc = fmapFunc;
	}

	public Comparator<? super T> getTheComparator()
	{
		return theComparator;
	}

	public void setTheComparator(Comparator<? super T> theComparator)
	{
		this.theComparator = theComparator;
	}

	public T getEleNotInCollection()
	{
		return eleNotInCollection;
	}

	public void setEleNotInCollection(T eleNotInCollection)
	{
		this.eleNotInCollection = eleNotInCollection;
	}

	public T[] getElems()
	{
		return elems;
	}

	public void setElems(T[] elems)
	{
		this.elems = elems;
	}
}