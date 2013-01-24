package com.github.detentor.codex.collections;

/**
 * Sequências representam estruturas onde a ordem dos elementos retornados pelo 
 * iterator é estável (ex: Mapas e Sets não são sequências, pois os itens 
 * retornados pelo iterador pode mudar). <br/><br/>
 * 
 * @author Vinicius Seufitele Pinto
 *
 */
public interface Seq<A> extends Iterable<A>
{

}
