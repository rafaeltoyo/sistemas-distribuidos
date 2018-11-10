package server.webservice.compra;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import server.model.voo.TipoPassagem;

/** Representa uma requisição de compra de passagem.
 * Define os atributos que devem ser enviados pelo cliente em XML via web
 * service.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
@XmlRootElement
public class CompraPassagem {
    /** Tipo: SOMENTE_IDA ou IDA_E_VOLTA. */
    @XmlElement
    private TipoPassagem tipo;
    
    /** Identificador do voo de ida. */
    @XmlElement
    private int idVooIda;
    
    /** Identificador do voo de volta. */
    @XmlElement
    private int idVooVolta;
    
    /** Número de pessoas. */
    @XmlElement
    private int numPessoas;
    
    /*------------------------------------------------------------------------*/

    /** Retorna o tipo da passagem.
     * @return tipo da passagem
     */
    public TipoPassagem getTipo() {
        return tipo;
    }

    /** Retorna o identificador do voo de ida.
     * @return identificador do voo de ida
     */
    public int getIdVooIda() {
        return idVooIda;
    }

    /** Retorna o identificador do voo de volta.
     * @return identificador do voo de volta
     */
    public int getIdVooVolta() {
        return idVooVolta;
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
    public CompraPassagem() {
        this.tipo = null;
        this.idVooIda = -1;
        this.idVooVolta = -1;
        this.numPessoas = -1;
    }

    /** Construtor principal que inicializa todos os atributos.
     * @param tipo tipo da passagem
     * @param idVooIda identificador do voo de ida
     * @param idVooVolta identificador do voo de volta
     * @param numPessoas número de pessoas
     */
    public CompraPassagem(TipoPassagem tipo, int idVooIda, int idVooVolta, int numPessoas) {
        this.tipo = tipo;
        this.idVooIda = idVooIda;
        this.idVooVolta = idVooVolta;
        this.numPessoas = numPessoas;
    }
}
