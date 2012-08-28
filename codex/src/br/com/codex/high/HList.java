package br.com.codex.high;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public class HList<Type, NextType>
{
	@SuppressWarnings("unchecked")
	private static final LList<Nil> Nil = new Nil();
	
	protected final LList<Type> value;
	protected final LList<NextType> nextList;
	
	
	protected HList(Type theValue, LList<NextType> theNextList)
	{
		this.value = new LList<Type>(theValue);
		this.nextList = theNextList;
	}
	
	/**
	 * Cria uma HList a partir do valor passado como parâmetro
	 * @param value
	 * @return
	 */
	public static <U> HList<U, Nil> from(final U value)
	{
		return new HList<U, Nil>(value, Nil);
	}
	
	/**
	 * Cria uma nova HList, a partir da aposição do elemento na frente da lista
	 * @param value
	 * @return
	 */
	public <V> HList<V, Type> cons(final V value)
	{
		return new HList<V, Type>(value, this.value);
	}
	
	
	
	private static class LList<K>
	{
		protected final K value;
		
		protected LList(final K theValue)
		{
			this.value = theValue;
		}
	}
	
	public static final class Nil extends LList
	{
		protected Nil()
		{
			super(null);
		}
	}
	

}
