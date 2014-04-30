package com.github.detentor.codex.collections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Function2;
import com.github.detentor.codex.function.PartialFunction1;
import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;
import com.github.detentor.codex.util.Builders;

/**
 * Para criar uma coleção (imutável) com base nesta implementação, basta prover o código dos seguintes métodos: <br/><br/>
 * 
 * {@link Iterable#iterator() iterator()}, {@link SharpCollection#size() size()} , {@link SharpCollection#builder() builder()} <br/><br/>
 * 
 * Não esquecer de sobrescrever o equals e o hashcode também. <br/><br/>
 * 
 * Para coleções mutáveis, veja {@link AbstractMutableCollection}. <br/><br/>
 * Subclasses que não possuam size() facilmente calculável devem sobrescrever o método isEmpty(). <br/> <br/>
 * 
 * NOTA: Subclasses devem sempre dar override nos métodos {@link #map(Function1) map}, {@link #collect(PartialFunction1) collect},
 * {@link #flatMap(Function1) flatMap} e {@link #zipWithIndex() zipWithIndex}.   
 * Devido à incompetência do Java com relação a Generics, isso é necessário para assegurar que o tipo
 * de retorno seja o mesmo da coleção. A implementação padrão (chamado o método da super classe é suficiente).
 * 
 * @author Vinícius Seufitele Pinto
 *
 * @param <T> O tipo de elementos da coleção
 */
public interface SharpCollection<T> extends Iterable<T>
{
	 /**
	 * Retorna o número de elementos desta coleção.
     *
     * @return O número de elementos desta coleção
     */
    int size();
    
    /**
     * Retorna um iterador sobre os elementos desta coleção. Não há garantias quanto à
     * ordem que os elementos serão retornados (a menos que esta coleção seja uma instância
     * de uma classe que provê essa garantia).
     * 
     * @return um <tt>Iterator</tt> sobre os elementos desta coleção
     */
    @Override
	public Iterator<T> iterator();
    
    /**
	 * Retorna um builder para esta coleção. Um builder é uma classe que permite a adição de elementos,
	 * e ela determina qual o tipo da coleção retornada.
	 * @see Builder
	 */
	<B, U extends SharpCollection<B>> Builder<B, U> builder();
	

//TODO: Verificar se os métodos podem ficar em todas as instâncias, ou somente nas mutáveis
//	/**
//     * Retorna uma <b>nova</b> coleção, com a adição do elemento passado como parâmetro. <br/><br/>
//     * 
//     * @param element o elemento a ser adicionado na coleção
//     * @return Uma nova coleção, com o elemento passado como parâmetro adicionado
//     */
//    SharpCollection<T> add(final T element);
//    
//    /**
//     * Retorna uma <b>nova</b> coleção, com a remoção do elemento passado como parâmetro. <br/><br/>
//     * Formalmente, remove um elemento <tt>e</tt> tal que
//     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>, se esta coleção
//     * contém um ou mais tais elementos.
//     *
//     * @param element O elemento a ser removido desta coleção, se presente
//     * @return Uma nova coleção, com o elemento passado como parâmetro removido
//     */
//    SharpCollection<T> remove(final T element);
//    
//    /**
//     * Retorna uma <b>nova</b> coleção, após a adição dos elementos do iterable passado como parâmetro. <br/><br/>
//     *
//     * @param col Coleção contendo elemento a serem adicionados nesta coleção
//     * @return Uma nova coleção, após os elementos passados como parâmetro serem adicionados
//     */
//    default SharpCollection<T> addAll(final Iterable<? extends T> col)
//    {
//    	SharpCollection<T> colRetorno = this;
//    	
//    	for (T ele : col)
//    	{
//    		colRetorno = colRetorno.add(ele);
//    	}
//    	return colRetorno;
//    }
//    
//    /**
//     * Retorna uma <b>nova</b> coleção, após a remoção dos elementos do iterable passado como parâmetro. <br/><br/>
//     * A coleção retornada não conterá elementos em comum com o iterable.
//     *
//     * @param col A coleção contendo elementos a serem removidos desta coleção
//     * @return  Uma nova coleção, após os elementos passados como parâmetro serem removidos
//     */
//    default SharpCollection<T> removeAll(final Iterable<T> col)
//    {
//    	SharpCollection<T> colRetorno = this;
//    	for (T ele : col)
//    	{
//    		colRetorno = colRetorno.remove(ele);
//    	}
//    	return colRetorno;
//    }

    /**
     * retorna <tt>true</tt> se esta coleção não contém elementos
     * @return <tt>true</tt> se esta coleção não contém elementos
     */
    default boolean isEmpty() 
    {
    	//Poderia ser substituído por !this.iterator().hasNext() ?
    	return this.size() == 0;
    }
    
    /**
     * retorna <tt>true</tt> se esta coleção contém elementos
     * @return <tt>true</tt> se esta coleção contém elementos
     */
    default boolean notEmpty()
    {
    	return ! isEmpty();
    }
    
    /**
     * Retorna <tt>true</tt> se esta coleção contém o elemento especificado.
     * Formalmente, retorna <tt>true</tt> se e somente se esta coleção contém pelo menos
     * um elemento <tt>e</tt> tal que <tt>(element==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;element.equals(e))</tt>.
     *
     * @param element O elemento cuja presença nesta coleção será testada
     * @return <tt>true</tt> se esta coleção contém o elemento especificado
     */
    default boolean contains(T element)
    {
    	for (final T ele : this)
		{
    		//TODO: a comparação do equals irá falhar para os casos que a coleção permitir null
			if (ele.equals(element))
			{
				return true;
			}
		}
		return false;
    }
    
    /**
     * Retorna <tt>true</tt> se esta coleção contém todos os elementos do iterator especificado.
     *
     * @param  col A coleção a ser verificada
     * @return <tt>true</tt> Se esta coleção contém todos os elementos do iterator especificado
     */
    default boolean containsAll(final Iterable<T> col)
    {
    	for (final T ele : col)
		{
			if (!this.contains(ele))
			{
				return false;
			}
		}
		return true;
    }
    
    /**
     * Retorna <tt>true</tt> se esta coleção contém algum dos elementos do iterator especificado.
     *
     * @param  col A coleção a ser verificada
     * @return <tt>true</tt> Se esta coleção algum dos elementos do iterator especificado
     */
    default boolean containsAny(final Iterable<T> col)
    {
    	for (final T ele : col)
		{
			if (this.contains(ele))
			{
				return true;
			}
		}
		return false;
    }

    //Métodos diversos:
    
    
	
//	/**
//	 TODO: Esse método deve utilizar o sliding (a ser criado).
//	 * Particiona esta coleção em coleções de tamanho fixo.
//	 * @param size O tamanho de cada coleção a ser retornada
//	 * @return Uma coleção de coleções, onde cada uma delas terá size itens. A última pode estar possivelmente truncada.
//	 */
//	SharpCollection<? extends SharpCollection<T>> grouped(final Integer size);
//	if (size <= 0)
//	{
//		throw new IllegalArgumentException("size deve ser maior do que zero");
//	}
//
//	final Builder<U, SharpCollection<U>> colOfCols = this.builder();
//	final Iterator<T> ite = this.iterator();
//	
//	int count = 0;
//
//	Builder<T, SharpCollection<T>> curColecao = this.builder();
//	
//	while (ite.hasNext())
//	{
//		count++;
//		curColecao.add(ite.next());
//		
//		if (count == size)
//		{
//			colOfCols.add((U)curColecao.result());
//			curColecao = this.builder();
//			count = 0;
//		}
//	}
//
//	if (count != 0)
//	{
//		colOfCols.add((U)curColecao.result());
//	}
//	return colOfCols.result();

	/**
	 * Chama o método toString em cada elemento desta coleção, concatenando os resultados.
	 * @return Uma string que representa a chamada ao método toString em cada elemento da coleção.
	 */
	default String mkString()
	{
		return mkString("", "", "");
	}

	/**
	 * Mostra todos os elementos desta coleção numa string utilizando, entre elas o separador passado
	 * como parâmetro.
	 * @param separator O separador a ser utilizado entre cada chamada ao método toString
	 * @return Uma string representada pela chamada ao método toString de cada elemento desta coleção, separados
	 * pelo separador passado como parâmetro.
	 */
	default String mkString(final String separator)
	{
		return mkString("", separator, "");
	}
	
	/**
	 * Mostra todos os elementos desta coleção numa string utilizando, entre elas o separador passado
	 * como parâmetro, iniciando por start e terminando com end.
	 * @param start A string a ser colocada no início
	 * @param separator O separador a ser utilizado entre cada chamada ao método toString
	 * @param end A string a ser colocada no fim
	 * @return Uma string representada pela chamada ao método toString de cada elemento desta coleção, separados
	 * pelo separador passado como parâmetro, começando por start e terminando com end.
	 */
	default String mkString(final String start, final String separator, final String end)
	{
		final StringBuilder sBuilder = new StringBuilder();
		final Iterator<T> ite = this.iterator();

		sBuilder.append(start);

		while (ite.hasNext())
		{
			sBuilder.append(ite.next());
			if (ite.hasNext())
			{
				sBuilder.append(separator);
			}
		}
		sBuilder.append(end);
		return sBuilder.toString();
	}
	
	
	//MÉTODOS DE ORDEM SUPERIOR

	/**
	 * Reduz os elementos desta coleção a partir da função de redução passada como parâmetro. <br/>
	 * A ordem que as operações são executadas nos elementos não está especificada, e pode ser não-determinística. <br/>
	 * 
	 * @param function Uma função de redução associativa a ser aplicada nos elementos desta coleção 
	 * @return Uma Option que poderá conter um elemento T, retornado a partir da redução dos elementos desta coleção.
	 */
	default Option<T> reduce(final Function2<? super T, ? super T, T> function)
	{
		if (this.isEmpty())
		{
			return Option.empty();
		}
		
		final Iterator<T> ite = this.iterator();
		T ele = ite.next();
		
		while (ite.hasNext())
		{
			ele = function.apply(ele, ite.next());
		}
		
		return Option.from(ele);
	}

	/**
	 * Aplica uma transformação nos elementos desta coleção, 
	 * a partir de um valor inicial e uma função associativa. <br/>
	 * A ordem que as operações são executadas nos elementos não está especificada, e pode ser não-determinística. <br/>
	 * 
	 * @param startValue O valor inicial, a ser retornado caso esta coleção esteja vazia
	 * @param function Uma função de transformação associativa a ser aplicada nos elementos desta coleção
	 * @return Um elemento do tipo B, retornado a partir da transformação dos elementos desta coleção.
	 */
	default <B> B fold(final B startValue, final Function2<? super B, ? super T, B> function)
	{
		final Iterator<T> ite = this.iterator();
		B ele = startValue;

		while (ite.hasNext())
		{
			ele = function.apply(ele, ite.next());
		}
		
		return ele;
	}
	
	/**
	 * Retorna o primeiro elemento (ordem definida pelo iterador da coleção) que satisfaz o predicado. <br/>
	 * 
	 * @param pred O predicado a ser utilizado para testar o elemento
	 * @return Uma Option que conterá o elemento se ele existir.
	 */
	default Option<T> find(final Function1<? super T, Boolean> pred)
	{
		final Iterator<T> ite = this.iterator();

		while (ite.hasNext())
		{
			final T curEle = ite.next();

			if (pred.apply(curEle))
			{
				return Option.from(curEle);
			}
		}
		return Option.empty();
	}
	
	/**
	 * Seleciona todos os elementos desta coleções que satisfazem um determinado predicado
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return Uma nova coleção consistindo de todos os elementos desta coleção que
	 * satisfazem o predicado pred. A ordem dos elementos será a mesma ordem retornada pelo iterator.
	 */
	default SharpCollection<T> filter(final Function1<? super T, Boolean> pred)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = builder();

		for (final T ele : this)
		{
			if (pred.apply(ele))
			{
				colecaoRetorno.add(ele);
			}
		}
		return colecaoRetorno.result();
	}
	
	/**
	 * Divide essa coleção em duas coleções, onde o primeiro elemento da tupla representa os elementos
	 * cujo predicado é satisfeito, e o segundo os elementos que não é
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return Uma tupla onde o primeiro elemento contém uma coleção cujos elementos satisfazem o predicado,
	 * e o segundo não satisfazem
	 */
	default Tuple2<? extends SharpCollection<T>, ? extends SharpCollection<T>> partition(final Function1<? super T, Boolean> pred)
	{
		final Builder<T, SharpCollection<T>> predTrue = builder();
		final Builder<T, SharpCollection<T>> predFalse = builder();
		
		for (final T ele : this)
		{
			if (pred.apply(ele))
			{
				predTrue.add(ele);
			}
			else
			{
				predFalse.add(ele);
			}
		}
		return Tuple2.from(predTrue.result(), predFalse.result());
	}
	
	/**
	 * Verifica se o predicado passado como parâmetro é satisfeito por algum elemento desta coleção.
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return true se existe algum elemento que satisfaz o predicado, ou false se não houver
	 */
	default boolean exists(final Function1<? super T, Boolean> pred)
	{
		for (final T ele : this)
		{
			if (pred.apply(ele))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Verifica se o predicado passado como parâmetro é satisfeito por todos os elementos desta coleção.
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return true se todos os elementos desta coleção satisfazem o predicado, ou false se existe algum
	 * elemento que não satisfaz o predicado.
	 */
	default boolean forall(final Function1<? super T, Boolean> pred)
	{
		for (final T ele : this)
		{
			if (!pred.apply(ele))
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Conta o número de elementos desta coleção que satisfazem o predicado passado como parâmetro.
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return O número de elementos desta coleção que satisfazem o predicado 
	 */
	default Integer count(final Function1<? super T, Boolean> pred)
	{
		int numElementos = 0;

		for (final T ele : this)
		{
			if (pred.apply(ele))
			{
				numElementos++;
			}
		}
		return numElementos;
	}
	
	/**
	 * Constrói uma nova coleção a partir da aplicação da função passada como parâmetro em cada elemento da coleção. <br/>
	 * A ordem é preservada, se ela estiver bem-definida.
	 * @param <B> O tipo da nova coleção.
	 * @param function Uma função que recebe um elemento desta coleção, e retorna um elemento de (potencialmente) outro tipo.
	 * @return Uma nova coleção, a partir da aplicação da função para cada elemento.
	 */
	default <B> SharpCollection<B> map(final Function1<? super T, B> function)
	{
		final Builder<B, SharpCollection<B>> colecaoRetorno = builder();

		for (final T ele : this)
		{
			colecaoRetorno.add(function.apply(ele));
		}
		return colecaoRetorno.result();
	}
	
	/**
	 * Constrói uma nova coleção a partir da aplicação da função parcial passada como parâmetro em cada elemento da coleção
	 * onde a função parcial está definida. A ordem é preservada, se ela estiver bem-definida. <br/><br/>
	 * Nos casos onde é necessário usar um filtro antes de aplicar um mapa, considere utilizar esta função. <br/>
	 * @param <B> O tipo da nova coleção.
	 * @param function Uma função que recebe um elemento desta coleção, e retorna um elemento de (potencialmente) outro tipo.
	 * @return Uma nova coleção, a partir da aplicação da função parcial para cada elemento onde ela está definida.
	 */
	default <B> SharpCollection<B> collect(final PartialFunction1<? super T, B> pFunction)
	{
		final Builder<B, SharpCollection<B>> colecaoRetorno = builder();

		for (final T ele : this)
		{
			if (pFunction.isDefinedAt(ele))
			{
				colecaoRetorno.add(pFunction.apply(ele));
			}
		}
		return colecaoRetorno.result();
	}
	
	/**
	 * Constrói uma nova coleção, a partir da aplicação da função passada como 
	 * parâmetro em cada elemento da coleção, coletando os resultados numa única coleção. <br/>
	 * @param <B> O tipo da nova coleção
	 * @param function Uma função que recebe um elemento desta coleção, e retorna um 
	 * iterator de elementos de (potencialmente) outro tipo.
	 * @return Uma nova coleção, a partir da aplicação da função para cada elemento, concatenando os elementos
	 * das coleções.
	 */
	default <B> SharpCollection<B> flatMap(final Function1<? super T, ? extends Iterable<B>> function)
	{
		final Builder<B, SharpCollection<B>> colecaoRetorno = builder();

		for (final T ele : this)
		{
			colecaoRetorno.addAll(function.apply(ele));
		}
		return colecaoRetorno.result();
	}

	/**
	 * Calcula a interseção desta coleção com o iterator passado como parâmetro. <br/>
	 * Serão coletados todos os elementos desta coleção que estejam contidos no iterator. <br/>
	 * @see java.util.Collection#retainAll
	 * @param <E> O tipo de dados desta coleção
	 * @param withCollection O iterator a ser verificado a interseção
	 * @return Uma nova coleção com todos os elementos desta coleção que também estejam contidos no iterator
	 */
	default SharpCollection<T> intersect(final Iterable<T> withCollection)
	{
		//TODO: Esse método precisa de melhoria no algoritmo - complexidade de memória está alta
		final Set<T> hashEle = new HashSet<T>();
		
		for (T ele : withCollection)
		{
			hashEle.add(ele);
		}

		final Builder<T, SharpCollection<T>> colecaoRetorno = builder();

		for (final T curEle : this)
		{
			if (hashEle.contains(curEle))
			{
				colecaoRetorno.add(curEle);
			}
		}
		return colecaoRetorno.result();
	}
	
	/**
	 * Retorna uma coleção que possui somente elementos distintos entre si (de acordo com o equals). <br/>
	 * A ordem é preservada, se ela estiver bem-definida.
	 * @return Uma coleção onde os elementos são todos distintos entre si.
	 */
	default SharpCollection<T> distinct()
	{
		//TODO: Esse método precisa de melhoria no algoritmo - complexidade de memória está alta
		final Set<T> hashEles = new LinkedHashSet<T>();
		
		for (T ele : this)
		{
			hashEles.add(ele);
		}
		
		final Builder<T, SharpCollection<T>> colecaoRetorno = builder();
		
		for (T ele : hashEles)
		{
			colecaoRetorno.add(ele);
		}

		return colecaoRetorno.result();
	}
	
	//MÉTODOS DE COMPARAÇÃO:
	
	/**
	 * Retorna o valor mínimo, a partir da função de comparação passada como parâmetro. <br/>
	 * Formalmente, retorna um valor T tal que não exista um elemento U onde comparator.compare(T, U) < 0. <br/>
	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado. 
	 * @param comparator A função de comparação entre os elementos.
	 * @return Uma Option que conterá o elemento com o menor valor na comparação, se ele existir
	 */
	default <K extends Comparator<? super T>> Option<T> min(final K comparator)
	{
		return reduce((param1, param2) -> comparator.compare(param1, param2) <= 0 ? param1 : param2);
	}
	
	/**
	 * Retorna o valor mínimo, a partir de uma função transformadora para um elemento comparável. <br/>
	 * Formalmente, retorna um valor T tal que não exista um elemento U onde func(T).compareTo(func(U)) < 0. <br/>
	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado. 
	 * @param func A função de transformação do tipo T em um tipo comparável.
	 * @return Uma Option que conterá o elemento com o menor valor na comparação, se ele existir
	 */
	default <K extends Comparable<? super K>> Option<T> min(final Function1<? super T, K> func)
	{
		return min((param1, param2) -> func.apply(param1).compareTo(func.apply(param2)));
	}
	
	/**
	 * Retorna o valor máximo, a partir da função de comparação passada como parâmetro. <br/>
	 * Formalmente, retorna um valor T tal que não exista um elemento U onde a comparator.compare(T, U) > 0. <br/>
	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado. 
	 * @param comparator A função de comparação entre os elementos.
	 * @return Uma Option que conterá o elemento com o maior valor na comparação, se ele existir
	 */
	default Option<T> max(final Comparator<? super T> comparator)
	{
		return reduce((param1, param2) -> comparator.compare(param1, param2) >= 0 ? param1 : param2);
	}
	
	/**
	 * Retorna o valor máximo, a partir de uma função transformadora para um elemento comparável. <br/>
	 * Formalmente, retorna um valor T tal que não exista um elemento U onde func(T).compareTo(func(U)) > 0. <br/>
	 * Havendo mais de um valor T com essa propriedade, o primeiro deles é retornado. 
	 * @param func A função de transformação do tipo T em um tipo comparável.
	 * @return Uma Option que conterá o elemento com o maior valor na comparação, se ele existir
	 */
	default <K extends Comparable<? super K>> Option<T> max(final Function1<? super T, K> func)
	{
		return max((param1, param2) -> func.apply(param1).compareTo(func.apply(param2)));
	}
	
	//MÉTODOS DE COLEÇÕES
	
	/**
	 * Retorna um iterable a partir de um builder. <br/>
	 *
	 * @param builder O builder cujos elementos desta coleção serão adicionados
	 * @return O resultado da adição de todos os elementos desta coleção ao builder.
	 */
	default <U extends Iterable<T>> U toColl(final Builder<T, U> builder)
	{
		builder.addAll(this);
		return builder.result();
	}
	
	/**
	 * Retorna um {@link List} a partir desta coleção.
	 * @return Uma instância de {@link ArrayList} a partir dos elementos desta coleção.
	 */
	default List<T> toList()
	{
		return toColl(Builders.arrayListBuilder());
	}
	
	/**
	 * Retorna um {@link Set} a partir desta coleção.
	 * @return Uma instância de {@link HashSet} a partir dos elementos desta coleção.
	 */
	default Set<T> toSet()
	{
		return toColl(Builders.hashSetBuilder());
	}
	
//	/**
//	 * Ordena esta coleção, de acordo com a função mapeamento passada como parâmetro. <br/>
//	 * Como exemplo, se você tiver uma lista de Strings e quiser ordená-las pelo tamanho, basta passar uma função
//	 * que transforma uma String em seu tamanho.
//	 * 
//	 * @param mapFunction A função de mapeamento para um tipo comparável
//	 * @return Uma nova coleção, com os elementos ordenados de acordo com a função de mapeamento
//	 */
//	<K extends Comparable<? super K>> SharpCollection<T> sortWith(final Function1<? super T, K> mapFunction);
	
//	/**
//	 * Classe com a implementação default do comparator
//	 */
//	protected static final class DefaultComparator<A extends Comparable<A>> implements Comparator<A>, Serializable
//	{
//		private static final long serialVersionUID = 6163897449143010763L;
//
//		public DefaultComparator()
//		{
//			//Definindo só para aumentar a visibilidade do construtor
//		}
//
//		@Override
//		public int compare(final A ob1, final A ob2)
//		{
//			return ob1.compareTo(ob2);
//		}
//	}
}
