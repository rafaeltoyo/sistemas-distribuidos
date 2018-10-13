package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** Interface RMI para o cliente da agência.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public interface AgencyClient extends Remote {
    /** Envia uma notificação de evento ao cliente.
     * @param str notificação
     * @throws RemoteException caso ocorra erro no RMI
     */
    void notifyEvent(String str) throws RemoteException;
}
