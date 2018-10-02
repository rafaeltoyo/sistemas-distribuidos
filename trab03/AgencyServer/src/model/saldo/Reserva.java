package model.saldo;

import java.util.Date;

public class Reserva {
    Date data;

    int quantidade;

    public Reserva(int quantidade) {
        this.data = new Date();
        this.quantidade = quantidade;
    }
}
