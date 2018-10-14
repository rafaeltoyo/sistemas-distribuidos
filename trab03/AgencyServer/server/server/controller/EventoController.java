package server.controller;

import server.model.cidade.Cidade;
import server.model.evento.Evento;
import server.model.evento.Interesse;
import server.model.hotel.Hotel;
import server.model.voo.Voo;
import remote.AgencyClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class EventoController {

    private static EventoController ourInstance = new EventoController();

    private HashMap<Interesse.TipoInteresse, HashMap<String, HashMap<String, ArrayList<Evento>>>> arvore;
    private HashMap<Integer, Evento> lista;

    private EventoController() {
        arvore = new HashMap<>();
        lista = new HashMap<>();
    }

    public static EventoController getInstance() {
        return ourInstance;
    }

    public Evento registrar(Interesse interesse, AgencyClient client) {
        Evento ev = new Evento(interesse, client);
        String origem = interesse.getTipo() == Interesse.TipoInteresse.HOSPEDAGEM ? "Default" : interesse.getOrigem().toString();

        if (lista.put(interesse.getId(), ev) == ev &&
                arvore.computeIfAbsent(interesse.getTipo(), hm -> new HashMap<>())
                        .computeIfAbsent(origem, hm -> new HashMap<>())
                        .computeIfAbsent(interesse.getDestino().toString(), al -> new ArrayList<>())
                        .add(ev)) {
            return ev;
        }
        return null;
    }

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

    public List<Evento> consultar(Voo voo) {
        return consultar(Interesse.TipoInteresse.VOO, voo.getOrigem(), voo.getDestino());
    }

    public List<Evento> consultar(Hotel hotel) {
        return consultar(Interesse.TipoInteresse.VOO, null, hotel.getLocal());
    }

    public Evento consultar(int id) {
        return lista.get(id);
    }

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
