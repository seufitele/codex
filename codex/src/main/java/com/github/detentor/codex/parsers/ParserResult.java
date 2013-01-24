package com.github.detentor.codex.parsers;

import java.util.LinkedList;

import com.github.detentor.codex.collections.mutable.ListSharp;

/**
 * Essa classe representa o resultado de um Parser. Ela é um ADT, que pode ser do tipo sucesso ou falha.
 * 
 * @author f9540702
 * 
 */
public class ParserResult
{
	protected final LinkedList<ParserErrorCode> errorStack;
	private final ListSharp<Object> resultList;
	private final boolean isSuccess;
	private final int lastPos;

	/**
	 * Cria um resultado para um parser que foi bem-sucedido, o resultado é um único elemento
	 * 
	 * @param result
	 * @return
	 */
	public static ParserResult createSuccess(final Object result, final int theLastPos)
	{
		return createSuccess(ListSharp.from(result), theLastPos);
	}

	/**
	 * Cria um resultado para um parser que foi bem-sucedido, onde os tokens pertencem à resultList
	 * 
	 * @param resultList
	 * @return
	 */
	public static ParserResult createSuccess(final ListSharp<Object> resultList, final int theLastPos)
	{
		return createSuccess(resultList, new LinkedList<ParserErrorCode>(), theLastPos);
	}

	/**
	 * Cria um resultado para um parser que foi bem-sucedido, cujo resultado é a lista passada como parâmetro, e a pilha de erros
	 * encontrada é a errorStack
	 * 
	 * @param resultList
	 * @param errorStack
	 * @return
	 */
	public static ParserResult createSuccess(final ListSharp<Object> resultList, final LinkedList<ParserErrorCode> errorStack,
			final int theLastPos)
	{
		return new ParserResult(errorStack, resultList, true, theLastPos);
	}
	
	
	public static ParserResult createFailure(final LinkedList<ParserErrorCode> errorStack, final int theLastPos)
	{
		return createFailure(errorStack, ListSharp.empty(), theLastPos);
	}

	/**
	 * Cria um parser para um resultado que falhou, com o código de erro passado como parâmetro
	 * 
	 * @param theError
	 * @return
	 */
	public static ParserResult createFailure(final ParserErrorCode theError, final int theLastPos)
	{
		return createFailure(theError, ListSharp.empty(), theLastPos);
	}

	/**
	 * Cria um parser para um resultado que falhou, com o código de erro passado como parâmetro e os resultados acumulados previamente
	 * 
	 * @param theError
	 * @return
	 */
	public static ParserResult createFailure(final ParserErrorCode theError, final ListSharp<Object> prevResult, final int theLastPos)
	{
		final LinkedList<ParserErrorCode> errStack = new LinkedList<ParserErrorCode>();
		errStack.add(theError);
		return createFailure(errStack, prevResult, theLastPos);
	}

	public static ParserResult createFailure(final LinkedList<ParserErrorCode> errorStack, final ListSharp<Object> prevResult, final int theLastPos)
	{
		return new ParserResult(errorStack, prevResult, false, theLastPos);
	}

	protected ParserResult(final LinkedList<ParserErrorCode> errorStack, final ListSharp<Object> resultList, final boolean isSuccess,
			final int theLastPos)
	{
		super();
		this.errorStack = errorStack;
		this.resultList = resultList;
		this.isSuccess = isSuccess;
		this.lastPos = theLastPos;
	}

	/**
	 * Cria um parser cujo resultado será o resultado da combinação deste parser e do parser passado como parâmetro. <br/>
	 * Se um dos parsers for de falha, o resultado será de falha. Em ambos os casos os tokens e os erros retornados por ambos serão
	 * combinados.
	 * 
	 * @param theParser O parser a ser combinado com este parser
	 * @return Um parser que representa a combinação deste parser com o parser passado como parâmetro
	 */
	public ParserResult and(final ParserResult theParser)
	{
		final LinkedList<ParserErrorCode> finalStack = new LinkedList<ParserErrorCode>(this.getErrorStack());
		finalStack.addAll(theParser.getErrorStack());
		
		final int theLastPos = this.isFailure() ? this.getLastPos() : theParser.getLastPos();

		final ListSharp<Object> resultTokens = ListSharp.from(this.getResult()).addAll(theParser.getResult());

		if (this.isFailure() || theParser.isFailure())
		{
			return createFailure(finalStack, resultTokens, theLastPos);
		}
		return createSuccess(resultTokens, finalStack, theLastPos);
	}

	/**
	 * Retorna os objetos resultantes do parse
	 * 
	 * @return
	 */
	public ListSharp<Object> getResult()
	{
		return resultList;
	}

	public LinkedList<ParserErrorCode> getErrorStack()
	{
		return errorStack;
	}

	public boolean isSuccess()
	{
		return isSuccess;
	}

	public boolean isFailure()
	{
		return !isSuccess();
	}

	public int getLastPos()
	{
		return lastPos;
	}

	/**
	 * Retorna a mensagem de associada a este parser. <br/>
	 * 
	 * @return
	 */
	public String getMessage()
	{
		return errorStack.peek().toString();
	}

	@Override
	public String toString()
	{
		return isSuccess() ? "Success: " + resultList.mkString("[", ", ", "]") : "Failure : " + ListSharp.from(errorStack).mkString("\n");
	}
}
