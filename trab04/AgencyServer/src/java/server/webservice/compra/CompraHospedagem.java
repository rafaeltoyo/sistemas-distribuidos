package server.webservice.compra;

import java.time.LocalDate;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import server.webservice.xmladapters.LocalDateAdapter;

/** Representa uma requisição de compra de hospedagem.
 * Define os atributos que devem ser enviados pelo cliente em XML via web
 * service.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
@XmlRootElement
public class CompraHospedagem {
    /** Identificador numérico do hotel. */
    @XmlElement
    private int idHotel;
    
    /** Data de início do período de hospedagem. */
    @XmlElement
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate dataIni;
    
    /** Data de fim do período de hospedagem. */
    @XmlElement
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate dataFim;
    
    /** Número de quartos. */
    @XmlElement
    private int numQuartos;
    
    /*------------------------------------------------------------------------*/

    /** Retorna o identificador do hotel.
     * @return identificador do hotel
     */
    public int getIdHotel() {
        return idHotel;
    }

    /** Retorna a data de início do período em que o cliente deseja ficar
     * hospedado.
     * @return data de início
     */
    public LocalDate getDataIni() {
        return dataIni;
    }

    /** Retorna a data de fim do período em que o cliente deseja ficar
     * hospedado.
     * @return data de fim
     */
    public LocalDate getDataFim() {
        return dataFim;
    }

    /** Retorna o número de quartos que o cliente deseja reservar.
     * @return número de quartos
     */
    public int getNumQuartos() {
        return numQuartos;
    }
    
    /*------------------------------------------------------------------------*/

    /** Construtor sem argumentos para permitir que o objeto seja utilizado
     * pelos métodos do web service como XML.
     * Inicializa todos os atributos com valores inválidos.
     */
    public CompraHospedagem() {
        this.idHotel = -1;
        this.dataIni = null;
        this.dataFim = null;
        this.numQuartos = -1;
    }

    /** Construtor principal que inicializa todos os atributos.
     * @param idHotel identificador do hotel
     * @param dataIni data de início do período
     * @param dataFim data de fim do período
     * @param numQuartos número de quartos
     */
    public CompraHospedagem(int idHotel, LocalDate dataIni, LocalDate dataFim, int numQuartos) {
        this.idHotel = idHotel;
        this.dataIni = dataIni;
        this.dataFim = dataFim;
        this.numQuartos = numQuartos;
    }
}
