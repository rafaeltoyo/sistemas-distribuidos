package remote;

import model.TipoPassagem;
import model.voo.Voo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;

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
    ArrayList<Voo> consultarPassagens(TipoPassagem tipo, String origem,
            String destino, Calendar dataIda, Calendar dataVolta,
            int numPessoas) throws RemoteException;

    //ArrayList<Hospedagem> consultarHospedagens() throws RemoteException;

    //boolean comprarHospedagem(Hospedagem hospedagem) throws RemoteException;

    //ArrayList<Pacote> consultarPacotes() throws RemoteException;

    //boolean comprarPacote(Pacote pacote) throws RemoteException;

    //boolean registraEvento(Evento evento) throws RemoteException;

    //boolean removerEvento(Evento evento) throws RemoteException;

}
