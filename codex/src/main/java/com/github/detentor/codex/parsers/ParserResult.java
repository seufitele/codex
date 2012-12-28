package com.github.detentor.codex.parsers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Essa classe representa o resultado de um Parser. Ela é um ADT, que pode ser do tipo sucesso ou falha.
 * 
 * @author f9540702
 * 
 */
public class ParserResult
{
	protected final LinkedList<ParserErrorCode> errorStack;
	private final List<Object> resultList;
	private final boolean isSuccess;

	public static ParserResult createSuccess(final List<Object> resultList)
	{
		return createSuccess(resultList, new LinkedList<ParserErrorCode>());
	}
	
	public static ParserResult createFailure(final ParserErrorCode theError)
	{
		return createFailure(theError, new ArrayList<Object>());
	}
	
	public static ParserResult createFailure(final ParserErrorCode theError, final List<Object> prevResult)
	{
		final LinkedList<ParserErrorCode> errStack = new LinkedList<ParserErrorCode>();
		errStack.add(theError);
		return createFailure(errStack, prevResult);
	}

	public static ParserResult createFailure(final LinkedList<ParserErrorCode> errorStack)
	{
		return createFailure(errorStack, new ArrayList<Object>());
	}
	
	public static ParserResult createSuccess(final Object result)
	{
		final List<Object> theResultList = new ArrayList<Object>(1);
		theResultList.add(result);
		return createSuccess(theResultList);
	}

	public static ParserResult createSuccess(final List<Object> resultList, final LinkedList<ParserErrorCode> errorStack)
	{
		return new ParserResult(errorStack, resultList, true);
	}

	public static ParserResult createFailure(final LinkedList<ParserErrorCode> errorStack, final List<Object> prevResult)
	{
		return new ParserResult(errorStack, prevResult, false);
	}

	protected ParserResult(final LinkedList<ParserErrorCode> errorStack, final List<Object> resultList, final boolean isSuccess)
	{
		super();
		this.errorStack = errorStack;
		this.resultList = resultList;
		this.isSuccess = isSuccess;
	}

	/**
	 * Retorna os objetos resultantes do parse
	 * 
	 * @return
	 */
	public List<Object> getResult()
	{
		return resultList;
	}

	public LinkedList<ParserErrorCode> getErrorStack()
	{
		return errorStack;
	}

	public boolean isSucess()
	{
		return isSuccess;
	}

	public boolean isFailure()
	{
		return !isSucess();
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

}
