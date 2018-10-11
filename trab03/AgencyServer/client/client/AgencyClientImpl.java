package client;

import remote.AgencyClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/** Representa o cliente da agência.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class AgencyClientImpl extends UnicastRemoteObject implements AgencyClient {
    // Sem atributos

    /*------------------------------------------------------------------------*/

    /** Construtor único.
     * @throws RemoteException caso ocorra erro no RMI
     */
    public AgencyClientImpl() throws RemoteException {
        super();
    }

    /*------------------------------------------------------------------------*/

    /** {@inheritDoc} */
    public void notifyEvent(String str) throws RemoteException {
        // FIXME: isso tá meio inútil
        System.out.println(str);
    }
}
