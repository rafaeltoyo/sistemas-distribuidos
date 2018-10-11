package model.eventos;

import model.cidade.Cidade;
import model.voo.Voo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/** Representa um "banco de dados" para armazenar os registros de interesse em
 * voos. Esta classe foi criada porque o HashMap interno é visualmente horrível,
 * e seria uma ótima ideia encapsulá-lo em uma classe para implementar métodos
 * para fazer consultas.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class ListaInteresseVoo {
    /** "Banco de dados" implementado como HashMaps aninhados.
     * A implementação foi feita dessa forma para facilitar a obtenção dos
     * clientes a notificar quando um evento ocorrer, pois caso isso fosse
     * implementado como um simples array, seria necessário iterar pelo array
     * inteiro para encontrar os clientes a notificar. HashMaps utilizam hash
     * para acelerar a consulta a um elemento.
     * As chaves de cada HashMap são:
     *   1: A primeira Cidade representa a origem do voo.
     *   2: A segunda Cidade representa o destino do voo.
     *   3: A LocalDate representa a data do voo.
     * Os valores do último hashmap são ArrayLists de InteresseVoo que mantêm os
     * registros propriamente ditos.
     * Sim, isso ficou feio. Por isso que está dentro de uma classe só para ele.
     */
    private HashMap<Cidade, HashMap<Cidade, HashMap<LocalDate, ArrayList<InteresseVoo>>>> interessesVoo;

    /*------------------------------------------------------------------------*/

    /** Construtor padrão para a lista de registro de interesses em voos.
     * Inicia a lista vazia.
     */
    public ListaInteresseVoo() {
        this.interessesVoo = new HashMap<>();
    }

    /*------------------------------------------------------------------------*/

    /** Adiciona na estrutura de dados um registro de interesse em um voo.
     * @param iv interesse em um voo
     * @return true se e somente se foi possível adicionar o registro
     */
    public boolean colocarInteresse(InteresseVoo iv) {
        // Heh
        return interessesVoo.computeIfAbsent(iv.getOrigem(), hm -> new HashMap<>())
                .computeIfAbsent(iv.getDestino(), hm -> new HashMap<>())
                .computeIfAbsent(iv.getData(), al -> new ArrayList<>())
                .add(iv);
    }

    /*------------------------------------------------------------------------*/

    /** Remove o registro de interesse de um cliente, se existir.
     * Os parâmetros origem, destino e data são especificados para facilitar a
     * busca do registro no banco de dados.
     * @param id identificador do registro
     * @param origem cidade de origem do voo "ex-desejado"
     * @param destino cidade de destino do voo "ex-desejado"
     * @param data data do voo "ex-desejado"
     * @return true se e somente se o registro existia e foi removido
     */
    public boolean removerInteresse(int id, Cidade origem, Cidade destino,
                                    LocalDate data) {
        HashMap<Cidade, HashMap<LocalDate, ArrayList<InteresseVoo>>> hmOrigem = interessesVoo.get(origem);
        if (hmOrigem == null) {
            return false;
        }

        HashMap<LocalDate, ArrayList<InteresseVoo>> hmDestino = hmOrigem.get(destino);
        if (hmDestino == null) {
            return false;
        }

        ArrayList<InteresseVoo> alData = hmDestino.get(data);
        if (alData == null) {
            return false;
        }

        return alData.removeIf(item -> (item.getId() == id));
    }

    /*------------------------------------------------------------------------*/

    /** Obtém a lista de todos os clientes que têm interesse em um voo que
     * atende determinados requisitos.
     * A lista é retornada em um formato read-only.
     * @param v voo
     * @return lista de clientes que têm interesse, ou null se não houver nenhum
     */
    public List<InteresseVoo> obterInteresses(Voo v) {
        HashMap<Cidade, HashMap<LocalDate, ArrayList<InteresseVoo>>> hmOrigem = interessesVoo.get(v.getOrigem());
        if (hmOrigem == null) {
            return null;
        }

        HashMap<LocalDate, ArrayList<InteresseVoo>> hmDestino = hmOrigem.get(v.getDestino());
        if (hmDestino == null) {
            return null;
        }

        ArrayList<InteresseVoo> alData = hmDestino.get(v.getData());
        if (alData == null) {
            return null;
        }

        return Collections.unmodifiableList(alData);
    }
}
