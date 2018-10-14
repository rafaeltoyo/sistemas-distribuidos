package client.controller;

import client.AgencyClientImpl;
import remote.AgencyServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/** Controlador do acesso a interface remota do servidor
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class RemoteController {

    /** Nome de host do serviço de nomes */
    private static final String NAMING_SERVICE_HOST = "localhost";

    /** Porta do serviço de nomes */
    private static final int NAMING_SERVICE_PORT = 11037;

    /** Referência ao serviço de nomes do servidor */
    Registry namingServiceRef;

    /** Referência remota do servidor */
    AgencyServer serverRef;

    /** Instância do cliente */
    AgencyClientImpl client;

    /*------------------------------------------------------------------------*/

    /** Instância única do controlador */
    private static RemoteController ourInstance = new RemoteController();

    /** Resgatar a instância única do controlador */
    public static RemoteController getInstance() {
        return ourInstance;
    }

    /*------------------------------------------------------------------------*/

    /** Construtor padrão desse controlador */
    private RemoteController() {
    }

    public void connect(ClientUIController ui) throws RemoteException, NotBoundException {
        connect(NAMING_SERVICE_HOST, NAMING_SERVICE_PORT, "server", ui);
    }

    public void connect(String host, int port, String name, ClientUIController ui) throws RemoteException, NotBoundException {
        namingServiceRef = LocateRegistry.getRegistry(host, port);
        serverRef = (AgencyServer) namingServiceRef.lookup(name);
        client = new AgencyClientImpl(ui);
    }

    /*------------------------------------------------------------------------*/
}
