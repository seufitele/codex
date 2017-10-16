package com.github.detentor.codex.function.arrow;

/**
 * As setas, em teoria das categorias, são mapeamentos que preservam a estrutura entre duas
 * entidades matemáticas. <br/><br/>
 * 
 * No contexto do codex, setas são funções mais poderosas
 *
 */
public interface Arrow
{
	/**
	 * Retorna a aridade da seta (número de parâmetros que ela espera)
	 * @return O número de parâmetros desta seta
	 */
	int getArity();
}
