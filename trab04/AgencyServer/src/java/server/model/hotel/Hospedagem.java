package server.model.hotel;

import server.model.saldo.Reserva;

import java.time.LocalDate;
import java.util.ArrayList;

/** Representa um dia de um hotel, contabilizando o número de quartos livres.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class Hospedagem {
    /** Informações da hospedagem */
    private InfoHospedagem infoHospedagem;

    /** Número máximo de quartos do hotel */
    private int quartosMax;

    /** Número de quartos disponíveis nesse dia */
    private int quartosDisp;

    /** Lista de reservas (quartos reservados) */
    private ArrayList<Reserva> reservas = new ArrayList<>();

    /*------------------------------------------------------------------------*/

    /** Retorna as informações da hospedagem.
     * @return informações da hospedagem
     */
    public InfoHospedagem getInfoHospedagem() {
        return infoHospedagem;
    }

    /** Retorna o número de quartos disponíveis na hospedagem (dia do hotel).
     * @return número de quartos disponíveis
     */
    public int getQuartosDisp() {
        return quartosDisp;
    }

    /*------------------------------------------------------------------------*/

    /** Instancia a hospedagem (dia de um hotel).
     * @param data data da hospedagem
     * @param quartosMax número de quartos do hotel
     */
    public Hospedagem(LocalDate data, int quartosMax) {
        this.infoHospedagem = new InfoHospedagem(data, quartosDisp);
        this.quartosMax = quartosMax;
        this.quartosDisp = quartosMax;
    }

    /*------------------------------------------------------------------------*/

    /** Reserva quartos no hotel no dia da hospedagem.
     * @param numQuartos número de quartos a reservar
     * @return um objeto Reserva instanciado, ou null, se falhar
     */
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

    /** Desfaz uma reserva de quartos no dia da hospedagem.
     * @param reserva reserva a desfazer
     * @return true se e somente se a reserva foi desfeita
     */
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
