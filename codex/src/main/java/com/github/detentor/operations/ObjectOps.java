
package com.github.detentor.operations;

import com.github.detentor.codex.function.Function1;

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
	public static final Function1<Object, Boolean> isEquals(final Object theObject)
	{
		return new Function1<Object, Boolean>()
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
	public static final Function1<Object, String> toString = Object::toString;

	/**
	 * Representa a função que transforma um objeto em sua representação em hashcode.
	 */
	public static final Function1<Object, Integer> toHashcode = Object::hashCode;
	
	/**
	 * Representa a função identidade
	 */
	public static final Function1<Object, Object> identity = param -> param;
	
	/**
	 * Transforma um enum em sua propriedade name
	 */
	public static final Function1<Enum<?>, String> enumToName = Enum::name;
	
	
	@SuppressWarnings("unchecked")
    public static <A, B> Function1<A, B> identity()
	{
	    return (Function1<A, B>) identity;
	}
	
	/**
	 * Representa a função que transforma um objeto em seu valor .class
	 */
	public static <A> Function1<A, Class<A>> toClass()
	{
		return new Function1<A, Class<A>>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public Class<A> apply(final A param)
			{
				return (Class<A>) param.getClass();
			}
		};
	}

}
