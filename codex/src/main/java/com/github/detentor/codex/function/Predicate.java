package com.github.detentor.codex.function;


/**
 * Interface de funções que recebem um parâmetro, e fazem algum tipo de validação. (f : A -> Boolean).
 * 
 * @author Vinícius Seufitele Pinto 
 *
 * @param <A> O tipo de dado de entrada da função
 */
public interface Predicate<A> extends Function1<A, Boolean>
{
	/**
	 * Retorna uma função predicado que inverte a lógica desta função
	 * 
	 * @return Uma função cujo resultado será o inverso do resultado desta função
	 */
	default Predicate<A> not()
	{
		return param -> ! Predicate.this.apply(param);
	}

//	/**
//	 * Faz a composição deste predicado com o predicado passado como parâmetro. <br/>
//	 * Em particular, o predicado retornado será verdadeiro se, e somente se, ele for verdadeiro para
//	 * este predicado e para o predicado passado como parâmetro. 
//	 *  
//	 * @param predicate O predicado a ser composto com esta função
//	 * @return Uma função que retornará true somente se a aplicação deste predicado e da função
//	 * passada como parâmetro retornarem true.
//	 */
//	default <G extends Function1<? super A, Boolean>> Predicate<A> compose(final G predicate)
//	{
//		return param -> Predicate.this.apply(param) && predicate.apply(param);
//	}
}
