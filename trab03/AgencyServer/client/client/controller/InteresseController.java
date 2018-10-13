package client.controller;

import client.model.Interesse;
import model.eventos.InteresseHotel;
import model.eventos.InteresseVoo;

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
        interesse.setStatus(Interesse.StatusInteresse.AGUARDANDO);
        this.interesses.add(interesse.getId(), interesse);
    }

    public void accept(int idInteresse) {
        this.interesses.get(idInteresse).setStatus(Interesse.StatusInteresse.DISPONIVEL);
    }

    public void accept(InteresseVoo interesseVoo) {
        accept(interesseVoo.getId());
    }

    public void accept(InteresseHotel interesseHotel) {
        accept(interesseHotel.getId());
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

    public void clear() {
        ArrayList<Interesse> temp = new ArrayList<>();
        for (Interesse interesse : this.interesses) {
            if (interesse.getStatus() == Interesse.StatusInteresse.DISPONIVEL) {
                temp.add(interesse);
            }
        }
        this.interesses.removeAll(temp);
    }
}
