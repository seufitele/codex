package com.github.detentor.codex.collections.mutable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;

import com.github.detentor.codex.collections.AbstractMutableLinearSeq;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.LinearSeq;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.monads.Option;

/**
 * Implementação "Sharp" de uma árvore binária
 * @author Vinicius Seufitele
 *
 */
public class BTreeSharp<T> extends AbstractMutableLinearSeq<T, BTreeSharp<T>> implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private BTreeSharp<T> left = null;
	private BTreeSharp<T> right = null;
	
	private T value;
//	private int count = 0;
	
	protected BTreeSharp(T value)
	{
		super();
		this.value = value;
//		this.count = 1;
	}
	
	public static <T> BTreeSharp<T> from(final T value)
	{
		return new BTreeSharp<T>(value);
	}
	
	public static <T> BTreeSharp<T> empty()
	{
		return new BTreeSharp<T>(null);
	}
	
	@Override
	public LinearSeq<T> tail()
	{
		ensureNotEmpty();
		return right;
	}

	@Override
	public T head()
	{
		ensureNotEmpty();
		return value;
	}
	
	@Override
	public boolean isEmpty()
	{
		return value == null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BTreeSharp<T> add(T element)
	{
		if (this.value == null)
		{
			this.value = element;
			return this;
		}
		final int compResult = ((Comparable<T>) element).compareTo(value);
		if (compResult != 0)
		{
			(compResult < 0 ? getLeft() : getRight()).add(element);
		}
		return this;
	}

	@Override
	public SharpCollection<T> remove(T element)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SharpCollection<T> clear()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <B> Builder<B, SharpCollection<B>> builder()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SharpCollection<T> sorted()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public BTreeSharp<T> getLeft()
	{
		if (left == null)
		{
			left = empty();
		}
		return left;
	}

	public BTreeSharp<T> getRight()
	{
		if (right == null)
		{
			right = empty();
		}
		return right;
	}

	@Override
	public SharpCollection<T> sorted(Comparator<? super T> comparator)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString()
	{
		return "[" + toStringPriv() + "]";
	}
	
	private String toStringPriv()
	{
		if (this.isEmpty()) 
		{
			return "";
		}
		final String leftPart = this.getLeft().isEmpty() ? "" : this.getLeft().toStringPriv() + ", ";
		final String rightPart = this.getRight().isEmpty() ? "" : ", " + this.getRight().toStringPriv();
		return leftPart + this.value.toString() + rightPart; 
	}

	@Override
	public Iterator<T> iterator()
	{
		return new Iterator<T>()
		{
			int curStep = 0;
			Iterator<T> ite = null;
			
			@Override
			public boolean hasNext()
			{
				return BTreeSharp.this.notEmpty() && (ite != null && ite.hasNext());
			}

			@Override
			public T next()
			{
				if (curStep == 0)
				{
					ite = BTreeSharp.this.getLeft().iterator();
					curStep++; //passa para o próximo passo
				}
				
				if (curStep == 1)
				{
					if (ite.hasNext())
					{
						return ite.next();
					}
					curStep++;
				}
				
				if (curStep == 2)
				{
					curStep++;
					return BTreeSharp.this.value;
				}
				
				if (curStep == 3)
				{
					ite = BTreeSharp.this.getRight().iterator();
					curStep++;
				}
				
				if (curStep == 4)
				{
					if (ite.hasNext())
					{
						return ite.next();
					}
					curStep++;
				}
				throw new IllegalArgumentException("não há mais elementos no iterator");
			}

			@Override
			public void remove()
			{
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	
	
}
