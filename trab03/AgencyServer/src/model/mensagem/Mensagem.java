package model.mensagem;

import java.io.Serializable;

/**
 * Classe para gerar uma mensagem de notificação de evento do servidor para o cliente
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class Mensagem implements Serializable {

    /** ID de referencia do evento que originou essa mensagem */
    private int id;

    /** Conteúdo da mensagem */
    private String mensagem;

    /**
     * Construtor padrão a partir de um ID e uma string
     * @param id ID do evento que originou essa mensagem
     * @param mensagem conteudo da mensagem
     */
    public Mensagem(int id, String mensagem) {
        this.id = id;
        this.mensagem = mensagem;
    }

    /**
     * Retorna o ID do evento que originou essa mensagem
     * @return ID do evento que originou essa mensagem
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna o conteúdo da mensagem
     * @return Conteúdo da mensagem
     */
    public String getMensagem() {
        return mensagem;
    }
}
