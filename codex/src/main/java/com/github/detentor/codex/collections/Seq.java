package com.github.detentor.codex.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.PartialFunction1;
import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;

/**
 * Sequências representam estruturas onde a ordem dos elementos retornados pelo 
 * iterator é estável (ex: Mapas e Sets não são sequências, pois os itens 
 * retornados pelo iterador pode mudar). <br/><br/>
 * 
 * @author Vinicius Seufitele Pinto
 *
 */
public interface Seq<T> extends SharpCollection<T>, PartialFunction1<Integer, T>
{
	default boolean isDefinedAt(final Integer forValue)
	{
		return forValue >= 0 && forValue < this.size();
	}
	
	/**
	 * Seleciona todos os elementos exceto o primeiro.<br/><br/>
	 * Retornará todos os elementos com exceção do primeiro elemento, na ordem retornada pelo iterator.
	 * @return Uma coleção que consiste de todos os elementos desta coleção, exceto o primeiro
	 * @throws NoSuchElementException Se esta coleção estiver vazia
	 */
	default Seq<T> tail()
	{
		if (this.isEmpty())
		{
			throw new NoSuchElementException("tail foi chamado para uma coleção vazia");
		}
		return drop(1);
	}

	/**
	 * Retorna o primeiro elemento desta coleção. <br/><br/>
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
	 * @return O último elemento desta coleção.
	 * @throws NoSuchElementException Se a coleção estiver vazia
	 */
	default T last()
	{
		if (this.isEmpty())
		{
			throw new NoSuchElementException("last foi chamado para uma coleção vazia");
		}
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
	
	/**
	 * Pega até os 'num' primeiros elementos desta coleção. 
	 * Se a coleção não possuir 'num' elementos, retorna todos eles. <br/>
	 * ATENÇÃO: Não afeta a lista original.
	 * @param num O número de elementos a pegar desta coleção.
	 * @return Uma nova coleção, com os 'num' primeiros elementos dela.
	 */
	default Seq<T> take(final Integer num)
	{
		final Builder<T, Seq<T>> colecaoRetorno = this.builder();
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
	default Seq<T> takeRight(final Integer num)
	{
		final int eleToSkip = Math.max(this.size() - num, 0);
		final Builder<T, Seq<T>> colecaoRetorno = this.builder();
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
	default Seq<T> drop(final Integer num)
	{
		final Builder<T, Seq<T>> colecaoRetorno = this.builder();
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
	default Seq<T> dropRight(final Integer num)
	{
		final int toAdd = Math.max(0, this.size() - num);
		final Builder<T, Seq<T>> colecaoRetorno = this.builder();
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
	default Tuple2<? extends Seq<T>, ? extends Seq<T>> splitAt(final Integer num)
	{
		final Builder<T, Seq<T>> colRetorno1 = this.builder();
		final Iterator<T> ite = this.iterator();
		int count = 0;

		while (count++ < num && ite.hasNext())
		{
			colRetorno1.add(ite.next());
		}
		
		final Builder<T, Seq<T>> colRetorno2 = this.builder();
		
		while (ite.hasNext())
		{
			colRetorno2.add(ite.next());
		}
		return Tuple2.from(colRetorno1.result(), colRetorno2.result());
	}
	
	
	/**
	 * Descarta os elementos do início da coleção enquanto eles satisfizerem o predicado. <br/>
	 * 
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return Os elementos desta coleção com exceção do grupo de elementos do início que satisfazem
	 * o predicado.
	 */
	default Seq<T> dropWhile(final Function1<? super T, Boolean> pred)
	{
		final Builder<T, Seq<T>> colecaoRetorno = builder();
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
	default Seq<T> dropRightWhile(final Function1<? super T, Boolean> pred)
	{
		final Builder<T, Seq<T>> colecaoRetorno = builder();
		final Iterator<T> ite = this.iterator();

		Builder<T, Seq<T>> tempCollection = builder();

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
	default Seq<T> takeWhile(final Function1<? super T, Boolean> pred)
	{
		final Builder<T, Seq<T>> colecaoRetorno = builder();
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
	default Seq<T> takeRightWhile(final Function1<? super T, Boolean> pred)
	{
		Builder<T, Seq<T>> colecaoRetorno = builder();
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
	
	/**
	 * Ordena esta coleção, de acordo com a função de ordenação passada como parâmetro. <br/>
	 *
	 * @param comparator A função de comparação, a ser utilizada para ordenar os elementos
	 * @return Uma nova coleção com os elementos ordenados
	 */
	Seq<T> sorted(final Comparator<? super T> comparator);

	//ATENÇÃO: Esse método está aqui apenas para permitir ao tipo 'U' ser acessado pelas classes
	//inferiores. Se esse método for removido, então a classe mais abaixo não vai conseguir saber
	//o tipo 'U', pois o método tail() também está definido em LinearSeq
	
//	@Override
//	public int hashCode()
//	{
//		int result = 1;
//
//		for (Object element : this)
//		{
//			result = 31 * result + (element == null ? 0 : element.hashCode());
//		}
//		return result;
//	}
//	
//	@Override
//	public boolean equals(Object obj)
//	{
//		if (this == obj)
//		{
//			return true;
//		}
//		if (obj == null || !(obj instanceof Seq<?>))
//		{
//			return false;
//		}
//		
//		if (this instanceof IndexedSeq<?> && obj instanceof IndexedSeq<?>)
//		{
//			return compareIndexed((IndexedSeq<?>) this, (IndexedSeq<?>)obj);
//		}
//
//		//Não verifica o tamanho por causa da complexidade
//		final Seq<?> other = (Seq<?>) obj;
//
//		final Iterator<T> thisIte = this.iterator();
//		final Iterator<?> otherIte = other.iterator();
//		
//		while (thisIte.hasNext() && otherIte.hasNext())
//		{
//			final T thisEle = thisIte.next();
//			final Object otherEle = otherIte.next();
//			
//			if (! (thisEle == null ? otherEle == null : thisEle.equals(otherEle)))
//			{
//				return false;
//			}
//		}
//		//Só retorna true se os dois iterators terminaram juntos
//		return thisIte.hasNext() == otherIte.hasNext();
//	}
//	
//	/**
//	 * Verificação mais rápida para os casos de ser sequências indexadas
//	 * @param iSeq1
//	 * @param iSeq2
//	 * @return
//	 */
//	private boolean compareIndexed(final IndexedSeq<?> iSeq1, final IndexedSeq<?> iSeq2)
//	{
//		if (iSeq1.size() != iSeq2.size())
//		{
//			return false;
//		}
//
//		// Verifica se os elementos são iguais
//		for (int i = 0; i < iSeq1.size(); i++)
//		{
//			if (! (iSeq1.apply(i) == null ? iSeq2.apply(i) == null : iSeq1.apply(i).equals(iSeq2.apply(i))))
//			{
//				return false;
//			}
//		}
//		return true;
//	}
	
	
	
	
	
}
