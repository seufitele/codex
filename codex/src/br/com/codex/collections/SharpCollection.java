package br.com.codex.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

import br.com.codex.monads.Option;
import br.com.codex.product.Tuple2;

/**
 * Interface padrão para coleções que admitem funções de ordem superior. 
 * 
 * @author f9540702 Vinícius Seufitele Pinto
 *
 * @param <T> O tipo de elementos da coleção
 */
public interface SharpCollection<T> extends Iterable<T>, HighOrderFunctions<T>
{
	 /**
	  * Retorna o número de elementos desta coleção.
     *
     * @return O número de elementos desta coleção
     */
    public int size();
    
    /**
     * retorna <tt>true</tt> se esta coleção não contém elementos
     * @return <tt>true</tt> se esta coleção não contém elementos
     */
    public boolean isEmpty();
    
    /**
     * retorna <tt>true</tt> se esta coleção contém elementos
     *
     * @return <tt>true</tt> se esta coleção contém elementos
     */
    public boolean notEmpty();
    
    /**
     * Retorna <tt>true</tt> se esta coleção contém o elemento especificado.
     * Formalmente, retorna <tt>true</tt> se e somente se esta coleção contém pelo menos
     * um elemento <tt>e</tt> tal que <tt>(element==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;element.equals(e))</tt>.
     *
     * @param element O elemento cuja presença nesta coleção será testada
     * @return <tt>true</tt> se esta coleção contém o elemento especificado
     */
    public boolean contains(T element);
    
    /**
     * Retorna um iterador sobre os elementos desta coleção. Não há garantias quanto à
     * ordem que os elementos serão retornados (a menos que esta coleção seja uma instância
     * de uma classe que provê essa garantia).
     * 
     * @return um <tt>Iterator</tt> sobre os elementos desta coleção
     */
    @Override
	public Iterator<T> iterator();
    
    //MÉTODOS BULK, cujas implementações são criadas a partir dos valores anteriores: 
    
    /**
     * Retorna <tt>true</tt> se esta coleção contém todos os elementos da coleção
     * especificada.
     *
     * @param  col A coleção a ser verificada
     * @return <tt>true</tt> Se esta coleção contém todos os elementos da coleção especificada
     */
    public boolean containsAll(SharpCollection<T> col);
    
    //Métodos diversos:
    
    /**
	 * Retorna o primeiro elemento desta coleção. <br/><br/>
	 * ATENÇÃO: Para coleções onde a posição de um elemento não está bem-definida (ex: sets), a chamada
	 * sucessiva a este método pode retornar elementos distintos.
	 * @return O primeiro elemento desta coleção.
	 * @throws NoSuchElementException Se a coleção estiver vazia
	 */
	public T head();
	
	 /**
	 * Retorna o primeiro elemento desta coleção como uma instância de Option. <br/><br/>
	 * @see Option 
	 * @return O primeiro elemento desta coleção como uma instância de Option
	 */
	public Option<T> headOption();
	
	/**
	 * Seleciona todos os elementos exceto o primeiro.<br/><br/>
	 * ATENÇÃO: Para coleções onde a posição de um elemento não está bem-definida (ex: sets), este método
	 * retornará todos os elementos com exceção do primeiro elemento retornado pelo iterator.
	 * @return Uma coleção que consiste de todos os elementos desta coleção, exceto o primeiro
	 * @throws NoSuchElementException Se esta coleção estiver vazia
	 */
	public SharpCollection<T> tail();
	
	/**
	 * Pega até os 'num' primeiros elementos desta coleção. 
	 * Se a coleção não possuir 'num' elementos, retorna todos eles. <br/>
	 * ATENÇÃO: Não afeta a lista original.
	 * @param num O número de elementos a pegar desta coleção.
	 * @return Uma nova coleção, com os 'num' primeiros elementos dela.
	 */
	public SharpCollection<T> take(final Integer num);
	
	/**
	 * Retorna esta coleção sem os 'num' primeiros elementos. 
	 * Se a coleção não possuir 'num' elementos, uma coleção vazia será retornada.<br/>
	 * ATENÇÃO: Não afeta a lista original.
	 * @param num O número de elementos a 'pular' desta coleção.
	 * @return Uma nova coleção, composta pelos elementos desta coleção, exceto os 'num' primeiros.
	 */
	public SharpCollection<T> drop(final Integer num);
	
	/**
	 * Chama o método toString em cada elemento desta coleção, concatenando os resultados.
	 * @return Uma string que representa a chamada ao método toString em cada elemento da coleção.
	 */
	public String mkString();

	/**
	 * Mostra todos os elementos desta coleção numa string utilizando, entre elas o separador passado
	 * como parâmetro.
	 * @param separator O separador a ser utilizado entre cada chamada ao método toString
	 * @return Uma string representada pela chamada ao método toString de cada método desta coleção, separados
	 * pelo separador passado como parâmetro.
	 */
	public String mkString(final String separator);
	
	/**
	 * Mostra todos os elementos desta coleção numa string utilizando, entre elas o separador passado
	 * como parâmetro, iniciando por start e terminando com end.
	 * @param start A string a ser colocada no início
	 * @param separator O separador a ser utilizado entre cada chamada ao método toString
	 * @param end A string a ser colocada no fim
	 * @return Uma string representada pela chamada ao método toString de cada método desta coleção, separados
	 * pelo separador passado como parâmetro, começando por start e terminando com end.
	 */
	public String mkString(final String start, final String separator, final String end);
	
	/**
	 * Retorna um builder para esta coleção. Um builder é uma classe que permite a adição de elementos,
	 * e ela determina qual o tipo da coleção retornada.
	 * @see Builder
	 */
	public <B> Builder<B, SharpCollection<B>> builder();
	
	/**
	 * Retorna uma coleção de tuplas a partir desta coleção, onde o primeiro elemento é o elemento desta coleção,
	 * e o segundo o seu índice (de acordo com o iterator).
	 * @return Uma coleção de tuplas, onde o primeiro elemento é o elemento original, e o segundo o seu índice
	 */
	public SharpCollection<Tuple2<T, Integer>> zipWithIndex();

}
