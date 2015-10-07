package com.github.detentor.codex.function.arrow;

import java.lang.reflect.Array;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.FunctionN;

/**
 * Interface que define uma seta que não recebe uma quantidade arbitrária de argumentos do tipo A, e retorna um valor do tipo B
 * 
 * @author f9540702 Vinícius Seufitele Pinto
 * 
 * @param <A> O tipo dos parâmetros da seta
 * @param <B> O tipo de retorno da seta
 */
public abstract class ArrowN<A, B> implements FunctionN<A, B>, Arrow
{
	/**
	 * Aplica, parcialmente, a seta, ou seja, guarda um parâmetro e retorna uma seta com aquele parâmetro setado
	 * 
	 * @param param1 O valor a ser aplicado à seta
	 * @return Uma nova seta, com o valor já aplicado
	 */
	public ArrowN<A, B> applyPartial(final A param1)
	{
		return new ArrowN<A, B>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public B apply(final A... params)
			{
				final A[] todosEle = (A[]) Array.newInstance(param1.getClass(), params.length + 1);
				todosEle[0] = param1;
				System.arraycopy(params, 0, todosEle, 1, params.length);
				return ArrowN.this.apply(todosEle);
			}
		};
	}

	@Override
	public int getArity()
	{
		return 0;
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
    public <C> ArrowN<A, C> and(final Function1<? super B, C> func)
    {
        return new ArrowN<A, C>()
        {
            @Override
            public C apply(A... params)
            {
                return func.apply(ArrowN.this.apply(params));
            }
        };
    }
}
