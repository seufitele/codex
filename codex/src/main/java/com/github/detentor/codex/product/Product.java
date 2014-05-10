package com.github.detentor.codex.product;

import java.io.Serializable;

/**
 * Na teoria dos conjuntos, um produto é uma sequência de n elementos, onde n é um inteiro positivo.<br/>
 * Um produto é igual a outro se, e somente se, os seus valores são iguais.<br/><br/>
 * 
 * @author Vinícius Seufitele Pinto
 * 
 */
public interface Product extends Serializable
{
	//VERIFICAR A POSSIBILIDADE DE TRANSFORMAR O PRODUTO EM UMA SHARPCOLLECTION
//	, Iterable<Object>, PartialFunction<Integer, Object>
//	/**
//	 * Retorna a aridade, ou seja, o número de elementos que este produto contém
//	 * @return A aridade deste produto
//	 */
//	int productArity();
	
	/**
	 * Adiciona um elemento neste produto, produzindo um produto com um elemento a mais
	 * @param value O valor a ser adicionado neste produto
	 * @return Um produto com um elemento a mais
	 */
	<K> Product add(K value);
}
