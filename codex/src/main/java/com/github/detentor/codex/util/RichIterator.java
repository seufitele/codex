package com.github.detentor.codex.util;

import java.util.Iterator;

import com.github.detentor.codex.monads.Option;

/**
 * Esse iterator é uma versão melhorada do Iterator do Java. <br/>
 * Essa versão inicial possui apenas algumas funcionalidades, mas, principalmente, ele representa
 * o bloco de construção de estruturas potencialmente infinitas, 
 * e que permitam funções de ordem superior. <br/> <br/>
 * 
 * O método 'remove' é, por default, configurado como 'unsupportedOperationException'.
 * 
 * @author f9540702 Vinícius Seufitele Pinto
 *
 */
public abstract class RichIterator<E> implements Iterator<E>
{
	/**
	 * Dá uma 'espiada' no próximo elemento, sem movimentar o ponteiro do iterador.
	 * 
	 * @return Uma option que estará vazia se não houver um próximo elemento, ou conterá o elemento que será retornado pela
	 * próxima chamada do método next.  
	 */
	public abstract Option<E> peekNext();

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("remove not supported by this iterator");		
	}

	/**
	 * Cria um RichIterator a partir do iterable passado como parâmetro. <br/>
	 * Note-se que os elementos do iterable passado só serão consumidos quando os elementos
	 * de RichIterator forem consumidos
	 * 
	 * @param iterable O iterable a ser transformado em RichIterator
	 * @return Um RichIterator, que encaspsula o iterable passado como parâmetro, para adicionar
	 * novas funcionalidades.
	 */
	public static <E> RichIterator<E> from(final Iterable<E> iterable)
	{
		return from(iterable.iterator());
	}

	/**
	 * Cria um RichIterator a partir do iterator passado como parâmetro. <br/>
	 * Note-se que os elementos do iterator passado só serão consumidos quando os elementos
	 * de RichIterator forem consumidos
	 * 
	 * @param iterator O iterator a ser transformado em RichIterator
	 * @return Um RichIterator, que encaspsula o iterator passado como parâmetro, para adicionar
	 * novas funcionalidades.
	 */
	public static <E> RichIterator<E> from(final Iterator<E> iterator)
	{
		return new RichIterator<E>()
		{
			Option<E> nextElement = iterator.hasNext() ? Option.from(iterator.next()) : Option.<E>empty(); 
			
			@Override
			public E next()
			{
				final E toReturn = nextElement.get();
				nextElement = iterator.hasNext() ? Option.from(iterator.next()) : Option.<E>empty();
				return toReturn;
			}

			@Override
			public boolean hasNext()
			{
				return nextElement.notEmpty();
			}
			
			@Override
			public Option<E> peekNext()
			{
				return nextElement;
			}
		};
	}
	
	@Override
	public String toString()
	{
		final StringBuilder sBuilder = new StringBuilder();
		
		sBuilder.append("RichIterator: [");
		
		while (this.hasNext())
		{
			sBuilder.append(this.next());
			if (this.peekNext().notEmpty())
			{
				sBuilder.append(", ");
			}
		}
		return sBuilder.toString() + "]";
	}
}
