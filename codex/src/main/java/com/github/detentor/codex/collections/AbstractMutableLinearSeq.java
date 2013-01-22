package com.github.detentor.codex.collections;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.PartialFunction;

/**
 * Classe que provê a implementação padrão de métodos mutáveis de coleções indexadas sequencialmente, para simplificar a 
 * criação de classes que os estenda. <br/> <br/>
 * 
 * Para criar uma coleção (mutável) com base nesta implementação, basta prover o código dos seguintes métodos: <br/> <br/>
 * 
 * 
 * {@link #head()}, {@link #tail()}, {@link #isEmpty()},
 * {@link SharpCollection#add(T) add()} , 
 * {@link SharpCollection#removeIndex(T) remove()} ,
 * {@link SharpCollection#clear() clear()} ,
 * {@link SharpCollection#builder() builder()} <br/> <br/>
 * 
 * Não esquecer de sobrescrever o equals e o hashcode também. <br/><br/>
 * 
 * Para coleções imutáveis, veja {@link AbstractLinearSeq}. <br/><br/>
 * 
 * NOTA: Subclasses devem sempre dar override nos métodos {@link #map(Function1) map}, {@link #collect(PartialFunction) collect} 
 * e {@link #flatMap(Function1) flatMap}. Devido à incompetência do Java com relação a Generics,
 * isso é necessário para assegurar que o tipo de retorno seja o mesmo da coleção. A implementação padrão (chamado o método da super
 * classe é suficiente).
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public abstract class AbstractMutableLinearSeq<T, U extends LinearSeq<T>> extends AbstractLinearSeq<T, U> 
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
