package model.saldo;

import java.util.ArrayList;

public class Saldo {

    private int max;

    private ArrayList<Reserva> reservas;

    public Saldo(int max) {
        this.max = max;
        this.reservas = new ArrayList<>();
    }

    public int consultarSaldo() {
        synchronized(this) {
            int gasto = 0;
            for (Reserva reserva : reservas) {
                gasto += reserva.quantidade;
            }
            return max - gasto;
        }
    }

    public Reserva pegarSaldo(int qnt) {
        synchronized (this) {
            Reserva reserva = null;
            if (qnt <= max) {
                reservas.add(reserva = new Reserva(qnt));
            }
            return reserva;
        }
    }
}