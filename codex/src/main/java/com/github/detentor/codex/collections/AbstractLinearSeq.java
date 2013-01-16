package com.github.detentor.codex.collections;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.PartialFunction;

/**
 * Classe que provê a implementação padrão de métodos de coleções sequenciais, para simplificar a criação de classes que os estenda. <br/>
 * <br/>
 * 
 * Para criar uma coleção (imutável) com base nesta implementação, basta prover o código dos seguintes métodos: <br/>
 * <br/>
 * 
 * {@link Iterable#iterator() iterator()} , {@link SharpCollection#size() size()} , {@link #apply(Integer)} ,
 * {@link SharpCollection#builder() builder()} <br/>
 * <br/>
 * 
 * Não esquecer de sobrescrever o equals e o hashcode também. <br/>
 * <br/>
 * 
 * Para coleções mutáveis, veja {@link AbstractMutableIndexedSeq}. <br/>
 * <br/>
 * 
 * NOTA: Subclasses devem sempre dar override nos métodos {@link #map(Function1) map}, {@link #collect(PartialFunction) collect} e
 * {@link #flatMap(Function1) flatMap}. Devido à incompetência do Java com relação a Generics, isso é necessário para assegurar que o tipo
 * de retorno seja o mesmo da coleção. A implementação padrão (chamado o método da super classe é suficiente).
 * 
 * @author Vinícius Seufitele Pinto
 * 
 */
public abstract class AbstractLinearSeq<T> extends AbstractSharpCollection<T, LinearSeq<T>> implements LinearSeq<T>
{
	@Override
	public boolean isDefinedAt(final Integer forValue)
	{
		return forValue >= 0 && forValue < this.size();
	}

	@Override
	public T apply(final Integer param)
	{
		return param.equals(0) ? this.head() : this.tail().apply(param - 1);
	}

	@Override
	public T last()
	{
		ensureNotEmpty("last foi chamado para uma coleção vazia");
		return this.apply(this.size() - 1);
	}

	@Override
	public LinearSeq<T> drop(final Integer num)
	{
		if (num <= 0)
		{
			return this;
		}
		return (LinearSeq<T>) this.tail().drop(num - 1);
	}

	@Override
	public LinearSeq<T> dropWhile(final Function1<? super T, Boolean> pred)
	{
		if (!pred.apply(this.head()))
		{
			return this;
		}
		return (LinearSeq<T>) this.tail().dropWhile(pred);
	}

	@Override
	public SharpCollection<T> dropRightWhile(final Function1<? super T, Boolean> pred)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SharpCollection<T> takeWhile(final Function1<? super T, Boolean> pred)
	{
		return null;
	}

	@Override
	public SharpCollection<T> takeRightWhile(final Function1<? super T, Boolean> pred)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
