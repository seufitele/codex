//package com.github.detentor.codex.cat;
//
//import com.github.detentor.codex.function.Function1;
//import com.github.detentor.codex.product.Tuple2;
//
//public interface State<S, A> extends Monad<A>, Function1<S, Tuple2<A, S>>
//{
////
////	@Override
////	public <B> State<S, B> fmap(Function1<A, B> app) 
////	{
////		// TODO Auto-generated method stub
////		return null;
////	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public default <B, C extends Monad<B>> State<S, B> bind(Function1<A, C> func) 
//	{
//		return state -> 
//		{
//			final Tuple2<A, S> temp = this.apply(state);
//			return ((State<S, B>)func.apply(temp.getVal1())).apply(temp.getVal2());
//		};
//	}
//
//	@Override
//	public default <U> State<S, U> pure(U value) 
//	{
//		return state -> Tuple2.from(value, state);
//	}
//}
