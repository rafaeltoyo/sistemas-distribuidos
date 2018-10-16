package server.controller;

import model.cidade.Cidade;
import model.evento.Evento;
import model.evento.Interesse;
import model.hotel.Hotel;
import model.voo.Voo;
import remote.AgencyClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Controlador de Eventos
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

    /** Construtor privado do singleton */
    private EventoController() {
        arvore = new HashMap<>();
        lista = new HashMap<>();
    }

    /**
     * Retorna a instância do singleton
     * @return Instância do controlador
     */
    public static EventoController getInstance() {
        return ourInstance;
    }

    /**
     * Registra um novo evento (Registro de interesse) no servidor
     * @param interesse Interesse do cliente
     * @param client Referência do cliente
     * @return Referência para o Evento criado
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

    /**
     * Consultar todos eventos a partir de tipo, origem e destino do evento
     * @param tipo Tipo do evento (Voo, Hospedagem ou Pacote)
     * @param origem Cidade de origem do cliente
     * @param destino Cidade destino de interesse do cliente
     * @return Lista com os eventos que possuem as características solicitadas
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

    /**
     * Consultar todos eventos a partir de um Voo
     * @param voo Voo para fornecer os dados de consulta
     * @return Lista com os eventos que possuem as características solicitadas
     */
    public List<Evento> consultar(Voo voo) {
        return consultar(Interesse.TipoInteresse.VOO, voo.getOrigem(), voo.getDestino());
    }

    /**
     * Consultar todos eventos a partir de um Hotel
     * @param hotel Hotel para fornecer os dados de consulta
     * @return Lista com os eventos que possuem as características solicitadas
     */
    public List<Evento> consultar(Hotel hotel) {
        return consultar(Interesse.TipoInteresse.HOSPEDAGEM, null, hotel.getLocal());
    }

    /**
     * Remover um Evento a partir de seu ID
     * @param id ID do evento
     * @return Se a exclusão foi realizada com sucesso
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
