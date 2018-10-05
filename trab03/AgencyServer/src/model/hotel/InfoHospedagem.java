package model.hotel;

import java.io.Serializable;
import java.time.LocalDate;

public class InfoHospedagem implements Serializable {
    /** Referência à data instanciada pelo Hotel */
    private LocalDate data;

    public int quartosDisp;

    /*------------------------------------------------------------------------*/

    public LocalDate getData() {
        return data;
    }

    /*------------------------------------------------------------------------*/

    public InfoHospedagem(LocalDate data, int quartosDisp) {
        this.data = data;
        this.quartosDisp = quartosDisp;
    }
}
