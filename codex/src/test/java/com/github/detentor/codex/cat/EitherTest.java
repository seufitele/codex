package com.github.detentor.codex.cat;

import com.github.detentor.codex.cat.Monad;
import com.github.detentor.codex.cat.monads.Either;
import com.github.detentor.codex.function.Function1;

/**
 * Testes para a classe de Either
 */
public class EitherTest
{
	private EitherTest()
	{
		//
	}

	/**
	 * Testa a option para os tipos: Functor, Applicative e Monad
	 */
	public static void testEither()
	{
		testEitherFunctor();
		testEitherApplicative();
		testEitherMonad();
	}
	
	private static final Function1<Integer, Integer> func1 = new Function1<Integer, Integer>()
	{
		@Override
		public Integer apply(final Integer param)
		{
			return param + param;
		}
	};
	
	private static final Function1<Integer, String> func2 = new Function1<Integer, String>()
	{
		@Override
		public String apply(final Integer param)
		{
			return param.toString();
		}
	};

	public static void testEitherFunctor()
	{
		CatTest.testFunctor(Either.createRight(5), func1, func2);
		CatTest.testFunctor(Either.<Integer, Integer>createLeft(5), func1, func2);
	}
	
	public static void testEitherApplicative()
	{
		final Either<Integer, Integer> rightEither = Either.createRight(8);
		final Either<Integer, Integer> leftEither = Either.createLeft(8);
		final Either<Integer, Function1<Integer, Integer>> emptyFunction1 = Either.<Integer, Function1<Integer, Integer>>createLeft(8);
		final Either<Integer, Function1<Integer, Integer>> emptyFunction2 = Either.<Integer, Function1<Integer, Integer>>createLeft(8);
		
		final Either<Integer, Function1<Integer, Integer>> eitherFunc1 = Either.<Integer, Function1<Integer, Integer>>createRight(func1);
		final Either<Integer, Function1<Integer, String>> eitherFunc2 = Either.<Integer, Function1<Integer, String>>createRight(func2);
		
		CatTest.testApplicative(rightEither, 10, eitherFunc1, eitherFunc2, func1);
		CatTest.testApplicative(rightEither, 10, emptyFunction1, eitherFunc2, func1);
		CatTest.testApplicative(rightEither, 10, eitherFunc1, emptyFunction2, func1);
		CatTest.testApplicative(rightEither, 10, emptyFunction1, emptyFunction2, func1);
		
		
		CatTest.testApplicative(leftEither, 10, eitherFunc1, eitherFunc2, func1);
		CatTest.testApplicative(leftEither, 10, emptyFunction1, eitherFunc2, func1);
		CatTest.testApplicative(leftEither, 10, eitherFunc1, emptyFunction2, func1);
		CatTest.testApplicative(leftEither, 10, emptyFunction1, emptyFunction2, func1);
		
	}
	
	public static void testEitherMonad()
	{
		final Either<Integer, Integer> either = Either.createRight(5);

		final Function1<Integer, Monad<Integer>> bindF = new Function1<Integer, Monad<Integer>>()
		{
			@Override
			public Monad<Integer> apply(final Integer param)
			{
				return Either.createRight(5);
			}
		};

		final Function1<Integer, Monad<Integer>> bindFEmpty = new Function1<Integer, Monad<Integer>>()
		{
			@Override
			public Monad<Integer> apply(final Integer param)
			{
				return Either.createLeft(5);
			}
		};

		final Function1<Integer, Monad<String>> bindF2 = new Function1<Integer, Monad<String>>()
		{
			@Override
			public Monad<String> apply(final Integer param)
			{
				return Either.createRight(param.toString());
			}
		};

		final Function1<Integer, Monad<String>> bindF2Empty = new Function1<Integer, Monad<String>>()
		{
			@Override
			public Monad<String> apply(final Integer param)
			{
				return Either.createRight(param.toString());
			}
		};

		CatTest.testMonad(either, Integer.valueOf(10), bindF, bindF2, func2);
		CatTest.testMonad(Either.<Integer, Integer>createLeft(5), Integer.valueOf(10), bindF, bindF2, func2);

		CatTest.testMonad(either, Integer.valueOf(10), bindFEmpty, bindF2, func2);
		CatTest.testMonad(either, Integer.valueOf(10), bindF, bindF2Empty, func2);
		CatTest.testMonad(either, Integer.valueOf(10), bindFEmpty, bindF2Empty, func2);

		CatTest.testMonad(Either.<Integer, Integer>createLeft(5), Integer.valueOf(10), bindFEmpty, bindF2, func2);
		CatTest.testMonad(Either.<Integer, Integer>createLeft(5), Integer.valueOf(10), bindF, bindF2Empty, func2);
		CatTest.testMonad(Either.<Integer, Integer>createLeft(5), Integer.valueOf(10), bindFEmpty, bindF2Empty, func2);
	}

}
