package com.github.detentor.codex.collections;

import com.github.detentor.codex.function.PartialFunction1;

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
	/**
	 * Seleciona todos os elementos exceto o primeiro.<br/><br/>
	 * Retornará todos os elementos com exceção do primeiro elemento, na ordem retornada pelo iterator.
	 * @return Uma coleção que consiste de todos os elementos desta coleção, exceto o primeiro
	 * @throws NoSuchElementException Se esta coleção estiver vazia
	 */
	Seq<T> tail();
	
	
	
	
	
	default boolean isDefinedAt(final Integer forValue)
	{
		return forValue >= 0 && forValue < this.size();
	}
	
	
	
	
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
