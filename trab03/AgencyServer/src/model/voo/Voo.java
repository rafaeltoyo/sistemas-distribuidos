package model.voo;

import model.cidade.Cidade;
import model.saldo.ObjComSaldo;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Calendar;

/** Representa um voo e mantém contagem interna do número de vagas
 * disponíveis (passagens) para compra.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class Voo extends ObjComSaldo implements Serializable {
    /** Contagem de voos para o auto-incremento do identificador */
    private static int count = 0;

    /** Identificador do voo */
    private int id;

    /** Local de origem (partida) */
    private Cidade origem;

    /** Local de destino (chegada) */
    private Cidade destino;

    /** Data do voo */
    private Calendar data;

    /*------------------------------------------------------------------------*/

    public int getId() {
        return id;
    }

    public Cidade getOrigem() {
        return origem;
    }

    public Cidade getDestino() {
        return destino;
    }

    public Calendar getData() {
        return data;
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
        this.id = (count++);
        this.origem = origem;
        this.destino = destino;
        this.data = data;
    }
}
