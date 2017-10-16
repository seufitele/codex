package com.github.detentor.codex.function.arrow;

import com.github.detentor.codex.cat.Applicative;
import com.github.detentor.codex.cat.Monad;
import com.github.detentor.codex.cat.functors.Profunctor;
import com.github.detentor.codex.function.Function1;

/**
 * Setas são funções mais poderosas.
 * 
 * @param <A>
 * @param <B>
 */
public abstract class Arrow1<A, B> implements Function1<A, B>, Arrow, Profunctor<A, B>, Monad<B>
{
	@Override
	public int getArity()
	{
		return 1;
	}

	/**
	 * Faz a composição desta seta com a seta passada como parâmetro. <br/>
	 * Formalmente, seja
	 * 
	 * <pre>
	 * f: A -> B e g: B -> C. Então, será retornado um h: A -> C.
	 * </pre>
	 * 
	 * @param <A> O tipo de dados de entrada da primeira seta
	 * @param <B> O tipo de dados de retorno da primeira seta, e de entrada da segunda (contravariante)
	 * @param <C> O tipo de dados de retorno da segunda seta
	 * @param func Uma função <b>g: B -> C</b>, a ser feita a composição.
	 * @return Uma seta <b>h: A -> C</b>, que representa a composição das duas setas.
	 */
	public <C> Arrow1<A, C> and(final Function1<? super B, C> func)
	{
		return new Arrow1<A, C>()
		{
			@Override
			public C apply(final A param)
			{
				return func.apply(Arrow1.this.apply(param));
			}
		};
	}

	/**
	 * Retorna uma seta de aridade 0, a partir da aplicação do primeiro parâmetro. <br/>
	 * Essa técnica é também conhecida como Currying.
	 * 
	 * @param param1 O parâmetro a ser aplicado parcialmente nesta seta
	 * @return Uma seta de aridade 0, que representa esta seta com o parâmetro passado fixado
	 */
	public Arrow0<B> applyPartial(final A param1)
	{
		return new Arrow0<B>()
		{
			@Override
			public B apply()
			{
				return Arrow1.this.apply(param1);
			}
		};
	}

	@Override
	public <C> Arrow1<A, C> map(final Function1<? super B, C> function)
	{
		return new Arrow1<A, C>()
		{
			@Override
			public C apply(final A param)
			{
				return function.apply(Arrow1.this.apply(param));
			}
		};
	}

	@Override
	public <C> Arrow1<C, B> contramap(final Function1<? super C, A> function)
	{
		return new Arrow1<C, B>()
		{
			@Override
			public B apply(final C param)
			{
				return Arrow1.this.apply(function.apply(param));
			}
		};
	}

	@Override
	public <C, D> Arrow1<C, D> dimap(final Function1<? super C, A> function1, final Function1<? super B, D> function2)
	{
		return this.contramap(function1).map(function2);
	}

	@Override
	public <U> Arrow1<A, U> pure(final U value)
	{
		return new Arrow1<A, U>()
		{
			@Override
			public U apply(final A param)
			{
				return value;
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C> Arrow1<A, C> ap(final Applicative<Function1<B, C>> applicative)
	{
		final Arrow1<A, Function1<B, C>> apval = (Arrow1<A, Function1<B, C>>) applicative;

		// (<*>) f g x = f x (g x)
		return new Arrow1<A, C>()
		{
			@Override
			public C apply(final A param)
			{
				return apval.apply(param).apply(Arrow1.this.apply(param));
			}
		};
	}

	@Override
	public <U> Arrow1<A, U> bind(final Function1<? super B, Monad<U>> function)
	{
		return new Arrow1<A, U>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public U apply(final A param)
			{
				final Arrow1<A, U> res = (Arrow1<A, U>) function.apply(Arrow1.this.apply(param));
				return res.apply(param);
			}
		};
	}
}
