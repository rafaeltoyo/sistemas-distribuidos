package server.controller;

import server.model.evento.Evento;
import server.model.hotel.Hotel;
import server.model.voo.Voo;
import shared.model.cidade.Cidade;
import shared.model.evento.Interesse;
import shared.remote.AgencyClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/** Esta classe é um singleton responsável pelo controle de eventos.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class EventoController {
    /** Instância única do controlador */
    private static EventoController ourInstance = new EventoController();

    /** Eventos armazenados em uma estrutura em árvore */
    private HashMap<Interesse.TipoInteresse, HashMap<String, HashMap<String, ArrayList<Evento>>>> arvore;

    /** Eventos armazenados em uma lista pelo seu ID */
    private HashMap<Integer, Evento> lista;

    /*------------------------------------------------------------------------*/

    /** Construtor privado do singleton */
    private EventoController() {
        arvore = new HashMap<>();
        lista = new HashMap<>();
    }

    /** Retorna a instância do singleton.
     * @return instância do controlador
     */
    public static EventoController getInstance() {
        return ourInstance;
    }

    /*------------------------------------------------------------------------*/

    /** Registra um novo evento (registro de interesse) no servidor.
     * @param interesse interesse do cliente
     * @param client referência do cliente (RMI)
     * @return objeto Evento criado
     */
    public Evento registrar(Interesse interesse, AgencyClient client) {
        Evento ev = new Evento(interesse, client);
        String origem = interesse.getTipo() == Interesse.TipoInteresse.HOSPEDAGEM ? "Default" : interesse.getOrigem().toString();

        if (arvore.computeIfAbsent(interesse.getTipo(), hm -> new HashMap<>())
                .computeIfAbsent(origem, hm -> new HashMap<>())
                .computeIfAbsent(interesse.getDestino().toString(), al -> new ArrayList<>())
                .add(ev)) {
            lista.put(interesse.getId(), ev);
            return ev;
        }
        return null;
    }

    /*------------------------------------------------------------------------*/

    /** Consulta todos os registros de interesse em eventos a partir de tipo,
     * origem e destino do evento.
     * @param tipo tipo do evento (Voo, Hospedagem ou Pacote)
     * @param origem cidade de origem do cliente
     * @param destino cidade destino de interesse do cliente
     * @return lista com os registros que possuem as características solicitadas
     */
    public List<Evento> consultar(Interesse.TipoInteresse tipo, Cidade origem, Cidade destino) {
        HashMap<String, HashMap<String, ArrayList<Evento>>> hashTipo = arvore.get(tipo);
        if (hashTipo == null) {
            return new ArrayList<>();
        }

        HashMap<String, ArrayList<Evento>> hashOrigem = hashTipo.get(origem != null ? origem.toString() : "Default");
        if (hashOrigem == null) {
            return new ArrayList<>();
        }

        ArrayList<Evento> result = hashOrigem.get(destino.toString());
        if (result == null) {
            return new ArrayList<>();
        }

        return Collections.unmodifiableList(result);
    }

    /*------------------------------------------------------------------------*/

    /** Consulta todos os registros de interesse em eventos a partir de um Voo.
     * @param voo voo para fornecer os dados de consulta
     * @return lista com os eventos que possuem as características solicitadas
     */
    public List<Evento> consultar(Voo voo) {
        return consultar(Interesse.TipoInteresse.VOO, voo.getOrigem(), voo.getDestino());
    }

    /*------------------------------------------------------------------------*/

    /** Consulta todos os registros de interesse em eventos a partir de um
     * Hotel.
     * @param hotel Hotel para fornecer os dados de consulta
     * @return Lista com os eventos que possuem as características solicitadas
     */
    public List<Evento> consultar(Hotel hotel) {
        return consultar(Interesse.TipoInteresse.HOSPEDAGEM, null, hotel.getLocal());
    }

    /*------------------------------------------------------------------------*/

    /** Remove um Evento a partir de seu ID.
     * @param id ID do evento
     * @return true se e somente se a exclusão foi realizada com sucesso
     */
    public boolean remover(int id) {
        Evento ev = lista.get(id);
        if (ev == null) {
            return false;
        }
        Interesse intr = ev.getInteresse();
        return arvore.get(intr.getTipo())
                .get(intr.getTipo() == Interesse.TipoInteresse.HOSPEDAGEM ? "Default" : intr.getOrigem().toString())
                .get(intr.getDestino().toString())
                .remove(ev)
                &&
                lista.remove(id) == ev;
    }
}
