package com.github.detentor.codex.product;

/**
 * Classe que objetiva centralizar a forma de criar tuplas.
 */
public class Tuple
{
    private Tuple()
    {
        // previne instanciação
    }

    /**
     * Cria uma tupla 2 a partir dos valores valor1 e valor2 passados como parâmetro.
     * 
     * @param <A> O tipo de dados do primeiro item
     * @param <B> O tipo de dados do segundo item
     * @param valor1 O valor do primeiro item
     * @param valor2 O valor do segundo item
     * @return Uma tupla 2 com os valores passados como parâmetro
     */
    public static <A, B> Tuple2<A, B> from(final A valor1, final B valor2)
    {
        return Tuple2.from(valor1, valor2);
    }

    /**
     * Cria uma tuple 3 a partir dos valores passados como parâmetro
     * 
     * @param <A> O tipo de dados do primeiro valor
     * @param <B> O tipo de dados do segundo valor
     * @param <C> O tipo de dados do terceiro valor
     * @param valor1 O valor do primeiro elemento
     * @param valor2 O valor do segundo elemento
     * @param valor3 O valor do terceiro elemento
     * @return Uma tuple3 com os valores passados como parâmetro
     */
    public static <A, B, C> Tuple3<A, B, C> from(final A valor1, final B valor2, final C valor3)
    {
        return Tuple3.from(valor1, valor2, valor3);
    }

    /**
     * Cria uma tuple 4 a partir dos valores passados como parâmetro
     * 
     * @param <A> O tipo de dados do primeiro valor
     * @param <B> O tipo de dados do segundo valor
     * @param <C> O tipo de dados do terceiro valor
     * @param <D> O tipo de dados do quarto valor
     * @param valor1 O valor do primeiro elemento
     * @param valor2 O valor do segundo elemento
     * @param valor3 O valor do terceiro elemento
     * @param valor4 O valor do quarto elemento
     * @return Uma tuple4 com os valores passados como parâmetro
     */
    public static <A, B, C, D> Tuple4<A, B, C, D> from(final A valor1, final B valor2, final C valor3, final D valor4)
    {
        return Tuple4.from(valor1, valor2, valor3, valor4);
    }

    /**
     * Cria uma tuple 5 a partir dos valores passados como parâmetro
     * 
     * @param <A> O tipo de dados do primeiro valor
     * @param <B> O tipo de dados do segundo valor
     * @param <C> O tipo de dados do terceiro valor
     * @param <D> O tipo de dados do quarto valor
     * @param <E> O tipo de dados do quinto valor
     * @param valor1 O valor do primeiro elemento
     * @param valor2 O valor do segundo elemento
     * @param valor3 O valor do terceiro elemento
     * @param valor4 O valor do quarto elemento
     * @param valor5 O valor do quinto elemento
     * @return Uma tuple5 com os valores passados como parâmetro
     */
    public static <A, B, C, D, E> Tuple5<A, B, C, D, E> from(final A valor1, final B valor2, final C valor3, final D valor4, final E valor5)
    {
        return Tuple5.from(valor1, valor2, valor3, valor4, valor5);
    }
}
