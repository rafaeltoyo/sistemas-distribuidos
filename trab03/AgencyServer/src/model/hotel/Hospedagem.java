package model.hotel;

import model.saldo.Reserva;

import java.time.LocalDate;
import java.util.ArrayList;

public class Hospedagem {

    private InfoHospedagem infoHospedagem;

    private int quartosMax;

    private int quartosDisp;

    private ArrayList<Reserva> reservas = new ArrayList<>();

    /*------------------------------------------------------------------------*/

    public InfoHospedagem getInfoHospedagem() {
        return infoHospedagem;
    }

    /*------------------------------------------------------------------------*/

    public Hospedagem(LocalDate data, int quartosMax) {
        this.infoHospedagem = new InfoHospedagem(data, quartosDisp);
        this.quartosMax = quartosMax;
        this.quartosDisp = quartosMax;
    }

    /*------------------------------------------------------------------------*/

    public Reserva reservar(int numQuartos) {
        synchronized (this) {
            Reserva reserva = null;

            if (numQuartos <= quartosDisp) {
                reservas.add(reserva = new Reserva(numQuartos));
                quartosDisp -= numQuartos;
                infoHospedagem.quartosDisp -= numQuartos;
            }

            return reserva;
        }
    }

    public boolean estornar(Reserva reserva) {
        synchronized (this) {
            if (reservas.remove(reserva)) {
                quartosDisp += reserva.getQuantidade();
                infoHospedagem.quartosDisp += reserva.getQuantidade();
                return true;
            }
            return false;
        }
    }
}
