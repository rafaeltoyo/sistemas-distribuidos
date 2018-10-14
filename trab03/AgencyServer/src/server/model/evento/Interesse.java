package server.model.evento;

import server.model.cidade.Cidade;

import java.io.Serializable;

/** Representação de um Interesse por parte do Cliente, sendo utilizado para listagem apenas.
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

    /** Valor máximo do interesse */
    private float valorMaximo;

    /**
     * Construtor padrão de um Interesse, sendo possível sua criação apenas após criar um registro de interesse
     * no servidor remoto, utilizando o ID gerado.
     * @param origem Cidade origem do interesse
     * @param destino Cidade destino do interesse
     * @param tipo Tipo do interesse
     * @param valorMaximo Valor máximo do interesse
     */
    public Interesse(Cidade origem, Cidade destino, TipoInteresse tipo, float valorMaximo) {
        this.id = 0;
        this.origem = origem;
        this.destino = destino;
        this.tipo = tipo;
        this.valorMaximo = valorMaximo;
    }

    /**
     * Retorna o ID do interesse
     * @return ID do interesse
     */
    public int getId() {
        return id;
    }

    /**
     * Alterar o ID do interesse
     * @param id ID do interesse
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna a cidade origem do interesse
     * @return Cidade origem do interesse
     */
    public Cidade getOrigem() {
        return origem;
    }

    /**
     * Retorna a cidade destino do interesse
     * @return Cidade destino do interesse
     */
    public Cidade getDestino() {
        return destino;
    }

    /**
     * Retorna o tipo do interesse
     * @return Tipo do interesse
     */
    public TipoInteresse getTipo() {
        return tipo;
    }

    /**
     * Retorna o valor máximo do interesse
     * @return Valor máximo do interesse
     */
    public float getValorMaximo() {
        return valorMaximo;
    }

    /**
     * Enum dos tipos de interesse existentes.
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

        /** Nome do tipo de interesse */
        private final String n;

        /**
         * Construtor para armazenar uma String como nomne do tipo de interesse.
         * @param n Nome do tipo de interesse.
         */
        TipoInteresse(final String n) {
            this.n = n;
        }

        /**
         * Sobrecarga do método toString para retornar a string fornecida no construtor.
         * @return Nome do tipo de interesse
         */
        @Override
        public String toString() {
            return n;
        }
    }
}
