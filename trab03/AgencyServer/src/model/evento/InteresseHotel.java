package model.evento;

import model.cidade.Cidade;
import remote.AgencyClient;

import java.time.LocalDate;

/** Representa um registro de interesse de um cliente em um hotel, com os
 * devidos parâmetros.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class InteresseHotel {
    /** Contagem de interesses para o auto-incremento do identificador */
    private static int count = 0;

    /** Identificador do registro de interesse */
    private int id;

    /** Cidade na qual o cliente deseja hospedagem */
    private Cidade destino;

    /** Data de entrada do cliente (primeira diária) */
    private LocalDate dataIni;

    /** Data de saída do cliente (não inclusa nas diárias) */
    private LocalDate dataFim;

    /** Referência ao cliente (RMI) */
    private AgencyClient clientRef;

    /*------------------------------------------------------------------------*/

    /** Retorna o identificador do registro.
     * @return identificador do registro
     */
    public int getId() {
        return id;
    }

    /** Retorna a cidade na qual o cliente deseja hospedagem.
     * @return cidade
     */
    public Cidade getDestino() {
        return destino;
    }

    /** Retorna a data em que o cliente pretende iniciar sua hospedagem
     * (primeira diária).
     * @return data de entrada
     */
    public LocalDate getDataIni() {
        return dataIni;
    }

    /** Retorna a data em que o cliente pretende sair da hospedagem (não inclusa
     * nas diárias)
     * @return data de saída
     */
    public LocalDate getDataFim() {
        return dataFim;
    }

    /** Retorna a referència ao cliente que fez o registro de interesse.
     * @return referência ao cliente (RMI)
     */
    public AgencyClient getClientRef() {
        return clientRef;
    }

    /*------------------------------------------------------------------------*/

    /** Construtor padrão de um interesse em hotel.
     * @param destino cidade na qual o cliente deseja hospedagem
     * @param dataIni data de entrada do cliente (primeira diária)
     * @param dataFim data de saída do cliente (não inclusa nas diárias)
     * @param clientRef referência ao cliente (RMI)
     */
    public InteresseHotel(Cidade destino, LocalDate dataIni, LocalDate dataFim,
                          AgencyClient clientRef) {
        this.id = (count++);
        this.destino = destino;
        this.dataIni = dataIni;
        this.dataFim = dataFim;
        this.clientRef = clientRef;
    }
}
