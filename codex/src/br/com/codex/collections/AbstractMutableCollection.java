package br.com.codex.collections;

/**
 * Classe que provê a implementação padrão de diversos métodos de coleções mutáveis, para simplificar a 
 * criação de classes que os estenda. <br/> <br/>
 * 
 * Para criar uma coleção mutável com base nesta implementação, basta prover o código dos seguintes métodos: <br/> <br/>
 * 
 * {@link Iterable#iterator() iterator()}, {@link SharpCollection#add(T) add()}, {@link SharpCollection#remove(T) remove()},
 * {@link SharpCollection#clear() clear()}, {@link SharpCollection#size() size()} , {@link SharpCollection#builder() builder()} <br/> <br/>
 * 
 * Não esquecer de sobrescrever o equals e o hashcode também. <br/><br/>
 * 
 * Subclasses que não possuam size() facilmente calculável devem sobrescrever o método isEmpty(). <br/> <br/>
 * 
 * NOTA: Subclasses devem sempre dar override nos métodos Map e FlatMap. Devido à incompetência do Java com relação a Generics,
 * isso é necessário para assegurar que o tipo de retorno seja o mesmo da coleção. A implementação padrão (chamado o método da super
 * classe é suficiente).
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public abstract class AbstractMutableCollection<T, U extends SharpCollection<T>> extends AbstractSharpCollection<T, U> 
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
