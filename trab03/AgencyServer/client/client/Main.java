package client;

import model.TipoPassagem;
import remote.AgencyServer;
import remote.Voo;

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
                System.out.println(voo.obterId());
                System.out.println(voo.obterData());
                System.out.println(voo.obterOrigem());
                System.out.println(voo.obterDestino());
                System.out.println(voo.obterPoltronasDisp());
                System.out.println(voo.obterPoltronasTotal());
            }
        }
        catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
