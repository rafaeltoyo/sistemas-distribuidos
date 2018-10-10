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

            // FIXME: Debug: Criação de voos
            Cidade[] cidades = {Cidade.CURITIBA, Cidade.SAO_PAULO, Cidade.FLORIANOPOLIS};
            criarVoos(cidades, LocalDate.of(2018, 1, 1), LocalDate.of(2018, 1, 5), 80, agencyServerImpl);

            /*
            FIXME: Debug: Criação de hotéis
            */

            Hotel hotel;

            hotel = new Hotel("Ibis", Cidade.CURITIBA, 400);
            hotel.adicionarHospedagem(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 1, 5));
            agencyServerImpl.adicionarHotel(hotel);

            hotel = new Hotel("Ibis", Cidade.SAO_PAULO, 400);
            hotel.adicionarHospedagem(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 1, 5));
            agencyServerImpl.adicionarHotel(hotel);

            hotel = new Hotel("Ibis", Cidade.FLORIANOPOLIS, 400);
            hotel.adicionarHospedagem(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 1, 5));
            agencyServerImpl.adicionarHotel(hotel);

            hotel = new Hotel("Slaviero", Cidade.CURITIBA, 200);
            hotel.adicionarHospedagem(LocalDate.of(2018, 1, 3), LocalDate.of(2018, 1, 5));
            agencyServerImpl.adicionarHotel(hotel);

            hotel = new Hotel("Slaviero", Cidade.SAO_PAULO, 200);
            hotel.adicionarHospedagem(LocalDate.of(2018, 1, 3), LocalDate.of(2018, 1, 5));
            agencyServerImpl.adicionarHotel(hotel);

            hotel = new Hotel("Slaviero", Cidade.FLORIANOPOLIS, 200);
            hotel.adicionarHospedagem(LocalDate.of(2018, 1, 3), LocalDate.of(2018, 1, 5));
            agencyServerImpl.adicionarHotel(hotel);

            hotel = new Hotel("Mercure", Cidade.CURITIBA, 100);
            hotel.adicionarHospedagem(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 1, 3));
            agencyServerImpl.adicionarHotel(hotel);

            hotel = new Hotel("Mercure", Cidade.SAO_PAULO, 100);
            hotel.adicionarHospedagem(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 1, 3));
            agencyServerImpl.adicionarHotel(hotel);

            hotel = new Hotel("Mercure", Cidade.FLORIANOPOLIS, 100);
            hotel.adicionarHospedagem(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 1, 3));
            agencyServerImpl.adicionarHotel(hotel);
        }
        catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    /** Cria voos entre todas as cidades fornecidas, para todos os dias no
     * intervalo de datas, com o número de poltronas especificado
     * @param cidades vetor contendo as cidades
     * @param dataIni data de início do intervalo
     * @param dataFim data de fim do intervalo (também são criados voos nessa
     *                data)
     * @param numPoltronas número de poltronas de cada voo
     */
    private static void criarVoos(Cidade[] cidades, LocalDate dataIni, LocalDate dataFim, int numPoltronas, AgencyServerImpl server) {
        // Copia o objeto LocalDate
        LocalDate dataIter = dataIni.plusDays(0);

        while (!dataIter.isAfter(dataFim)) {
            for (Cidade c1 : cidades) {
                for (Cidade c2 : cidades) {
                    // Cria cópia do objeto
                    LocalDate data = dataIter.plusDays(0);

                    Voo v = new Voo(c1, c2, data, numPoltronas);
                    server.adicionarVoo(v);
                }
            }

            dataIter = dataIter.plusDays(1);
        }
    }
}
