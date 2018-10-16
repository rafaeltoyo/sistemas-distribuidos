package model.hotel;

import model.cidade.Cidade;

public class InfoHotel {
    /** Contagem de hotéis para o auto-incremento do identificador */
    private static int count = 0;

    /** Identificador numérico do hotel */
    private int id;

    /** Nome do hotel */
    private String nome;

    /** Cidade do hotel */
    private Cidade local;

    /** Número de quartos do hotel */
    private int numQuartos;

    /*------------------------------------------------------------------------*/

    /** Retorna o identificador do hotel
     * @return identificador do hotel
     */
    public int getId() {
        return id;
    }

    /** Retorna o nome do hotel
     * @return nome do hotel
     */
    public String getNome() {
        return nome;
    }

    /** Retorna o cidade do hotel
     * @return cidade do hotel
     */
    public Cidade getLocal() {
        return local;
    }

    /** Retorna o número de quartos do hotel
     * @return número de quartos do hotel
     */
    public int getNumQuartos() {
        return numQuartos;
    }

    /*------------------------------------------------------------------------*/

    /**
     * Construtor padrão de informação de um hotel
     * @param nome Nome do hotel
     * @param local Cidade do hotel
     * @param numQuartos Número de quartos do hotel
     */
    public InfoHotel(String nome, Cidade local, int numQuartos) {
        this.id = (count++);
        this.nome = nome;
        this.local = local;
        this.numQuartos = numQuartos;
    }

}
