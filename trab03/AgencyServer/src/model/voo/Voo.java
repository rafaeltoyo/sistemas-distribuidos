package model.voo;

import model.cidade.Cidade;
import model.saldo.ObjComSaldo;
import model.saldo.Reserva;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/** Representa um voo e mantém contagem interna do número de vagas
 * disponíveis (passagens) para compra.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class Voo {
    private int poltronasTotal;

    private int poltronasDisp;

    private ArrayList<Reserva> reservas = new ArrayList<>();

    private InfoVoo infoVoo;

    /*------------------------------------------------------------------------*/

    public InfoVoo getInfoVoo() {
        return infoVoo;
    }

    public int getId() {
        return infoVoo.getId();
    }

    public Cidade getOrigem() {
        return infoVoo.getOrigem();
    }

    public Cidade getDestino() {
        return infoVoo.getDestino();
    }

    public Calendar getData() {
        return infoVoo.getData();
    }

    public int getPoltronasDisp() {
        synchronized (this) {
            return poltronasDisp;
        }
    }

    /*------------------------------------------------------------------------*/

    /** Construtor para um novo voo.
     * Obtém um identificador com auto-incremento.
     * Considera que todas as poltronas da aeronave estão disponíveis para
     * compra.
     * @param origem local de origem (partida)
     * @param destino local de destino (chegada)
     * @param data data de partida
     * @param poltronasTotal número total de poltronas da aeronave
     */
    public Voo(Cidade origem, Cidade destino, Calendar data, int poltronasTotal) {
        this.poltronasTotal = poltronasTotal;
        this.poltronasDisp = poltronasTotal;
        infoVoo = new InfoVoo(origem, destino, data, poltronasTotal);
    }

    public Reserva reservar(int numPessoas) {
        synchronized (this) {
            Reserva reserva = null;

            if (numPessoas <= poltronasDisp) {
                reservas.add(reserva = new Reserva(numPessoas));
                poltronasDisp -= numPessoas;
                infoVoo.poltronasDisp -= numPessoas;
            }

            return reserva;
        }
    }

    public boolean estornar(Reserva reserva) {
        synchronized (this) {
            if (reservas.remove(reserva)) {
                poltronasDisp += reserva.getQuantidade();
                infoVoo.poltronasDisp += reserva.getQuantidade();
                return true;
            }
            return false;
        }
    }
}
