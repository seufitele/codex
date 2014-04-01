package com.github.detentor.codex.collections;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.PartialFunction1;
import com.github.detentor.codex.product.Tuple2;

/**
 * Classe que provê a implementação padrão de métodos mutáveis de coleções indexadas sequencialmente, para simplificar a criação de
 * classes que os estenda. <br/>
 * <br/>
 * 
 * Para criar uma coleção (mutável) com base nesta implementação, basta prover o código dos seguintes métodos: <br/>
 * <br/>
 * 
 * 
 * {@link #head()}, {@link #tail()}, {@link #isEmpty()}, {@link SharpCollection#add(T) add()} , {@link SharpCollection#removeIndex(T)
 * remove()} , {@link SharpCollection#clear() clear()} , {@link SharpCollection#builder() builder()} <br/>
 * <br/>
 * 
 * Não esquecer de sobrescrever o equals e o hashcode também. <br/>
 * <br/>
 * 
 * Para coleções imutáveis, veja {@link AbstractLinearSeq}. <br/>
 * <br/>
 * 
 * NOTA: Subclasses devem sempre dar override nos métodos {@link #map(Function1) map}, {@link #collect(PartialFunction1) collect},
 * {@link #flatMap(Function1) flatMap} e {@link #zipWithIndex() zipWithIndex}. 
 * Devido à incompetência do Java com relação a Generics, isso é necessário para assegurar que o
 * tipo de retorno seja o mesmo da coleção. A implementação padrão (chamado o método da super classe é suficiente).
 * 
 * @author Vinícius Seufitele Pinto
 * 
 */
public abstract class AbstractMutableLinearSeq<T, U extends LinearSeq<T>> implements LinearSeq<T>,
		MutableSharpCollection<T>
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

	// Os métodos são reimplementados aqui porque o java não carrega o tipo genérico para cima:
	// Como os métodos, se não estiverem aqui, estarão guardados na classe superior, então
	// na hora de verificar qual o retorno, o compilador não sabe qual o tipo U definido, pegando
	// o tipo da própria classe.
	// Seria interessante se o compilador fosse esperto o suficiente para "subir", a partir da classe
	// mais baixa, carregando o tipo genérico definido. Isso, em teoria, é possível e, a partir daí,
	// o tipo de retorno seria correto.

	@Override
	public U take(final Integer num)
	{
		return super.take(num);
	}

	@Override
	public U takeRight(final Integer num)
	{
		return super.takeRight(num);
	}

	@Override
	public U takeWhile(final Function1<? super T, Boolean> pred)
	{
		return super.takeWhile(pred);
	}

	@Override
	public U takeRightWhile(final Function1<? super T, Boolean> pred)
	{
		return super.takeRightWhile(pred);
	}

	@Override
	public U drop(final Integer num)
	{
		return super.drop(num);
	}

	@Override
	public U dropRight(final Integer num)
	{
		return super.dropRight(num);
	}

	@Override
	public U dropWhile(final Function1<? super T, Boolean> pred)
	{
		return super.dropWhile(pred);
	}

	@Override
	public U dropRightWhile(final Function1<? super T, Boolean> pred)
	{
		return super.dropRightWhile(pred);
	}

	@SuppressWarnings("unchecked")
	@Override
	public U filter(final Function1<? super T, Boolean> pred)
	{
		return (U) super.filter(pred);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Tuple2<U, U> partition(final Function1<? super T, Boolean> pred)
	{
		return (Tuple2<U, U>) super.partition(pred);
	}

	@SuppressWarnings("unchecked")
	@Override
	public U intersect(final Iterable<T> withCollection)
	{
		return (U) super.intersect(withCollection);
	}

	@SuppressWarnings("unchecked")
	@Override
	public U distinct()
	{
		return (U) super.distinct();
	}

}
