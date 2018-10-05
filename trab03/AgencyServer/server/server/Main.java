package server;

import model.cidade.Cidade;
import model.hotel.Hotel;
import model.voo.Voo;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;

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

            AgencyServerImpl agencyServerImpl = new AgencyServerImpl();
            namingServiceRef.bind("server", agencyServerImpl);

            // FIXME: Debug
            LocalDate data = LocalDate.of(2018, 9, 27);
            Voo voo = new Voo(Cidade.CURITIBA, Cidade.FLORIANOPOLIS, data, 80);
            agencyServerImpl.adicionarVoo(voo);

            // FIXME: Debug
            data = LocalDate.of(2018, 9, 27);
            voo = new Voo(Cidade.FLORIANOPOLIS, Cidade.CURITIBA, data, 80);
            agencyServerImpl.adicionarVoo(voo);

            // FIXME: Debug
            data = LocalDate.of(2018, 9, 28);
            voo = new Voo(Cidade.CURITIBA, Cidade.FLORIANOPOLIS, data, 80);
            agencyServerImpl.adicionarVoo(voo);

            // FIXME: Debug
            data = LocalDate.of(2018, 9, 28);
            voo = new Voo(Cidade.FLORIANOPOLIS, Cidade.CURITIBA, data, 20);
            agencyServerImpl.adicionarVoo(voo);

            // FIXME: Debug
            data = LocalDate.of(2018, 9, 27);
            voo = new Voo(Cidade.CURITIBA, Cidade.SAO_PAULO, data, 80);
            agencyServerImpl.adicionarVoo(voo);

            // FIXME: Debug
            data = LocalDate.of(2018, 9, 28);
            voo = new Voo(Cidade.SAO_PAULO, Cidade.CURITIBA, data, 80);
            agencyServerImpl.adicionarVoo(voo);

            // FIXME: Debug
            data = LocalDate.of(2018, 10, 4);
            Hotel hotel = new Hotel("Ibis", Cidade.CURITIBA, 400);
            hotel.adicionarHospedagem(data, LocalDate.of(2018, 10, 7));
            agencyServerImpl.adicionarHotel(hotel);

            // FIXME: Debug
            data = LocalDate.of(2018, 10, 4);
            hotel = new Hotel("Slaviero", Cidade.BELO_HORIZONTE, 400);
            hotel.adicionarHospedagem(data, LocalDate.of(2018, 10, 7));
            agencyServerImpl.adicionarHotel(hotel);
        }
        catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
