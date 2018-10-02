package model.hotel;

import model.cidade.Cidade;
import model.saldo.ObjComSaldo;

import java.util.Calendar;

public class InfoHotel {

    private static int count = 0;

    private int id;

    private Cidade local;

    private Calendar data;

    private int quartosDisp;

    /*------------------------------------------------------------------------*/

    public int getId() {
        return id;
    }

    /*------------------------------------------------------------------------*/

    public InfoHotel(Cidade local, Calendar dataEntrada, Calendar dataSaida,
                     int numQuartos) {
        this.id = (count++);
        this.local = local;
        this.data = dataEntrada;
        this.quartosDisp = numQuartos;
    }

}
