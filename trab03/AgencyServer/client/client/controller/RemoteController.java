package client.controller;

import client.AgencyClientImpl;
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
    private Registry namingServiceRef;

    /** Referência remota do servidor */
    private AgencyServer serverRef;

    /** Instância do cliente */
    private AgencyClientImpl client;

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

        try {
            namingServiceRef = LocateRegistry.getRegistry(NAMING_SERVICE_HOST,
                    NAMING_SERVICE_PORT);

            serverRef = (AgencyServer) namingServiceRef.lookup("server");

            client = new AgencyClientImpl(serverRef);

            // FIXME: Debug
            //testarVoos();

            // FIXME: Debug
            testarHoteis();
        }
        catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

    }

    /*------------------------------------------------------------------------*/

    /** Wrapper para o teste dos voos.
     * @throws RemoteException se ocorrer erro no RMI
     */
    private void testarVoos() throws RemoteException {
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

    /** Wrapper para o teste dos hotéis.
     * @throws RemoteException caso ocorra erro no RMI
     */
    private void testarHoteis() throws RemoteException {
        LocalDate dataIda = LocalDate.of(2018, 10, 5);
        LocalDate dataVolta = LocalDate.of(2018, 10, 7);

        HashMap<InfoHotel, ArrayList<InfoHospedagem>> madoka = serverRef.consultarHospedagens(Cidade.CURITIBA, dataIda, dataVolta, 1, 2);

        for (HashMap.Entry<InfoHotel, ArrayList<InfoHospedagem>> entry : madoka.entrySet()) {
            InfoHotel hotel = entry.getKey();
            ArrayList<InfoHospedagem> hospedagens = entry.getValue();

            System.out.println(hotel.getId());
            for (InfoHospedagem hosp : hospedagens) {
                System.out.println(hosp.getData());
            }
        }
    }

    /*------------------------------------------------------------------------*/
}
