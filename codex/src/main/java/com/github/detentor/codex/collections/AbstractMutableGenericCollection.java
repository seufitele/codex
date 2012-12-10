package com.github.detentor.codex.collections;

import com.github.detentor.codex.function.Function1;

/**
 * Classe que provê a implementação padrão de diversos métodos de coleções mutáveis, para simplificar a 
 * criação de classes que os estenda. <br/> <br/>
 * 
 * Para criar uma coleção mutável com base nesta implementação, basta prover o código dos seguintes métodos: <br/> <br/>
 * 
 * {@link Iterable#iterator() iterator()}, {@link SharpCollection#add(T) add()}, {@link SharpCollection#removeIndex(T) remove()},
 * {@link SharpCollection#clear() clear()}, {@link SharpCollection#size() size()} , {@link SharpCollection#builder() builder()} 
 * <br/><br/>
 * 
 * Não esquecer de sobrescrever o {@link #equals(Object) equals} e o {@link #hashCode() hashcode} também. <br/><br/>
 * 
 * Subclasses que não possuam {@link #size() size()} facilmente calculável devem sobrescrever o método 
 * {@link #isEmpty() isEmpty()}.<br/> <br/>
 * 
 * NOTA: Subclasses devem sempre dar override nos métodos {@link HighOrderFunctions#map(Function1) map()}, 
 * {@link HighOrderFunctions#flatMap(br.com.codex.function.Function1) flatMap()} e {@link #zipWithIndex() zipWithIndex()}.
 * Devido à incompetência do Java com relação a generics, isso é necessário para assegurar que o tipo de retorno seja o mesmo da coleção. 
 * A implementação padrão (chamado o método da super classe é suficiente).
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public abstract class AbstractMutableGenericCollection<T, U extends SharpCollection<T>> extends AbstractGenericCollection<T, U> 
																						implements MutableSharpCollection<T>
{
	@SuppressWarnings("unchecked")
	@Override
	public U addAll(final Iterable<? extends T> col)
	{
		for (final T ele : col)
		{
			this.add(ele);
		}
		return (U) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public U removeAll(final Iterable<T> col)
	{
		for (final T ele : col)
		{
			this.remove(ele);
		}
		return (U) this;
	}
}
