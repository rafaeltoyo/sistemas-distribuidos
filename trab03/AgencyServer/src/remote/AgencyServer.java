package remote;

import model.TipoPassagem;
import model.cidade.Cidade;
import model.hotel.InfoHospedagem;
import model.hotel.InfoHotel;
import model.voo.InfoVoo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/** Interface RMI para o servidor da agência.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public interface AgencyServer extends Remote {
    /** Retorna uma lista de passagens que atendem aos atributos fornecidos
     * nos parâmetros.
     * @param tipo somente ida ou ida e volta
     * @param origem local de origem do voo
     * @param destino local de destino do voo
     * @param dataIda data do voo de ida
     * @param dataVolta data do voo de volta, caso o tipo seja ida e volta
     * @param numPessoas número de passagens desejadas
     * @return lista de passagens aéreas disponíveis que atendem aos parâmetros
     * @throws RemoteException caso ocorra erro no RMI
     */
    ArrayList<InfoVoo> consultarPassagens(TipoPassagem tipo, Cidade origem,
            Cidade destino, LocalDate dataIda, LocalDate dataVolta,
            int numPessoas) throws RemoteException;

    boolean comprarPassagens(TipoPassagem tipo, int idVooIda, int idVooVolta,
            int numPessoas) throws RemoteException;

    HashMap<InfoHotel, ArrayList<InfoHospedagem>> consultarHospedagens(
            Cidade local, LocalDate dataIni, LocalDate dataFim)
            throws RemoteException;

    //boolean comprarHospedagem(InfoHotel hospedagem) throws RemoteException;

    //ArrayList<Pacote> consultarPacotes() throws RemoteException;

    //boolean comprarPacote(Pacote pacote) throws RemoteException;

    //boolean registraEvento(Evento evento) throws RemoteException;

    //boolean removerEvento(Evento evento) throws RemoteException;

}
