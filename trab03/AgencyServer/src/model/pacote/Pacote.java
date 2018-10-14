package model.pacote;

import model.hotel.InfoHotel;
import model.voo.InfoVoo;

import java.io.Serializable;
import java.time.LocalDate;

/** Representa um pacote.
 * É utilizado pelo cliente para enviar uma solicitação de compra de pacote ao
 * servidor.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class Pacote implements Serializable {
    /** Informações do voo de ida */
    private InfoVoo vooIda;

    /** Informações do voo de volta */
    private InfoVoo vooVolta;

    /** Informações do hotel */
    private InfoHotel hotel;

    /** Data de ida */
    private LocalDate dataIda;

    /** Data de volta */
    private LocalDate dataVolta;

    /*------------------------------------------------------------------------*/

    /** Retorna as informações do voo de ida.
     * @return informações do voo de ida.
     */
    public InfoVoo getVooIda() {
        return vooIda;
    }

    /** Retorna as informações do voo de volta.
     * @return informações do voo de volta.
     */
    public InfoVoo getVooVolta() {
        return vooVolta;
    }

    /** Retorna as informações do hotel.
     * @return informações do hotel
     */
    public InfoHotel getHotel() {
        return hotel;
    }

    /** Retorna a data do voo de ida (que também é a data da primeira diária do
     * hotel)
     * @return data de ida
     */
    public LocalDate getDataIda() {
        return dataIda;
    }

    /** Retorna a data do voo de volta (que não é a data da última diária do
     * hotel)
     * @return data de volta
     */
    public LocalDate getDataVolta() {
        return dataVolta;
    }

    /*------------------------------------------------------------------------*/

    /** Construtor padrão de um Pacote.
     * @param vooIda informações do voo de ida
     * @param vooVolta informações do voo de volta
     * @param hotel informações do hotel
     * @param dataIda data de ida
     * @param dataVolta data de volta
     */
    public Pacote(InfoVoo vooIda, InfoVoo vooVolta, InfoHotel hotel,
                  LocalDate dataIda, LocalDate dataVolta) {
        this.vooIda = vooIda;
        this.vooVolta = vooVolta;
        this.hotel = hotel;
        this.dataIda = dataIda;
        this.dataVolta = dataVolta;
    }
}
