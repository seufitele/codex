package com.github.detentor.codex.collections;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.function.Function2;
import com.github.detentor.codex.function.PartialFunction1;
import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;

/**
 * Interface que determina os métodos que as coleções que possuem suporte a funções de ordem
 * superior deve prover para serem utilizadas. 
 * 
 * @author Vinícius Seufitele Pinto
 *
 * @param <T> O tipo de dados da coleção
 */
public interface HighOrderFunctions<T>
{
	/**
	 * Descarta os elementos do início da coleção enquanto eles satisfizerem o predicado. <br/>
	 * 
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return Os elementos desta coleção com exceção do grupo de elementos do início que satisfazem
	 * o predicado.
	 */
	SharpCollection<T> dropWhile(final Function1<? super T, Boolean> pred);
	
	/**
	 * Descarta os elementos do fim da coleção enquanto eles satisfizerem o predicado. <br/>
	 * 
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return Os elementos desta coleção com exceção do grupo de elementos do fim que satisfazem
	 * o predicado.
	 */
	SharpCollection<T> dropRightWhile(final Function1<? super T, Boolean> pred);

	/**
	 * Pega os elementos da coleção enquanto eles satisfizerem o predicado. <br/>
	 * 
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return O mais longo prefixo desta coleção onde todos os elementos satisfazem o predicado. 
	 */
	SharpCollection<T> takeWhile(final Function1<? super T, Boolean> pred);
	
	/**
	 * Pega os elementos da coleção, começando no último elemento, enquanto eles satisfizerem o predicado. <br/>
	 * 
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return O mais longo prefixo (da direita para a esquerda) desta coleção onde todos os elementos satisfazem o predicado. 
	 */
	SharpCollection<T> takeRightWhile(final Function1<? super T, Boolean> pred);
	
	/**
	 * Retorna o primeiro elemento (ordem definida pelo iterador da coleção) que satisfaz o predicado. <br/>
	 * 
	 * @param pred O predicado a ser utilizado para testar o elemento
	 * @return Uma Option que conterá o elemento se ele existir.
	 */
	Option<T> find(final Function1<? super T, Boolean> pred);
	
	/**
	 * Seleciona todos os elementos desta coleções que satisfazem um determinado predicado
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return Uma nova coleção consistindo de todos os elementos desta coleção que
	 * satisfazem o predicado pred. A ordem dos elementos será a mesma ordem retornada pelo iterator.
	 */
	SharpCollection<T> filter(final Function1<? super T, Boolean> pred);
	
	/**
	 * Divide essa coleção em duas coleções, onde o primeiro elemento da tupla representa os elementos
	 * cujo predicado é satisfeito, e o segundo os elementos que não é
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return Uma tupla onde o primeiro elemento contém uma coleção cujos elementos satisfazem o predicado,
	 * e o segundo não satisfazem
	 */
	Tuple2<? extends SharpCollection<T>, ? extends SharpCollection<T>> partition(final Function1<? super T, Boolean> pred);

	/**
	 * Verifica se o predicado passado como parâmetro é satisfeito por algum elemento desta coleção.
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return true se existe algum elemento que satisfaz o predicado, ou false se não houver
	 */
	boolean exists(final Function1<? super T, Boolean> pred);
	
	/**
	 * Verifica se o predicado passado como parâmetro é satisfeito por todos os elementos desta coleção.
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return true se todos os elementos desta coleção satisfazem o predicado, ou false se existe algum
	 * elemento que não satisfaz o predicado.
	 */
	boolean forall(final Function1<? super T, Boolean> pred);
	
	/**
	 * Executa, na ordem do iterator desta coleção, a função passada como parâmetro, 
	 * acumulando o resultado a cada iteração. <br/>
	 * EX: Se você tiver uma coleção de inteiros, você pode utilizar o 
	 * foldLeft para retornar a soma dos elementos, como no exemplo abaixo:
	 * <pre>
	 * myArray.foldLeft(0, new Action<Integer, Integer> (){ 
	 *     Integer apply(Integer param1, Integer param2){ 
	 *       return param1 + param2;
	 *     }
	 * } </pre>
	 * @param <B> O retorno da função foldLeft
	 * @param startValue O valor inicial a ser aplicação na ação
	 * @param function A função a ser executada a cada passo
	 * @return Um valor B, a partir da aplicação da função passada como parâmetro em cada elemento.
	 */
	<B> B foldLeft(final B startValue, final Function2<B, ? super T, B> function);
	
	/**
	 * Constrói uma nova coleção a partir da aplicação da função passada como parâmetro em cada elemento da coleção. <br/>
	 * A ordem é preservada, se ela estiver bem-definida.
	 * @param <B> O tipo da nova coleção.
	 * @param function Uma função que recebe um elemento desta coleção, e retorna um elemento de (potencialmente) outro tipo.
	 * @return Uma nova coleção, a partir da aplicação da função para cada elemento.
	 */
	<B> SharpCollection<B> map(final Function1<? super T, B> function);
	
	/**
	 * Constrói uma nova coleção a partir da aplicação da função parcial passada como parâmetro em cada elemento da coleção
	 * onde a função parcial está definida. A ordem é preservada, se ela estiver bem-definida. <br/><br/>
	 * Nos casos onde é necessário usar um filtro antes de aplicar um mapa, considere utilizar esta função. <br/>
	 * @param <B> O tipo da nova coleção.
	 * @param function Uma função que recebe um elemento desta coleção, e retorna um elemento de (potencialmente) outro tipo.
	 * @return Uma nova coleção, a partir da aplicação da função parcial para cada elemento onde ela está definida.
	 */
	<B> SharpCollection<B> collect(final PartialFunction1<? super T, B> pFunction);

	/**
	 * Constrói uma nova coleção, a partir da aplicação da função passada 
	 * como parâmetro em cada elemento da coleção, coletando os resultados numa
	 * única coleção. <br/>
	 * @param <B> O tipo da nova coleção
	 * @param function Uma função que recebe um elemento desta coleção, e retorna uma 
	 * coleção de elementos de (potencialmente) outro tipo.
	 * @return Uma nova coleção, a partir da aplicação da função para cada elemento, concatenando os elementos
	 * das coleção.
	 */
	<B> SharpCollection<B> flatMap(final Function1<? super T, ? extends SharpCollection<B>> function);
	
	/**
	 * Conta o número de elementos desta coleção que satisfazem o predicado passado como parâmetro.
	 * @param pred O predicado a ser utilizado para testar os elementos
	 * @return O número de elementos desta coleção que satisfazem o predicado 
	 */
	Integer count(final Function1<? super T, Boolean> pred);
	
	/**
	 * Mostra todos os elementos desta coleção numa string, extraída a partir de mapFunction utilizando, 
	 * entre eles, o separador passado como parâmetro, iniciando por start e terminando com end. <br/>
	 * Este método permite criar uma String a partir de uma função arbitrária sobre os elementos, ao contrário
	 * do {@link SharpCollection#mkString(String, String, String) mkString}, onde é sempre chamado
	 * o {@link #toString() toString} do objeto.
	 * @param start A string a ser colocada no início
	 * @param separator O separador a ser utilizado entre cada chamada ao método toString
	 * @param end A string a ser colocada no fim
	 * @param mapFunction Uma função que transforma os elementos desta coleção em String
	 * @return Uma string representada pela chamada de mapFunction a cada elemento desta coleção, separados
	 * pelo separador passado como parâmetro, começando por start e terminando com end.
	 */
	String mkString(final String start, final String separator, final String end, final Function1<? super T, String> mapFunction);
	
}
