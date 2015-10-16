package com.github.detentor.codex.function;

/**
 * Interface de funções que recebem quatro parâmetros, e retornam um valor. (f : A -> B -> C -> D -> E).
 * 
 * @author Vinícius Seufitele Pinto 
 *
 * @param <A> O tipo de dado do primeiro parâmetro
 * @param <B> O tipo de dado do segundo parâmetro
 * @param <C> O tipo de dado do terceiro parâmetro
 * @param <D> O tipo de dado do quarto parâmetro
 * @param <E> O tipo de dado de saída da função
 * 
 */
public interface Function4<A, B, C, D, E> 
{
//	default int getArity()
//	{
//		return 4;
//	}
//
//	/**
//	 * Retorna uma função de aridade 2, a partir da aplicação do primeiro parâmetro. <br/>
//	 * Essa técnica é também conhecida como Currying.
//	 * 
//	 * @param param1 O parâmetro a ser aplicado parcialmente nesta função
//	 * @return Uma função de aridade 2, que representa esta função com o parâmetro passado fixado
//	 */
//	default public Function3<B, C, D, E> applyPartial(final A param1)
//	{
//		return (param2, param3, param4) -> Function4.this.apply(param1, param2, param3, param4);
//	}

	/**
	 * Executa a função, a partir da aplicação dos três parâmetros.
	 * @param param1 O primeiro parâmetro a ser passado para a função
	 * @param param2 O segundo parâmetro a ser passado para a função
	 * @param param3 O terceiro parâmetro a ser passado para a função
	 * @return Um valor do tipo D, obtido após a aplicação da função para os parâmetros 
	 */
	E apply(final A param1, final B param2, final C param3, final D param4);
}