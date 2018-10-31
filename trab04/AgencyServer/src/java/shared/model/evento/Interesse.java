package shared.model.evento;

import shared.model.cidade.Cidade;
import shared.model.saldo.Dinheiro;

import java.io.Serializable;

/** Esta classe representa um interesse por parte do cliente.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class Interesse implements Serializable {
    /** ID de referência do interesse no servidor remoto */
    private int id;

    /** Cidade origem do interesse */
    private Cidade origem;

    /** Cidade destino do interesse */
    private Cidade destino;

    /** Tipo do interesse */
    private TipoInteresse tipo;

    /** Valor máximo dos voos */
    private Dinheiro valorMaximoVoo;

    /** Valor máximo da diária do hotel */
    private Dinheiro valorMaximoHotel;

    /*------------------------------------------------------------------------*/

    /** Retorna o ID do interesse.
     * @return ID do interesse
     */
    public int getId() {
        return id;
    }

    /** Retorna a cidade origem do interesse.
     * @return cidade origem do interesse
     */
    public Cidade getOrigem() {
        return origem;
    }

    /** Retorna a cidade destino do interesse.
     * @return cidade destino do interesse
     */
    public Cidade getDestino() {
        return destino;
    }

    /** Retorna o tipo do interesse.
     * @return tipo do interesse
     */
    public TipoInteresse getTipo() {
        return tipo;
    }

    /** Retorna o valor máximo do interesse de voo.
     * @return valor máximo do interesse de voo
     */
    public Dinheiro getValorMaximoVoo() {
        return valorMaximoVoo;
    }

    /** Retorna o valor máximo da diária de um hotel.
     * @return valor máximo da diária de um hotel
     */
    public Dinheiro getValorMaximoHotel() {
        return valorMaximoHotel;
    }

    /*------------------------------------------------------------------------*/

    /** Alterar o ID do interesse.
     * @param id ID do interesse
     */
    public void setId(int id) {
        this.id = id;
    }

    /*------------------------------------------------------------------------*/

    /** Construtor padrão de um Interesse, sendo possível sua criação apenas
     * após criar um registro de interesse no servidor remoto, utilizando o ID
     * gerado.
     * @param origem cidade origem do interesse
     * @param destino cidade destino do interesse
     * @param tipo tipo do interesse
     * @param valorMaximoVoo valor máximo do interesse em voo
     * @param valorMaximoHotel valor máximo do interesse em hotel
     */
    public Interesse(Cidade origem, Cidade destino, TipoInteresse tipo, Dinheiro valorMaximoVoo, Dinheiro valorMaximoHotel) {
        this.id = 0;
        this.origem = origem;
        this.destino = destino;
        this.tipo = tipo;
        this.valorMaximoVoo = valorMaximoVoo;
        this.valorMaximoHotel = valorMaximoHotel;
    }

    /*------------------------------------------------------------------------*/

    /** Enum dos tipos de interesse existentes.
     * @author Rafael Hideo Toyomoto
     * @author Victor Barpp Gomes
     */
    public enum TipoInteresse {
        /** Interesse em Voos */
        VOO("Voo"),

        /** Interesse em Hospedagens */
        HOSPEDAGEM("Hospedagem"),

        /** Interesse em Pacotes */
        PACOTE("Pacote")
        ;

        /*--------------------------------------------------------------------*/

        /** Nome do tipo de interesse */
        private final String n;

        /*--------------------------------------------------------------------*/

        /** Construtor para armazenar uma String como nomne do tipo de
         * interesse.
         * @param n Nome do tipo de interesse.
         */
        TipoInteresse(final String n) {
            this.n = n;
        }

        /*--------------------------------------------------------------------*/

        /** Sobrecarga do método toString para retornar a string fornecida no
         * construtor.
         * @return Nome do tipo de interesse
         */
        @Override
        public String toString() {
            return n;
        }
    }
}
