package model;

import remote.Voo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;

/** Representa um voo e mantém contagem interna do número de vagas
 * disponíveis (passagens) para compra.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class VooImpl extends UnicastRemoteObject implements Voo {
    /** Contagem de voos para o auto-incremento do identificador */
    private static int count = 0;

    /** Identificador do voo */
    private int id;

    /** Local de origem (partida) */
    private String origem;

    /** Local de destino (chegada) */
    private String destino;

    /** Data do voo */
    private Calendar data;

    /** Número de poltronas total */
    private int poltronasTotal;

    /** Número de poltronas disponíveis */
    private int poltronasDisp;

    /*------------------------------------------------------------------------*/

    public int getId() {
        return id;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public Calendar getData() {
        return data;
    }

    public int getPoltronasTotal() {
        return poltronasTotal;
    }

    public int getPoltronasDisp() {
        return poltronasDisp;
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
     * @throws RemoteException caso ocorra erro no RMI
     */
    public VooImpl(String origem, String destino, Calendar data,
                   int poltronasTotal) throws RemoteException {
        super();
        this.id = (count++);
        this.origem = origem;
        this.destino = destino;
        this.data = data;
        this.poltronasTotal = poltronasTotal;
        this.poltronasDisp = poltronasTotal;
    }

    /** Construtor para um voo com todos os atributos explícitos.
     * Atualiza o contador estático da classe caso o ID fornecido seja maior
     * que o número atual.
     * @param id identificador
     * @param origem local de origem (partida)
     * @param destino local de destino (chegada)
     * @param data data de partida
     * @param poltronasTotal número total de poltronas da aeronave
     * @param poltronasDisp número de vagas disponíveis para compra
     * @throws RemoteException caso ocorra erro no RMI
     */
    public VooImpl(int id, String origem, String destino, Calendar data,
            int poltronasTotal, int poltronasDisp) throws RemoteException {
        this.id = id;
        this.origem = origem;
        this.destino = destino;
        this.data = data;
        this.poltronasTotal = poltronasTotal;
        this.poltronasDisp = poltronasDisp;

        // Atualiza o auto-incremento para impedir que dois voos tenham o
        // mesmo identificador.
        if (id > count) {
            count = id + 1;
        }
    }

    /*------------------------------------------------------------------------*/

    @Override
    public int obterId() {
        return id;
    }

    @Override
    public String obterOrigem() throws RemoteException {
        return origem;
    }

    @Override
    public String obterDestino() throws RemoteException {
        return destino;
    }

    @Override
    public Calendar obterData() throws RemoteException {
        return data;
    }

    @Override
    public int obterPoltronasTotal() throws RemoteException {
        return poltronasTotal;
    }

    @Override
    public int obterPoltronasDisp() throws RemoteException {
        return poltronasDisp;
    }

    @Override
    public boolean comprar(int numPassagens) throws RemoteException {
        // TODO
        return false;
    }
}
