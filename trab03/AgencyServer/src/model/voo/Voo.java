package model.voo;

import model.cidade.Cidade;
import model.saldo.Reserva;

import java.time.LocalDate;
import java.util.ArrayList;

/** Representa um voo e mantém contagem interna do número de vagas
 * disponíveis (passagens) para compra.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class Voo {
    /** Número de poltronas total */
    private int poltronasTotal;

    /** Número de poltronas disponíveis */
    private int poltronasDisp;

    /** Conjunto de reservas (compras realizadas) */
    private ArrayList<Reserva> reservas = new ArrayList<>();

    /** Informações */
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

    public LocalDate getData() {
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
    public Voo(Cidade origem, Cidade destino, LocalDate data,
               int poltronasTotal) {
        this.poltronasTotal = poltronasTotal;
        this.poltronasDisp = poltronasTotal;
        infoVoo = new InfoVoo(origem, destino, data, poltronasTotal);
    }

    /** Implementa a reserva (compra) de passagens do voo.
     * @param numPessoas número de poltronas a comprar
     * @return um objeto Reserva, se bem sucedido, ou null, se falhar
     */
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

    /** Desfaz uma compra de passagens
     * @param reserva reserva a estornar
     * @return true se e somente se a compra foi estornada com sucesso
     */
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
