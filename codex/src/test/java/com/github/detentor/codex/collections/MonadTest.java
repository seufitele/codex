package com.github.detentor.codex.collections;

import org.junit.Test;


/**
 * Faz o teste para todas as instâncias de Monad
 */
public class MonadTest
{

	@Test
	public void testCat()
	{
		OptionTest.testOption();
	}
	
	@Test
	public void testSharpCollection()
	{
		try
		{
			OptionTest.testOption();
		}
		catch (final AssertionError ae)
		{
			//System.out.println("Erro ao fazer o teste para a classe " + ele.getSharpCollection().getClass());
			throw new RuntimeException(ae);
		}
	}
}