package server.webservice.compra;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import server.model.voo.TipoPassagem;

/**
 *
 * @author Victor
 */
@XmlRootElement
public class CompraPassagem {
    @XmlElement
    private TipoPassagem tipo;
    
    @XmlElement
    private int idVooIda;
    
    @XmlElement
    private int idVooVolta;
    
    @XmlElement
    private int numPessoas;
    
    /*------------------------------------------------------------------------*/

    public TipoPassagem getTipo() {
        return tipo;
    }

    public int getIdVooIda() {
        return idVooIda;
    }

    public int getIdVooVolta() {
        return idVooVolta;
    }

    public int getNumPessoas() {
        return numPessoas;
    }
    
    /*------------------------------------------------------------------------*/

    public CompraPassagem() {
        this.tipo = null;
        this.idVooIda = -1;
        this.idVooVolta = -1;
        this.numPessoas = -1;
    }

    public CompraPassagem(TipoPassagem tipo, int idVooIda, int idVooVolta, int numPessoas) {
        this.tipo = tipo;
        this.idVooIda = idVooIda;
        this.idVooVolta = idVooVolta;
        this.numPessoas = numPessoas;
    }
}
