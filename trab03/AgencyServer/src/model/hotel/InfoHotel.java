package model.hotel;

import model.cidade.Cidade;

import java.time.LocalDate;

public class InfoHotel {

    private static int count = 0;

    private int id;

    private Cidade local;

    private LocalDate data;

    private int quartosDisp;

    /*------------------------------------------------------------------------*/

    public int getId() {
        return id;
    }

    /*------------------------------------------------------------------------*/

    public InfoHotel(Cidade local, LocalDate dataEntrada, LocalDate dataSaida,
                     int numQuartos) {
        this.id = (count++);
        this.local = local;
        this.data = dataEntrada;
        this.quartosDisp = numQuartos;
    }

}
