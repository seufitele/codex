package br.com.codex.collections.mutable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import br.com.codex.collections.AbstractMutableCollection;
import br.com.codex.collections.Builder;
import br.com.codex.collections.SharpCollection;
import br.com.codex.collections.builders.ArrayBuilder;
import br.com.codex.function.Function1;

/**
 * Essa classe representa uma lista mutável, cujos elementos são armazenados num ArrayList usando composição. <br/>
 * 
 * Os métodos desta classe são utilizados para favorecer a programação funcional, e possuem a mesma nomeclatura 
 * encontrada no Scala. Os métodos desta classe são extremamente poderosos, e simplificam a codificação de estruturas mais complexas.<br/> <br/>
 * 
 * Coleções como funções parciais: <br/>
 * Coleções podem ser vistas como funções parciais. ListSharp, por exemplo, é uma função que faz corresponder, para cada inteiro, um elemento do tipo T.
 * 
 * @author f9540702 Vinícius Seufitele Pinto
 */
public class ListSharp<T> extends AbstractMutableCollection<T, ListSharp<T>> implements Function1<Integer, T>
{
	private final List<T> backingList;

	/**
	 * Construtor privado. Instâncias devem ser criadas com o 'from'
	 */
	private ListSharp()
	{
		backingList = new ArrayList<T>();
	}

	private ListSharp(final List<T> backList)
	{
		this.backingList = backList;
	}

	/**
	 * Constrói uma instância de ListSharp vazia.
	 * @param <A> O tipo de dados da instância
	 * @return Uma instância de ListSharp vazia.
	 */
	public static <A> ListSharp<A> empty()
	{
		return new ListSharp<A>();
	}

	/**
	 * Cria uma instância de ListSharp a partir da lista passada como parâmetro. <br/>
	 * ATENÇÃO: Mudanças estruturais na lista original também afetam esta lista.
	 * @param <T> O tipo de dados guardado pela lista
	 * @param theList A lista a partir da qual esta ListSharp será criada
	 * @return Uma listSharp que contém a referência à lista passada como parâmetro
	 */
	public static <T> ListSharp<T> from(final List<T> theList)
	{
		return new ListSharp<T>(theList);
	}

	/**
	 * Cria uma instância de ListSharp a partir dos elementos existentes
	 * no iterable passado como parâmetro. A ordem da adição dos elementos será
	 * a mesma ordem do iterable.
	 * 
	 * @param <T> O tipo de dados da lista
	 * @param theIterable O iterator que contém os elementos
	 * @return Uma lista criada a partir da adição de todos os elementos do iterador
	 */
	public static <T> ListSharp<T> from(final Iterable<T> theIterable)
	{
		final ListSharp<T> retorno = new ListSharp<T>();
		for (final T ele : theIterable)
		{
			retorno.add(ele);
		}
		return retorno;
	}
	
	/**
	 * Cria uma nova ListSharp, a partir dos valores passados como parâmetro. <br/>
	 * Esse método é uma forma mais compacta de se criar ListSharp.
	 * @param <A> O tipo de dados da ListSharp a ser retornada. 
	 * @param collection A ListSharp a ser criada, a partir dos valores
	 * @return Uma nova ListSharp, cujos elementos são os elementos passados como parâmetro
	 */
	public static <T> ListSharp<T> from(final T... valores)
	{
		final ListSharp<T> retorno = new ListSharp<T>();

		for (final T ele : valores)
		{
			retorno.add(ele);
		}
		return retorno;
	}

	@Override
	public int size()
	{
		return backingList.size();
	}

	@Override
	public boolean contains(final T element)
	{
		return backingList.contains(element);
	}

	@Override
	public Iterator<T> iterator()
	{
		return backingList.iterator();
	}

	@Override
	public ListSharp<T> add(final T element)
	{
		backingList.add(element);
		return this;
	}

	@Override
	public ListSharp<T> remove(final T element)
	{
		backingList.remove(element);
		return this;
	}

	@Override
	public ListSharp<T> clear()
	{
		backingList.clear();
		return this;
	}

	@Override
	public <B> Builder<B, SharpCollection<B>> builder()
	{
		return new ArrayBuilder<B>();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (backingList == null ? 0 : backingList.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final ListSharp other = (ListSharp) obj;
		if (backingList == null)
		{
			if (other.backingList != null)
			{
				return false;
			}
		}
		else if (!backingList.equals(other.backingList))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return backingList.toString();
	}

	@Override
	public <B> ListSharp<B> map(final Function1<T, B> function)
	{
		return (ListSharp<B>) super.map(function);
	}

	@Override
	public <B> ListSharp<B> flatMap(final Function1<T, ? extends SharpCollection<B>> function)
	{
		return (ListSharp<B>) super.flatMap(function);
	}

	/**
	 * Transforma esta coleção em um mapa de coleções de acordo com uma função discriminadora. </br>
	 * Em outras palavras, aplica a coleção passada como parâmetro a cada elemento desta coleção,
	 * criando um mapa onde a chave é o resultado da função aplicada, e o valor é uma coleção de
	 * elementos desta coleção que retornam aquele valor à função.
	 * @param <B> O tipo de retorno da função
	 * @param funcao Uma função que transforma um item desta coleção em outro tipo
	 * @return Um mapa, onde a chave é o resultado da função, e os valores uma coleção de elementos
	 * cujo resultado da função aplicada seja o mesmo.
	 */
	public <A> MapSharp<A, ListSharp<T>> groupBy(final Function1<T, A> function)
	{
		final MapSharp<A, ListSharp<T>> mapaRetorno = MapSharp.from(new HashMap<A, ListSharp<T>>());

		for (final T curEle : this)
		{
			final A value = function.apply(curEle);
			mapaRetorno.add(value, mapaRetorno.getOrElse(value, ListSharp.<T>empty()).add(curEle));
		}
		return mapaRetorno;
	}

	@Override
	public T apply(final Integer param)
	{
		return backingList.get(param);
	}
	
	@Override
	public ListSharp<T> take(Integer num)
	{
		return ListSharp.from(backingList.subList(0, Math.min(num, this.size())));
	}

	/**
	 * Retorna a referência à lista que contém os elementos desta coleção. <br/>
	 * ATENÇÃO: Mudanças estruturais na lista retornada também afetam esta coleção.
	 * @return A referência à lista que serve como container de dados desta coleção
	 */
	@Override
	public List<T> toList()
	{
		return backingList;
	}

}
