package client;

import model.TipoPassagem;
import model.cidade.Cidade;
import model.hotel.InfoHospedagem;
import model.hotel.InfoHotel;
import model.voo.InfoVoo;
import remote.AgencyServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

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

            // FIXME: Debug
            //testarVoos(serverRef);

            // FIXME: Debug
            testarHoteis(serverRef);
        }
        catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    /*------------------------------------------------------------------------*/

    private static void testarVoos(AgencyServer serverRef) throws RemoteException {
        LocalDate data = LocalDate.of(2018, 9, 27);
        LocalDate datavolta = LocalDate.of(2018, 9, 28);
        ArrayList<InfoVoo> voos = serverRef.consultarPassagens(TipoPassagem.IDA_E_VOLTA, Cidade.CURITIBA, Cidade.FLORIANOPOLIS, data, datavolta, 1);
        for (InfoVoo voo : voos) {
            System.out.println(voo.getId());
            System.out.println(voo.getData());
            System.out.println(voo.getOrigem());
            System.out.println(voo.getDestino());
            System.out.println(voo.poltronasDisp);
        }

        // FIXME: Debug da compra de voos
        InfoVoo vooIda = null;
        InfoVoo vooVolta = null;

        for (InfoVoo voo : voos) {
            if (voo.getOrigem() == Cidade.CURITIBA && voo.getDestino() == Cidade.FLORIANOPOLIS) {
                vooIda = voo;
            }
            else if (voo.getOrigem() == Cidade.FLORIANOPOLIS && voo.getDestino() == Cidade.CURITIBA) {
                vooVolta = voo;
            }

            if (vooIda != null && vooVolta != null) {
                break;
            }
        }
        if (vooIda != null && vooVolta != null) {
            for (int i = 0; i < 100; ++i) {
                if (serverRef.comprarPassagens(TipoPassagem.IDA_E_VOLTA, vooIda.getId(), vooVolta.getId(), 1)) {
                    System.out.println("Deu boa");
                } else {
                    System.out.println("Não deu boa");
                }
            }
        }
    }

    /*------------------------------------------------------------------------*/

    private static void testarHoteis(AgencyServer serverRef) throws RemoteException {
        LocalDate dataIda = LocalDate.of(2018, 10, 5);
        LocalDate dataVolta = LocalDate.of(2018, 10, 7);

        HashMap<InfoHotel, ArrayList<InfoHospedagem>> madoka = serverRef.consultarHospedagens(Cidade.CURITIBA, dataIda, dataVolta);

        for (HashMap.Entry<InfoHotel, ArrayList<InfoHospedagem>> entry : madoka.entrySet()) {
            InfoHotel hotel = entry.getKey();
            ArrayList<InfoHospedagem> hospedagens = entry.getValue();

            System.out.println(hotel.getId());
            for (InfoHospedagem hosp : hospedagens) {
                System.out.println(hosp.getData());
            }
        }
    }
}
