package br.com.tbdc.client.controller;

import br.com.tbdc.client.AgencyClientImpl;
import br.com.tbdc.rmi.InterfaceCoordenador;

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
    private static final String REGISTRY_HOST = "localhost";

    /** Porta do serviço de nomes */
    private static final int REGISTRY_PORT = 11037;

    /** Referência ao serviço de nomes do servidor */
    private Registry registry;

    /** Referência remota do servidor */
    private InterfaceCoordenador serverRef;

    /** Instância do cliente */
    private AgencyClientImpl client;

    /*------------------------------------------------------------------------*/

    public InterfaceCoordenador getServerRef() {
        return serverRef;
    }

    /*------------------------------------------------------------------------*/

    /** Instância única do controlador */
    private static RemoteController ourInstance = new RemoteController();

    /** Resgatar a instância única do controlador.
     * @return instância
     */
    public static RemoteController getInstance() {
        return ourInstance;
    }

    /*------------------------------------------------------------------------*/

    /** Construtor padrão desse controlador */
    private RemoteController() {
    }

    /*------------------------------------------------------------------------*/

    /** Apenas invoca a função de conexão com alguns parâmetros padrão.
     * @param ui referência à interface gráfica (para o objeto cliente).
     * @throws RemoteException caso ocorra erro no RMI
     * @throws NotBoundException caso não exista a entrada "name" no serviço de
     */
    public void connect(ClientUIController ui) throws RemoteException, NotBoundException {
        connect(REGISTRY_HOST, REGISTRY_PORT, "servidor_coordenador", ui);
    }

    /** Obtém a referência do servidor no serviço de nomes e instancia um objeto
     * cliente.
     * @param host nome de host, ex: "localhost"
     * @param port porta, ex: 11037
     * @param name string a procurar no serviço de nomes
     * @param ui referência à interface gráfica (para o objeto cliente).
     * @throws RemoteException caso ocorra erro no RMI
     * @throws NotBoundException caso não exista a entrada "name" no serviço de
     * nomes
     */
    public void connect(String host, int port, String name, ClientUIController ui) throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(host, port);
        serverRef = (InterfaceCoordenador) registry.lookup(name);
        client = new AgencyClientImpl(ui);
    }
}
