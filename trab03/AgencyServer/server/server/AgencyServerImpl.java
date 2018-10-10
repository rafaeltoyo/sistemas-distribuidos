package server;

import model.cidade.Cidade;
import model.hotel.Hospedagem;
import model.hotel.Hotel;
import model.hotel.InfoHospedagem;
import model.hotel.InfoHotel;
import model.pacote.ConjuntoPacote;
import model.saldo.Reserva;
import model.voo.InfoVoo;
import model.voo.TipoPassagem;
import model.voo.Voo;
import remote.AgencyServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/** Representa o servidor da agência.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class AgencyServerImpl extends UnicastRemoteObject
        implements AgencyServer {
    /** Lista de voos disponíveis */
    private ArrayList<Voo> voos = new ArrayList<>();

    /** Lista de hotéis cadastrados */
    private ArrayList<Hotel> hoteis = new ArrayList<>();

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

    /** Adiciona um hotel à lista de hotéis do servidor
     * @param hotel hotel já instanciado e inicializado
     */
    public void adicionarHotel(Hotel hotel) {
        hoteis.add(hotel);
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
    @Override
    public HashMap<InfoHotel, ArrayList<InfoHospedagem>> consultarHospedagens(
            Cidade local, LocalDate dataIni, LocalDate dataFim,
            int numQuartos, int numPessoas)
            throws RemoteException {
        // FIXME:
        // Será que a informação de cada dia é realmente relevante ao cliente?
        // Se não for, pode-se simplesmente retornar um ArrayList<InfoHotel>.
        // O cliente compra hospedagem utilizando apenas o ID do hotel e as
        // datas de chegada e saída.

        HashMap<InfoHotel, ArrayList<InfoHospedagem>> result = new HashMap<>();

        for (Hotel h : hoteis) {
            // Pula hotéis em outras cidades
            if (!local.equals(h.getLocal())) {
                continue;
            }

            LocalDate data = dataIni.plusDays(0);

            ArrayList<InfoHospedagem> hospedagens = new ArrayList<>();

            // Considera-se que o cliente sai na data de volta.
            // Portanto, não são incluídas hospedagens para o dia de volta.
            while (data.isBefore(dataFim)) {
                Hospedagem hosp = h.getHospedagemData(data);
                if (hosp == null) {
                    // Deu ruim, esse hotel não está oferecendo hospedagem em
                    // um dos dias do período
                    hospedagens.clear();
                    break;
                }

                if (hosp.getQuartosDisp() < numPessoas) {
                    // Deu ruim, esse hotel não pode receber o cliente em todos
                    // os dias do período
                    hospedagens.clear();
                    break;
                }

                // FIXME: vamos só ignorar o número de pessoas?

                hospedagens.add(hosp.getInfoHospedagem());

                data = data.plusDays(1);
            }

            // Apenas envia o hotel se tiver vagas em todos os dias do período
            if (!hospedagens.isEmpty()) {
                result.put(h.getInfoHotel(), hospedagens);
            }
        }

        return result;
    }

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
    @Override
    public boolean comprarHospedagem(int idHotel, LocalDate dataIni,
            LocalDate dataFim, int numQuartos) throws RemoteException {
        // Busca o hotel
        Hotel hotel = null;
        for (Hotel h : hoteis) {
            if (h.getId() == idHotel) {
                hotel = h;
                break;
            }
        }

        if (hotel == null) {
            // Hotel não existe
            return false;
        }

        // Faz a reserva, se possível, e retorna true se bem sucedido
        return hotel.reservar(dataIni, dataFim, numQuartos);
    }

    /*------------------------------------------------------------------------*/

    /** Faz uma consulta de voos e hotéis para os dados fornecidos, e retorna os
     * resultados em um objeto.
     * No servidor, não existem pacotes explícitos, apenas voos e hotéis. Por
     * isso, essa consulta é implementada com chamadas às consultas de passagens
     * e de hospedagens.
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
    public ConjuntoPacote consultarPacotes(Cidade origem, Cidade destino,
            LocalDate dataIda, LocalDate dataVolta, int numQuartos,
            int numPessoas) throws RemoteException {
        // Obtém todos os voos
        ArrayList<InfoVoo> voos = consultarPassagens(TipoPassagem.IDA_E_VOLTA,
                origem, destino, dataIda, dataVolta, numPessoas);

        // Obtém as informações de hotéis
        HashMap<InfoHotel, ArrayList<InfoHospedagem>> hosps = consultarHospedagens(
                destino, dataIda, dataVolta, numQuartos, numPessoas);

        ConjuntoPacote conjuntoPacote = new ConjuntoPacote();
        if (!voos.isEmpty() && !hosps.isEmpty()) {
            for (InfoVoo v : voos) {
                if (v.getOrigem() == origem) {
                    conjuntoPacote.adicionarVooIda(v);
                } else {
                    conjuntoPacote.adicionarVooVolta(v);
                }
            }
            for (HashMap.Entry<InfoHotel, ArrayList<InfoHospedagem>> entry : hosps.entrySet()) {
                conjuntoPacote.adicionarHospedagem(entry.getKey(), entry.getValue());
            }
        }

        return conjuntoPacote;
    }
}
