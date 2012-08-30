package com.github.detentor.codex.monads;

import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class OptionTest
{
	
	/**
	 * Assegura que funciona os testes para o option vazio
	 */
	@Test
	public void testEmptyOption() 
	{
		final Option<Integer> optionVazio = Option.from(null);
	
		assertTrue(optionVazio.isEmpty());
	}
	

}
