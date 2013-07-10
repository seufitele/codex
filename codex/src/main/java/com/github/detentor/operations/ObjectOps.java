package com.github.detentor.operations;

import com.github.detentor.codex.function.arrow.Arrow1;

/**
 * Essa classe provê funções comuns ao trabalhar com {@link Object}.
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
	 * Retorna uma seta que valida se o objeto passado como parâmetro é igual a 'theObject'
	 * @param theObject O objeto a ser usado como comparador para a seta.
	 * @return Uma seta que valida se o objeto passado como parâmetro é igual a 'theObject'
	 */
	public static final Arrow1<Object, Boolean> isEquals(final Object theObject)
	{
		return new Arrow1<Object, Boolean>()
		{
			@Override
			public Boolean apply(final Object param)
			{
				return theObject == null ? param == null : theObject.equals(param);
			}
		};
	}

	/**
	 * Representa a função que transforma um objeto em sua representação em String.
	 */
	public static final Arrow1<Object, String> toString = new Arrow1<Object, String>()
	{
		@Override
		public String apply(final Object param)
		{
			return param.toString();
		}
	};

	/**
	 * Representa a função que transforma um objeto em sua representação em hashcode.
	 */
	public static final Arrow1<Object, Integer> toHashcode = new Arrow1<Object, Integer>()
	{
		@Override
		public Integer apply(final Object param)
		{
			return param.hashCode();
		}
	};
	
	/**
	 * Representa a função identidade
	 */
	public static final Arrow1<Object, Object> identity = new Arrow1<Object, Object>()
	{
		@Override
		public Object apply(final Object param)
		{
			return param;
		}
	};

}
