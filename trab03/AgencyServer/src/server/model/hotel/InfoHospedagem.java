package server.model.hotel;

import java.io.Serializable;
import java.time.LocalDate;

/** Representa as informações de uma hospedagem (dia em um hotel). Contém apenas
 * informações que podem ser relevantes para o cliente. Objetos dessa classe
 * podem ser enviados por rede.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class InfoHospedagem implements Serializable {
    /** Data da hospedagem
     * No servidor, é mantida uma referência a este campo, que é utilizada como
     * chave para o mapeamento entre datas e hospedagens. */
    private LocalDate data;

    /** Número de quartos disponíveis nesse dia no hotel */
    public int quartosDisp;

    /*------------------------------------------------------------------------*/

    /** Retorna a data da hospedagem
     * @return data da hospedagem
     */
    public LocalDate getData() {
        return data;
    }

    /*------------------------------------------------------------------------*/

    /** Construtor padrão para as informações de hospedagem.
     * @param data data da hospedagem
     * @param quartosDisp número de quartos disponíveis nesse dia
     */
    public InfoHospedagem(LocalDate data, int quartosDisp) {
        this.data = data;
        this.quartosDisp = quartosDisp;
    }
}
