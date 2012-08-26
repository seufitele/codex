package br.com.codex.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Essa classe encapsula a classe Date do Java, provendo métodos mais sucintos
  * para realizar operações básicas.
 * 
 * ATENÇÃO: Classe IMUTÁVEL
 * Autor: Vinícius Seufitele Pinto (F9540702)
 */
public class DateTime implements Comparable<DateTime>
{
	private final Calendar calendar;
	
	/**
	 * Constrói uma nova instância do DateTime, com a data e hora que refletem o momento da
	 * criação do objeto.
	 */
	public DateTime()
	{
		this(new Date());
	}
	
	/**
	 * Cria uma nova instância do DateTime, com base no tempo representado
	 * pelo calendar passado como parâmetro.
	 * @param theCalendar O calendar que representará o tempo deste DateTime
	 */
	public DateTime(final Calendar theCalendar)
	{
		this(theCalendar.getTime());
	}
	
	/**
	 * Cria uma nova instância do DateTime, com base no tempo representado
	 * pela Data passado como parâmetro.
	 * @param date A data que representará o tempo deste DateTime
	 */
	public DateTime(final Date date)
	{
		final Calendar calTemp = Calendar.getInstance();
		calTemp.setTime(date);
		calendar = calTemp;
	}
	
	/**
	 * Cria uma nova instância do DateTime, representando
	 * o dia, mes e ano passados como parâmetro, com os outros campos
	 * (hora, minuto, segundo, milisegundo) definidos como 0.
	 * @param dia O dia da data a ser criada
	 * @param mes O mês da data a ser criada
	 * @param ano O ano da data a ser criada
	 */
	public DateTime(final Integer dia, final Integer mes, final Integer ano)
	{
		this(dia, mes, ano, 0, 0, 0, 0);
	}
	
	/**
	 * Cria uma nova instância do DateTime, representando
	 * o dia, mes e ano, hora, minuto, segundo e milisegundo passados como parâmetro.
	 * @param dia O dia da data a ser criada
	 * @param mes O mês da data a ser criada
	 * @param ano O ano da data a ser criada
	 * @param hora A hora da data a ser criada (0-23)
	 * @param minuto O minuto da data a ser criada (0-59)
	 * @param segundo O segundo da data a ser criada (0-59)
	 * @param milisegundo O milisegundo da data a ser criada (0-999)
	 */
	public DateTime(final Integer dia, final Integer mes, final Integer ano, final Integer hora, 
    			final Integer minuto, final Integer segundo, final Integer milisegundo)
	{
		final Calendar theCal = Calendar.getInstance();

        theCal.set(Calendar.DAY_OF_MONTH, dia);
        theCal.set(Calendar.MONTH, mes - 1);
        theCal.set(Calendar.YEAR, ano);
        theCal.set(Calendar.HOUR_OF_DAY, hora);
        theCal.set(Calendar.MINUTE, minuto);
        theCal.set(Calendar.SECOND, segundo);
        theCal.set(Calendar.MILLISECOND, milisegundo);
        
        calendar = theCal;
	}
	
	/**
	 * Retorna um DateTime que representa a data e hora atual do sistema
	 * @return Um DateTime que representa a data e hora atual do sistema
	 */
	public static DateTime now()
	{
		return new DateTime();
	}
	
	/**
	 * Adiciona uma determinada quantidade de dias para a data representada por este DateTime.
	 * @param quantidade A quantidade a ser adicionada
	 * @return Um novo DateTime que representa a data adicionada
	 */
	public DateTime addDays(final Integer quantidade)
	{
		return somar(Calendar.DAY_OF_MONTH, quantidade);
	}
	
	/**
	 * Adiciona uma determinada quantidade de meses para a data representada por este DateTime.
	 * @param quantidade A quantidade a ser adicionada
	 * @return Um novo DateTime que representa a data adicionada
	 */
	public DateTime addMonths(final Integer quantidade)
	{
		return somar(Calendar.MONTH, quantidade);
	}
	
	/**
	 * Adiciona uma determinada quantidade de anos para a data representada por este DateTime.
	 * @param quantidade A quantidade a ser adicionada
	 * @return Um novo DateTime que representa a data adicionada
	 */
	public DateTime addYears(final Integer quantidade)
	{
		return somar(Calendar.YEAR, quantidade);
	}
	
	/**
	 * Adiciona uma determinada quantidade de horas para a data representada por este DateTime.
	 * @param quantidade A quantidade a ser adicionada
	 * @return Um novo DateTime que representa a data adicionada
	 */
	public DateTime addHours(final Integer quantidade)
	{
		return somar(Calendar.HOUR_OF_DAY, quantidade);
	}
	
	/**
	 * Adiciona uma determinada quantidade de minutos para a data representada por este DateTime.
	 * @param quantidade A quantidade a ser adicionada
	 * @return Um novo DateTime que representa a data adicionada
	 */
	public DateTime addMinutes(final Integer quantidade)
	{
		return somar(Calendar.MINUTE, quantidade);
	}
	
	/**
	 * Adiciona uma determinada quantidade de segundos para a data representada por este DateTime.
	 * @param quantidade A quantidade a ser adicionada
	 * @return Um novo DateTime que representa a data adicionada
	 */
	public DateTime addSeconds(final Integer quantidade)
	{
		return somar(Calendar.SECOND, quantidade);
	}
	
	/**
	 * Adiciona uma determinada quantidade de milisegundos para a data representada por este DateTime.
	 * @param quantidade A quantidade a ser adicionada
	 * @return Um novo DateTime que representa a data adicionada
	 */
	public DateTime addMilliseconds(final Integer quantidade)
	{
		return somar(Calendar.MILLISECOND, quantidade);
	}
	
	/**
	 * Verifica se esta data é anterior à data passada como parâmetro
	 * @param data A data a ser verificada
	 * @return True se esta data for anterior à data passada como parâmetro, ou false do contrário
	 */
	public boolean before(final Date data)
	{
		return this.toDate().before(data);
	}
	
	/**
	 * Verifica se esta data é anterior ao calendar passado como parâmetro
	 * @param cal O calendar a ser verificado
	 * @return True se esta data for anterior ao calendar passado como parâmetro, ou false do contrário
	 */
	public boolean before(final Calendar cal)
	{
		return this.before(cal.getTime());
	}
	
	/**
	 * Verifica se esta data é anterior ao DateTime passado como parâmetro
	 * @param data O DateTime a ser verificado
	 * @return True se esta data for anterior ao DateTime passado como parâmetro, ou false do contrário
	 */
	public boolean before(final DateTime data)
	{
		return this.before(data.toDate());
	}
	
	/**
	 * Verifica se esta data (dia/mês/ano) é anterior à data de hoje (excluindo verificação de hora)
	 * @return True se esta data for anterior à data de hoje, ou false do contrário
	 */
	public boolean beforeToday()
	{
		return this.before(DateTime.now().datePart());
	}
	
	/**
	 * Verifica se esta data é posterior à data passada como parâmetro
	 * @param data A data a ser verificada
	 * @return True se esta data for posterior à data passada como parâmetro, ou false do contrário
	 */
	public boolean after(final Date data)
	{
		return this.toDate().after(data);
	}
	
	/**
	 * Verifica se esta data é posterior ao calendar passado como parâmetro
	 * @param cal O calendar a ser verificado
	 * @return True se esta data for posterior ao calendar passado como parâmetro, ou false do contrário
	 */
	public boolean after(final Calendar cal)
	{
		return this.after(cal.getTime());
	}

	/**
	 * Verifica se esta data é posterior ao DateTime passado como parâmetro
	 * @param data O DateTime a ser verificado
	 * @return True se esta data for posterior ao DateTime passado como parâmetro, ou false do contrário
	 */
	public boolean after(final DateTime data)
	{
		return this.after(data.toDate());
	}
	
	/**
	 * Verifica se esta data (dia/mês/ano) é posterior à data de hoje (excluindo verificação de hora)
	 * @return True se esta data for posterior à data de hoje, ou false do contrário
	 */
	public boolean afterToday()
	{
		return this.after(DateTime.now());
	}
	
	/**
	 * Retorna o mês guardado nesta data.
	 * ATENÇÃO: O mês será um valor no intervalo 1 - 12  
	 * @return Um inteiro, no intervalo 1 - 12, que representa o mês guardado nesta data
	 */
    public Integer getMonth()
    {
    	return calendar.get(Calendar.MONTH) + 1;
    }
    
	/**
	 * Retorna o dia do mês guardado nesta data.
	 * @return Um inteiro, que representa o dia do mês guardado nesta data
	 */
    public Integer getDay()
    {
    	return calendar.get(Calendar.DAY_OF_MONTH);
    }
    
    /**
	 * Retorna o ano guardado nesta data.
	 * @return Um inteiro, que representa o ano guardado nesta data
	 */
    public Integer getYear()
    {
    	return calendar.get(Calendar.YEAR);
    }
    
    /**
	 * Retorna a hora do dia guardada nesta data (0 - 23).
	 * @return Um inteiro, que representa a hora guardada nesta data.
	 */
    public Integer getHour()
    {
    	return calendar.get(Calendar.HOUR_OF_DAY);
    }
    
    /**
	 * Retorna o minuto do dia guardado nesta data (0 - 59).
	 * @return Um inteiro, que representa o minuto guardado nesta data.
	 */
    public Integer getMinute()
    {
    	return calendar.get(Calendar.MINUTE);
    }
    
    /**
	 * Retorna o segundo do dia guardado nesta data (0 - 59).
	 * @return Um inteiro, que representa o segundo guardado nesta data.
	 */
    public Integer getSecond()
    {
    	return calendar.get(Calendar.SECOND);
    }
    
    /**
	 * Retorna o milisegundo do dia guardado nesta data (0 - 999).
	 * @return Um inteiro, que representa o milisegundo guardado nesta data.
	 */
    public Integer getMillisecond()
    {
    	return calendar.get(Calendar.MILLISECOND);
    }
    
    /**
	 * Retorna o nome do mês guardado nesta data.
	 * @return O nome do mês guardado nesta data.
	 */
    public String getMonthName()
    {
    	return DateFormatSymbols.getInstance().getMonths()[this.getMonth() - 1];
    }
    
    /**
	 * Formata esta data de acordo com o padrão passado como parâmetro.
	 * @param O padrão da data. Ex: 'dd/MM/yyyy HH:mm'
	 * @return Uma string que representa esta data formatada.
	 */
    public String format(final String pattern)
    {
    	return new SimpleDateFormat(pattern).format(this.toDate());
    }
    
    /**
     * Retorna esta data, com a hora, minuto, segundo e milisegundos com valor 0.
     * @return Esta data, com as informações de hora, minuto, segundo e milisegundo com valor 0.
     */
    public DateTime datePart()
    {
    	return new DateTime(this.getDay(), this.getMonth(), this.getYear());
    }
    
    /**
     * Converte essa instância DateTime numa instância Date que representa 
     * o mesmo TimeStamp.
     * @return Uma instância Date que representa o mesmo TimeStamp desta instância.
     */
    public Date toDate()
    {
    	return calendar.getTime();
    }
    
    /**
     * Converte essa instância DateTime numa instância Date onde os valores de hora, minuto, segundo
     * e milisegundo são de valor 0. 
     * @return Uma instância Date que representa o mesmo TimeStamp desta instância, com a hora, minuto, segundo
     * e milisegundo com valor 0.
     */
    public Date toDatePart()
    {
    	return datePart().toDate();
    }
   
    //Métodos 'helper'
    private DateTime somar(final Integer campo, final Integer quantidade) 
    {
    	final Calendar calTemp = Calendar.getInstance(); //Cria um novo calendar para não bagunçar o atual
    	calTemp.setTime(this.toDate());
    	calTemp.add(campo, quantidade);
    	return new DateTime(calTemp);
    }
    //
    
	@Override
	public int compareTo(final DateTime otherDate)
	{
		return this.toDate().compareTo(otherDate.toDate());
	}

	@Override
	public int hashCode()
	{
		return this.toDate().hashCode();
	}

	@Override
	public boolean equals(final Object obj)
	{
		return (obj != null) && (obj instanceof DateTime) && this.toDate().equals(((DateTime) obj).toDate()) ;
	}

	@Override
	public String toString()
	{
		return this.toDate().toString();
	}
    
}
