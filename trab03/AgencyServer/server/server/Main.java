package server;

import model.VooImpl;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Calendar;

/** Representa o ponto de entrada da aplicação servidor.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class Main {
    /** Porta do serviço de nomes */
    private static final int NAMING_SERVICE_PORT = 11037;

    /*------------------------------------------------------------------------*/

    /** Inicializa o serviço de nomes e a aplicação servidor.
     * @param args argumentos de linha de comando
     */
    public static void main(String[] args) {
        try {
            Registry namingServiceRef = LocateRegistry.createRegistry(
                    NAMING_SERVICE_PORT);

            // FIXME: Debug
            Calendar data = Calendar.getInstance();
            data.set(2018, Calendar.SEPTEMBER, 27);
            VooImpl voo = new VooImpl("CWB", "GRU", data, 80);

            AgencyServerImpl agencyServerImpl = new AgencyServerImpl();
            namingServiceRef.bind("server", agencyServerImpl);

            // FIXME: Debug
            agencyServerImpl.adicionarVoo(voo);
        }
        catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
