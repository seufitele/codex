package br.com.codex.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.codex.function.Function2;
import br.com.codex.product.Tuple2;

/**
 * Essa classe encapsula as funcionalidades de um Parser Combinator, para simplificar 
 * a utilização dos mesmos. <br/>
 * 
 * A criação da classe espera apenas uma string com a regex que define o parser. <br/>
 * Opcionalmente, pode-se definir os seguintes atributos: <br/>
 * isOptional : Quando true, o parser retornará um valor vazio quando o match da regex falhar. <br/>
 * discard : Quando true, o valor retornado pelo parser será descartado. Útil quando há uma combinação de parsers, e alguns
 * possuem informações que não são úteis. <br/>
 * 
 * @author Vinícius Seufitele Pinto
 */
public class Parser
{
	private final boolean isOptional;
	private final boolean discard;
	private final Pattern pattern;
	private final Function2<String, Integer, Tuple2<Integer, List<String>>> funcao;
	
	/**
	 * Constrói um parser que valida a regex passada como parâmetro.
	 * @param regex A regex a ser validada pelo parser
	 */
	public Parser(final String regex)
	{
		this(Pattern.compile(regex), null, false, false);
	}
	
	/**
	 * Constrói um parser a partir da regex e dos dois parâmetros passados
	 * @param regex A regex a ser validada pelo parser
	 * @param optional Se é obrigatório que o parse tenha sucesso
	 * @param toDiscard Se o valor retornado pelo parse será descartado
	 */
	public Parser(final String regex, final boolean optional, final boolean toDiscard)
	{
		this(Pattern.compile(regex), null, optional, toDiscard);
	}
	
	/**
	 * Construtor privado, a ser utilizado somente por esta classe, para utilizar a composição de parsers.
	 * @param thePattern
	 * @param aFuncao
	 * @param optional
	 * @param toDiscard
	 */
	private Parser(final Pattern thePattern, final Function2<String, Integer, Tuple2<Integer, List<String>>> aFuncao, 
			final boolean optional, final boolean toDiscard)
	{
		super();
		this.pattern = thePattern;
		this.funcao = aFuncao == null ? new Parse() : aFuncao;
		this.isOptional = optional;
		this.discard = toDiscard;
	}
	
	/**
	 * Classe privada que representa a composição entre dois parsers
	 * 
	 * @author f9540702 Vinícius Seufitele Pinto
	 *
	 */
	private class Parse implements Function2<String, Integer, Tuple2<Integer, List<String>>>
	{
		/**
		 * 
		 */
		@Override
		public Tuple2<Integer, List<String>> apply(final String text, final Integer theStart)
		{
			final Matcher resultado = pattern.matcher(text);
			final int start = skipSpaces(theStart,text);
			//Infelizmente o matcher pode encontrar o resultado em qualquer posição, portanto temos de verificar
			//se a posição inicial da string encontrada é a mesma
			final boolean hasSucceeded = resultado.find(start) && resultado.start() == start;  

			//Só faltou realmente se não sucedeu e não é opcional
			if (!hasSucceeded && !isOptional)
			{
				throw new IllegalArgumentException("parse falhou na posição " + theStart);
			}

			final List<String> listaRetorno = new ArrayList<String>();

			if (!discard)
			{
				if (hasSucceeded)
				{
					listaRetorno.add(resultado.group());
				}
				else 
				{
					listaRetorno.add(""); //Adiciona uma string vazia
				}
			}
			final int lastPos = hasSucceeded ? resultado.end() : start;
			return new Tuple2<Integer, List<String>>(lastPos, listaRetorno);
		}
		
		private int skipSpaces(final int start, final String sequence)
		{
			int finalPos = start;
			
			while (finalPos < sequence.length() && sequence.charAt(finalPos) == ' ')
			{
				finalPos++;
			}
			return finalPos;
		}
	}
	
	private Tuple2<Integer, List<String>> parse(final String text, final int start)
	{
		return this.funcao.apply(text, start);
	}
	
	/**
	 * Tenta a aplicar este parse ao texto passado como parâmetro. <br/>
	 * Se bem-sucedido, retorna uma tupla onde o primeiro elemento é a última posição computada, 
	 * e uma lista de Strings com os tokens encontrados. <br/>
	 * @param text O texto no qual este parser será aplicado
	 * @return Uma tupla onde o primeiro elemento é a posição depois do último caracter verificado, e
	 * o segundo é uma lista de tokens retornados por este parser.
	 */
	public Tuple2<Integer, List<String>> parse(final String text)
	{
		return parse(text, 0);
	}
	
	/**
	 * Cria uma composição deste parser com o parser passado como parâmetro. <br/>
	 * O parser retornado só será bem-sucedido se este parser e o outro parser forem bem-sucedidos em sequência.
	 * @param parser O parser a ser composto com este parser
	 * @return Um novo parser que representa a composição entre eles
	 */
	public Parser and(final Parser parser)
	{
		return new Parser(pattern, new Function2<String, Integer, Tuple2<Integer,List<String>>>()
		{
			@Override
			public Tuple2<Integer, List<String>> apply(final String param1, final Integer param2)
			{
				//Computa o primeiro resultado
				final Tuple2<Integer, List<String>> primeiroResultado = parse(param1, param2);
				
				//Computa o segundo resultado
				final Tuple2<Integer, List<String>> segundoResultado = parser.funcao.apply(param1, primeiroResultado.getVal1());
				
				//Cria uma lista com os dois resultados
				final List<String> listaRetorno = new ArrayList<String>();
				
				listaRetorno.addAll(primeiroResultado.getVal2());
				listaRetorno.addAll(segundoResultado.getVal2());
				
				//Retorna a composição dos resultados
				return new Tuple2<Integer, List<String>>(segundoResultado.getVal1(), listaRetorno);
			}
		}, parser.isOptional, parser.discard);
	}

}
