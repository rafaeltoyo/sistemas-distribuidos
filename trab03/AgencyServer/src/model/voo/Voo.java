package model.voo;

import model.cidade.Cidade;
import model.saldo.ObjComSaldo;
import model.saldo.Reserva;

import java.io.Serializable;
import java.util.Calendar;

/** Representa um voo e mantém contagem interna do número de vagas
 * disponíveis (passagens) para compra.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class Voo extends ObjComSaldo implements Serializable {
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
        return saldo.consultarSaldo();
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
        super(poltronasTotal);
        infoVoo = new InfoVoo(origem, destino, data, poltronasTotal);
    }

    @Override
    public Reserva reservar(int numPessoas) {
        synchronized (saldo) {
            Reserva ret = super.reservar(numPessoas);
            if (ret != null) {
                infoVoo.poltronasDisp -= numPessoas;
            }
            return ret;
        }
    }
}
