package client.controller;

import shared.model.evento.Interesse;

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
        Interesse interesse = null;
        for (Interesse it : interesses) {
            if (it.getId() == idInteresse) {
                interesse = it;
            }
        }
        this.interesses.remove(interesse);
    }
}
