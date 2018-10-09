package client.controller;

import javafx.stage.Stage;

/** Controlador das telas da aplicação
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class ViewController {

    /** Referência da aplicação principal */
    private Stage window;

    /*------------------------------------------------------------------------*/

    /** Instância única do controlador */
    private static ViewController ourInstance = new ViewController();

    /** Resgatar a instância única do controlador */
    public static ViewController getInstance() {
        return ourInstance;
    }

    /*------------------------------------------------------------------------*/

    /** Construtor padrão desse controlador */
    private ViewController() {
    }

    /*------------------------------------------------------------------------*/

    /** Iniciar esse controlador
     * @param stage Stage base da aplicação
     */
    public void init(Stage stage) {
        window = stage;
    }

    /*------------------------------------------------------------------------*/
}
