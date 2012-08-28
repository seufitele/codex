package com.github.detentor.testing;

import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.operations.IntegerOp;


public class JavaTeste
{
	public static void main(String[] args)
	{
		Option<Integer> valor = Option.from(null);
		
		System.out.println(valor.map(IntegerOp.square));
		
	}
	
	
	

}
