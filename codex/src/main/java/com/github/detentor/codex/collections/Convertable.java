package com.github.detentor.codex.collections;

import java.util.List;
import java.util.Set;

/**
 * As classes que assinam essa interface devem prover métodos para transformar
 * a sua classe em coleções Java. <br/>
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public interface Convertable<T>
{
	/**
	 * Transforma essa classe em uma lista do Java, como instância de ArrayList.
	 * @return Essa classe como uma lista do Java.
	 */
	List<T> toList();
	
	/**
	 * Transforma essa classe em uma lista do Java, utilizando o builder
	 * passado como parâmetro.
	 * @return Essa classe como uma lista do Java, com a instância definida pelo builder.
	 */
	List<T> toList(final Builder<T, List<T>> builder);
	
	/**
	 * Transforma essa classe em um set do Java, como instância de HashSet.
	 * @return Essa classe como um set do Java.
	 */
	Set<T> toSet();
	
	/**
	 * Transforma essa classe em um set do Java, utilizando o builder passado como parâmetro.
	 * @return Essa classe como um set do Java, com a instância definida pelo builder.
	 */
	Set<T> toSet(final Builder<T, Set<T>> builder);

}
