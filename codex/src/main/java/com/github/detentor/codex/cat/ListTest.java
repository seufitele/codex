package com.github.detentor.codex.cat;

import java.util.Arrays;

import com.github.detentor.codex.collections.immutable.ListSharp;
import com.github.detentor.codex.function.Function1;

public class ListTest<T> extends ListSharp<T> implements Foldable<T>
{
	private static final long serialVersionUID = 1L;

	public ListTest(final T[] copyOf)
	{
		super(copyOf);
	}

	@SafeVarargs
	public static <T> ListTest<T> from(final T... valores)
	{
		return new ListTest<T>(Arrays.copyOf(valores, valores.length));
	}
	
	@Override
	public <B> B foldMap(Function1<? super T, ? extends B> func, Monoid<B> monoid)
	{
		B accum = monoid.empty();

		for (final T ele : this)
		{
			accum = monoid.reduce(accum, func.apply(ele));
		}

		return accum;
	}
	
//	@Override
//	public <B> B foldRight(B startValue, Function2<? super T, B, B> function)
//	{
//		B curEle = startValue;
//				
//		for (T ele : this)
//		{
//			curEle = function.apply(ele, curEle);
//		}
//		
//		return curEle;
//	}

	public static void main(final String[] args)
	{
		System.out.println("foldMap: " + ListTest.from(1, 2, 3, 4, 5).foldMap(k -> k.toString() , new StringMon()));
		System.out.println("foldLeft: " + ListTest.from(1, 2, 3, 4, 5).foldLeft("", (curEle, curStr) -> curStr + curEle));
		System.out.println("foldRight: " + ListTest.from(1, 2, 3, 4, 5).foldRight("", (curEle, curStr) -> curStr + curEle));
	}

	private static final class StringMon implements Monoid<String>
	{
		@Override
		public String empty()
		{
			return "";
		}

		@Override
		public String reduce(String param1, String param2)
		{
			return param1 + param2;
		}
		
	}
}
