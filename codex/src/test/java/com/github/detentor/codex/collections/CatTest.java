package com.github.detentor.codex.collections;

import static org.junit.Assert.assertTrue;

import com.github.detentor.codex.cat.Applicative;
import com.github.detentor.codex.cat.Functor;
import com.github.detentor.codex.cat.Monad;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Function2;
import com.github.detentor.operations.ObjectOps;

/**
 * Verificação das classes que pertencem ao pacote das categorias.
 * Basicamente o que testado é se as leis dos functores são satisfeitas.
 *
 */
public class CatTest
{
	private CatTest()
	{

	}
	
	/**
	 * Faz a verificação das leis do functor
	 * @param functor O functor a ser verificado
	 * @param func1 Uma função que transforma um valor do tipo <A> em um valor do tipo <B>
	 * @param func2 Uma função que transforma um valor do tipo <B> em um valor do tipo <C>
	 */
	public static <A, B, C> void testFunctor(final Functor<A> functor, final Function1<A, B> func1, final Function1<B, C> func2)
	{
		final Function1<A, C> compose = func1.compose(func2);
		//1 - fmap id  ==  id 
		//2 - fmap (f . g)  ==  fmap f . fmap g
		
		assertTrue(functor.map(ObjectOps.identity).equals(ObjectOps.identity.apply(functor)));
		assertTrue(functor.map(compose).equals(functor.map(func1).map(func2)));
	}
	
	public static <A, B, C, D> void testApplicative(final Applicative<A> appl, final A value, 
			final Applicative<Function1<A, B>> u, 
			final Applicative<Function1<B, C>> v,
			final Function1<A, B> func1)
	{
		//1 - pure id <*> v = v
		//2 - pure (.) <*> u <*> v <*> w = u <*> (v <*> w)
		//3 - pure f <*> pure x = pure (f x)
		//4 - u <*> pure y = pure ($ y) <*> u
		
		Function1<Function1<A, B>, Function1<Function1<B, C>, Function1<A, C>>> compose = new Function1<Function1<A,B>, Function1<Function1<B,C>,Function1<A,C>>>()
		{
			@Override
			public Function1<Function1<B, C>, Function1<A, C>> apply(final Function1<A, B> param1)
			{
				return new Function1<Function1<B,C>, Function1<A,C>>()
				{
					@Override
					public Function1<A, C> apply(final Function1<B, C> param2)
					{
						return new Function1<A, C>()
						{
							@Override
							public C apply(A param3)
							{
								return param2.apply(param1.apply(param3));
							}
						};
					}
				};
			}
		};
		
		Function2<A, Function1<A, B>, B> appFunc = new Function2<A, Function1<A,B>, B>()
		{
			@Override
			public B apply(A param1, Function1<A, B> param2)
			{
				return param2.apply(param1);
			}
		};
		
		assertTrue(appl.ap(appl.pure((Function1<A, A>)ObjectOps.<A, A>identity())).equals(appl));
		assertTrue(appl.ap(v.ap(u.ap(appl.pure(compose)))).equals(appl.ap(u).ap(v)));
		assertTrue(appl.pure(value).ap(appl.pure(func1)).equals(appl.pure(func1.apply(value))));
		assertTrue(appl.pure(value).ap(u).equals(u.ap(appl.pure((Function1<Function1<A, B>, B>)appFunc.applyPartial(value)))));
	}
	
	/**
	 * Faz a verificação das leis das monads.
	 * @param monad A monad a ser verificada
	 * @param value Um valor do tipo <A>
	 * @param bind1 Uma função que transforma um valor do tipo <A> em uma Monad do tipo <B>
	 * @param bind2 Uma função que transforma um valor do tipo <B> em uma Monad do tipo <C>
	 * @param func1 Uma função que transforma valores do tipo <B> em valores do tipo <C>
	 */
	public static <A, B, C> void testMonad(final Monad<A> monad, final A value,
			final Function1<A, Monad<B>> bind1, final Function1<B, Monad<C>> bind2, final Function1<B, C> func1)
	{
		//1 - return a >>= k  =  k a
		//2 - m >>= return  =  m
		//3 - m >>= (x -> k x >>= h)  =  (m >>= k) >>= h
		
		Function2<Monad<A>, A, Monad<A>> pureF = new Function2<Monad<A>, A, Monad<A>>()
		{
			@Override
			public Monad<A> apply(Monad<A> param1, A param2)
			{
				return param1.pure(param2);
			}
		};
		
		Function1<A, Monad<C>> comb = new Function1<A, Monad<C>>()
		{
			@Override
			public Monad<C> apply(A param)
			{
				return bind1.apply(param).bind(bind2);
			}
		};
		
		assertTrue(monad.bind(pureF.applyPartial(monad)).equals(monad));
		assertTrue(monad.pure(value).bind(bind1).equals(bind1.apply(value)));
		assertTrue(monad.bind(comb).equals(monad.bind(bind1).bind(bind2)));
	}

}