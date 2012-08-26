package br.com.codex.monads;

import java.util.NoSuchElementException;

import br.com.codex.collections.mutable.ListSharp;

/**
 * Option é uma mônade que representa uma operação que pode ou não retornar um valor. <br/> <br/>
 * 
 * Option pode ser vista como uma lista, que estará vazia se a operação não foi bem-sucedida,
 * ou conterá um elemento se ela foi.
 * 
 * @author f9540702 Vinícius Seufitele Pinto
 *
 * @param <T> O tipo de dados a ser guardado no option
 */
public class Option<T>
{
	private final ListSharp<T> value;

	private Option()
	{
		value = ListSharp.<T>empty();
	}
	
	@SuppressWarnings("unchecked")
	private Option(final T theValue)
	{
		value = ListSharp.from(theValue);
	}

	/**
	 * Cria um option para uma operação que falhou (Option está vazio)
	 * @param <T> O tipo de dados que option deveria conter
	 * @return Uma option que representa uma computação não sucedida
	 */
	public static <T> Option<T> empty()
	{
		return new Option<T>();
	}
	
	/**
	 * Cria um option para uma operação que falhou (Option está vazio)
	 * @param <T> O tipo de dados que option deveria conter
	 * @return Uma option que representa uma computação não sucedida
	 */
	public static <T> Option<T> from(final T theValue)
	{
		return new Option<T>(theValue);
	}
	
    /**
     * retorna <tt>true</tt> se esta coleção não contém elementos
     * @return <tt>true</tt> se esta coleção não contém elementos
     */
    public boolean isEmpty()
    {
    	return value.isEmpty();
    }
    
    /**
     * retorna <tt>true</tt> se esta coleção contém elementos
     *
     * @return <tt>true</tt> se esta coleção contém elementos
     */
    public boolean notEmpty()
    {
    	return ! isEmpty();
    }
    
    /**
     * Retorna o valor T se ele existir nesta Option. Chamar esse método
     * com Option sem elementos causará um erro.
     *  
     * @return O valor T guardado nesta Option
     * @throws NoSuchElementException Se a coleção estiver vazia
     */
    public T get()
    {
    	return value.head();
    }
    
    /**
	 * Retorna o valor contido nesta Option, ou o valor determinado pela parte
	 * do else se ela não contém nenhum valor.
	 * @param elsePart O valor a ser retornado, caso esta Option esteja vazia
	 * @return Um valor T, que será o valor desta Option, ou a parte do else.
	 */
    public T getOrElse(final T elsePart)
    {
    	return value.isEmpty() ? elsePart : value.head(); 
    }

	@Override
	public String toString()
	{
		return value.toString();
	}

}
