package client;

import client.controller.ClientUIController;
import shared.model.mensagem.Mensagem;
import shared.remote.AgencyClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/** Representa o cliente da agência.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class AgencyClientImpl extends UnicastRemoteObject implements AgencyClient {
    private ClientUIController ui;

    /*------------------------------------------------------------------------*/

    /** Construtor único.
     * @throws RemoteException caso ocorra erro no RMI
     */
    public AgencyClientImpl(ClientUIController ui) throws RemoteException {
        super();
        this.ui = ui;
    }

    /*------------------------------------------------------------------------*/

    /** {@inheritDoc} */
    public void notifyEvent(Mensagem msg) throws RemoteException {
        if (ui != null) {
            ui.colocarMensagem(msg);
        }
        else {
            System.out.println(msg.getMensagem());
        }
    }
}
