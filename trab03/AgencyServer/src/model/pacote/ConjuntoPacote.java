package model.pacote;

import model.hotel.InfoHospedagem;
import model.hotel.InfoHotel;
import model.voo.InfoVoo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Representa:
 *   um conjunto de voos de ida;
 *   um conjunto de voos de volta;
 *   um conjunto de hotéis que podem receber o cliente entre o dia de ida e o de
 *   volta.
 * Objetos ConjuntoPacote não são armazenados no servidor. Apenas são criados
 * como resposta a uma consulta por pacotes.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class ConjuntoPacote implements Serializable {
    /** Conjunto de voos de ida */
    private ArrayList<InfoVoo> voosIda;

    /** Conjunto de voos de volta */
    private ArrayList<InfoVoo> voosVolta;

    /** Conjunto de hotéis que podem receber o cliente no período informado */
    private HashMap<InfoHotel, ArrayList<InfoHospedagem>> hospedagens;

    /*------------------------------------------------------------------------*/

    /** Retorna uma referência read-only à lista de voos de ida.
     * @return lista de voos de ida
     */
    public List<InfoVoo> getVoosIda() {
        return Collections.unmodifiableList(voosIda);
    }

    /** Retorna uma referência read-only à lista de voos de volta.
     * @return lista de voos de volta
     */
    public List<InfoVoo> getVoosVolta() {
        return Collections.unmodifiableList(voosVolta);
    }

    /** Retorna uma referência read-only ao HashMap de hotéis e hospedagens.
     * @return HashMap de hotéis e hospedagens.
     */
    public Map<InfoHotel, ArrayList<InfoHospedagem>> getHospedagens() {
        return Collections.unmodifiableMap(hospedagens);
    }

    /*------------------------------------------------------------------------*/

    /** Construtor padrão de um ConjuntoPacote, que instancia as listas
     * internas.
     */
    public ConjuntoPacote() {
        hospedagens = new HashMap<>();
        voosIda = new ArrayList<>();
        voosVolta = new ArrayList<>();
    }

    /*------------------------------------------------------------------------*/

    /** Adiciona as informações de um voo de ida à lista de voos de ida.
     * @param v voo de ida a adicionar à lista
     */
    public void adicionarVooIda(InfoVoo v) {
        voosIda.add(v);
    }

    /*------------------------------------------------------------------------*/

    /** Adiciona as informações de um voo de volta à lista de voos de volta.
     * @param v voo de volta a adicionar à lista
     */
    public void adicionarVooVolta(InfoVoo v) {
        voosVolta.add(v);
    }

    /*------------------------------------------------------------------------*/

    /** Adiciona um hotel e seus dias de hospedagem ao HashMap.
     * @param h informações do hotel
     * @param hp informações de hospedagem
     */
    public void adicionarHospedagem(InfoHotel h, ArrayList<InfoHospedagem> hp) {
        hospedagens.put(h, hp);
    }

    /*------------------------------------------------------------------------*/
}
