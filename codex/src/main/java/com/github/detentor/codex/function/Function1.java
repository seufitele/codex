package com.github.detentor.codex.function;

/**
 * Interface de funções que recebem um parâmetro, e retornam um valor. (f : A -> B).
 * 
 * @author Vinícius Seufitele Pinto 
 *
 * @param <A> O tipo de dado de entrada da função
 * @param <B> O tipo de dado de saída da função
 */
public interface Function1<A, B>
{
	default public int getArity()
	{
		return 1;
	}

	/**
	 * Faz a composição desta função com a função passada como parâmetro. <br/>
	 * Formalmente, seja
	 * 
	 * <pre>
	 * f: A -> B e g: B -> C. Então, será retornado um h: A -> C.
	 * </pre>
	 * 
	 * @param <A> O tipo de dados de entrada da primeira função
	 * @param <B> O tipo de dados de retorno da primeira função, e de entrada da segunda (contravariante)
	 * @param <C> O tipo de dados de retorno da segunda função
	 * @param func Uma função <b>g: B -> C</b>, a ser feita a composição.
	 * @return Uma função <b>h: A -> C</b>, que representa a composição das duas funções.
	 */
	default public <C> Function1<A, C> compose(final Function1<? super B, ? extends C> func)
	{
		return param -> func.apply(Function1.this.apply(param));
	}
	
	/**
	 * Retorna uma função de aridade 0, a partir da aplicação do primeiro parâmetro. <br/>
	 * Essa técnica é também conhecida como Currying.
	 * 
	 * @param param O parâmetro a ser aplicado parcialmente nesta função
	 * @return Uma função de aridade 0, que representa esta função com o parâmetro passado fixado
	 */
	default public Function0<B> applyPartial(final A param)
	{
		return () -> Function1.this.apply(param);
	}

	/**
	 * Executa a função, retornando um valor do tipo B
	 * @param param O parâmetro a ser passado para a função
	 * @return Um valor do tipo B
	 */
	B apply(final A param);
}
