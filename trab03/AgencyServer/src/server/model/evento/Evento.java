package server.model.evento;

import shared.model.evento.Interesse;
import shared.remote.AgencyClient;

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

    /**
     * Construtor de um Evento a partir de um interesse e uma referência do cliente
     * @param interesse Interesse do cliente
     * @param clientRef Referência do cliente
     */
    public Evento(Interesse interesse, AgencyClient clientRef) {
        interesse.setId(++count);
        this.interesse = interesse;
        this.clientRef = clientRef;
    }

    /**
     * Retorna a instância do interesse desse evento
     * @return Interesse do evento
     */
    public Interesse getInteresse() {
        return interesse;
    }

    /**
     * Retorna a referência do cliente desse evento
     * @return Referência do cliente do evento
     */
    public AgencyClient getClientRef() {
        return clientRef;
    }
}
