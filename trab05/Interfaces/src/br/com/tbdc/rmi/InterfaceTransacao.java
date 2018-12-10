package br.com.tbdc.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * =====================================================================================================================
 * Interface RMI para o servidor da agência para consulta de status.
 * =====================================================================================================================
 *
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public interface InterfaceTransacao extends Remote {

    boolean responder(int idTransacao, boolean resposta) throws RemoteException;

    /** Retorna o identificador da transação.
     * @return ID da transação
     */
    int getIdTransacao() throws RemoteException;

}
