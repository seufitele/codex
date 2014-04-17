package com.github.detentor.codex.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.github.detentor.codex.collections.mutable.ListSharp;

/**
 * Essa classe provê métodos helper para lidar com arquivos.
 * 
 * @author Vinícius Seufitele Pinto
 *
 */
public final class Files
{
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private Files()
	{
		//previne instanciação
	}

	/**
	 * Retorna um vetor com os bytes lidos de um arquivo.
	 * 
	 * @param fileName O nome do arquivo a ser lido
	 * @return Um vetor de bytes com o conteúdo do arquivo
	 */
	public static byte[] readBytes(final String fileName)
	{
		final File arq = new File(fileName);

		try(FileInputStream fis = new FileInputStream(arq))
		{
			byte[] data = new byte[(int)arq.length()];
			fis.read(data);
			fis.close();
			return data;
		}
		catch (IOException ioe)
		{
			throw new IllegalArgumentException(ioe);
		}
	}

	/**
	 * Lê o conteúdo do arquivo como uma String no formato cp1252.
	 * ATENÇÃO: A string estará mal-formada se ela foi gravada no formato UTF-8.
	 * @param fileName O nome do arquivo a ser lido.
	 * @return Uma String com o conteúdo do arquivo. 
	 */
	public static String readString(final String fileName)
	{
		return readString(fileName, "cp1252");
	}

	/**
	 * Lê o conteúdo do arquivo como uma String no formato escolhido.
	 * @param fileName O nome do arquivo a ser lido.
	 * @param charsetName O nome do charset a ser usado ao transformar o arquivo em String.
	 * @return Uma String com o conteúdo do arquivo.
	 */
	public static String readString(final String fileName, final String charsetName)
	{
		try
		{
			return new String(readBytes(fileName), charsetName);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * Lê o conteúdo do arquivo como linhas. <br/>
	 * O separador de linhas utilizado será o padrão do sistema operacional (em Windows é \r\n, no Linux \n). 
	 * @param fileName O nome do arquivo a ser lido.
	 * @return Um {@link ListSharp} com as linhas do arquivo
	 */
	public static ListSharp<String> readLines(final String fileName)
	{
		return ListSharp.from(readString(fileName).split(LINE_SEPARATOR));
	}
	
	/**
	 * Grava em um arquivo o vetor de dados passado como parâmetro
	 * @param fileName O nome do arquivo a ser gravado
	 * @param dados Os dados a serem gravados no arquivo
	 */
	public static void writeBytes(final String fileName, final byte[] dados)
	{
		final File arq = new File(fileName);

		try(FileOutputStream fis = new FileOutputStream(arq))
		{
			fis.write(dados);
			fis.close();
		}
		catch (IOException ioe)
		{
			throw new IllegalArgumentException(ioe);
		}
	}
	
	/**
	 * Grava em um arquivo a String passada como parâmetro
	 * @param fileName O nome do arquivo a ser gravado
	 * @param theString A String a ser gravada no arquivo
	 */
	public static void writeString(final String fileName, final String theString)
	{
		writeBytes(fileName, theString.getBytes());
	}
}
