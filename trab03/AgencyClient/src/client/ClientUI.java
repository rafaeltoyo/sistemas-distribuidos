package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Esta classe herda de Application, do JavaFX, e é o ponto inicial de execução
 * do programa com interface gráfica.
 * @author Rafael Hideo Toyomoto
 * @author Victor Barpp Gomes
 */
public class ClientUI extends Application {
    /** Carrega a interface FXML e inicializa uma janela.
     * @param primaryStage stage primário
     * @throws Exception caso ocorra erro no JavaFX
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/ClientUI.fxml"));
        primaryStage.setTitle("Cliente Agência");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    /** Método chamado quando a janela é fechada. Finaliza todas as threads,
     * incluindo o Java RMI.
     * @throws Exception caso ocorra algum erro no método stop do JavaFX
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }

    /** Ponto de entrada da aplicação.
     * @param args argumentos de linha de comando
     */
    public static void main(String[] args) {
        launch(args);
    }
}
