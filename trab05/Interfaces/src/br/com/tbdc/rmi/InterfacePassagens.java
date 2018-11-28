package br.com.tbdc.rmi;

import br.com.tbdc.model.cidade.Cidade;
import br.com.tbdc.model.voo.InfoVoo;
import br.com.tbdc.model.voo.TipoPassagem;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * =====================================================================================================================
 * Interface RMI para o servidor de companhia aérea.
 * =====================================================================================================================
 *
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public interface InterfacePassagens extends Remote {

    /**
     * =================================================================================================================
     * Retorna uma lista de passagens que atendem aos atributos fornecidos nos parâmetros.
     * =================================================================================================================
     *
     * @param tipo       somente ida ou ida e volta
     * @param origem     local de origem do voo
     * @param destino    local de destino do voo
     * @param dataIda    data do voo de ida
     * @param dataVolta  data do voo de volta, caso o tipo seja ida e volta
     * @param numPessoas número de passagens desejadas
     * @return lista de passagens aéreas disponíveis que atendem aos parâmetros
     * @throws RemoteException caso ocorra erro no RMI
     */
    ArrayList<InfoVoo> consultarPassagens(TipoPassagem tipo,
                                          Cidade origem,
                                          Cidade destino,
                                          LocalDate dataIda,
                                          LocalDate dataVolta,
                                          int numPessoas) throws RemoteException;

    /**
     * =================================================================================================================
     * Tenta comprar passagens de um voo de ida e opcionalmente de um voo de volta.
     * Se o parâmetro tipo for IDA_E_VOLTA, primeiro tenta-se comprar as passagens de ida e depois as passagens de
     * volta. Caso seja efetuada a reserva das passagens de ida, mas ocorra falha na reserva das passagens de volta, a
     * reserva da passagem de ida é revertida (não é efetuada a sua compra).
     * =================================================================================================================
     *
     * @param tipo       SOMENTE_IDA ou IDA_E_VOLTA
     * @param idVooIda   identificador do voo de ida
     * @param idVooVolta identificador do voo de volta
     * @param numPessoas número de passagens a adquirir (para ambos os voos)
     * @return true se e somente se a compra for bem sucedida
     * @throws RemoteException caso ocorra erro no RMI
     */
    boolean comprarPassagens(TipoPassagem tipo,
                             int idVooIda,
                             int idVooVolta,
                             int numPessoas) throws RemoteException;

}
