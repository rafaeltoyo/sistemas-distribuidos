package client.controller;

import server.model.evento.Interesse;
import server.model.evento.InteresseHotel;
import server.model.evento.InteresseVoo;

import java.util.ArrayList;

public class InteresseController {

    private static InteresseController ourInstance = new InteresseController();

    private ArrayList<Interesse> interesses = new ArrayList<>();

    private InteresseController() {
    }

    public static InteresseController getInstance() {
        return ourInstance;
    }

    public ArrayList<Interesse> getInteresses() {
        return interesses;
    }

    public void create(Interesse interesse) {
        this.interesses.add(interesse.getId(), interesse);
    }

    public void remove(int idInteresse) {
        this.interesses.remove(idInteresse);
    }

    public void remove(InteresseVoo interesseVoo) {
        remove(interesseVoo.getId());
    }

    public void remove(InteresseHotel interesseHotel) {
        remove(interesseHotel.getId());
    }
}
