package br.com.codex.collections.builders;

import java.util.HashMap;
import java.util.Map;

import br.com.codex.collections.Builder;
import br.com.codex.collections.SharpCollection;
import br.com.codex.collections.mutable.MapSharp;
import br.com.codex.product.Tuple2;

/**
 * Essa classe é um builder para Set baseado em um MapSharp. <br/>
 * 
 * @author f9540702 Vinícius Seufitele Pinto
 *
 * @param <K, V> K é o tipo de dados da chave, V é o tipo de dados do valor.
 */
public class HashMapBuilder<K,V> implements Builder<Tuple2<K, V>, SharpCollection<Tuple2<K, V>>>
{
	final Map<K, V> hashMap = new HashMap<K, V>();

	@Override
	public void add(final Tuple2<K, V> element)
	{
		hashMap.put(element.getVal1(), element.getVal2());
	}

	@Override
	public MapSharp<K, V> result()
	{
		return MapSharp.from(hashMap);
	}


}
