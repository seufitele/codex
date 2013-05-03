package com.github.detentor.operations;

import com.github.detentor.codex.function.Function1;

/**
 * Essa classe provê funções comuns ao trabalhar com {@link String}.
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public final class CharOps
{
	private CharOps()
	{
		//previne instanciação
	}
	
	public static final Character[] lift(final char[] theArray)
	{
		Character[] retorno = new Character[theArray.length];
		
		for (int i = 0; i < retorno.length; i++)
		{
			retorno[i] = theArray[i];
		}
		
		return retorno;
	}

	/**
	 * Representa a função que converte uma String para Integer
	 */
	public static final Function1<Character, String> toString = new Function1<Character, String>()
	{
		@Override
		public String apply(final Character param)
		{
			return String.valueOf(param);
		}
	};
	
	/**
	 * Representa a função que converte uma String para Integer
	 */
	public static final Function1<Character, Character> toUpperCase = new Function1<Character, Character>()
	{
		@Override
		public Character apply(final Character param)
		{
			return Character.toUpperCase(param);
		}
	};
	
	/**
	 * Representa a função que converte uma String para Integer
	 */
	public static final Function1<Character, Integer> toCharCode = new Function1<Character, Integer>()
	{
		@Override
		public Integer apply(final Character param)
		{
			return (int) param;
		}
	};
	
}
