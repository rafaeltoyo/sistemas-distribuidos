package server;

import model.TipoPassagem;
import model.VooImpl;
import remote.AgencyServer;
import remote.Voo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;

/** Representa o servidor da agência.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class AgencyServerImpl extends UnicastRemoteObject
        implements AgencyServer {
    /** Lista de voos disponíveis */
    private ArrayList<VooImpl> voos = new ArrayList<>();

    /*------------------------------------------------------------------------*/

    /** Construtor único.
     * @throws RemoteException caso ocorra erro no RMI
     */
    public AgencyServerImpl() throws RemoteException {
        super();
    }

    /*------------------------------------------------------------------------*/

    /** Adiciona um voo à lista de voos do servidor
     * @param voo voo já instanciado e inicializado
     */
    public void adicionarVoo(VooImpl voo) {
        voos.add(voo);
    }

    /*------------------------------------------------------------------------*/

    /** Retorna uma lista de passagens que atendem aos atributos fornecidos
     * nos parâmetros.
     * @param tipo somente ida ou ida e volta
     * @param origem identificador do local de origem do voo
     * @param destino identificador do local de destino do voo
     * @param dataIda data do voo de ida
     * @param dataVolta data do voo de volta, caso o tipo seja ida e volta
     * @param numPessoas número de passagens desejadas
     * @return lista de passagens aéreas disponíveis que atendem aos parâmetros
     * @throws RemoteException caso ocorra erro no RMI
     */
    @Override
    public ArrayList<Voo> consultarPassagens(TipoPassagem tipo,
            String origem, String destino, Calendar dataIda, Calendar dataVolta,
            int numPessoas) throws RemoteException {
        // TODO
        ArrayList<Voo> result = new ArrayList<>();
        for (VooImpl voo : voos) {
            result.add(voo);
        }
        return result;
    }

}
