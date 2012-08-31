package com.github.detentor.codex.function;

/**
 * 
 * Uma função parcial é uma função que pode não estar definida para todos os valores de entrada. <br/><br/>
 * Métodos que esperam funções parciais devem, antes de aplicar a função, chamar o método {@link #isDefinedAt}, 
 * que retorna true caso a função esteja definida para o argumento passado como parâmetro. <br/><br/>
 * O comportamento de uma função, quando chamada para um argumento que ela não está definida, não é especificado. <br/><br/>
 * É importante observar que é responsabilidade do implementador assegurar que retorno do método {@link #isDefinedAt}
 * condiz com o comportamento esperado pelo método {@link Function1#apply(Object) apply} da função. 
 * 
 * @author Vinícius Seufitele Pinto
 * 
 * @param <A> O tipo de dados de entrada
 * @param <B> O tipo de dados da saída
 */
public interface PartialFunction<A, B> extends Function1<A, B>
{
	/**
	 * Retorna <tt>true</tt> se, e somente se, essa função possui um retorno válido para o argumento passado como parâmetro.
	 * 
	 * @param forValue O valor onde a função será testada
	 * @return <tt>true</tt> se a função está definida para o argumento, ou <tt>false</tt> do contrário
	 */
	boolean isDefinedAt(final A forValue);

}
