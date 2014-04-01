package com.github.detentor.codex.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.github.detentor.codex.collections.Builder;

public class Builders 
{
	private Builders()
	{
		//previne instanciação
	}
	
	/**
	 * Builder genérico para {@link HashSet}.
	 * @return Um builder para HashSet.
	 */
	@SuppressWarnings("unchecked")
	public static <From, To extends Iterable<From>> Builder<From, To> hashSetBuilder()
	{
		return (Builder<From, To>) new HashSetBuilder<From>();
	}
	
	/**
	 * Builder genérico para {@link LinkedHashSet}.
	 * @return Um builder para HashSet.
	 */
	@SuppressWarnings("unchecked")
	public static <From, To extends Iterable<From>> Builder<From, To> linkedHashSetBuilder()
	{
		return (Builder<From, To>) new LinkedHashSetBuilder<From>();
	}
	
	/**
	 * Builder genérico para {@link ArrayList}.
	 * @return Um builder para ArrayList.
	 */
	@SuppressWarnings("unchecked")
	public static <From, To extends Iterable<From>> Builder<From, To> arrayListBuilder()
	{
		return (Builder<From, To>) new ArrayListBuilder<From>();
	}

	
	
	
	
	/**
	 * Essa classe é um builder para Set baseado em um HashSet. <br/>
	 * @param <E> O tipo de dados armazenado no HashSet.
	 */
	private static final class HashSetBuilder<E> implements Builder<E, Set<E>>
	{
		private final Set<E> backingSet = new HashSet<E>();

		@Override
		public void add(final E element)
		{
			backingSet.add(element);
		}

		@Override
		public Set<E> result()
		{
			return backingSet;
		}
	}
	
	/**
	 * Essa classe é um builder para Set baseado em um HashSet. <br/>
	 * @param <E> O tipo de dados armazenado no HashSet.
	 */
	private static final class LinkedHashSetBuilder<E> implements Builder<E, Set<E>>
	{
		private final Set<E> backingSet = new LinkedHashSet<>();

		@Override
		public void add(final E element)
		{
			backingSet.add(element);
		}

		@Override
		public Set<E> result()
		{
			return backingSet;
		}
	}
	
	/**
	 * Essa classe é um builder para List baseado em um ArrayList.
	 * @param <E> O tipo de dados do ArrayList retornado
	 */
	private static final class ArrayListBuilder<E> implements Builder<E, List<E>>
	{
		private final List<E> list = new ArrayList<E>();

		@Override
		public void add(final E element)
		{
			list.add(element);
		}

		@Override
		public List<E> result()
		{
			return list;
		}
	}
	

}
