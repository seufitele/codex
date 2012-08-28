package com.github.detentor.codex.monads;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.operations.IntegerOp;


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
		assertTrue(optionVazio.map(IntegerOp.square).isEmpty());
	}
	

}
