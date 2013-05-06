package com.github.detentor.codex.collections.immutable;

import java.io.Serializable;
import java.util.Arrays;

import com.github.detentor.codex.collections.AbstractIndexedSeq;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.IndexedSeq;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.collections.mutable.ListSharp;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.PartialFunction;
import com.github.detentor.codex.product.Tuple2;
import com.github.detentor.operations.CharOps;
import com.github.detentor.operations.StringOps;

/**
 * RichString é um encapsulamento feito sobre uma String, para que seja
 * possível trabalhar com Strings como se trabalha com uma {@link SharpCollection}. <br/> <br/>
 * 
 * ATENÇÃO: Versão beta.
 * 
 * @author Vinicius Seufitele Pinto
 *
 */
public class RichString extends AbstractIndexedSeq<Character, RichString> implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static final RichString EMPTY = new RichString("");

	private final String value;

	/**
	 * Retorna uma RichString vazia
	 * @return Uma RichString vazia
	 */
	public static RichString empty()
	{
		return EMPTY;
	}

	/**
	 * Cria uma RichString a partir da String passada como parâmetro. <br/> 
	 * ATENÇÃO: Se o valor passado como parâmetro for nulo, será retornada uma coleção vazia.
	 * 
	 * @param theValue A string a partir da qual RichString será criada
	 * @return Uma RichString que encapsula a string criada
	 */
	public static RichString from(final String theValue)
	{
		return theValue == null ? empty() : new RichString(theValue);
	}
	
	/**
	 * Construtor privado, para criar Strings internamente
	 */
	protected RichString(final String theValue)
	{
		this.value = theValue;
	}

	@Override
	public Character apply(final Integer pos)
	{
		return value.charAt(pos);
	}

	@Override
	public int size()
	{
		return value.length();
	}

	@Override
	public RichString subsequence(final int startIndex, final int endIndex)
	{
		return from(value.substring(startIndex, endIndex));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Builder<Character, SharpCollection<Character>> builder()
	{
		return new RichStringBuilder();
	}

	@Override
	public <B> IndexedSeq<B> map(final Function1<? super Character, B> function)
	{
		return ListSharp.from(CharOps.lift(value.toCharArray())).map(function);
	}

	public RichString map(final Function1<? super Character, Character> function)
	{
		return (RichString) super.map(function);
	}

	@Override
	public <B> IndexedSeq<B> flatMap(final Function1<? super Character, ? extends SharpCollection<B>> function)
	{
		return ListSharp.from(CharOps.lift(value.toCharArray())).flatMap(function);
	}

	public RichString flatMap(final Function1<? super Character, ? extends RichString> function)
	{
		return (RichString) super.flatMap(function);
	}

	@Override
	public <B> IndexedSeq<B> collect(final PartialFunction<? super Character, B> pFunction)
	{
		return ListSharp.from(CharOps.lift(value.toCharArray())).collect(pFunction);
	}

	public RichString collect(final PartialFunction<? super Character, Character> pFunction)
	{
		return (RichString) super.collect(pFunction);
	}
	
	@Override
	public IndexedSeq<Tuple2<Character, Integer>> zipWithIndex()
	{
		return ListSharp.from(CharOps.lift(value.toCharArray())).zipWithIndex();
	}
	
	/**
	 * Retorna uma RichString que representa a adição desta coleção com a String passada como parâmetro. <br/>
	 * @param stringToAdd A string a ser adicionada nesta String. Se o parâmetro for nulo será retornada esta mesma
	 * instância.
	 * @return Uma RichString que representa a adição das duas Strings
	 */
	public RichString add(final String stringToAdd)
	{
		return stringToAdd == null ? this : from(this.value + stringToAdd);
	}

	/**
	 * Substitui cada substring desta RichString que equivale à expressão regular passada como parâmetro.
	 * 
	 * @param regex A expressão regular que irá localizar as substrings a serem substituídas
	 * @param replacement A string que irá substituir as substrings definidas pela expressão regular
	 * @return Uma RichString que representa a string substituída
	 */
	public RichString replaceAll(final String regex, final String replacement)
	{
		return from(value.replaceAll(regex, replacement));
	}
	
	/**
	 * Retorna uma RichString cujos caracteres estão em properCase, ou seja,
	 * com as primeiras letras das palavras transformadas em maiúsculas.
	 * 
	 * @return Uma RichString com as palavras transformadas em properCase.
	 */
	public RichString toProperCase()
	{
		return from(StringOps.toProperCase.apply(this.toString()));
	}

	/**
	 * Retorna uma RichString cujos caracteres estão em minúsculo.
	 * 
	 * @return  Uma RichString cujos caracteres estão em minúsculo.
	 */
	public RichString toLowerCase()
	{
		return from(value.toLowerCase());
	}

	/**
	 * Retorna uma RichString cujos caracteres estão em maiúsculo.
	 * 
	 * @return  Uma RichString cujos caracteres estão em maiúsculo.
	 */
	public RichString toUpperCase()
	{
		return from(value.toUpperCase());
	}
	
	/**
	 * Retorna uma RichString sem whitespace no início ou no fim. <br/>
	 * Para mais informações sobre Whitespace, veja {@link String#trim()}.
	 * 
	 * @return Uma RichString que representa uma String sem espaços no início ou fim.
	 */
	public RichString trim()
	{
		return from(value.trim());
	}

	/**
	 * Retorna true se a String representada por esta coleção é equivalente à String passada como parâmetro
	 * @return True se, e somente se, a String representada por esta coleção é equivalente à String passada como parâmetro
	 */
	public boolean equals(final String theString)
	{
		return this.value.equals(theString);
	}
	
	/**
	 * Retorna true se a String representada por esta coleção é equivalente à String passada como parâmetro,
	 * ignorando maiúsculas e minúsculas.
	 * @return True se, e somente se, a String representada por esta coleção é equivalente à String passada como parâmetro
	 */
	public boolean equalsIgnoreCase(final String theString)
	{
		return this.value.equalsIgnoreCase(theString);
	}
	
	/**
	 * Retorna uma RichString que representa a ordenação dos caracteres desta coleção
	 * 
	 * @return Uma RichString, com os caracteres desta coleção em ordem ascendente
	 */
	public RichString sorted()
	{
		char[] chars = value.toCharArray();
		Arrays.sort(chars);
		return from(String.valueOf(chars));
	}
	
	/**
	 * Retorna uma RichString que representa os caracteres desta String em ordem reversa. <br/>
	 * Complexidade O(n).
	 * 
	 * @return Uma RichString com os caracteres desta String em ordem reversa.
	 */
	public RichString reverse()
	{
		//Usa swap para inverter os elementos de maneira mais rápida
		final int upperBound = this.value.length() / 2;
		final char[] reversedStr = value.toCharArray();

		int lastIndex = this.value.length() -1;
		char tempChar = ' ';
		
		for (int i = 0; i < upperBound; i++)
		{
			tempChar = reversedStr[i];
			reversedStr[i] = reversedStr[lastIndex];
			reversedStr[lastIndex--] = tempChar;
		}
		return from(String.valueOf(reversedStr));
	}
	
	@Override
	public String toString()
	{
		return value;
	}

	/**
	 * Builder para String
	 */
	private static final class RichStringBuilder implements Builder<Character, SharpCollection<Character>>
	{
		private final StringBuilder sBuilder = new StringBuilder();

		@Override
		public void add(final Character element)
		{
			sBuilder.append(element);
		}

		@Override
		public RichString result()
		{
			return from(sBuilder.toString());
		}
	}
}
