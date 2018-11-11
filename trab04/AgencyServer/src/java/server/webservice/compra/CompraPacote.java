package server.webservice.compra;

import java.time.LocalDate;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import server.webservice.xmladapters.LocalDateAdapter;

/** Representa uma requisição de compra de pacote.
 * Define os atributos que devem ser enviados pelo cliente em XML via web
 * service.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
@XmlRootElement
public class CompraPacote {
    /** Identificador do voo de ida. */
    @XmlElement
    private int idVooIda;
    
    /** Identificador do voo de volta. */
    @XmlElement
    private int idVooVolta;
    
    /** Identificador do hotel. */
    @XmlElement
    private int idHotel;
    
    /** Data do voo de ida e da primeira diária do hotel. */
    @XmlElement
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate dataIda;
    
    /** Data do voo de volta e dia de saída do hotel. */
    @XmlElement
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate dataVolta;
    
    /** Número de quartos. */
    @XmlElement
    private int numQuartos;
    
    /** Número de pessoas. */
    @XmlElement
    private int numPessoas;
    
    /*------------------------------------------------------------------------*/

    /** Retorna o identificador do voo de ida
     * @return identificador do voo de ida
     */
    public int getIdVooIda() {
        return idVooIda;
    }

    /** Retorna o identificador do voo de volta
     * @return identificador do voo de volta
     */
    public int getIdVooVolta() {
        return idVooVolta;
    }

    /** Retorna o identificador do hotel
     * @return identificador do hotel
     */
    public int getIdHotel() {
        return idHotel;
    }

    /** Retorna a data de ida.
     * @return data de ida
     */
    public LocalDate getDataIda() {
        return dataIda;
    }

    /** Retorna a data de volta.
     * @return data de volta
     */
    public LocalDate getDataVolta() {
        return dataVolta;
    }

    /** Retorna o número de quartos.
     * @return número de quartos
     */
    public int getNumQuartos() {
        return numQuartos;
    }

    /** Retorna o número de pessoas.
     * @return número de pessoas
     */
    public int getNumPessoas() {
        return numPessoas;
    }
    
    /*------------------------------------------------------------------------*/

    /** Construtor sem argumentos para permitir que o objeto seja utilizado
     * pelos métodos do web service como XML.
     * Inicializa todos os atributos com valores inválidos.
     */
    public CompraPacote() {
        this.idVooIda = -1;
        this.idVooVolta = -1;
        this.idHotel = -1;
        this.dataIda = null;
        this.dataVolta = null;
        this.numQuartos = -1;
        this.numPessoas = -1;
    }

    /** Construtor principal que inicializa todos os atributos.
     * @param idVooIda identificador do voo de ida
     * @param idVooVolta identificador do voo de volta
     * @param idHotel identificador do hotel
     * @param dataIda data de início do pacote
     * @param dataVolta data de fim do pacote
     * @param numQuartos número de quartos
     * @param numPessoas número de pessoas
     */
    public CompraPacote(int idVooIda, int idVooVolta, int idHotel, LocalDate dataIda, LocalDate dataVolta, int numQuartos, int numPessoas) {
        this.idVooIda = idVooIda;
        this.idVooVolta = idVooVolta;
        this.idHotel = idHotel;
        this.dataIda = dataIda;
        this.dataVolta = dataVolta;
        this.numQuartos = numQuartos;
        this.numPessoas = numPessoas;
    }
}
