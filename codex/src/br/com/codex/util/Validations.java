package br.com.codex.util;

/**
 * Esta classe contém métodos para ser importados estaticamente, criados para simplificar
 * e diminuir a repetição de código para validações mais comuns.
 * 
 * @author f9540702 Vinícius Seufitele Pinto
 *
 */
public final class Validations
{
	private Validations()
	{
	}

	/**
	 * Verifica se uma determinada condição é verdadeira, disparando uma IllegalArgumentException se não for.
	 * @param condition A condição a ser verificada
	 * @throws IllegalArgumentException Se a condição falhar
	 */
	public static void require(final boolean condition)
	{
		require(condition, "Falha ao avaliar a condição");
	}
	
	/**
	 * Verifica se uma determinada condição é verdadeira, disparando uma IllegalArgumentException com a
	 * mensagem definida se não for.
	 * @param condition A condição a ser verificada
	 * @param message A mensagem no caso de falha
	 * @throws IllegalArgumentException Se a condição falhar
	 */
	public static void require(final boolean condition, final String message)
	{
		if (! condition)
		{
			throw new IllegalArgumentException(message);
		}
	}

}
