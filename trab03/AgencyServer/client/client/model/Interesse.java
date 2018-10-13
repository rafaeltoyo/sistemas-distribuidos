package client.model;

import model.cidade.Cidade;

/** Representação de um Interesse por parte do Cliente, sendo utilizado para listagem apenas.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class Interesse {

    /** ID de referência do interesse no servidor remoto */
    private int id;

    /** Cidade alvo do interesse */
    private Cidade cidade;

    /** Tipo do interesse */
    private TipoInteresse tipo;

    /** Status do interesse */
    private StatusInteresse status;

    /**
     * Construtor padrão de um Interesse, sendo possível sua criação apenas após criar um registro de interesse
     * no servidor remoto, utilizando o ID gerado.
     * @param id ID do registro de interesse no servidor remoto
     * @param cidade Cidade do interesse
     * @param tipo Tipo do interesse
     * @param status Status do interesse
     */
    public Interesse(int id, Cidade cidade, TipoInteresse tipo, StatusInteresse status) {
        this.id = id;
        this.cidade = cidade;
        this.tipo = tipo;
        this.status = status;
    }

    /**
     * Retorna o ID do interesse
     * @return ID do interesse
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna a Cidade de interesse
     * @return Cidade de interesse
     */
    public Cidade getCidade() {
        return cidade;
    }

    /**
     * Retorna o Tipo do interesse
     * @return Tipo do interesse
     */
    public TipoInteresse getTipo() {
        return tipo;
    }

    /**
     * Retorna o Status do interesse
     * @return Status do interesse
     */
    public StatusInteresse getStatus() {
        return status;
    }

    /**
     * Alterar o Status do interesse
     * @param status Novo status
     */
    public void setStatus(StatusInteresse status) {
        this.status = status;
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

    /**
     * Enum dos status de interesse existentes.
     * @author Rafael Hideo Toyomoto
     * @author Victor Barpp Gomes
     */
    public enum StatusInteresse {
        /** Interesse aguardando resposta */
        AGUARDANDO("Aguardando"),

        /** Interesse respondido e está disponível */
        DISPONIVEL("Disponível")
        ;

        /** Nome do status do interesse */
        private final String n;

        /**
         * Construtor para armazenar uma String como nomne do status do interesse.
         * @param n Nome do tipo de interesse.
         */
        StatusInteresse(final String n) {
            this.n = n;
        }

        /**
         * Sobrecarga do método toString para retornar a string fornecida no construtor.
         * @return Nome do status do interesse
         */
        @Override
        public String toString() {
            return n;
        }
    }
}
