package com.github.detentor.codex.collections;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Function2;
import com.github.detentor.codex.function.PartialFunction1;
import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;

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
	<B> Builder<B, SharpCollection<B>> builder();
	
	/**
	 * Ordena esta coleção, de acordo com a função de ordenação passada como parâmetro. <br/>
	 *
	 * @param comparator A função de comparação, a ser utilizada para ordenar os elementos
	 * @return Uma nova coleção com os elementos ordenados
	 */
	SharpCollection<T> sorted(final Comparator<? super T> comparator);
    
    
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
     *
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
    
    /**
	 * Retorna o primeiro elemento desta coleção. <br/><br/>
	 * ATENÇÃO: Para coleções onde a posição de um elemento não está bem-definida (ex: sets), a chamada
	 * sucessiva a este método pode retornar elementos distintos.
	 * @return O primeiro elemento desta coleção.
	 * @throws NoSuchElementException Se a coleção estiver vazia
	 */
	default T head()
	{
		if (this.isEmpty())
		{
			throw new NoSuchElementException("head foi chamado para uma coleção vazia");
		}
		
		return this.iterator().next();
	}
	
	 /**
	 * Retorna uma instância de Option que contém o primeiro elemento, se ele existir, 
	 * ou uma Option vazia, se a coleção estiver vazia. 
	 * @see Option 
	 * @return O primeiro elemento desta coleção como uma instância de Option
	 */
	default Option<T> headOption()
	{
		return this.isEmpty() ? Option.<T> empty() : Option.from(this.head());
	}
	
	/**
	 * Retorna o último elemento desta coleção. <br/><br/>
	 * ATENÇÃO: Para coleções onde a posição de um elemento não está bem-definida (ex: sets), a chamada
	 * sucessiva a este método pode retornar elementos distintos.
	 * @return O último elemento desta coleção.
	 * @throws NoSuchElementException Se a coleção estiver vazia
	 */
	default T last()
	{
		return takeRight(1).head();
	}
	
	/**
	 * Retorna uma instância de Option que contém o último elemento, 
	 * se ele existir, ou uma Option vazia, se a coleção estiver vazia. 
	 * @see Option 
	 * @return O primeiro elemento desta coleção como uma instância de Option
	 */
	default Option<T> lastOption()
	{
		return this.isEmpty() ? Option.<T> empty() : Option.from(this.last());
	}
	
//	/**
//	//TODO: por que existe tail na sharpCollection? parece mais coisas de seq, e não de sharpCollection. drop(1)
	// pode ser usado ao invés
//	 * Seleciona todos os elementos exceto o primeiro.<br/><br/>
//	 * ATENÇÃO: Para coleções onde a posição de um elemento não está bem-definida (ex: sets), este método
//	 * retornará todos os elementos com exceção do primeiro elemento retornado pelo iterator.
//	 * @return Uma coleção que consiste de todos os elementos desta coleção, exceto o primeiro
//	 * @throws NoSuchElementException Se esta coleção estiver vazia
//	 */
//	default SharpCollection<T> tail()
//	{
//		if (this.isEmpty())
//		{
//			throw new NoSuchElementException("tail foi chamado para uma coleção vazia");
//		}
//		
//		//TODO: por que esse método dispara exceção e o drop(1) não?
//		return drop(1);
//	}

	/**
	 * Pega até os 'num' primeiros elementos desta coleção. 
	 * Se a coleção não possuir 'num' elementos, retorna todos eles. <br/>
	 * ATENÇÃO: Não afeta a lista original.
	 * @param num O número de elementos a pegar desta coleção.
	 * @return Uma nova coleção, com os 'num' primeiros elementos dela.
	 */
	default SharpCollection<T> take(final Integer num)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = this.builder();
		final Iterator<T> ite = this.iterator();
		int count = 0;

		while (count++ < num && ite.hasNext())
		{
			colecaoRetorno.add(ite.next());
		}
		return colecaoRetorno.result();
	}
	
	/**
	 * Pega até os 'num' últimos elementos desta coleção. 
	 * Se a coleção não possuir 'num' elementos, retorna todos eles. <br/>
	 * ATENÇÃO: Não afeta a lista original.
	 * @param num O número de elementos a pegar desta coleção.
	 * @return Uma nova coleção, com os 'num' últimos elementos dela.
	 */
	default SharpCollection<T> takeRight(final Integer num)
	{
		final int eleToSkip = Math.max(this.size() - num, 0);
		final Builder<T, SharpCollection<T>> colecaoRetorno = this.builder();
		final Iterator<T> ite = this.iterator();
		int curCount = 0;

		while (ite.hasNext() && curCount < eleToSkip)
		{
			ite.next();
			curCount++;
		}

		while (ite.hasNext())
		{
			colecaoRetorno.add(ite.next());
		}
		return colecaoRetorno.result();
	}
	
	/**
	 * Retorna esta coleção sem os 'num' primeiros elementos. 
	 * Se a coleção não possuir 'num' elementos, uma coleção vazia será retornada.<br/>
	 * ATENÇÃO: Não afeta a lista original.
	 * @param num O número de elementos a 'pular' desta coleção.
	 * @return Uma nova coleção, composta pelos elementos desta coleção, exceto os 'num' primeiros.
	 */
	default SharpCollection<T> drop(final Integer num)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = this.builder();
		final Iterator<T> ite = this.iterator();

		int count = 0;

		while (count++ < num && ite.hasNext())
		{
			ite.next();
		}

		while (ite.hasNext())
		{
			colecaoRetorno.add(ite.next());
		}

		return colecaoRetorno.result();
	}
	
	/**
	 * Retorna esta coleção sem os 'num' últimos elementos. 
	 * Se a coleção não possuir 'num' elementos, uma coleção vazia será retornada.<br/>
	 * ATENÇÃO: Não afeta a lista original.
	 * @param num O número de elementos a 'pular' (contando da direita para a esquerda) desta coleção.
	 * @return Uma nova coleção, composta pelos elementos desta coleção, exceto os 'num' últimos.
	 */
	default SharpCollection<T> dropRight(final Integer num)
	{
		final int toAdd = Math.max(0, this.size() - num);
		final Builder<T, SharpCollection<T>> colecaoRetorno = this.builder();
		final Iterator<T> ite = this.iterator();

		int count = 0;

		while (ite.hasNext() && count++ < toAdd)
		{
			colecaoRetorno.add(ite.next());
		}

		return colecaoRetorno.result();
	}
	
	/**
	 * Divide esta coleção em duas na posição definida. <br/>
	 * Equivalente a {@link #take(num)} e {@link #drop(num)}, mas mais eficiente.
	 * @param num A posição em que a lista será dividida (exclusive).
	 * @return Um par de listas que consiste nos 'num' primeiros elementos, e os outros restantes.
	 */
	default Tuple2<? extends SharpCollection<T>, ? extends SharpCollection<T>> splitAt(final Integer num)
	{
		final Builder<T, SharpCollection<T>> colRetorno1 = this.builder();
		final Iterator<T> ite = this.iterator();
		int count = 0;

		while (count++ < num && ite.hasNext())
		{
			colRetorno1.add(ite.next());
		}
		
		final Builder<T, SharpCollection<T>> colRetorno2 = this.builder();
		
		while (ite.hasNext())
		{
			colRetorno2.add(ite.next());
		}
		return Tuple2.from(colRetorno1.result(), colRetorno2.result());
	}
	
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
	
	
	/**
	 * Descarta os elementos do início da coleção enquanto eles satisfizerem o predicado. <br/>
	 * 
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return Os elementos desta coleção com exceção do grupo de elementos do início que satisfazem
	 * o predicado.
	 */
	default SharpCollection<T> dropWhile(final Function1<? super T, Boolean> pred)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = builder();
		final Iterator<T> ite = this.iterator();

		while (ite.hasNext())
		{
			final T curEle = ite.next();

			if (!pred.apply(curEle))
			{
				colecaoRetorno.add(curEle);
				break;
			}
		}

		while (ite.hasNext())
		{
			colecaoRetorno.add(ite.next());
		}

		return colecaoRetorno.result();
	}
	
	/**
	 * Descarta os elementos do fim da coleção enquanto eles satisfizerem o predicado. <br/>
	 * 
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return Os elementos desta coleção com exceção do grupo de elementos do fim que satisfazem
	 * o predicado.
	 */
	default SharpCollection<T> dropRightWhile(final Function1<? super T, Boolean> pred)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = builder();
		final Iterator<T> ite = this.iterator();

		Builder<T, SharpCollection<T>> tempCollection = builder();

		while (ite.hasNext())
		{
			final T curEle = ite.next();

			if (pred.apply(curEle))
			{
				// Esse predicado pode ser o último
				tempCollection.add(curEle);
			}
			else
			{
				// Adiciona os elementos que seriam descartados
				for (final T ele : tempCollection.result())
				{
					colecaoRetorno.add(ele);
				}
				// Adiciona o elemento atual
				colecaoRetorno.add(curEle);
				tempCollection = builder(); // reseta o builder
			}
		}
		return colecaoRetorno.result();
	}
	
	
	/**
	 * Pega os elementos da coleção enquanto eles satisfizerem o predicado. <br/>
	 * 
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return O mais longo prefixo desta coleção onde todos os elementos satisfazem o predicado. 
	 */
	default SharpCollection<T> takeWhile(final Function1<? super T, Boolean> pred)
	{
		final Builder<T, SharpCollection<T>> colecaoRetorno = builder();
		final Iterator<T> ite = this.iterator();

		while (ite.hasNext())
		{
			final T curEle = ite.next();

			if (pred.apply(curEle))
			{
				colecaoRetorno.add(curEle);
			}
			else
			{
				break;
			}
		}
		return colecaoRetorno.result();
	}
	
	
	/**
	 * Pega os elementos da coleção, começando no último elemento, enquanto eles satisfizerem o predicado. <br/>
	 * 
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return O mais longo prefixo (da direita para a esquerda) desta coleção onde todos os elementos satisfazem o predicado. 
	 */
	default SharpCollection<T> takeRightWhile(final Function1<? super T, Boolean> pred)
	{
		Builder<T, SharpCollection<T>> colecaoRetorno = builder();
		final Iterator<T> ite = this.iterator();

		while (ite.hasNext())
		{
			final T curEle = ite.next();

			if (pred.apply(curEle))
			{
				// Coleta os elementos que satisfazem o predicado
				colecaoRetorno.add(curEle);
			}
			else
			{
				colecaoRetorno = builder(); // reseta o builder
			}
		}
		return colecaoRetorno.result();
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
	 * Executa, na ordem do iterator desta coleção, a função passada como parâmetro, 
	 * acumulando o resultado a cada iteração. <br/>
	 * EX: Se você tiver uma coleção de inteiros, você pode utilizar o 
	 * foldLeft para retornar a soma dos elementos, como no exemplo abaixo:
	 * <pre>
	 * myArray.foldLeft(0, new Action<Integer, Integer> (){ 
	 *     Integer apply(Integer param1, Integer param2){ 
	 *       return param1 + param2;
	 *     }
	 * } </pre>
	 * @param <B> O retorno da função foldLeft
	 * @param startValue O valor inicial a ser aplicação na ação
	 * @param function A função a ser executada a cada passo
	 * @return Um valor B, a partir da aplicação da função passada como parâmetro em cada elemento.
	 */
	default <B> B foldLeft(final B startValue, final Function2<B, ? super T, B> function)
	{
		B accumulator = startValue;

		for (final T ele : this)
		{
			accumulator = function.apply(accumulator, ele);
		}
		return accumulator;
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
	 * Constrói uma nova coleção, a partir da aplicação da função passada 
	 * como parâmetro em cada elemento da coleção, coletando os resultados numa
	 * única coleção. <br/>
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
		//TODO: Complexidade algorítmica está horrível. Isso porque o builder não expõe os elementos dentro dele
		//é melhor até criar um LinkedSet do que usar take o tempo todo.
		final Builder<T, SharpCollection<T>> colecaoRetorno = builder();
		int count = -1;

		for (final T ele : this)
		{
			if (!this.take(++count).contains(ele))
			{
				colecaoRetorno.add(ele);
			}
		}
		return colecaoRetorno.result();
	}
	
	/**
	 * Retorna uma coleção de tuplas a partir desta coleção, onde o primeiro elemento é o elemento desta coleção,
	 * e o segundo o seu índice (de acordo com o iterator).
	 * @return Uma coleção de tuplas, onde o primeiro elemento é o elemento original, e o segundo o seu índice
	 */
	default SharpCollection<Tuple2<T, Integer>> zipWithIndex()
	{
		final Builder<Tuple2<T, Integer>, SharpCollection<Tuple2<T, Integer>>> colecaoRetorno = builder();
		int curIndex = -1;

		for (final T ele : this)
		{
			colecaoRetorno.add(Tuple2.from(ele, ++curIndex));
		}
		return colecaoRetorno.result();
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
}
