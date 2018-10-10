package client;

import model.cidade.Cidade;
import model.hotel.InfoHospedagem;
import model.hotel.InfoHotel;
import model.pacote.ConjuntoPacote;
import model.voo.InfoVoo;
import model.voo.TipoPassagem;
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
            //testarHoteis(serverRef);

            // FIXME: Debug
            testarPacotes(serverRef);
        }
        catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    /*------------------------------------------------------------------------*/

    /** Wrapper para o teste dos voos.
     * @param serverRef referência ao servidor
     * @throws RemoteException se ocorrer erro no RMI
     */
    private static void testarVoos(AgencyServer serverRef) throws RemoteException {
        LocalDate data = LocalDate.of(2018, 1, 2);
        LocalDate datavolta = LocalDate.of(2018, 1, 3);
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
     * @param serverRef referência ao servidor
     * @throws RemoteException caso ocorra erro no RMI
     */
    private static void testarHoteis(AgencyServer serverRef) throws RemoteException {
        LocalDate dataIda = LocalDate.of(2018, 1, 3);
        LocalDate dataVolta = LocalDate.of(2018, 1, 5);

        HashMap<InfoHotel, ArrayList<InfoHospedagem>> madoka = serverRef.consultarHospedagens(Cidade.CURITIBA, dataIda, dataVolta, 100, 2);

        for (HashMap.Entry<InfoHotel, ArrayList<InfoHospedagem>> entry : madoka.entrySet()) {
            InfoHotel hotel = entry.getKey();
            ArrayList<InfoHospedagem> hospedagens = entry.getValue();

            System.out.println(hotel.getId());
            for (InfoHospedagem hosp : hospedagens) {
                System.out.println(hosp.getData());
            }

            System.out.println(serverRef.comprarHospedagem(hotel.getId(), dataIda, dataVolta, 100));
            System.out.println(serverRef.comprarHospedagem(hotel.getId(), dataVolta.minusDays(1), dataVolta, 100));
        }
    }

    /*------------------------------------------------------------------------*/

    /** Wrapper para o teste dos pacotes.
     * @param serverRef referência ao servidor
     * @throws RemoteException caso ocorra erro no RMI
     */
    private static void testarPacotes(AgencyServer serverRef) throws RemoteException {
        LocalDate dataIda = LocalDate.of(2018, 1, 3);
        LocalDate dataVolta = LocalDate.of(2018, 1, 5);

        ConjuntoPacote pacotes = serverRef.consultarPacotes(Cidade.SAO_PAULO, Cidade.CURITIBA, dataIda, dataVolta, 1, 2);

        System.out.println("Opções de voos de ida:");
        for (InfoVoo v : pacotes.getVoosIda()) {
            System.out.println("Voo " + v.getId() + " (" + v.getData() + "): " + v.getOrigem() + " para " + v.getDestino());
        }

        System.out.println("Opções de voos de volta:");
        for (InfoVoo v : pacotes.getVoosVolta()) {
            System.out.println("Voo " + v.getId() + " (" + v.getData() + "): " + v.getOrigem() + " para " + v.getDestino());
        }

        System.out.println("Opções de hospedagem:");
        for (HashMap.Entry<InfoHotel, ArrayList<InfoHospedagem>> entry : pacotes.getHospedagens().entrySet()) {
            InfoHotel h = entry.getKey();
            System.out.println("Hotel " + h.getId() + " (" + h.getNome() + "): " + h.getLocal());
        }
    }
}
