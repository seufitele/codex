package com.github.detentor.codex.collections.mutable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import com.github.detentor.codex.collections.AbstractMutableGenericCollection;
import com.github.detentor.codex.collections.Builder;
import com.github.detentor.codex.collections.SharpCollection;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.PartialFunction1;
import com.github.detentor.codex.product.Tuple2;

/**
 * Essa classe representa uma conjunto (Set) mutável, cujos elementos são armazenados num HashSet usando composição. <br/>
 * 
 * Os métodos desta classe são utilizados para favorecer a programação funcional, e possuem a mesma nomeclatura 
 * encontrada no Scala. Os métodos desta classe são extremamente poderosos, e simplificam a codificação de estruturas
 * mais complexas.<br/> <br/>
 *
 * Note que esta classe é uma função que está definida para os valores que ela contém. Em particular, uma chamada
 * ao método {@link #apply(Object) apply} retornará <tt>true</tt> se, e somente se, esta coleção contém o elemento.
 * 
 * @author Vinícius Seufitele Pinto
 */
public class SetSharp<T> extends AbstractMutableGenericCollection<T, SetSharp<T>> implements PartialFunction1<T, Boolean>, Serializable
{
	private static final long serialVersionUID = 1L;

	private final Set<T> backingSet;

	/**
	 */
	protected SetSharp()
	{
		this(new HashSet<T>());
	}

	/**
	 * Construtor privado. Instâncias devem ser criadas com o 'from'
	 */
	protected SetSharp(final Set<T> fromSet)
	{
		super();
		backingSet = fromSet;
	}

	/**
	 * Cria uma instância de SetSharp a partir dos elementos existentes no iterable 
	 * passado como parâmetro. A ordem da adição dos elementos será a mesma ordem do iterable. <br/><br/>
	 * Se o iterable for do tipo {@link TreeSet} ou {@link LinkedHashSet} o tipo será preservado, 
	 * do contrário será retornada uma instância de SetSharp que utiliza o {@link HashSet}.
	 * 
	 * @param <T> O tipo de dados do set
	 * @param theIterable O iterator que contém os elementos
	 * @return Um set criado a partir da adição de todos os elementos do iterador
	 */
	public static <T> SetSharp<T> from(final Iterable<T> theIterable)
	{
		final SetSharp<T> retorno = SetSharp.empty(typeFromIterable(theIterable));
		retorno.addAll(theIterable);
		return retorno;
	}

	/**
	 * Cria um novo SetSharp, a partir dos valores passados como parâmetro. <br/>
	 * Esse método é uma forma mais compacta de se criar SetSharp.
	 * 
	 * @param <A> O tipo de dados do SetSharp a ser retornada.
	 * @param valores Os valores que irão compor o SetSharp
	 * @return Um novo SetSharp, cujos elementos são os elementos passados como parâmetro
	 */
	public static <T> SetSharp<T> from(final T... valores)
	{
		final SetSharp<T> retorno = SetSharp.empty();

		for (final T ele : valores)
		{
			retorno.add(ele);
		}
		return retorno;
	}
	
	/**
	 * Constrói uma instância de SetSharp vazia. <br/>
	 * Equivale a chamar o método {@link #empty(HASH_SET)}
	 * @param <T> O tipo de dados da instância
	 * @return Uma instância de SetSharp vazia.
	 */
	public static <T> SetSharp<T> empty()
	{
		return new SetSharp<T>();
	}
	
	/**
	 * Constrói uma instância de SetSharp vazia, baseado no tipo passado como parâmetro.
	 * 
	 * @param setType O tipo de SetSharp a ser construído
	 * @param <T> O tipo de dados da instância
	 * @return Uma instância de SetSharp vazia, do tipo definido.
	 */
	public static <T> SetSharp<T> empty(final SetSharpType setType)
	{
		Set<T> setInstance = null;
		
		switch(setType)
		{
			case LINKED_HASH_SET:
				setInstance = new LinkedHashSet<T>();
				break;
			case HASH_SET:
				setInstance = new HashSet<T>();
				break;
			case TREE_SET:
				setInstance = new TreeSet<T>();
				break;
			default:
				throw new IllegalArgumentException("Tipo de SetSharp não reconhecido");
		}
		return new SetSharp<T>(setInstance);
	}
	
	@Override
	public int size()
	{
		return backingSet.size();
	}
	
	@Override
	public boolean contains(final T element)
	{
		return backingSet.contains(element);
	}

	@Override
	public Iterator<T> iterator()
	{
		return backingSet.iterator();
	}

	@Override
	public SetSharp<T> add(final T element)
	{
		backingSet.add(element);
		return this;
	}

	@Override
	public SetSharp<T> remove(final T element)
	{
		backingSet.remove(element);
		return this;
	}

	@Override
	public SetSharp<T> clear()
	{
		backingSet.clear();
		return this;
	}
	
	/**
	 * Retorna o tipo de SetSharp apropriado para o iterable passado como parâmetro
	 * @param theIterable O iterable cujo SetSharpType será calculado
	 * @return
	 */
	private static <T> SetSharpType typeFromIterable(final Iterable<T> theIterable)
	{
		if (theIterable instanceof LinkedHashSet<?>)
		{
			return SetSharpType.LINKED_HASH_SET;
		}
		else if (theIterable instanceof TreeSet<?>)
		{
			return SetSharpType.TREE_SET;
		}
		
		//Retorna o tipo padrão para os outros casos
		return SetSharpType.HASH_SET;
	}

	@Override
	public <B> Builder<B, SharpCollection<B>> builder()
	{
		Builder<B, SharpCollection<B>> builderRetorno = null;
		
		if (backingSet instanceof LinkedHashSet<?>)
		{
			builderRetorno = new SetBuilder<B>(new LinkedHashSet<B>());
		}
		else if (backingSet instanceof TreeSet<?>)
		{
			builderRetorno = new SetBuilder<B>(new TreeSet<B>());
		}
		else
		{
			//Retorno padrão para um tipo não específico
			builderRetorno = new SetBuilder<B>(new HashSet<B>());
		}
		
		return builderRetorno;
	}
	
	/**
	 * Transforma este SetSharp em um {@link Set}
	 * @return O {@link Set} que representa esta coleção
	 */
	@Override
	public Set<T> toSet()
	{
		final Builder<T, SharpCollection<T>> builderRetorno = builder();
		
		for (T ele : this)
		{
			builderRetorno.add(ele);
		}
		
		return ((SetSharp<T>) builderRetorno.result()).backingSet;
	}

	@Override
	public <B> SetSharp<B> map(final Function1<? super T, B> function)
	{
		return (SetSharp<B>) super.map(function);
	}

	@Override
	public <B> SetSharp<B> flatMap(final Function1<? super T, ? extends Iterable<B>> function)
	{
		return (SetSharp<B>) super.flatMap(function);
	}
	
	@Override
	public <B> SetSharp<B> collect(final PartialFunction1<? super T, B> pFunction)
	{
		return (SetSharp<B>) super.collect(pFunction);
	}

	@Override
	public SetSharp<Tuple2<T, Integer>> zipWithIndex()
	{
		return (SetSharp<Tuple2<T, Integer>>) super.zipWithIndex();
	}

	/**
	 * Verifica se este conjunto possui o elemento passado como parâmetro
	 * @param param O elemento a ser verificado se existe no conjunto
	 * @return True se este conjunto contém o elemento, ou false se não contém
	 */
	@Override
	public Boolean apply(final T param)
	{
		return backingSet.contains(param);
	}
	
	@Override
	public boolean isDefinedAt(final T forValue)
	{
		return backingSet.contains(forValue);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (backingSet == null ? 0 : backingSet.hashCode());
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
		final SetSharp other = (SetSharp) obj;
		
		if (backingSet == null)
		{
			if (other.backingSet != null)
			{
				return false;
			}
		}
		else if (!backingSet.equals(other.backingSet))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return backingSet.toString();
	}
	
	/**
	 * Builder de Sets a partir de um backingSet apropriado
	 * @param <E>
	 */
	private static final class SetBuilder<E> implements Builder<E, SharpCollection<E>>
	{
		private final Set<E> theBackingSet;
		
		protected SetBuilder(final Set<E> backingSet)
		{
			super();
			this.theBackingSet = backingSet;
		}

		@Override
		public void add(final E element)
		{
			theBackingSet.add(element);
		}

		@Override
		public SetSharp<E> result()
		{
			return new SetSharp<E>(theBackingSet);
		}
	}
	
	public enum SetSharpType
	{
		HASH_SET, LINKED_HASH_SET, TREE_SET;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SetSharp<T> sorted()
	{
		return sorted(new DefaultComparator());
	}

	/**
	 * Transforma este SetSharp num Set que mantém os elementos ordenados. <br/>
	 * {@inheritDoc}
	 */
	@Override
	public SetSharp<T> sorted(final Comparator<? super T> comparator)
	{
		return new SetSharp<T>(new TreeSet<T>(comparator)).addAll(this);
	}
}
