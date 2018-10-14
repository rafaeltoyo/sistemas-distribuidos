package server.model.saldo;

import java.util.Date;

public class Reserva {
    Date data;

    private int quantidade;

    public int getQuantidade() {
        return quantidade;
    }

    public Reserva(int quantidade) {
        this.data = new Date();
        this.quantidade = quantidade;
    }
}
