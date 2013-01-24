package com.github.detentor.operations;

import com.github.detentor.codex.function.Function1;

/**
 * Essa classe provê funções comuns ao trabalhar com {@link String}.
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public final class StringOps
{
	private StringOps()
	{
		//previne instanciação
	}

	/**
	 * Representa a função identidade, ou seja, a função que retorna a própria String.
	 */
	public static final Function1<String, String> identity = new Function1<String, String>()
	{
		@Override
		public String apply(final String param)
		{
			return param;
		}
	};

	/**
	 * Representa a função que converte uma String para uppercase. Null-safe.
	 */
	public static final Function1<String, String> upperCase = new Function1<String, String>()
	{
		@Override
		public String apply(final String param)
		{
			return param == null ? null : param.toUpperCase();
		}
	};
	
	/**
	 * Representa a função que faz um trim e converte uma String para uppercase. Null-safe.
	 */
	public static final Function1<String, String> trimUpperCase = new Function1<String, String>()
	{
		@Override
		public String apply(final String param)
		{
			return param == null ? null : param.trim().toUpperCase();
		}
	};
	
	/**
	 * Representa a função que converte uma string para lowercase. Null-safe.
	 */
	public static final Function1<String, String> lowerCase = new Function1<String, String>()
	{
		@Override
		public String apply(final String param)
		{
			return param == null ? null : param.toLowerCase();
		}
	};
	
	/**
	 * Representa a função que faz um trim e converte uma String para lowercase. Null-safe.
	 */
	public static final Function1<String, String> trimLowerCase = new Function1<String, String>()
	{
		@Override
		public String apply(final String param)
		{
			return param == null ? null : param.trim().toLowerCase();
		}
	};
	
	/**
	 * Representa a função que converte uma String para Integer
	 */
	public static final Function1<String, Integer> toInt = new Function1<String, Integer>()
	{
		@Override
		public Integer apply(final String param)
		{
			return Integer.parseInt(param);
		}
	};
}
