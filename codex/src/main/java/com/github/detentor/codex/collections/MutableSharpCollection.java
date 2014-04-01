package com.github.detentor.codex.collections;

/**
 * Essa interface deve ser assinada pelas coleções que permitem mutabilidade.
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public interface MutableSharpCollection<T> extends SharpCollection<T>
{
	/**
     * Adiciona o elemento passado como parâmetro nesta coleção. <br/><br/>
     * 
     * @param element o elemento a ser adicionado nesta coleção
     * @return A referência à coleção, após o elemento ser adicionado.
     */
    SharpCollection<T> add(final T element);
    
    /**
     * Adiciona todos os elementos da coleção especificada nesta coleção.
     *
     * @param col Coleção contendo elemento a serem adicionados nesta coleção
     * @return A referência a esta coleção, após a adição dos elementos
     */
    default SharpCollection<T> addAll(final Iterable<? extends T> col)
    {
    	for (T ele : col)
    	{
    		add(ele);
    	}
    	return this;
    }
    
    /**
     * Remove uma instância do elemento passado como parâmetro desta lista, se ele
     * estiver presente. <br/>
     * Formalmente, remove um elemento <tt>e</tt> tal que
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>, se esta coleção
     * contém um ou mais tais elementos.
     *
     * @param element O elemento a ser removido desta coleção, se presente
     * @return A referência à coleção, após o elemento ser removido.
     */
    SharpCollection<T> remove(final T element);
    
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
    default SharpCollection<T> removeAll(final Iterable<T> col)
    {
    	for (T ele : col)
    	{
    		remove(ele);
    	}
    	return this;
    }

    /**
     * Remove todos os elementos desta coleção. 
     * @return A referência a esta coleção, após ela ser limpa.
     */
    SharpCollection<T> clear();
}
