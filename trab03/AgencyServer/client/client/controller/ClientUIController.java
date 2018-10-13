package client.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import model.cidade.Cidade;
import model.voo.InfoVoo;
import model.voo.TipoPassagem;
import remote.AgencyServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.ArrayList;

public class ClientUIController {
    /** Nome de host do serviço de nomes */
    private static final String NAMING_SERVICE_HOST = "localhost";

    /** Porta do serviço de nomes */
    private static final int NAMING_SERVICE_PORT = 11037;

    /** Referência ao servidor (RMI) */
    private AgencyServer serverRef;

    /*------------------------------------------------------------------------*/

    private ObservableList<InfoVoo> olInfoVooIda = FXCollections.observableArrayList();

    private ObservableList<InfoVoo> olInfoVooVolta = FXCollections.observableArrayList();

    @FXML
    private RadioButton radioSomenteIdaVoo;

    @FXML
    private RadioButton radioIdaEVoltaVoo;

    @FXML
    private ToggleGroup tipoVoo;

    @FXML
    private ChoiceBox<Cidade> choiceOrigemVoo;

    @FXML
    private ChoiceBox<Cidade> choiceDestinoVoo;

    @FXML
    private DatePicker dateVooIda;

    @FXML
    private DatePicker dateVooVolta;

    @FXML
    private Spinner<Integer> spinnerNumPessoasVoo;

    @FXML
    private Button buttonConsultarVoo;

    @FXML
    private TableView<InfoVoo> tableVooIda;

    @FXML
    private TableView<InfoVoo> tableVooVolta;

    @FXML
    private TableColumn<InfoVoo, Number> columnIdVooIda;

    @FXML
    private TableColumn<InfoVoo, String> columnOrigemVooIda;

    @FXML
    private TableColumn<InfoVoo, String> columnDestinoVooIda;

    @FXML
    private TableColumn<InfoVoo, String> columnDataVooIda;

    @FXML
    private TableColumn<InfoVoo, Number> columnPoltronasVooIda;

    @FXML
    private TableColumn<InfoVoo, Number> columnIdVooVolta;

    @FXML
    private TableColumn<InfoVoo, String> columnOrigemVooVolta;

    @FXML
    private TableColumn<InfoVoo, String> columnDestinoVooVolta;

    @FXML
    private TableColumn<InfoVoo, String> columnDataVooVolta;

    @FXML
    private TableColumn<InfoVoo, Number> columnPoltronasVooVolta;

    /*------------------------------------------------------------------------*/

    @FXML
    public void initialize() {
        // Inicializa as ChoiceBox com as cidades do enum Cidade
        choiceOrigemVoo.getItems().setAll(Cidade.values());
        choiceDestinoVoo.getItems().setAll(Cidade.values());

        // Inicializa o seletor de número de pessoas
        spinnerNumPessoasVoo.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, 1));

        // Seleciona o radio "Somente Ida" por padrão
        tipoVoo.selectToggle(radioSomenteIdaVoo);

        // Configura as colunas
        columnIdVooIda.setCellValueFactory(item -> new SimpleIntegerProperty(item.getValue().getId()));
        columnOrigemVooIda.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getOrigem().toString()));
        columnDestinoVooIda.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getDestino().toString()));
        columnDataVooIda.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getData().toString()));
        columnPoltronasVooIda.setCellValueFactory(item -> new SimpleIntegerProperty(item.getValue().poltronasDisp));
        columnIdVooVolta.setCellValueFactory(item -> new SimpleIntegerProperty(item.getValue().getId()));
        columnOrigemVooVolta.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getOrigem().toString()));
        columnDestinoVooVolta.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getDestino().toString()));
        columnDataVooVolta.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getData().toString()));
        columnPoltronasVooVolta.setCellValueFactory(item -> new SimpleIntegerProperty(item.getValue().poltronasDisp));

        // Configura o botão
        buttonConsultarVoo.setOnAction(this::consultarVoos);

        try {
            connectToServer();
        }
        catch (RemoteException | NotBoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Falha na comunicação com o servidor.");
            alert.showAndWait();

            // Aqui, é preferível usar System.exit a usar Platform.exit.
            System.exit(-1);
        }
    }

    /*------------------------------------------------------------------------*/

    private void connectToServer() throws RemoteException, NotBoundException {
        Registry namingServiceRef = LocateRegistry.getRegistry(
                NAMING_SERVICE_HOST, NAMING_SERVICE_PORT);
        serverRef = (AgencyServer) namingServiceRef.lookup(
                "server");
    }

    /*------------------------------------------------------------------------*/

    private void consultarVoos(ActionEvent event) {
        Cidade origem = choiceOrigemVoo.getValue();
        Cidade destino = choiceDestinoVoo.getValue();
        LocalDate dataIda = dateVooIda.getValue();
        LocalDate dataVolta = dateVooVolta.getValue();
        int numPessoas = spinnerNumPessoasVoo.getValue();
        TipoPassagem tipoPassagem = (tipoVoo.getSelectedToggle() == radioSomenteIdaVoo)? TipoPassagem.SOMENTE_IDA : TipoPassagem.IDA_E_VOLTA;

        ArrayList<InfoVoo> voos = null;
        try {
            voos = serverRef.consultarPassagens(tipoPassagem, origem, destino, dataIda, dataVolta, numPessoas);
        }
        catch (RemoteException e) {
            // TODO: Mostrar mensagem de erro
            e.printStackTrace();
        }

        olInfoVooIda.clear();
        olInfoVooVolta.clear();
        if (voos != null) {
            for (InfoVoo v : voos) {
                if (v.getOrigem() == origem) {
                    olInfoVooIda.add(v);
                }
                else {
                    olInfoVooVolta.add(v);
                }
            }
        }

        tableVooIda.setItems(olInfoVooIda);
        tableVooVolta.setItems(olInfoVooVolta);
    }
}
