package com.github.detentor.operations;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.arrow.Arrow1;

/**
 * Essa classe provê funções comuns ao trabalhar com {@link Character}.
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
	
	/**
	 * Promove o array de char para a sua versão Object.
	 * 
	 * @param theArray O array de char a ser promovido
	 * @return Um array de Character, criado a partir do array de char
	 */
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
	 * Representa a função que converte um Character para String
	 */
	public static final Arrow1<Character, String> toString = new Arrow1<Character, String>()
	{
		@Override
		public String apply(final Character param)
		{
			return String.valueOf(param);
		}
	};
	
	/**
	 * Representa a função que converte um Character para sua versão UpperCase
	 */
	public static final Arrow1<Character, Character> toUpperCase = new Arrow1<Character, Character>()
	{
		@Override
		public Character apply(final Character param)
		{
			return Character.toUpperCase(param);
		}
	};
	
}
