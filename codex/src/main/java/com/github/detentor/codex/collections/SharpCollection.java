package com.github.detentor.codex.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.github.detentor.codex.cat.monads.Option;
import com.github.detentor.codex.product.Tuple2;

/**
 * Interface padrão para coleções que admitem funções de ordem superior. 
 * 
 * @author Vinícius Seufitele Pinto
 *
 * @param <T> O tipo de elementos da coleção
 */
public interface SharpCollection<T> extends Iterable<T>, HighOrderFunctions<T>, ComparisonFunctions<T>
{
	 /**
	 * Retorna o número de elementos desta coleção.
     *
     * @return O número de elementos desta coleção
     */
    int size();
    
    /**
     * retorna <tt>true</tt> se esta coleção não contém elementos
     * @return <tt>true</tt> se esta coleção não contém elementos
     */
    boolean isEmpty();
    
    /**
     * retorna <tt>true</tt> se esta coleção contém elementos
     *
     * @return <tt>true</tt> se esta coleção contém elementos
     */
    boolean notEmpty();
    
    /**
     * Retorna <tt>true</tt> se esta coleção contém o elemento especificado.
     * Formalmente, retorna <tt>true</tt> se e somente se esta coleção contém pelo menos
     * um elemento <tt>e</tt> tal que <tt>(element==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;element.equals(e))</tt>.
     *
     * @param element O elemento cuja presença nesta coleção será testada
     * @return <tt>true</tt> se esta coleção contém o elemento especificado
     */
    boolean contains(T element);
    
    /**
     * Retorna <tt>true</tt> se esta coleção contém todos os elementos do iterator especificado.
     *
     * @param  col A coleção a ser verificada
     * @return <tt>true</tt> Se esta coleção contém todos os elementos do iterator especificado
     */
    boolean containsAll(final Iterable<T> col);
    
    /**
     * Retorna <tt>true</tt> se esta coleção contém algum dos elementos do iterator especificado.
     *
     * @param  col A coleção a ser verificada
     * @return <tt>true</tt> Se esta coleção algum dos elementos do iterator especificado
     */
    boolean containsAny(final Iterable<T> col);

    /**
     * Retorna um iterador sobre os elementos desta coleção. A ordem será preservada, se ela estiver
     * bem-definida.
     * 
     * @return um <tt>Iterator</tt> sobre os elementos desta coleção
     */
    @Override
	public Iterator<T> iterator();
    
    //Métodos diversos:
    
    /**
	 * Retorna o primeiro elemento desta coleção. <br/><br/>
	 * ATENÇÃO: Para coleções onde a posição de um elemento não está bem-definida (ex: sets), a chamada
	 * sucessiva a este método pode retornar elementos distintos.
	 * @return O primeiro elemento desta coleção.
	 * @throws NoSuchElementException Se a coleção estiver vazia
	 */
	T head();
	
	 /**
	 * Retorna uma instância de Option que contém o primeiro elemento, se ele existir, 
	 * ou uma Option vazia, se a coleção estiver vazia. 
	 * @see Option 
	 * @return O primeiro elemento desta coleção como uma instância de Option
	 */
	Option<T> headOption();
	
	/**
	 * Retorna o último elemento desta coleção. <br/><br/>
	 * ATENÇÃO: Para coleções onde a posição de um elemento não está bem-definida (ex: sets), a chamada
	 * sucessiva a este método pode retornar elementos distintos.
	 * @return O último elemento desta coleção.
	 * @throws NoSuchElementException Se a coleção estiver vazia
	 */
	T last();
	
	/**
	 * Retorna uma instância de Option que contém o último elemento, 
	 * se ele existir, ou uma Option vazia, se a coleção estiver vazia. 
	 * @see Option 
	 * @return O primeiro elemento desta coleção como uma instância de Option
	 */
	Option<T> lastOption();
	
	/**
	 * Seleciona todos os elementos exceto o primeiro.<br/><br/>
	 * ATENÇÃO: Para coleções onde a posição de um elemento não está bem-definida (ex: sets), este método
	 * retornará todos os elementos com exceção do primeiro elemento retornado pelo iterator.
	 * @return Uma coleção que consiste de todos os elementos desta coleção, exceto o primeiro
	 * @throws NoSuchElementException Se esta coleção estiver vazia
	 */
	SharpCollection<T> tail();
	
	/**
	 * Pega até os 'num' primeiros elementos desta coleção. 
	 * Se a coleção não possuir 'num' elementos, retorna todos eles. <br/>
	 * ATENÇÃO: Não afeta a lista original.
	 * @param num O número de elementos a pegar desta coleção.
	 * @return Uma nova coleção, com os 'num' primeiros elementos dela.
	 */
	SharpCollection<T> take(final Integer num);
	
	/**
	 * Pega até os 'num' últimos elementos desta coleção. 
	 * Se a coleção não possuir 'num' elementos, retorna todos eles. <br/>
	 * ATENÇÃO: Não afeta a lista original.
	 * @param num O número de elementos a pegar desta coleção.
	 * @return Uma nova coleção, com os 'num' últimos elementos dela.
	 */
	SharpCollection<T> takeRight(final Integer num);
	
	/**
	 * Retorna esta coleção sem os 'num' primeiros elementos. 
	 * Se a coleção não possuir 'num' elementos, uma coleção vazia será retornada.<br/>
	 * ATENÇÃO: Não afeta a lista original.
	 * @param num O número de elementos a 'pular' desta coleção.
	 * @return Uma nova coleção, composta pelos elementos desta coleção, exceto os 'num' primeiros.
	 */
	SharpCollection<T> drop(final Integer num);
	
	/**
	 * Retorna esta coleção sem os 'num' últimos elementos. 
	 * Se a coleção não possuir 'num' elementos, uma coleção vazia será retornada.<br/>
	 * ATENÇÃO: Não afeta a lista original.
	 * @param num O número de elementos a 'pular' (contando da direita para a esquerda) desta coleção.
	 * @return Uma nova coleção, composta pelos elementos desta coleção, exceto os 'num' últimos.
	 */
	SharpCollection<T> dropRight(final Integer num);
	
	/**
	 * Divide esta coleção em duas na posição definida. <br/>
	 * Equivalente a {@link #take(num)} e {@link #drop(num)}, mas mais eficiente.
	 * @param num A posição em que a lista será dividida (exclusive).
	 * @return Um par de listas que consiste nos 'num' primeiros elementos, e os outros restantes.
	 */
	Tuple2<? extends SharpCollection<T>, ? extends SharpCollection<T>> splitAt(final Integer num);
	
	/**
	 * Particiona esta coleção em coleções de tamanho fixo.
	 * @param size O tamanho de cada coleção a ser retornada
	 * @return Uma coleção de coleções, onde cada uma delas terá size itens. A última pode estar possivelmente truncada.
	 */
	SharpCollection<? extends SharpCollection<T>> grouped(final Integer size);

	/**
	 * Chama o método toString em cada elemento desta coleção, concatenando os resultados.
	 * @return Uma string que representa a chamada ao método toString em cada elemento da coleção.
	 */
	String mkString();

	/**
	 * Mostra todos os elementos desta coleção numa string utilizando, entre elas o separador passado
	 * como parâmetro.
	 * @param separator O separador a ser utilizado entre cada chamada ao método toString
	 * @return Uma string representada pela chamada ao método toString de cada elemento desta coleção, separados
	 * pelo separador passado como parâmetro.
	 */
	String mkString(final String separator);
	
	/**
	 * Mostra todos os elementos desta coleção numa string utilizando, entre elas o separador passado
	 * como parâmetro, iniciando por start e terminando com end.
	 * @param start A string a ser colocada no início
	 * @param separator O separador a ser utilizado entre cada chamada ao método toString
	 * @param end A string a ser colocada no fim
	 * @return Uma string representada pela chamada ao método toString de cada elemento desta coleção, separados
	 * pelo separador passado como parâmetro, começando por start e terminando com end.
	 */
	String mkString(final String start, final String separator, final String end);
	
	/**
	 * Retorna um builder para esta coleção. Um builder é uma classe que permite a adição de elementos,
	 * e ela determina qual o tipo da coleção retornada.
	 * @see Builder
	 */
	<B> Builder<B, SharpCollection<B>> builder();
	
	/**
	 * Retorna uma coleção de tuplas a partir desta coleção, onde o primeiro elemento é o elemento desta coleção,
	 * e o segundo o seu índice (de acordo com o iterator).
	 * @return Uma coleção de tuplas, onde o primeiro elemento é o elemento original, e o segundo o seu índice
	 */
	SharpCollection<Tuple2<T, Integer>> zipWithIndex();
	
	/**
	 * Calcula a interseção desta coleção com o iterator passado como parâmetro. <br/>
	 * Serão coletados todos os elementos desta coleção que estejam contidos no iterator. <br/>
	 * @see java.util.Collection#retainAll
	 * @param <E> O tipo de dados desta coleção
	 * @param withCollection O iterator a ser verificado a interseção
	 * @return Uma nova coleção com todos os elementos desta coleção que também estejam contidos no iterator
	 */
	SharpCollection<T> intersect(final Iterable<T> withCollection);

	/**
	 * Retorna uma coleção que possui somente elementos distintos entre si (de acordo com o equals). <br/>
	 * A ordem é preservada, se ela estiver bem-definida.
	 * @return Uma coleção onde os elementos são todos distintos entre si.
	 */
	SharpCollection<T> distinct();
	
	/**
	 * Ordena esta coleção, de acordo com a ordenação natural de seus elementos. <br/>
	 * Esse método não está definido quando os elementos contidos nesta coleção não são instâncias 
	 * de {@link Comparable} ou {@link Comparator}.
	 * 
	 * @return Uma nova coleção com os elementos ordenados
	 */
	SharpCollection<T> sorted();

	/**
	 * Ordena esta coleção, de acordo com a função de ordenação passada como parâmetro. <br/>
	 *
	 * @param comparator A função de comparação, a ser utilizada para ordenar os elementos
	 * @return Uma nova coleção com os elementos ordenados
	 */
	SharpCollection<T> sorted(final Comparator<? super T> comparator);
	
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
