package server.webservice.compra;

import java.time.LocalDate;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import server.webservice.xmladapters.LocalDateAdapter;

/**
 *
 * @author Victor
 */
@XmlRootElement
public class CompraPacote {
    @XmlElement
    private int idVooIda;
    
    @XmlElement
    private int idVooVolta;
    
    @XmlElement
    private int idHotel;
    
    @XmlElement
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate dataIda;
    
    @XmlElement
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate dataVolta;
    
    @XmlElement
    private int numQuartos;
    
    @XmlElement
    private int numPessoas;
    
    /*------------------------------------------------------------------------*/

    public int getIdVooIda() {
        return idVooIda;
    }

    public int getIdVooVolta() {
        return idVooVolta;
    }

    public int getIdHotel() {
        return idHotel;
    }

    public LocalDate getDataIda() {
        return dataIda;
    }

    public LocalDate getDataVolta() {
        return dataVolta;
    }

    public int getNumQuartos() {
        return numQuartos;
    }

    public int getNumPessoas() {
        return numPessoas;
    }
    
    /*------------------------------------------------------------------------*/

    public CompraPacote() {
        this.idVooIda = -1;
        this.idVooVolta = -1;
        this.idHotel = -1;
        this.dataIda = null;
        this.dataVolta = null;
        this.numQuartos = -1;
        this.numPessoas = -1;
    }

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
