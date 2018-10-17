package shared.remote;

import shared.model.cidade.Cidade;
import shared.model.evento.Interesse;
import shared.model.hotel.InfoHotelRet;
import shared.model.pacote.ConjuntoPacote;
import shared.model.voo.InfoVoo;
import shared.model.voo.TipoPassagem;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

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
    boolean comprarPassagens(TipoPassagem tipo, int idVooIda, int idVooVolta,
            int numPessoas) throws RemoteException;

    /*------------------------------------------------------------------------*/

    /** Retorna um mapa cujas chaves são os hotéis e os valores são listas de
     * hospedagens (data + número de quartos disponíveis), com base nos
     * parâmetros fornecidos.
     * @param local cidade do hotel
     * @param dataIni data de chegada (primeira diária)
     * @param dataFim data de saída (não é inclusa no resultado)
     * @param numQuartos número de quartos desejados
     * @param numPessoas número de pessoas (total, não por quarto)
     * @return mapa com informações de hotel e hospedagem
     * @throws RemoteException caso ocorra erro no RMI
     */
    ArrayList<InfoHotelRet> consultarHospedagens(Cidade local,
            LocalDate dataIni, LocalDate dataFim, int numQuartos,
            int numPessoas) throws RemoteException;

    /*------------------------------------------------------------------------*/

    /** Tenta comprar hospedagem em um hotel para todas as noites entre as datas
     * informadas.
     * @param idHotel identificador do hotel
     * @param dataIni data de entrada
     * @param dataFim data de saída (não é incluída na reserva, ou seja, é feita
     *                reserva somente até o dia anterior à data de saída)
     * @param numQuartos número de quartos desejados
     * @return true se e somente se a compra for bem sucedida
     * @throws RemoteException caso ocorra erro no RMI
     */
    boolean comprarHospedagem(int idHotel, LocalDate dataIni, LocalDate dataFim,
            int numQuartos) throws RemoteException;

    /*------------------------------------------------------------------------*/

    /** Faz uma consulta de voos e hotéis para os dados fornecidos, e retorna os
     * resultados em um objeto.
     * No servidor, não existem pacotes explícitos, apenas voos e hotéis.
     * @param origem local de origem do voo
     * @param destino local de destino do voo e cidade do hotel
     * @param dataIda data do voo de ida e de chegada no hotel
     * @param dataVolta data do voo de volta e de saída do hotel (não é incluída
     *                  reserva de hotel para a data de saída)
     * @param numQuartos número de quartos de hotel desejados
     * @param numPessoas número de passagens desejadas
     * @return conjunto de voos de ida, voos de volta e hospedagens que atendem
     * os dados fornecidos
     * @throws RemoteException caso ocorra erro no RMI
     */
    ConjuntoPacote consultarPacotes(Cidade origem, Cidade destino,
            LocalDate dataIda, LocalDate dataVolta, int numQuartos,
            int numPessoas) throws RemoteException;

    /*------------------------------------------------------------------------*/

    /** Tenta comprar um pacote (voo ida + voo volta + hotel) com os parâmetros
     * especificados.
     * @param idVooIda identificador do voo de ida
     * @param idVooVolta identificador do voo de volta
     * @param idHotel identificador do hotel
     * @param dataIda data do voo de ida (data de entrada no hotel)
     * @param dataVolta data do voo de volta (dia seguinte à data de saída do
     *                  hotel)
     * @param numQuartos número de quartos a comprar (hotel)
     * @param numPessoas número de pessoas (passagens)
     * @return true se e somente se a compra foi bem sucedida
     * @throws RemoteException caso ocorra erro no RMI
     */
    boolean comprarPacote(int idVooIda, int idVooVolta, int idHotel,
             LocalDate dataIda, LocalDate dataVolta, int numQuartos,
             int numPessoas) throws RemoteException;

    /*------------------------------------------------------------------------*/

    /** Registra um interesse de um cliente.
     * @param interesse objeto Interesse com os dados
     * @param client referência do cliente (RMI)
     * @return identificador do registro (Evento) no servidor
     * @throws RemoteException caso ocorra erro no RMI
     */
    int registrarInteresse(Interesse interesse, AgencyClient client) throws RemoteException;

    /*------------------------------------------------------------------------*/

    /** Remove um interesse de cliente a partir do identificador.
     * @param id identificador do registro de interesse
     * @param client referência do cliente (RMI)
     * @return true se e somente se o registro foi removido com sucesso
     * @throws RemoteException caso ocorra erro no RMI
     */
    boolean removerInteresse(int id, AgencyClient client) throws RemoteException;
}
