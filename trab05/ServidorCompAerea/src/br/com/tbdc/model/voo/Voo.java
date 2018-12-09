package br.com.tbdc.model.voo;

import br.com.tbdc.model.cidade.Cidade;
import br.com.tbdc.model.saldo.Dinheiro;
import br.com.tbdc.model.saldo.Reserva;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/** Representa um voo e mantém contagem interna do número de vagas disponíveis
 * (passagens) para compra.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class Voo implements Serializable {
    /** Número de poltronas total */
    private int poltronasTotal;

    /** Número de poltronas disponíveis */
    private int poltronasDisp;

    /** Conjunto de reservas (compras realizadas) */
    private ArrayList<Reserva> reservas = new ArrayList<>();

    /** Informações */
    private InfoVoo infoVoo;

    /*------------------------------------------------------------------------*/

    /** Retorna as informações do voo
     * @return informações do voo
     */
    public InfoVoo getInfoVoo() {
        return infoVoo;
    }

    /** Retorna o identificador numérico do voo
     * @return identificador do voo
     */
    public int getId() {
        return infoVoo.getId();
    }

    /** Retorna a cidade de origem do voo
     * @return cidade de origem do voo
     */
    public Cidade getOrigem() {
        return infoVoo.getOrigem();
    }

    /** Retorna a cidade de destino do voo
     * @return cidade de destino do voo
     */
    public Cidade getDestino() {
        return infoVoo.getDestino();
    }

    /** Retorna a data do voo
     * @return data do voo
     */
    public LocalDate getData() {
        return infoVoo.getData();
    }

    /** Retorna o número de poltronas disponíveis no voo
     * @return número de poltronas disponíveis no voo
     */
    public int getPoltronasDisp() {
        synchronized (this) {
            return poltronasDisp;
        }
    }

    /** Retorna o preço unitário da passagem
     * @return preço de uma passagem
     */
    public Dinheiro getPrecoPassagem() {
        return infoVoo.getPrecoPassagem();
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
     * @param precoPassagem valor da passagem
     */
    public Voo(Cidade origem, Cidade destino, LocalDate data,
               int poltronasTotal, Dinheiro precoPassagem) {
        this.poltronasTotal = poltronasTotal;
        this.poltronasDisp = poltronasTotal;
        infoVoo = new InfoVoo(origem, destino, data, poltronasTotal, precoPassagem);
    }

    /*------------------------------------------------------------------------*/

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
