package com.github.detentor.codex.collections;

import com.github.detentor.codex.cat.Monad;
import com.github.detentor.codex.cat.monads.State;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.product.Tuple2;

/**
 * 
 * @author vinicius
 *
 */
public class StateTest
{
	private StateTest()
	{
		//
	}

	/**
	 * Testa a option para os tipos: Functor, Applicative e Monad
	 */
	public static void testState()
	{
		testStateFunctor();
		testStateApplicative();
		testStateMonad();
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
	
	private static State<Object, Integer> initialState = new State<Object, Integer>()
	{
		@Override
		public Tuple2<Integer, Object> apply(Object param)
		{
			return Tuple2.from(5, param);
		}
	};

	public static void testStateFunctor()
	{
		CatTest.testFunctor(initialState, func1, func2);
	}
	
	public static void testStateApplicative()
	{
		State<Object, Function1<Integer, Integer>> stateFunc1 = new State<Object, Function1<Integer, Integer>>()
		{
			@Override
			public Tuple2<Function1<Integer, Integer>, Object> apply(Object param)
			{
				return Tuple2.from(func1, param);
			}
		};
		
		State<Object, Function1<Integer, String>> stateFunc2 = new State<Object, Function1<Integer, String>>()
		{
			@Override
			public Tuple2<Function1<Integer, String>, Object> apply(Object param)
			{
				return Tuple2.from(func2, param);
			}
		};

		CatTest.testApplicative(initialState, 10, stateFunc1, stateFunc2, func1);
	}
	
	public static void testStateMonad()
	{
		final Function1<Integer, Monad<Integer>> bindF1 = new Function1<Integer, Monad<Integer>>()
		{
			@Override
			public Monad<Integer> apply(final Integer param1)
			{
				return new State<Object, Integer>()
				{
					@Override
					public Tuple2<Integer, Object> apply(Object param2)
					{
						return Tuple2.from(param1, param2);
					}
				};
			}
		};

		final Function1<Integer, Monad<String>> bindF2 = new Function1<Integer, Monad<String>>()
		{
			@Override
			public Monad<String> apply(final Integer param1)
			{
				return new State<Object, String>()
				{
					@Override
					public Tuple2<String, Object> apply(Object param2)
					{
						return Tuple2.from(param1.toString(), param2);
					}
				};
			}
		};

		CatTest.testMonad(initialState, 10, bindF1, bindF2, func2);
	}
}
