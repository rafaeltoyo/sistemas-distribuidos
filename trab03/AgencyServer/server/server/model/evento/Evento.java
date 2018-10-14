package server.model.evento;

import remote.AgencyClient;

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

    public Evento(Interesse interesse, AgencyClient clientRef) {
        interesse.setId(count++);
        this.interesse = interesse;
        this.clientRef = clientRef;
    }

    public Interesse getInteresse() {
        return interesse;
    }

    public AgencyClient getClientRef() {
        return clientRef;
    }
}
