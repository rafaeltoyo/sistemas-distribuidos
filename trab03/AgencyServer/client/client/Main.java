package client;

import model.TipoPassagem;
import model.voo.Voo;
import remote.AgencyServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Calendar;

/** Representa o ponto de entrada da aplicação cliente.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class Main {
    /** Nome de host do serviço de nomes */
    private static final String NAMING_SERVICE_HOST = "localhost";

    /** Porta do serviço de nomes */
    private static final int NAMING_SERVICE_PORT = 11037;

    /*------------------------------------------------------------------------*/

    /** Inicializa a aplicação cliente.
     * @param args argumentos de linha de comando
     */
    public static void main(String[] args) {
        try {
            Registry namingServiceRef = LocateRegistry.getRegistry(
                    NAMING_SERVICE_HOST, NAMING_SERVICE_PORT);
            AgencyServer serverRef = (AgencyServer) namingServiceRef.lookup(
                    "server");

            AgencyClientImpl client = new AgencyClientImpl(serverRef);

            Calendar data = Calendar.getInstance();
            data.set(2018, Calendar.SEPTEMBER, 27);
            ArrayList<Voo> voos = serverRef.consultarPassagens(TipoPassagem.SOMENTE_IDA, "CWB", "GRU", data, null, 1);

            for (Voo voo : voos) {
                System.out.println(voo.getId());
                System.out.println(voo.getData());
                System.out.println(voo.getOrigem());
                System.out.println(voo.getDestino());
                System.out.println(voo.getPoltronasDisp());
            }
        }
        catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
