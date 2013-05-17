package com.github.detentor.codex.function.arrow;

import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.function.PartialFunction0;

/**
 * 
 * Uma função parcial é uma função que pode não estar definida para todos os valores de entrada. <br/><br/>
 * Métodos que esperam funções parciais devem, antes de aplicar a função, chamar o método {@link #isDefined}, 
 * que retorna true caso a função esteja definida. <br/><br/>
 * O comportamento de uma função, ao ser chamada quando não está definida, não é especificado. <br/><br/>
 * É importante observar que é responsabilidade do implementador assegurar que retorno do método {@link #isDefined}
 * condiz com o comportamento esperado pelo método {@link Function0#apply() apply} da função. 
 * 
 * @author Vinícius Seufitele Pinto
 * 
 * @param <A> O tipo de dados de entrada
 * @param <B> O tipo de dados da saída
 */
public abstract class PartialArrow0<A> implements PartialFunction0<A>
{
	/**
	 * Retorna <tt>true</tt> se, e somente se, essa função possui um retorno válido. Note-se que, como a função
	 * não possui parâmetros, o retorno inválido deverá ser computado de alguma outra maneira.
	 * 
	 * @return <tt>true</tt> se a função está definida, ou <tt>false</tt> do contrário
	 */
	public abstract boolean isDefined();
}
