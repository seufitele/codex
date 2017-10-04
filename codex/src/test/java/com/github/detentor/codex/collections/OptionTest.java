package com.github.detentor.codex.collections;

import com.github.detentor.codex.cat.Monad;
import com.github.detentor.codex.cat.monads.Option;
import com.github.detentor.codex.function.Function1;

public class OptionTest
{
	private OptionTest()
	{
		//
	}

	/**
	 * Testa a option para os tipos: Functor, Applicative e Monad
	 */
	public static void testOption()
	{
		testOptionMonad();
		testOptionFunctor();
		testOptionApplicative();
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

	public static void testOptionFunctor()
	{
		CatTest.testFunctor(Option.from(5), func1, func2);
		CatTest.testFunctor(Option.<Integer> empty(), func1, func2);
	}
	
	public static void testOptionApplicative()
	{
		final Option<Integer> emptyOption = Option.<Integer>empty();
		final Option<Function1<Integer, Integer>> emptyFunction1 = Option.<Function1<Integer, Integer>>empty();
		final Option<Function1<Integer, String>> emptyFunction2 = Option.<Function1<Integer, String>>empty();
		
		CatTest.testApplicative(Option.from(5), 10, Option.from(func1), Option.from(func2), func1);
		CatTest.testApplicative(Option.from(5), 10, emptyFunction1, Option.from(func2), func1);
		CatTest.testApplicative(Option.from(5), 10, Option.from(func1), emptyFunction2, func1);
		CatTest.testApplicative(Option.from(5), 10, emptyFunction1, emptyFunction2, func1);
		
		
		CatTest.testApplicative(emptyOption, 10, Option.from(func1), Option.from(func2), func1);
		CatTest.testApplicative(emptyOption, 10, emptyFunction1, Option.from(func2), func1);
		CatTest.testApplicative(emptyOption, 10, Option.from(func1), emptyFunction2, func1);
		CatTest.testApplicative(emptyOption, 10, emptyFunction1, emptyFunction2, func1);
	}
	
	public static void testOptionMonad()
	{
		final Option<Integer> option = Option.from(5);

		final Function1<Integer, Monad<Integer>> bindF = new Function1<Integer, Monad<Integer>>()
		{
			@Override
			public Monad<Integer> apply(final Integer param)
			{
				return Option.from(param);
			}
		};

		final Function1<Integer, Monad<Integer>> bindFEmpty = new Function1<Integer, Monad<Integer>>()
		{
			@Override
			public Monad<Integer> apply(final Integer param)
			{
				return Option.empty();
			}
		};

		final Function1<Integer, Monad<String>> bindF2 = new Function1<Integer, Monad<String>>()
		{
			@Override
			public Monad<String> apply(final Integer param)
			{
				return Option.from(param.toString());
			}
		};

		final Function1<Integer, Monad<String>> bindF2Empty = new Function1<Integer, Monad<String>>()
		{
			@Override
			public Monad<String> apply(final Integer param)
			{
				return Option.from(param.toString());
			}
		};

		CatTest.testMonad(option, Integer.valueOf(10), bindF, bindF2, func2);
		CatTest.testMonad(Option.<Integer> empty(), Integer.valueOf(10), bindF, bindF2, func2);

		CatTest.testMonad(option, Integer.valueOf(10), bindFEmpty, bindF2, func2);
		CatTest.testMonad(option, Integer.valueOf(10), bindF, bindF2Empty, func2);
		CatTest.testMonad(option, Integer.valueOf(10), bindFEmpty, bindF2Empty, func2);

		CatTest.testMonad(Option.<Integer> empty(), Integer.valueOf(10), bindFEmpty, bindF2, func2);
		CatTest.testMonad(Option.<Integer> empty(), Integer.valueOf(10), bindF, bindF2Empty, func2);
		CatTest.testMonad(Option.<Integer> empty(), Integer.valueOf(10), bindFEmpty, bindF2Empty, func2);
	}

}
