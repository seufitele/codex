package com.github.detentor.codex.monoids;

import com.github.detentor.codex.cat.Monoid;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Functions;
import com.github.detentor.operations.ObjectOps;

/**
 * Essa classe representa o endomorfismo de monóides <br/>
 * O objetivo dessa classe é apenas prover uma instância de monóide para funções endomórficas, ou seja, onde o tipo da entrada e saída são
 * iguais.
 *
 * @param <A> O parâmetro de entrada e saída da função
 */
public class Endo<A> implements Monoid<Function1<A, A>>
{
	@Override
	public Function1<A, A> empty()
	{
		return ObjectOps.identity();
	}

	@Override
	public Function1<A, A> reduce(final Function1<A, A> param1, final Function1<A, A> param2)
	{
		//Haskell
		// Endo f `mappend` Endo g = Endo (f . g)
		return Functions.compose(param1, param2);
	}

	/**
	 * Retorna o Dual desta monóide, ou seja, esta monóide com os parâmetros do {@link #reduce(Object, Object)} invertidos.
	 * 
	 * @return O Dual desta monóide, onde os parâmetros de {@link #reduce(Object, Object)} estão invertidos.
	 */
	public Endo<A> getDual()
	{
		return new Endo<A>()
		{
			@Override
			public Function1<A, A> reduce(final Function1<A, A> param1, final Function1<A, A> param2)
			{
				return Endo.this.reduce(param2, param1);
			}
		};
	}
}
