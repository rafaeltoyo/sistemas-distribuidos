package br.com.tbdc.controller;

import br.com.tbdc.model.Hospedagem;
import br.com.tbdc.model.Hotel;
import br.com.tbdc.model.cidade.Cidade;
import br.com.tbdc.model.hotel.InfoHotelRet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Esta classe é um singleton que controla todos os hotéis do servidor por meio
 * de métodos CRUD.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class ControladorHotel {
    /** Instância do singleton */
    private static final ControladorHotel instance = new ControladorHotel();

    /** Lista de hotéis em memória */
    private List<Hotel> hoteis;

    /*------------------------------------------------------------------------*/

    /** Construtor privado sem argumentos que apenas inicializa a lista de voos.
     */
    private ControladorHotel() {
        hoteis = new ArrayList<>();
    }

    /** Retorna a instância do singleton.
     * @return instância do singleton
     */
    public static synchronized ControladorHotel getInstance() {
        return instance;
    }

    /*------------------------------------------------------------------------*/

    /** Adiciona um hotel à lista de hotéis do servidor
     * @param hotel hotel já instanciado e inicializado
     */
    public void adicionarHotel(Hotel hotel) {
        hoteis.add(hotel);
    }

    /*------------------------------------------------------------------------*/

    /** Permite adicionar novas hospedagens (datas) a um hotel já existente, a
     * partir de seu identificador.
     * @param idHotel identificador do hotel
     * @param dataIni data de início do período
     * @param dataFim data de fim do período (também é incluída no intervalo)
     */
    public void adicionarHospedagem(int idHotel, LocalDate dataIni, LocalDate dataFim) {
        // Obtém o hotel
        Hotel h = hoteis.stream().filter(item -> (item.getId() == idHotel)).findFirst().orElse(null);

        if (h != null) {
            h.adicionarHospedagem(dataIni, dataFim);
        }
    }

    /*------------------------------------------------------------------------*/

    /** Retorna um vetor de informações de hotéis que atendem aos parâmetros
     * fornecidos.
     * @param local cidade do hotel
     * @param dataIni data de chegada (primeira diária)
     * @param dataFim data de saída (não é inclusa no resultado)
     * @param numQuartos número de quartos desejados
     * @param numPessoas número de pessoas (total, não por quarto)
     * @return vetor com informações de hotel e hospedagem
     */
    public ArrayList<InfoHotelRet> consultarHospedagens(Cidade local, LocalDate dataIni, LocalDate dataFim, int numQuartos, int numPessoas) {
        // Cria um vetor de InfoHotelRet para enviar ao cliente
        ArrayList<InfoHotelRet> result = new ArrayList<>();

        for (Hotel h : hoteis) {
            // Pula hotéis em outras cidades
            if (!local.equals(h.getLocal())) {
                continue;
            }

            // Inicia a contagem de número de quartos disponíveis no número
            // máximo de quartos do hotel
            int quartosDisp = h.getInfoHotel().getNumQuartos();

            // Flag usada depois do loop para adicionar um hotel ou não à lista
            // de retorno
            boolean podeReceber = true;

            // Considera-se que o cliente sai na data de volta.
            // Portanto, não são incluídas hospedagens para o dia de volta.
            LocalDate data = dataIni.plusDays(0);
            while (data.isBefore(dataFim)) {
                Hospedagem hosp = h.getHospedagemData(data);
                if (hosp == null) {
                    // Deu ruim, esse hotel não está oferecendo hospedagem em
                    // um dos dias do período
                    podeReceber = false;
                    break;
                }

                int hospQuartosDisp = hosp.getQuartosDisp();
                if (hospQuartosDisp < numPessoas) {
                    // Deu ruim, esse hotel não pode receber o cliente em todos
                    // os dias do período
                    podeReceber = false;
                    break;
                }

                if (hospQuartosDisp < quartosDisp) {
                    // Há um dia mais lotado, atualiza o número
                    quartosDisp = hospQuartosDisp;
                }

                // FIXME: vamos só ignorar o número de pessoas?

                data = data.plusDays(1);
            }

            // Apenas envia o hotel se tiver vagas em todos os dias do período
            if (podeReceber) {
                InfoHotelRet ihr = new InfoHotelRet(h.getInfoHotel(),
                        quartosDisp, dataIni, dataFim, numQuartos);
                result.add(ihr);
            }
        }

        return result;
    }

    /*------------------------------------------------------------------------*/

    /** Tenta comprar hospedagem em um hotel para todas as noites entre as datas informadas.
     *
     * @param idHotel identificador do hotel
     * @param dataIni data de entrada
     * @param dataFim data de saída (não é incluída na reserva, ou seja, é feita
     *                reserva somente até o dia anterior à data de saída)
     * @param numQuartos número de quartos desejados
     * @return true se e somente se a compra for bem sucedida */
    public boolean comprarHospedagem(int idHotel, LocalDate dataIni, LocalDate dataFim, int numQuartos) {
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

}
