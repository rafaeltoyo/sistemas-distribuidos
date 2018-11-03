package server.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import server.model.mensagem.Mensagem;

/** Interface RMI para o cliente da agência.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public interface AgencyClient extends Remote {
    /** Envia uma notificação de evento ao cliente.
     * @param msg notificação
     * @throws RemoteException caso ocorra erro no RMI
     */
    void notifyEvent(Mensagem msg) throws RemoteException;
}
