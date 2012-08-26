package br.com.codex.collections;

/**
 * Essa interface deve ser assinada pelas coleções que permitem mutabilidade.
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public interface MutableSharpCollection<T>
{
	/**
     * Adiciona o elemento passado como parâmetro nesta coleção. <br/><br/>
     * 
     * @param element o elemento a ser adicionado nesta coleção
     * @return A referência à coleção, após o elemento ser adicionado.
     */
    public SharpCollection<T> add(T element);
    
    /**
     * Remove uma instância do elemento passado como parâmetro desta lista, se ele
     * estiver presente. <br/>
     * Formalmente, remove um elemento <tt>e</tt> tal que
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>, se esta coleção
     * contém um ou mais tais elementos.
     *
     * @param O elemento a ser removido desta coleção, se presente
     * @return A referência à coleção, após o elemento ser removido.
     */
    public SharpCollection<T> remove(T element);
    
    /**
     * Remove todos os elementos desta coleção.
     * @return A referência a esta coleção, após ela ser limpa.
     */
    public SharpCollection<T> clear();
    
    /**
     * Adiciona todos os elementos da coleção especificada nesta coleção.
     *
     * @param col Coleção contendo elemento a serem adicionados nesta coleção
     * @return A referência a esta coleção, após a adição dos elementos
     */
    public SharpCollection<T> addAll(Iterable<? extends T> col);

    /**
     * Remove desta coleção todos os elementos que também estão contidos na coleção 
     * especificada. <br/> <br/>
     * 
     * Depois que esta chamada retornar, esta coleção não conterá elementos em comum
     * com a coleção especificada.
     *
     * @param col A coleção contendo elementos a serem removidos desta coleção
     * @return A referência a esta coleção, após a remoção dos elementos
     */
    public SharpCollection<T> removeAll(Iterable<T> col);

}
