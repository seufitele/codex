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
	 * Verifica se os dois objetos são iguais. Em particular, a principal diferença deste método é ser
	 * Null-safe, ou seja, a utilização do método não disparará {@link NullPointerException}, o que aconteceria
	 * caso fosse usado object1.equals(object2), e object1 fosse nulo.
	 * 
	 * @param object1 O primeiro objeto a ser comparado
	 * @param object2 O segundo objeto a ser comparado
	 * @return True se, e somente se, os dois objetos são nulos, ou object1.equals(object2) retornar true.
	 */
	public static final <A, B> boolean isEquals(final A object1, final B object2)
	{
		return (object1 == null && object2 == null) || 
					(object1 != null && object2 != null && object1.equals(object2)); 
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
