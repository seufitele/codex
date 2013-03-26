//package com.github.detentor.codex.monads;
//
//import com.github.detentor.codex.function.Function0;
//import com.github.detentor.codex.function.Function1;
//
///**
// * Mônade cujos dados são calculados de maneira 'lazy'. 
// * 
// * @author Vinicius Seufitele Pinto
// *
// * @param <A>
// */
//public class LazyMonad<A> implements Monad<A>
//{
//	private final Function0<A> theValue;
//
//	protected LazyMonad(final Function0<A> valueFunction)
//	{
//		theValue = valueFunction;
//	}
//
//	public static <C, D extends Function0<C>> LazyMonad<C> from(final D valueFunction)
//	{
//		return new LazyMonad<C>(valueFunction);
//	}
//
//	@Override
//	public Monad<A> unit(final A theValue)
//	{
//		return null;
//	}
//
//	@Override
//	public <C> LazyMonad<C> bind(final Function1<A, C> mapFunction)
//	{
//		return LazyMonad.from(new Function0<C>()
//		{
//			@Override
//			public C apply()
//			{
//				return mapFunction.apply(LazyMonad.this.theValue.apply());
//			}
//		});
//	}
//
//	@Override
//	public String toString()
//	{
//		return theValue.apply().toString();
//	}
//
//}
