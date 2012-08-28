package br.com.teste;

import br.com.codex.collections.mutable.ListSharp;
import br.com.codex.function.Function1;
import br.com.codex.high.HList;
import br.com.codex.high.HList.Nil;

public class JavaTeste
{
	public static void main(String[] args)
	{
		ListSharp<Integer> k = ListSharp.empty();
		
		for (int i = 0; i < 750000; i++)
		{
			k.add(i);
		}
		
		long startTime = System.currentTimeMillis();
		
		for (int i = 0; i < 30; i++)
		{
			ListSharp<Integer> t = k.dropWhile(new Function1<Integer, Boolean>()
			{
				@Override
				public Boolean apply(Integer param)
				{
					return param < 750000;
				}
			});
		}
		//314
		
		System.out.println("Demorou: " + (System.currentTimeMillis() - startTime));
		
		
	}

}
