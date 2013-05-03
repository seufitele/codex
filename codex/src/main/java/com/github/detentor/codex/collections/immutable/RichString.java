package com.github.detentor.codex.collections.immutable;

import com.github.detentor.codex.collections.AbstractIndexedSeq;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.IndexedSeq;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.operations.CharOps;

public class RichString extends AbstractIndexedSeq<Character, RichString>
{
	private final String value;
	
	protected RichString(final String theValue)
	{
		this.value = theValue;
	}

	@Override
	public Character apply(Integer pos)
	{
		return value.charAt(pos);
	}

	@Override
	public int size()
	{
		return value.length();
	}

	@Override
	public RichString subsequence(int startIndex, int endIndex)
	{
		return new RichString(value.substring(startIndex, endIndex));
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

	private static final class RichStringBuilder implements Builder<Character, SharpCollection<Character>>
	{
		private final StringBuilder sBuilder = new StringBuilder();

		@Override
		public void add(Character element)
		{
			sBuilder.append(element);
		}

		@Override
		public RichString result()
		{
			return new RichString(sBuilder.toString());
		}
	}
	
	@Override
	public String toString()
	{
		return value;
	}
	
	public static void main(String[] args)
	{
		final RichString x = new RichString("legal");
		System.out.println(x.map(CharOps.toCharCode));
	}
	
}
