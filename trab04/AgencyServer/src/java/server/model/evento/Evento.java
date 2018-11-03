package server.model.evento;

import server.remote.AgencyClient;

/** Representa um registro de interesse de um cliente em um hotel, com os
 * devidos parâmetros.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class Evento {
    /** Contagem de interesses para o auto-incremento do identificador */
    private static int count = 0;

    /** Dados sobre o interesse que esse evento representa */
    private Interesse interesse;

    /** Referência ao cliente (RMI) */
    private AgencyClient clientRef;

    /*------------------------------------------------------------------------*/

    /** Retorna a instância do interesse desse evento.
     * @return interesse do evento
     */
    public Interesse getInteresse() {
        return interesse;
    }

    /** Retorna a referência do cliente desse evento.
     * @return referência do cliente do evento
     */
    public AgencyClient getClientRef() {
        return clientRef;
    }

    /*------------------------------------------------------------------------*/

    /** Construtor único, que recebe um interesse e uma referência do cliente.
     * @param interesse interesse do cliente
     * @param clientRef referência do cliente
     */
    public Evento(Interesse interesse, AgencyClient clientRef) {
        interesse.setId(++count);
        this.interesse = interesse;
        this.clientRef = clientRef;
    }
}
