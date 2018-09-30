package model.saldo;

import java.io.Serializable;
import java.util.ArrayList;

public class Saldo implements Serializable {

    private int max;
    private int atual;

    private ArrayList<Reserva> reservas;

    public Saldo(int max) {
        this.max = max;
        this.atual = max;
        this.reservas = new ArrayList<>();
    }

    public int consultarSaldo() {
        synchronized (this) {
            return atual;
        }
    }

    public Reserva pegarSaldo(int qnt) {
        synchronized (this) {
            Reserva reserva = null;
            if (qnt <= atual) {
                reservas.add(reserva = new Reserva(qnt));
                atual -= qnt;
            }
            return reserva;
        }
    }

    public boolean estornar(Reserva reserva) {
        if (reservas.remove(reserva)) {
            atual += reserva.quantidade;
            return true;
        }
        return false;
    }

}
