package server;

import model.TipoPassagem;
import model.cidade.Cidade;
import model.saldo.Reserva;
import model.voo.InfoVoo;
import model.voo.Voo;
import remote.AgencyServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;

/** Representa o servidor da agência.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class AgencyServerImpl extends UnicastRemoteObject
        implements AgencyServer {
    /** Lista de voos disponíveis */
    private ArrayList<Voo> voos = new ArrayList<>();

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
    public void adicionarVoo(Voo voo) {
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
    public ArrayList<InfoVoo> consultarPassagens(TipoPassagem tipo,
            Cidade origem, Cidade destino, LocalDate dataIda,
            LocalDate dataVolta, int numPessoas) throws RemoteException {
        ArrayList<InfoVoo> result = new ArrayList<>();
        for (Voo voo : voos) {
            // FIXME: precisa synchronized para ler?

            // Adiciona voos de ida
            if (origem.equals(voo.getOrigem()) &&
                    destino.equals(voo.getDestino()) &&
                    dataIda.equals(voo.getData()) &&
                    numPessoas <= voo.getPoltronasDisp()) {
                result.add(voo.getInfoVoo());
            }

            // Adiciona voos de volta
            if (tipo == TipoPassagem.IDA_E_VOLTA && dataVolta != null) {
                if (origem.equals(voo.getDestino()) &&
                        destino.equals(voo.getOrigem()) &&
                        dataVolta.equals(voo.getData()) &&
                        numPessoas <= voo.getPoltronasDisp()) {
                    result.add(voo.getInfoVoo());
                }
            }
        }
        return result;
    }

    /*------------------------------------------------------------------------*/

    /** Tenta comprar passagens de um voo de ida e opcionalmente de um voo de
     * volta.
     * Se o parâmetro tipo for IDA_E_VOLTA, primeiro tenta-se comprar as
     * passagens de ida e depois as passagens de volta. Caso seja efetuada a
     * reserva das passagens de ida, mas ocorra falha na reserva das passagens
     * de volta, a reserva da passagem de ida é revertida (não é efetuada a
     * sua compra).
     * @param tipo SOMENTE_IDA ou IDA_E_VOLTA
     * @param idVooIda identificador do voo de ida
     * @param idVooVolta identificador do voo de volta
     * @param numPessoas número de passagens a adquirir (para ambos os voos)
     * @return true se e somente se a compra for bem sucedida
     * @throws RemoteException caso ocorra erro no RMI
     */
    @Override
    public boolean comprarPassagens(TipoPassagem tipo, int idVooIda,
            int idVooVolta, int numPessoas) throws RemoteException {
        if (tipo == TipoPassagem.IDA_E_VOLTA) {
            return comprarPassagensIdaEVolta(idVooIda, idVooVolta, numPessoas);
        }
        return comprarPassagensSomenteIda(idVooIda, numPessoas);
    }

    /*------------------------------------------------------------------------*/

    /** Tenta efetuar compra de passagem de ida.
     * Chamada pela função comprarPassagens.
     * @param idVoo identificador do voo de ida
     * @param numPessoas número de passagens a adquirir
     * @return true se e somente se a compra for bem sucedida
     */
    private boolean comprarPassagensSomenteIda(int idVoo, int numPessoas) {
        Voo voo = null;

        // Busca o voo
        for (Voo v : voos) {
            if (v.getId() == idVoo) {
                voo = v;
                break;
            }
        }

        // Retorna false se não encontrou o voo
        if (voo == null) {
            return false;
        }

        // Retorna true se conseguir comprar o número desejado,
        // e false caso contrário
        return voo.reservar(numPessoas) != null;
    }

    /*------------------------------------------------------------------------*/

    /** Tenta efetuar compra de passagens de ida e volta.
     * Chamada pela função comprarPassagens.
     * @param idVooIda identificador do voo de ida
     * @param idVooVolta identificador do voo de volta
     * @param numPessoas número de passagens a adquirir (para ambos os voos)
     * @return true se e somente se a compra for bem sucedida para ambos os voos
     */
    private boolean comprarPassagensIdaEVolta(int idVooIda, int idVooVolta,
            int numPessoas) {
        Voo vooIda = null;
        Voo vooVolta = null;

        // Busca os voos
        for (Voo v : voos) {
            int idVoo = v.getId();

            if (idVoo == idVooIda) {
                vooIda = v;
            }
            else if (idVoo == idVooVolta) {
                vooVolta = v;
            }

            if (vooIda != null && vooVolta != null) {
                break;
            }
        }

        // Retorna false se não encontrou algum dos voos
        if (vooIda == null || vooVolta == null) {
            return false;
        }

        Reserva reservaVooIda = vooIda.reservar(numPessoas);
        if (reservaVooIda != null) {
            if (vooVolta.reservar(numPessoas) != null) {
                // Retorna true se conseguir comprar ida e volta
                return true;
            }
            // Caso não consiga comprar a volta,
            // faz rollback na compra do voo de ida e retorna false
            vooIda.estornar(reservaVooIda);
        }
        // Retorna false se não conseguir comprar o número desejado para a ida
        return false;
    }
}
