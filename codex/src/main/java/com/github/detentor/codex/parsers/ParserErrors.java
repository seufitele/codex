package com.github.detentor.codex.parsers;

public enum ParserErrors implements ParserErrorCode
{
	END_OF_STREAM("Fim inesperado do fluxo de dados"), 
	UNPARSED_TOKENS("Existem caracteres dos dados de entrada que não foram parseados"), 
	UNEXPECTED_TOKEN("Token não esperado");

	private final String errorMessage;

	private ParserErrors(final String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString()
	{
		return errorMessage;
	}
}
