package br.com.tbdc.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * =====================================================================================================================
 * Interface RMI para os servidores participantes responderem a transação aberta pelo coordenador.
 * =====================================================================================================================
 *
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public interface InterfaceTransacao extends Remote {

    /**
     * =================================================================================================================
     * Responder o servidor coordenador se a transação pode ou não ser realizada
     * =================================================================================================================
     *
     * @param idTransacao ID da transação da resposta
     * @param resposta    Resposta da viabilidade da transação solicitada
     * @return Sucesso do serviço em registrar a resposta enviada
     * @throws RemoteException caso ocorra erro no RMI
     */
    boolean responder(int idTransacao, boolean resposta) throws RemoteException;

    /** Retorna o identificador da transação.
     * @return ID da transação
     */
    int getIdTransacao() throws RemoteException;

}
