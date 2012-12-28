package com.github.detentor.operations;

import com.github.detentor.codex.function.Function1;

/**
 * Essa classe provê funções comuns ao trabalhar com {@link String}.
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public final class ObjectOps
{
	private ObjectOps()
	{
		//previne instanciação
	}

	/**
	 * Representa a função que transforma um objeto em sua representação em String.
	 */
	public static final Function1<Object, String> toString = new Function1<Object, String>()
	{
		@Override
		public String apply(final Object param)
		{
			return param.toString();
		}
	};

	
}
