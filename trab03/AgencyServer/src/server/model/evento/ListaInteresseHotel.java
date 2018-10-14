package server.model.evento;

import server.model.cidade.Cidade;
import server.model.hotel.Hospedagem;
import server.model.hotel.Hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/** Representa um "banco de dados" para armazenar os registros de interesse em
 * hospedagem.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class ListaInteresseHotel {
    /** "Banco de dados" implementado como um HashMap.
     * A implementação foi feita dessa forma para facilitar a obtenção dos
     * clientes a notificar quando um evento ocorrer.
     * Entretanto, nesse caso não foi possível utilizar as datas como chaves de
     * um HashMap interno (como é na ListaInteresseVoo), porque sua
     * interpretação é a de um intervalo de tempo.
     * A chave do HashMap é a cidade do hotel, e os valores são ArrayLists de
     * objetos InteresseHotel, que mantêm os registros propriamente ditos.
     */
    private HashMap<Cidade, ArrayList<InteresseHotel>> interessesHotel;

    /*------------------------------------------------------------------------*/

    /** Construtor padrão para a lista de registro de interesses em hotéis.
     * Inicia a lista vazia.
     */
    public ListaInteresseHotel() {
        this.interessesHotel = new HashMap<>();
    }

    /*------------------------------------------------------------------------*/

    /** Adiciona na estrutura de dados um registro de interesse em um hotel.
     * @param ih interesse em um hotel
     * @return true se e somente se foi possível adicionar o registro
     */
    public boolean colocarInteresse(InteresseHotel ih) {
        return interessesHotel.computeIfAbsent(ih.getDestino(), al -> new ArrayList<>())
                .add(ih);
    }

    /*------------------------------------------------------------------------*/

    /** Remove o registro de interesse de um cliente, se existir.
     * O parâmetro destino é especificado para facilitar a busca do registro no
     * banco de dados.
     * @param id identificador do registro
     * @param destino cidade do hotel "ex-desejado"
     * @return true se e somente se o registro existia e foi removido
     */
    public boolean removerInteresse(int id, Cidade destino) {
        ArrayList<InteresseHotel> alDestino = interessesHotel.get(destino);
        if (alDestino == null) {
            return false;
        }

        return alDestino.removeIf(item -> (item.getId() == id));
    }

    /*------------------------------------------------------------------------*/

    /** Obtém a lista de todos os registros de interesse que são atendidos por
     * um hotel.
     * @param h hotel
     * @return lista read-only de todos os registros de interesse atendidos
     */
    public List<InteresseHotel> obterInteresses(Hotel h) {
        // Array que será retornado
        ArrayList<InteresseHotel> ret = new ArrayList<>();

        ArrayList<InteresseHotel> alDestino = interessesHotel.get(h.getLocal());
        if (alDestino != null) {
            // Infelizmente, como um hotel pode adicionar hospedagens em partes
            // (ex: adiciona hospedagem em um dia, e depois adiciona em outro),
            // a melhor maneira para verificar se um evento aconteceu é iterar
            // por todos os registros de interesse e verificar se o hotel atende
            // algum (ou alguns) deles.
            for (InteresseHotel ih : alDestino) {
                LocalDate data = ih.getDataIni();
                LocalDate dataFim = ih.getDataFim();

                // Itera por todas as datas no registro de interesse do cliente,
                // exceto data de fim (seguindo o padrão de todas as outras
                // funções relacionadas a hospedagem: o último dia é o dia em
                // que o cliente sai do hotel, e não é uma diária).
                // Se o hotel não puder receber ninguém em alguma das datas,
                // quebra o loop prematuramente e não adiciona o registro de
                // interesse do usuário na lista de retorno.
                boolean broken = false;
                while (data.isBefore(dataFim)) {
                    Hospedagem hosp = h.getHospedagemData(data);
                    if (hosp == null || hosp.getQuartosDisp() <= 0) {
                        broken = true;
                        break;
                    }

                    data = data.plusDays(1);
                }

                // Adiciona o registro apenas se o loop não foi terminado pelo
                // break.
                if (!broken) {
                    ret.add(ih);
                }
            }
        }

        return Collections.unmodifiableList(ret);
    }
}
