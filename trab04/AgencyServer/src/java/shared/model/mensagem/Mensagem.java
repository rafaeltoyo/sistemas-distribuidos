package shared.model.mensagem;

import java.io.Serializable;

/** Esta classe simboliza uma mensagem de notificação de evento do servidor para
 * o cliente.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class Mensagem implements Serializable {
    /** ID de referencia do evento que originou essa mensagem */
    private int id;

    /** Conteúdo da mensagem */
    private String mensagem;

    /*------------------------------------------------------------------------*/

    /** Retorna o ID do evento que originou essa mensagem.
     * @return ID do evento que originou essa mensagem
     */
    public int getId() {
        return id;
    }

    /** Retorna o conteúdo da mensagem.
     * @return conteúdo da mensagem
     */
    public String getMensagem() {
        return mensagem;
    }

    /*------------------------------------------------------------------------*/

    /** Construtor padrão a partir de um ID e uma string.
     * @param id ID do evento que originou essa mensagem
     * @param mensagem conteúdo da mensagem
     */
    public Mensagem(int id, String mensagem) {
        this.id = id;
        this.mensagem = mensagem;
    }
}
