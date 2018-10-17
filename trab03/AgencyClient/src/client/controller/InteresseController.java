package client.controller;

import shared.model.evento.Interesse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Esta classe é um singleton que controla os interesses do cliente.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class InteresseController {
    /** Instância do singleton */
    private static InteresseController ourInstance = new InteresseController();

    /** Lista de interesses */
    private ArrayList<Interesse> interesses = new ArrayList<>();

    /*------------------------------------------------------------------------*/

    /** Retorna a lista de interesses.
     * @return lista de interesses
     */
    public List<Interesse> getInteresses() {
        return Collections.unmodifiableList(interesses);
    }

    /*------------------------------------------------------------------------*/

    /** Construtor privado do singleton (vazio) */
    private InteresseController() {
    }

    /** Retorna a instância do singleton.
     * @return instância
     */
    public static InteresseController getInstance() {
        return ourInstance;
    }

    /*------------------------------------------------------------------------*/

    /** Cria uma nova entrada na lista de interesses.
     * @param interesse interesse
     */
    public void create(Interesse interesse) {
        this.interesses.add(interesse.getId(), interesse);
    }

    /** Remove uma entrada da lista de interesses pelo identificador.
     * @param idInteresse identificador do interesse a remover
     */
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
