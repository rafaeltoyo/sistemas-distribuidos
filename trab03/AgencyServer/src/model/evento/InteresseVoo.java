package model.evento;

import model.cidade.Cidade;
import remote.AgencyClient;

import java.time.LocalDate;

/** Representa um registro de interesse de um cliente em um voo, com os devidos
 * parâmetros.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class InteresseVoo {
    /** Contagem de interesses para o auto-incremento do identificador */
    private static int count = 0;

    /** Identificador do registro de interesse */
    private int id;

    /** Origem do voo desejado pelo cliente */
    private Cidade origem;

    /** Destino do voo desejado pelo cliente */
    private Cidade destino;

    /** Data do voo desejado pelo cliente */
    private LocalDate data;

    /** Referência ao cliente (RMI) */
    private AgencyClient clientRef;

    /*------------------------------------------------------------------------*/

    /** Retorna o identificador do registro.
     * @return identificador do registro
     */
    public int getId() {
        return id;
    }

    /** Retorna a cidade de origem do voo desejado pelo cliente.
     * @return cidade de origem do voo desejado
     */
    public Cidade getOrigem() {
        return origem;
    }

    /** Retorna a cidade de destino do voo desejado pelo cliente.
     * @return cidade de destino do voo desejado
     */
    public Cidade getDestino() {
        return destino;
    }

    /** Retorna a data do voo desejado pelo cliente.
     * @return data do voo desejado
     */
    public LocalDate getData() {
        return data;
    }

    /** Retorna a referència ao cliente que fez o registro de interesse.
     * @return referência ao cliente (RMI)
     */
    public AgencyClient getClientRef() {
        return clientRef;
    }

    /*------------------------------------------------------------------------*/

    /** Construtor padrão de um registro de interesse de voo.
     * @param origem cidade de origem do voo desejado
     * @param destino cidade de destino do voo desejado
     * @param data data do voo desejado
     * @param clientRef referência ao cliente (RMI)
     */
    public InteresseVoo(Cidade origem, Cidade destino, LocalDate data,
                        AgencyClient clientRef) {
        this.id = (count++);
        this.origem = origem;
        this.destino = destino;
        this.data = data;
        this.clientRef = clientRef;
    }
}
