package com.github.detentor.operations;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.github.detentor.codex.collections.immutable.ListSharp;
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
	 * Representa a função que converte uma String para uppercase. Null-safe.
	 */
	public static final Function1<String, String> toUpperCase = (param -> param == null ? null : param.toUpperCase());
	
	/**
	 * Representa a função que converte uma string para lowercase. Null-safe.
	 */
	public static final Function1<String, String> toLowerCase = (param -> param == null ? null : param.toLowerCase());
	
	/**
	 * Representa a função que faz um trim na String. Null-safe
	 */
	public static final Function1<String, String> trim = (param -> param == null ? null : param.trim());
	
	/**
	 * Representa a função que converte uma String para Integer
	 */
	public static final Function1<String, Integer> toInt = (param -> Integer.parseInt(param)); 
	
	/**
	 * Representa a função que converte uma String para o seu tamanho
	 */
	public static final Function1<String, Integer> toSize = String::length;
	
	/**
	 * Representa a função que converte uma string para properCase. Null-safe.
	 */
	public static final Function1<String, String> toProperCase = new Function1<String, String>()
	{
		@Override
		public String apply(final String param)
		{
			if (param == null)
			{
				return null;
			}

			final List<String> excecoes = Arrays.asList("da", "de", "di", "do", "du", "das", "des", "dis", "dos", "dus", "na", "ne", "ni",
					"no", "nu", "em", "com", "não", "ao");

			// Transforma a "frase" em palavras
			final String[] palavras = param.split(" ");

			final char letraa = 'a';
			final char letraz = 'z';

			// A distância da letra minúscula para a maiúscula é 32
			final int caseDistance = 32;
			final int ignoreWordSize = 4;
			final Locale localeBR = new Locale("pt", "BR");

			for (int i = 0; i < palavras.length; i++)
			{
				// Converte a palavra para caixa baixa
				palavras[i] = palavras[i].toLowerCase(localeBR);

				// Se o tamanho da palavra for maior que 1,
				// ou se for a primeira palavra, converte a primeira
				// letra para maiúscula
				if ((palavras[i].length() > 1) || (i == 0))
				{
					final char[] letras = palavras[i].toCharArray();
					int firstLetter = 0;

					// Pula os chars não ascii
					while ((firstLetter < letras.length) && (letras[firstLetter] < letraa || letras[firstLetter] > letraz))
					{
						firstLetter++;
					}

					// A distância da letra minúscula para a maiúscula é 32
					if (firstLetter < letras.length)
					{
						letras[firstLetter] -= caseDistance;
					}

					// Verifica as exceções somente se não for a primeira palavra,
					// e se o tamanho da palavra for menor que 4.
					if ((palavras[i].length() < ignoreWordSize) && (i > 0))
					{
						final String curPalavra = String.valueOf(letras).trim().toLowerCase(localeBR);

						// Converte a palavra para o lower case
						if (excecoes.contains(curPalavra))
						{
							// Retorna a palavra para o char original
							if (firstLetter < letras.length)
							{
								letras[firstLetter] += caseDistance;
							}
						}
					}
					palavras[i] = String.valueOf(letras);
				}
			}
			return ListSharp.from(palavras).mkString(" ");
		}
	};
}
