package com.github.detentor.codex.cat;

import com.github.detentor.codex.function.Function1;

public class Either<Left, Right> implements Monad<Right>
{
	
	public static void main(String[] args) {
		Either<Double, Integer> e = new Either<>();
		Either<Double, Integer> val2 = e.fmap(Object::toString).fmap(String::length);
	}


	@Override
	public <B> Either<Left, B> fmap(Function1<Right, B> func) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public <U, V extends Monad<? extends Monad<U>>> Either<Left, U> join(V monad) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public <B, C extends Monad<B>> Monad<B> bind(Function1<Right, C> func) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <U> Monad<U> pure(U value) {
		// TODO Auto-generated method stub
		return null;
	}

}
