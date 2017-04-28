package com.github.detentor.codex.monads;

import com.github.detentor.codex.cat.Applicative;
import com.github.detentor.codex.cat.Monad;
import com.github.detentor.codex.function.Function1;

/**
 * Either é uma mônade que representa um dentre dois tipos possíveis (união disjunta). <br/>
 * Instâncias de Either são ou instâncias de {@link Left} ou instâncias de {@link Right}. <br/>
 * <br/>
 * Either representa uma alternativa a {@link Option}, onde a Option vazia seria representado por Left. Ao contrário de Option, entretanto,
 * Left pode possuir um valor útil (por exemplo, uma mensagem de erro). <br/>
 * <br/>
 * 
 * Convenção dita que Left é usado para os casos de falha (resultados não esperados), e Right para os de sucesso (resultados esperados).
 * <br/>
 * <br/>
 * 
 * @author Vinícius Seufitele Pinto
 *
 * @param <B> O tipo de dados esperado (valor correto)
 * @param <A> O tipo de dados não esperado (valor incorreto, de erro)
 */
public abstract class Either<B, A> implements Monad<A>
{
	/**
	 * Cria uma instância de Either que contém um valor em Right
	 */
	public static final <B, A> Either<B, A> createRight(final A theValue)
	{
		return new Right<B, A>(theValue);
	}

	/**
	 * Cria uma instância de Either que contém um valor em Left
	 */
	public static final <B, A> Either<B, A> createLeft(final B theValue)
	{
		return new Left<B, A>(theValue);
	}

	/**
	 * Retorna se esta mônade possui um valor em Right.
	 * 
	 * @return true se esta mônade possuir um valor em right
	 */
	public boolean isRight()
	{
		return this instanceof Right<?, ?>;
	}

	/**
	 * Retorna se esta mônade possui um valor em Left
	 * 
	 * @return true se esta mônade possuir um valor em left
	 */
	public boolean isLeft()
	{
		return !isRight();
	}

	/**
	 * Retorna o valor de Right, se ele existir
	 * 
	 * @throws UnsupportedOperationException Caso a instância seja de Left
	 * 
	 * @return O valor contido em Right
	 */
	public abstract A getRight();

	/**
	 * Retorna o valor de Left, se ele existir
	 * 
	 * @throws UnsupportedOperationException Caso a instância seja de Right
	 * 
	 * @return O valor contido em Left
	 */
	public abstract B getLeft();

	@Override
	public <C> Either<B, C> pure(final C value)
	{
		return new Right<B, C>(value);
	}

	/**
	 * Cria um novo Either com o valor de Right, se existir, mapeado.
	 * 
	 * @param <C> O novo tipo do
	 * @param mapFunction A função de mapeamento a ser utilizada
	 * @return Uma instância de Either com o valor em Right mapeado, se ele existir
	 */
	@Override
	public <C> Either<B, C> map(final Function1<? super A, C> mapFunction)
	{
		return isLeft() ? Either.<B, C> createLeft(getLeft()) : Either.<B, C> createRight(mapFunction.apply(this.getRight()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C> Either<B, C> ap(final Applicative<Function1<A, C>> applicative)
	{
		final Either<B, Function1<A, C>> eitherAp = (Either<B, Function1<A, C>>) applicative;

		if (this.isLeft())
		{
			return (Either<B, C>) this;
		}

		if (eitherAp.isLeft())
		{
			return (Either<B, C>) eitherAp;
		}

		return this.map(eitherAp.getRight());
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public <U> Either<B, U> bind(final Function1<? super A, ? extends Monad<U>> function)
	{
		if (this.isLeft())
		{
			return (Either<B, U>) this;
		}

		return (Either<B, U>) function.apply(this.getRight());
	}

	@Override
	public String toString()
	{
		return isRight() ? "Right: " + getRight() : "Left: " + getLeft();
	}

	/**
	 * Classe que representa o valor 'correto' contido em Either.
	 * 
	 * @param <B> O tipo de Left
	 * @param <A> O tipo de Right
	 */
	public static final class Right<B, A> extends Either<B, A>
	{
		private final A value;

		protected Right(final A theValue)
		{
			value = theValue;
		}

		@Override
		public A getRight()
		{
			return value;
		}

		@Override
		public B getLeft()
		{
			throw new UnsupportedOperationException("getLeft chamado para Right");
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + (value == null ? 0 : value.hashCode());
			return result;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public boolean equals(final Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (obj == null)
			{
				return false;
			}
			if (getClass() != obj.getClass())
			{
				return false;
			}
			final Right other = (Right) obj;
			if (value == null)
			{
				if (other.value != null)
				{
					return false;
				}
			}
			else if (!value.equals(other.value))
			{
				return false;
			}
			return true;
		}
	}

	/**
	 * Classe que representa o valor 'errado' contido em Either.
	 *
	 * @param <B> O tipo de Left
	 * @param <A> O tipo de Right
	 */
	public static final class Left<B, A> extends Either<B, A>
	{
		private final B value;

		protected Left(final B theValue)
		{
			value = theValue;
		}

		@Override
		public A getRight()
		{
			throw new UnsupportedOperationException("getRight chamado para Left");
		}

		@Override
		public B getLeft()
		{
			return value;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + (value == null ? 0 : value.hashCode());
			return result;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public boolean equals(final Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (obj == null)
			{
				return false;
			}
			if (getClass() != obj.getClass())
			{
				return false;
			}
			final Left other = (Left) obj;
			if (value == null)
			{
				if (other.value != null)
				{
					return false;
				}
			}
			else if (!value.equals(other.value))
			{
				return false;
			}
			return true;
		}
	}
}
