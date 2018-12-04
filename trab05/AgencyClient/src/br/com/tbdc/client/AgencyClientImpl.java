package br.com.tbdc.client;

import br.com.tbdc.client.controller.ClientUIController;
import br.com.tbdc.rmi.InterfaceCliente;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/** Representa o cliente da agência.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class AgencyClientImpl extends UnicastRemoteObject implements InterfaceCliente {
    private ClientUIController ui;

    /*------------------------------------------------------------------------*/

    /** Construtor único.
     * @throws RemoteException caso ocorra erro no RMI
     * @param ui interface gráfica do usuáruo
     */
    public AgencyClientImpl(ClientUIController ui) throws RemoteException {
        super();
        this.ui = ui;
    }

    /*------------------------------------------------------------------------*/
}
