package com.github.detentor.codex.cat;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Functions;
import com.github.detentor.codex.function.arrow.Arrow1;
import com.github.detentor.codex.function.arrow.Arrow2;
import com.github.detentor.operations.ObjectOps;

/**
 * Verificação das classes que pertencem ao pacote das categorias. <br/>
 * Possui verificação para Functores, Applicatives e Monads
 *
 */
public class CatTest
{
	@Test
	public void testCat()
	{
		OptionTest.testOption();
		EitherTest.testEither();
		StateTest.testState();
	}

	/**
	 * Faz a verificação das leis do functor
	 * 
	 * <pre>
	 * 1 - fmap id  ==  id 
	 * 2 - fmap (f . g)  ==  fmap f . fmap g
	 * </pre>
	 * 
	 * @param functor O functor a ser verificado
	 * @param func1 Uma função que transforma um valor do tipo <A> em um valor do tipo <B>
	 * @param func2 Uma função que transforma um valor do tipo <B> em um valor do tipo <C>
	 */
	public static <A, B, C> void testFunctor(final Functor<A> functor, final Function1<A, B> func1, final Function1<B, C> func2)
	{
		final Arrow1<A, C> compose = Functions.compose(func1, func2);
		assertTrue(functor.map(ObjectOps.identity).equals(ObjectOps.identity.apply(functor)));
		assertTrue(functor.map(compose).equals(functor.map(func1).map(func2)));
	}

	/**
	 * Faz a verificação das leis do applicative.
	 * 
	 * <pre>
	 *  1 - pure id <*> v = v
	 *  2 - pure (.) <*> u <*> v <*> w = u <*> (v <*> w)
	 *  3 - pure f <*> pure x = pure (f x)
	 *  4 - u <*> pure y = pure ($ y) <*> u
	 * </pre>
	 * 
	 * @param appl O applicative a ser verificado
	 * @param value Um valor do tipo A, para testar a propriedade 3 do Applicative
	 * @param u Applicative com uma função, para testar a associatividade
	 * @param v Applicative com uma função, para testar a associatividade
	 * @param func1 Uma função de A para B, para testar a propriedade 3
	 */
	public static <A, B, C, D> void testApplicative(final Applicative<A> appl, final A value, final Applicative<Function1<A, B>> u,
			final Applicative<Function1<B, C>> v, final Function1<A, B> func1)
	{

		final Function1<Function1<A, B>, Function1<Function1<B, C>, Function1<A, C>>> compose = new Function1<Function1<A, B>, Function1<Function1<B, C>, Function1<A, C>>>()
		{
			@Override
			public Function1<Function1<B, C>, Function1<A, C>> apply(final Function1<A, B> param1)
			{
				return new Function1<Function1<B, C>, Function1<A, C>>()
				{
					@Override
					public Function1<A, C> apply(final Function1<B, C> param2)
					{
						return new Function1<A, C>()
						{
							@Override
							public C apply(final A param3)
							{
								return param2.apply(param1.apply(param3));
							}
						};
					}
				};
			}
		};

		final Arrow2<A, Function1<A, B>, B> appFunc = new Arrow2<A, Function1<A, B>, B>()
		{
			@Override
			public B apply(final A param1, final Function1<A, B> param2)
			{
				return param2.apply(param1);
			}
		};

		assertTrue(appl.ap(appl.pure((Function1<A, A>) ObjectOps.<A, A> identity())).equals(appl));
		assertTrue(appl.ap(v.ap(u.ap(appl.pure(compose)))).equals(appl.ap(u).ap(v)));
		assertTrue(appl.pure(value).ap(appl.pure(func1)).equals(appl.pure(func1.apply(value))));
		assertTrue(appl.pure(value).ap(u).equals(u.ap(appl.pure((Function1<Function1<A, B>, B>) appFunc.applyPartial(value)))));
	}

	/**
	 * Faz a verificação das leis das monads
	 * 
	 * <pre>
	 * 1 - return a >>= k  =  k a
	 * 2 - m >>= return  =  m
	 * 3 - m >>= (x -> k x >>= h)  =  (m >>= k) >>= h
	 * </pre>
	 * 
	 * @param monad A monad a ser verificada
	 * @param value Um valor do tipo <A>
	 * @param bind1 Uma função que transforma um valor do tipo <A> em uma Monad do tipo <B>
	 * @param bind2 Uma função que transforma um valor do tipo <B> em uma Monad do tipo <C>
	 * @param func1 Uma função que transforma valores do tipo <B> em valores do tipo <C>
	 */
	public static <A, B, C> void testMonad(final Monad<A> monad, final A value, final Function1<A, Monad<B>> bind1,
			final Function1<B, Monad<C>> bind2, final Function1<B, C> func1)
	{
		final Arrow2<Monad<A>, A, Monad<A>> pureF = new Arrow2<Monad<A>, A, Monad<A>>()
		{
			@Override
			public Monad<A> apply(final Monad<A> param1, final A param2)
			{
				return param1.pure(param2);
			}
		};

		final Arrow1<A, Monad<C>> comb = new Arrow1<A, Monad<C>>()
		{
			@Override
			public Monad<C> apply(final A param)
			{
				return bind1.apply(param).bind(bind2);
			}
		};

		assertTrue(monad.bind(pureF.applyPartial(monad)).equals(monad));
		assertTrue(monad.pure(value).bind(bind1).equals(bind1.apply(value)));
		assertTrue(monad.bind(comb).equals(monad.bind(bind1).bind(bind2)));
	}

}
