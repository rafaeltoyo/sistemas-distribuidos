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
public class CompraHospedagem {
    @XmlElement
    private int idHotel;
    
    @XmlElement
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate dataIni;
    
    @XmlElement
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate dataFim;
    
    @XmlElement
    private int numQuartos;
    
    /*------------------------------------------------------------------------*/

    public int getIdHotel() {
        return idHotel;
    }

    public LocalDate getDataIni() {
        return dataIni;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public int getNumQuartos() {
        return numQuartos;
    }
    
    /*------------------------------------------------------------------------*/

    public CompraHospedagem() {
        this.idHotel = -1;
        this.dataIni = null;
        this.dataFim = null;
        this.numQuartos = -1;
    }

    public CompraHospedagem(int idHotel, LocalDate dataIni, LocalDate dataFim, int numQuartos) {
        this.idHotel = idHotel;
        this.dataIni = dataIni;
        this.dataFim = dataFim;
        this.numQuartos = numQuartos;
    }
}
