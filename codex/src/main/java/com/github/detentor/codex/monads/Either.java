package com.github.detentor.codex.monads;

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
 * Convenção dita que Left é usado para os casos de falha (resultados não esperados), e Right para os de sucesso (resultados esperados). <br/>
 * <br/>
 * 
 * @author Vinícius Seufitele Pinto
 *
 * @param <B> O tipo de dados esperado (valor correto)
 * @param <A> O tipo de dados não esperado (valor incorreto, de erro)
 */
public abstract class Either<B, A> implements Monad< A>
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

    @Override
    public <C> Either<B, C> pure(C value)
    {
        return new Right<B, C>(value);
    }

    /**
     * Retorna o valor de Left, se ele existir
     * 
     * @throws UnsupportedOperationException Caso a instância seja de Right
     * 
     * @return O valor contido em Left
     */
    public abstract B getLeft();

    @Override
    public String toString()
    {
        return isRight() ? "Right: " + getRight() : "Left: " + getLeft();
    }

    public abstract <U> Either<B, U> bind(final Function1<? super A, Monad<U>> function);

    /**
     * Cria um novo Either com o valor de Right, se existir, mapeado.
     * 
     * @param <C>
     * @param mapFunction
     * @return
     */
    public <C> Either<B, C> map(final Function1<? super A, C> mapFunction)
    {
        return isLeft() ? Either.<B, C> createLeft(getLeft()) : Either.<B, C> createRight(mapFunction.apply(this.getRight()));
    }

    /**
     * Classe que representa o valor 'correto' contido em Either.
     * 
     * @param <B>
     * @param <A>
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

        @SuppressWarnings({ "unchecked" })
        @Override
        public <U> Either<B, U> bind(final Function1<? super A, Monad<U>> function)
        {
            return (Either<B, U>) function.apply(value);
        }
    }

    /**
     * Classe que representa o valor 'errado' contido em Either.
     *
     * @param <B>
     * @param <A>
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

        @SuppressWarnings({ "unchecked" })
        @Override
        public <C> Either<B, C> bind(final Function1<? super A, Monad<C>> function)
        {
            return (Either<B, C>) this;
        }
    }
}
