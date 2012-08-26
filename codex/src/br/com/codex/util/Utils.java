package br.com.codex.util;

/**
 * Esta classe contém métodos para ser importados estaticamente, criados para reduzir a
 * repetição de código.
 * 
 * @author f9540702 Vinícius Seufitele Pinto
 *
 */
public class Utils
{
	/**
	 * Retorna o primeiro valor não-nulo (se existir), de uma sequência de potenciais valores nulos.
	 * @param <B> O tipo de dados a ser verificado
	 * @param params Os parâmetros que possivelmente estarão nulos
	 * @return O primeiro valor não-nulo encontrado, ou null se todos forem nulos
	 */
	public static <B> B coalesce(final B... params)
	{
		for (B ele : params)
		{
			if (ele != null)
			{
				return ele;
			}
		}
		return null;
	}
	
}
