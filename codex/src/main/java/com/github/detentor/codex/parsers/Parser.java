package com.github.detentor.codex.parsers;

import com.github.detentor.codex.collections.mutable.ListSharp;
import com.github.detentor.codex.function.Function1;

/**
 * Essa classe é a implementação de um parser, que dada uma String (<code>input</code>) realiza a extração de tokens baseado em suas
 * regras pré-definidas. <br/>
 * As regras são definidas em função de uma <code>Regex</code> que é passada na criação e/ou na combinação dessas regras utilizando os
 * métodos para este fim. <br/>
 * Por exemplo, o método {@link Parser#and(Parser) and} combina dois parsers de modo que o parser final obrigue a verificação das duas
 * regex.<br/>
 * <br/>
 * Este parser é particularmente útil para implementações de DSL's - Domain-Specific Language. São uma espécie de mini-linguagens que
 * resolvem problemas específicos. Veja <a href=http://en.wikipedia.org/wiki/Domain-specific_language>Domain-specific language</a> para
 * mais informações. <br/>
 * <br/>
 * Um exemplo de implementação de uma DSL usando este parser é a Bumos - Bean Utils Mapping Object Syntax, seu compilador implementa as
 * regras utilizando este parser como base, para mais informações consulte sua documentação: {@link BumosCompiler}
 * 
 * @author Vinicius de Lima Nogueira (F9540141)
 * @author Vinicius Seufitele Pinto (F9540702)
 */
public class Parser
{
	protected final Parser[] injectedParser;
	private final Function1<Character, Boolean> charRule;

	// /**
	// * Parser vazio que sempre retorna uma lista vazia e não consome nada da input
	// */
	// public final static Parser empty = new Parser()
	// {
	// @Override
	// protected Tuple2<List<Object>, Integer> parse(final String input, final Integer startPos)
	// {
	// return Tuple2.<List<Object>, Integer> from(new ArrayList<Object>(), startPos);
	// }
	// };

	private Parser()
	{
		this((Function1<Character, Boolean>) null);
	}

	private Parser(final Function1<Character, Boolean> theCharRule)
	{
		super();
		this.charRule = theCharRule;
		injectedParser = new Parser[1];
	}

	private Parser(final Parser[] theInjectedParser)
	{
		this.charRule = null;
		injectedParser = theInjectedParser;
	}

	/**
	 * Cria um parser base com a <code>theRegex</code> como regra
	 * 
	 * @param theRegex
	 */
	public static Parser from(final Function1<Character, Boolean> theCharRule)
	{
		return new Parser(theCharRule);
	}

	/**
	 * Combina o parser atual com o <code>parserAnd</code> usando uma conjunção lógica <i>(E/AND)</i>, isto é, o parser só vai retornar
	 * se as duas regras combinadas forem satisfeitas
	 * 
	 * @param parserAnd
	 */
	public Parser and(final Parser parserAnd)
	{
		return new Parser(injectedParser)
		{
			@Override
			protected ParserResult parse(final String input, final Integer startPos)
			{
				final ParserResult firstResult = Parser.this.parse(input, startPos);

				if (firstResult.isFailure())
				{
					return firstResult;
				}
				final ParserResult secondResult = parserAnd.parse(input, firstResult.getLastPos());
				return firstResult.and(secondResult);
			}
		};
	}

	/**
	 * Combina o parser atual com o <code>parserOr</code> usando uma disjunção lógica <i>(OU/OR)</i>, isto é, o parser vai retornar se
	 * qualquer uma das duas regras forem satisfeitas
	 * 
	 * @param parserOr
	 */
	public Parser or(final Parser parserOr)
	{
		return new Parser(injectedParser)
		{
			@Override
			protected ParserResult parse(final String input, final Integer startPos)
			{
				final ParserResult firstResult = Parser.this.parse(input, startPos);

				if (firstResult.isSuccess())
				{
					return firstResult;
				}
				return parserOr.parse(input, startPos);
			}
		};
	}

	/**
	 * Efetua o parse propriamente dito da input
	 * 
	 * @param input entrada que será informada
	 * @return Uma classe que representa o resultado do Parser
	 */
	public ParserResult parse(final String input)
	{
		final ParserResult parseResult = parse(input, 0);

		if (parseResult.isSuccess() && parseResult.getLastPos() != input.length())
		{
			// Falhou por que não varreu toda a input
			return ParserResult.createFailure(ParserErrors.UNPARSED_TOKENS, parseResult.getResult(), parseResult.getLastPos());
		}
		return parseResult;
	}

	protected ParserResult parse(final String input, final Integer startPos)
	{
		if (startPos >= input.length())
		{
			return ParserResult.createFailure(ParserErrors.END_OF_STREAM, startPos);
		}

		final char charToCheck = input.charAt(startPos);

		if (charRule.apply(charToCheck))
		{
			final String value = String.valueOf(charToCheck);
			return ParserResult.createSuccess(ListSharp.<Object>from(value), startPos + value.length());
		}
		// Não encontrou o token que era para ter sido encontrado
		return ParserResult.createFailure(ParserErrors.UNEXPECTED_TOKEN, startPos);
	}
	
	public Parser star()
	{
		return rep(0);
	}
	
	public Parser plus()
	{
		return rep(1);
	}

	/**
	 * Combina o parser atual com ele mesmo, fazendo com que o novo parser retorne se as regras deste parser forem satisfeitas
	 * <code>min</code> ou N vezes, é um operador quantificador de repetição similar a <code>{min,}</code> na Regex
	 * 
	 * @param min mínimo de ocorrências
	 */
	private Parser rep(final int min)
	{
		return rep(min, Integer.MAX_VALUE);
	}

	/**
	 * Combina o parser atual com ele mesmo, fazendo com que o novo parser retorne se as regras deste parser forem satisfeitas de
	 * <code>min</code> à <code>max</code> vezes, é um operador quantificador de repetição similar a <code>{min,max}</code> na Regex
	 * 
	 * @param min mínimo de ocorrências
	 * @param max máximo de ocorrências
	 */
	private Parser rep(final int min, final int max)
	{
		if (min < 0)
		{
			throw new IllegalArgumentException("argumento min não deve ser menor do que zero");
		}
		
		if (max < min)
		{
			throw new IllegalArgumentException("argumento max não deve ser menor min");
		}
		
		return new Parser(injectedParser)
		{
			@Override
			protected ParserResult parse(final String input, final Integer startPos)
			{
				ParserResult curParser = Parser.this.parse(input, startPos);
				ParserResult result = curParser;
				int qtdOccur = result.isFailure() ? 0 : 1;

				while (qtdOccur < max && !(curParser = Parser.this.parse(input, result.getLastPos())).isFailure())
				{
					if (curParser.isSuccess())
					{
						qtdOccur++;
						result = result.and(curParser);
					}
				}

				if (qtdOccur < min)
				{
					return result.and(ParserResult.createFailure(ParserErrors.UNEXPECTED_TOKEN, result.getLastPos()));
				}
				return result;
			}
		};
	}

	// /**
	// * Combina o parser atual com ele mesmo, fazendo com que o novo parser retorne se as regras deste parser forem satisfeitas 0 ou N
	// vezes,
	// * é um operador quantificador de repetição similar ao <code>*</code> da Regex
	// */
	// public Parser repStar()
	// {
	// return rep(0);
	// }
	//
	// /**
	// * Combina o parser atual com ele mesmo, fazendo com que o novo parser retorne se as regras deste parser forem satisfeitas 1 ou N
	// vezes,
	// * é um operador quantificador de repetição similar ao <code>+</code> da Regex
	// */
	// public Parser repPlus()
	// {
	// return rep(1);
	// }
	//

	// /**
	// * Marca o ponto, como sendo um ponto de injeção para um "parser futuro", depois de algumas operações você deve usar o método
	// * {@link Parser#inject() inject()} para injetar o parser neste ponto. <br/>
	// * É particularmente útil para se fazer parser recursivos
	// */
	// public Parser markIPoint()
	// {
	// return new Parser(injectedParser)
	// {
	// @Override
	// protected Tuple2<ParserResult, Integer> parse(final String input, final Integer startPos)
	// {
	// final Tuple2<ParserResult, Integer> firstResult = Parser.this.parse(input, startPos);
	// final Tuple2<ParserResult, Integer> secondResult = injectedParser[0].parse(input, firstResult.getVal2());
	//
	// final List<Object> allResults = new ArrayList<Object>();
	// allResults.addAll(firstResult.getVal1().getResult());
	// allResults.addAll(secondResult.getVal1().getResult());
	//
	// final LinkedList<ParserErrorCode> allFailures = new LinkedList<ParserErrorCode>();
	// allFailures.addAll(firstResult.getVal1().getErrorStack());
	// allFailures.addAll(secondResult.getVal1().getErrorStack());
	//
	// return Tuple2.from(ParserResult.createSuccess(allResults, allFailures), secondResult.getVal2());
	// }
	// };
	// }

	// /**
	// * Injeta o atual parser dentro de um Injection Point marcado anteriormente. Vide {@link Parser#markIPoint() markIPoint()}.
	// */
	// public Parser inject()
	// {
	// injectedParser[0] = this;
	// return new Parser()
	// {
	// @Override
	// public ParserResult parse(final String input)
	// {
	// return Parser.this.parse(input);
	// }
	// };
	// }
	//
	// /**
	// * Mapea os resultados encontrados por este parse a outros utilizando a função de mapeamento passada por parametro
	// *
	// * @param func função de mapeamento
	// */
	// public Parser map(final Function1<List<Object>, List<Object>> func)
	// {
	// return new Parser(injectedParser)
	// {
	// @Override
	// protected Tuple2<ParserResult, Integer> parse(final String input, final Integer startPos)
	// {
	// final Tuple2<ParserResult, Integer> parseResult = Parser.this.parse(input, startPos);
	// final List<Object> mappedResult = func.apply(parseResult.getVal1().getResult());
	// final ParserResult pResult = ParserResult.createSuccess(mappedResult, parseResult.getVal1().getErrorStack());
	// return Tuple2.from(pResult, parseResult.getVal2());
	// }
	// };
	// }
}
