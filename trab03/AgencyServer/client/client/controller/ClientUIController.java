package client.controller;

import client.model.Interesse;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.cidade.Cidade;
import model.hotel.InfoHospedagem;
import model.hotel.InfoHotel;
import model.voo.InfoVoo;
import model.voo.TipoPassagem;
import remote.AgencyServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

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

    private ObservableList<InfoHotel> olInfoHotel = FXCollections.observableArrayList();

    private ObservableList<InfoVoo> olInfoVooIdaPac = FXCollections.observableArrayList();

    private ObservableList<InfoVoo> olInfoVooVoltaPac = FXCollections.observableArrayList();

    private ObservableList<InfoHotel> olInfoHotelPac = FXCollections.observableArrayList();

    private ObservableList<Interesse> olInteresse = FXCollections.observableArrayList();

    /*------------------------------------------------------------------------*/

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
    private Button buttonComprarVoo;

    @FXML
    private Button buttonInteresseVoo;

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
    private ChoiceBox<Cidade> choiceCidadeHosp;

    @FXML
    private DatePicker dateChegadaHosp;

    @FXML
    private DatePicker dateSaidaHosp;

    @FXML
    private Spinner<Integer> spinnerNumQuartosHosp;

    @FXML
    private Spinner<Integer> spinnerNumPessoasHosp;

    @FXML
    private Button buttonConsultarHosp;

    @FXML
    private Button buttonComprarHosp;

    @FXML
    private Button buttonInteresseHosp;

    @FXML
    private TableView<InfoHotel> tableHospedagem;

    @FXML
    private TableColumn<InfoHotel, String> columnNomeHosp;

    @FXML
    private TableColumn<InfoHotel, String> columnCidadeHosp;

    @FXML
    private TableColumn<InfoHotel, String> columnChegadaHosp;

    @FXML
    private TableColumn<InfoHotel, Number> columnDiariasHosp;

    @FXML
    private TableColumn<InfoHotel, Number> columnQuartosHosp;

    /*------------------------------------------------------------------------*/

    @FXML
    private ChoiceBox<Cidade> choiceOrigemPacote;

    @FXML
    private ChoiceBox<Cidade> choiceDestinoPacote;

    @FXML
    private DatePicker datePacoteIda;

    @FXML
    private DatePicker datePacoteVolta;

    @FXML
    private Spinner<Integer> spinnerNumQuartosPacote;

    @FXML
    private Spinner<Integer> spinnerNumPessoasPacote;

    @FXML
    private Button buttonConsultarPacote;

    @FXML
    private Button buttonComprarPacote;

    @FXML
    private Button buttonInteressePacote;

    @FXML
    private TableView<InfoVoo> tableVooIdaPac;

    @FXML
    private TableView<InfoVoo> tableVooVoltaPac;

    @FXML
    private TableView<InfoHotel> tableHospedagemPac;

    @FXML
    private TableColumn<InfoVoo, Number> columnIdVooIdaPac;

    @FXML
    private TableColumn<InfoVoo, String> columnOrigemVooIdaPac;

    @FXML
    private TableColumn<InfoVoo, String> columnDestinoVooIdaPac;

    @FXML
    private TableColumn<InfoVoo, String> columnDataVooIdaPac;

    @FXML
    private TableColumn<InfoVoo, Number> columnPoltronasVooIdaPac;

    @FXML
    private TableColumn<InfoVoo, Number> columnIdVooVoltaPac;

    @FXML
    private TableColumn<InfoVoo, String> columnOrigemVooVoltaPac;

    @FXML
    private TableColumn<InfoVoo, String> columnDestinoVooVoltaPac;

    @FXML
    private TableColumn<InfoVoo, String> columnDataVooVoltaPac;

    @FXML
    private TableColumn<InfoVoo, Number> columnPoltronasVooVoltaPac;

    @FXML
    private TableColumn<InfoHotel, String> columnNomeHospPac;

    @FXML
    private TableColumn<InfoHotel, String> columnCidadeHospPac;

    @FXML
    private TableColumn<InfoHotel, String> columnChegadaHospPac;

    @FXML
    private TableColumn<InfoHotel, Number> columnDiariasHospPac;

    @FXML
    private TableColumn<InfoHotel, Number> columnQuartosHospPac;

    /*------------------------------------------------------------------------*/

    @FXML
    private ChoiceBox<Interesse.TipoInteresse> choiceTipoInteresse;

    @FXML
    private ChoiceBox<Cidade> choiceDestinoInteresse;

    @FXML
    private TextField textValorInteresse;

    @FXML
    private Button buttonAdicionarInteresse;

    @FXML
    private Button buttonExcluirInteresse;

    @FXML
    private TableView<Interesse> tableInteresse;

    @FXML
    private TableColumn<Interesse, Number> columnInteresseId;

    @FXML
    private TableColumn<Interesse, String> columnInteresseDestino;

    @FXML
    private TableColumn<Interesse, String> columnInteresseTipo;

    @FXML
    private TableColumn<Interesse, String> columnInteresseStatus;

    /*------------------------------------------------------------------------*/

    @FXML
    public void initialize() {
        inicializarVoos();
        inicializarHospedagens();
        inicializarPacotes();
        inicializarInteresse();

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

    private void inicializarVoos() {
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

        // Configura o botão de consulta
        buttonConsultarVoo.setOnAction(this::consultarVoos);

        // Configura o botão de compra
        buttonComprarVoo.setOnAction(this::comprarVoos);
    }

    private void inicializarHospedagens() {
        // Inicializa a ChoiceBox com as cidades do enum Cidade
        choiceCidadeHosp.getItems().setAll(Cidade.values());

        // Inicializa o seletor de número de quartos e de pessoas
        spinnerNumQuartosHosp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3, 1));
        spinnerNumPessoasHosp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, 1));

        // Configura as colunas
        columnNomeHosp.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getNome()));
        columnCidadeHosp.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getLocal().toString()));
        columnChegadaHosp.setCellValueFactory(item -> new SimpleStringProperty("")); // FIXME
        columnDiariasHosp.setCellValueFactory(item -> new SimpleIntegerProperty(0)); // FIXME
        columnQuartosHosp.setCellValueFactory(item -> new SimpleIntegerProperty(item.getValue().getNumQuartos()));

        // Configura o botão de consulta
        buttonConsultarHosp.setOnAction(this::consultarHospedagens);

        // Configura o botão de compra
        //buttonComprarHosp.setOnAction(this::comprarVoos);
    }

    private void inicializarPacotes() {
        // Inicializa as ChoiceBox com as cidades do enum Cidade
        choiceOrigemPacote.getItems().setAll(Cidade.values());
        choiceDestinoPacote.getItems().setAll(Cidade.values());

        // Inicializa o seletor de número de pessoas
        spinnerNumQuartosPacote.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3, 1));
        spinnerNumPessoasPacote.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, 1));

        // Configura as colunas
        columnIdVooIdaPac.setCellValueFactory(item -> new SimpleIntegerProperty(item.getValue().getId()));
        columnOrigemVooIdaPac.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getOrigem().toString()));
        columnDestinoVooIdaPac.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getDestino().toString()));
        columnDataVooIdaPac.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getData().toString()));
        columnPoltronasVooIdaPac.setCellValueFactory(item -> new SimpleIntegerProperty(item.getValue().poltronasDisp));
        columnIdVooVoltaPac.setCellValueFactory(item -> new SimpleIntegerProperty(item.getValue().getId()));
        columnOrigemVooVoltaPac.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getOrigem().toString()));
        columnDestinoVooVoltaPac.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getDestino().toString()));
        columnDataVooVoltaPac.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getData().toString()));
        columnPoltronasVooVoltaPac.setCellValueFactory(item -> new SimpleIntegerProperty(item.getValue().poltronasDisp));
        columnNomeHospPac.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getNome()));
        columnCidadeHospPac.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getLocal().toString()));
        columnChegadaHospPac.setCellValueFactory(item -> new SimpleStringProperty("")); // FIXME
        columnDiariasHospPac.setCellValueFactory(item -> new SimpleIntegerProperty(0)); // FIXME
        columnQuartosHospPac.setCellValueFactory(item -> new SimpleIntegerProperty(item.getValue().getNumQuartos()));

        // Configura o botão de consulta
        buttonConsultarPacote.setOnAction(this::consultarPacotes);

        // Configura o botão de compra
        //buttonComprarPacote.setOnAction(this::comprarVoos);
    }

    private void inicializarInteresse() {
        // Inicializa as ChoiceBox com as cidades do enum Cidade
        choiceTipoInteresse.getItems().setAll(Interesse.TipoInteresse.values());
        choiceDestinoInteresse.getItems().setAll(Cidade.values());

        // Criar mascara de número float no campo de valor máximo
        textValorInteresse.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d+([\\.|,]\\d+)?")) {
                textValorInteresse.setText(oldValue);
            }
        });

        // Configura as colunas
        columnInteresseId.setCellValueFactory(item -> new SimpleIntegerProperty(item.getValue().getId()));
        columnInteresseDestino.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getCidade().toString()));
        columnInteresseTipo.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getTipo().toString()));
        columnInteresseStatus.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getStatus().toString()));

        // Configura o botão de consulta
        buttonAdicionarInteresse.setOnAction(this::registrarInteresse);
        buttonExcluirInteresse.setOnAction(this::excluirInteresse);

        consultarInteresses();
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

        if (!validarConsultaVoos(origem, destino, dataIda, dataVolta, numPessoas, tipoPassagem)) {
            return;
        }

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

    private boolean validarConsultaVoos(Cidade origem, Cidade destino, LocalDate dataIda, LocalDate dataVolta, int numPessoas, TipoPassagem tipoPassagem) {
        boolean ok = true;
        String error = "Forneça um valor para os seguintes campos:";

        if (origem == null) {
            ok = false;
            error += "\n  Cidade de origem";
        }
        if (destino == null) {
            ok = false;
            error += "\n  Cidade de destino";
        }
        if (dataIda == null) {
            ok = false;
            error += "\n  Data de ida";
        }
        if (tipoPassagem == TipoPassagem.IDA_E_VOLTA && dataVolta == null) {
            ok = false;
            error += "\n  Data de volta";
        }
        if (numPessoas < 1 || numPessoas > 8) {
            ok = false;
            error += "\n  Número de pessoas (entre 1 e 8)";
        }
        if (!ok) {
            Alert alert = new Alert(Alert.AlertType.ERROR, error);
            alert.show();
        }

        return ok;
    }

    private void comprarVoos(ActionEvent event) {
        InfoVoo vooIda = tableVooIda.getSelectionModel().getSelectedItem();
        InfoVoo vooVolta = tableVooVolta.getSelectionModel().getSelectedItem();
        int idVooVolta = 0;
        int numPessoas = spinnerNumPessoasVoo.getValue();

        TipoPassagem tipoPassagem = TipoPassagem.IDA_E_VOLTA;
        if (vooVolta == null) {
            tipoPassagem = TipoPassagem.SOMENTE_IDA;
        }
        else {
            idVooVolta = vooVolta.getId();
        }

        if (vooIda == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Selecione os voos que deseja comprar.");
            alert.show();
            return;
        }

        // TODO: Janela de confirmação

        try {
            if (serverRef.comprarPassagens(tipoPassagem, vooIda.getId(), idVooVolta, numPessoas)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Compra realizada com sucesso!");
                alert.show();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Falha na compra.");
                alert.show();
            }
        }
        catch (RemoteException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Falha na comunicação com o servidor.");
        }
    }

    /*------------------------------------------------------------------------*/

    private void consultarHospedagens(ActionEvent event) {
        Cidade cidade = choiceCidadeHosp.getValue();
        LocalDate dataEntrada = dateChegadaHosp.getValue();
        LocalDate dataSaida = dateSaidaHosp.getValue();
        int numQuartos = spinnerNumQuartosHosp.getValue();
        int numPessoas = spinnerNumPessoasHosp.getValue();

        if (!validarConsultaHospedagens(cidade, dataEntrada, dataSaida, numQuartos, numPessoas)) {
            return;
        }

        HashMap<InfoHotel, ArrayList<InfoHospedagem>> hospedagens = null;
        try {
            hospedagens = serverRef.consultarHospedagens(cidade, dataEntrada, dataSaida, numQuartos, numPessoas);
        }
        catch (RemoteException e) {
            // TODO: Mostrar mensagem de erro
            e.printStackTrace();
        }

        olInfoHotel.clear();
        if (hospedagens != null) {
            olInfoHotel.addAll(hospedagens.keySet());
        }

        tableHospedagem.setItems(olInfoHotel);
    }

    private boolean validarConsultaHospedagens(Cidade cidade, LocalDate dataEntrada, LocalDate dataSaida, int numQuartos, int numPessoas) {
        boolean ok = true;
        String error = "Forneça um valor para os seguintes campos:";

        if (cidade == null) {
            ok = false;
            error += "\n  Cidade";
        }
        if (dataEntrada == null) {
            ok = false;
            error += "\n  Data de chegada";
        }
        if (dataSaida == null) {
            ok = false;
            error += "\n  Data de saida";
        }
        if (numQuartos < 1 || numQuartos > 3) {
            ok = false;
            error += "\n  Número de quartos (entre 1 e 3)";
        }
        if (numPessoas < 1 || numPessoas > 8) {
            ok = false;
            error += "\n  Número de pessoas (entre 1 e 8)";
        }
        if (!ok) {
            Alert alert = new Alert(Alert.AlertType.ERROR, error);
            alert.show();
        }

        return ok;
    }

    /*------------------------------------------------------------------------*/

    private void consultarPacotes(ActionEvent event) {
        Cidade origem = choiceOrigemPacote.getValue();
        Cidade destino = choiceDestinoPacote.getValue();
        LocalDate dataIda = datePacoteIda.getValue();
        LocalDate dataVolta = datePacoteVolta.getValue();
        int numQuartos = spinnerNumQuartosPacote.getValue();
        int numPessoas = spinnerNumPessoasPacote.getValue();

        if (!validarConsultaPacotes(origem, destino, dataIda, dataVolta, numQuartos, numPessoas)) {
            return;
        }

        HashMap<InfoHotel, ArrayList<InfoHospedagem>> hospedagens = null;
        ArrayList<InfoVoo> voos = null;
        try {
            hospedagens = serverRef.consultarHospedagens(destino, dataIda, dataVolta, numQuartos, numPessoas);
            voos = serverRef.consultarPassagens(TipoPassagem.IDA_E_VOLTA, origem, destino, dataIda, dataVolta, numPessoas);
        }
        catch (RemoteException e) {
            // TODO: Mostrar mensagem de erro
            e.printStackTrace();
        }

        olInfoHotelPac.clear();
        olInfoVooIdaPac.clear();
        olInfoVooVoltaPac.clear();

        if (hospedagens != null) {
            olInfoHotelPac.addAll(hospedagens.keySet());
        }
        if (voos != null) {
            for (InfoVoo v : voos) {
                if (v.getOrigem() == origem) {
                    olInfoVooIdaPac.add(v);
                }
                else {
                    olInfoVooVoltaPac.add(v);
                }
            }
        }

        tableHospedagemPac.setItems(olInfoHotelPac);
        tableVooIdaPac.setItems(olInfoVooIdaPac);
        tableVooVoltaPac.setItems(olInfoVooVoltaPac);
    }

    private boolean validarConsultaPacotes(Cidade origem, Cidade destino, LocalDate dataIda, LocalDate dataVolta, int numQuartos, int numPessoas) {
        boolean ok = true;
        String error = "Forneça um valor para os seguintes campos:";

        if (origem == null) {
            ok = false;
            error += "\n  Cidade de origem";
        }
        if (destino == null) {
            ok = false;
            error += "\n  Cidade de destino";
        }
        if (dataIda == null) {
            ok = false;
            error += "\n  Data de ida";
        }
        if (dataVolta == null) {
            ok = false;
            error += "\n  Data de volta";
        }
        if (numQuartos < 1 || numQuartos > 3) {
            ok = false;
            error += "\n  Número de quartos (entre 1 e 3)";
        }
        if (numPessoas < 1 || numPessoas > 8) {
            ok = false;
            error += "\n  Número de pessoas (entre 1 e 8)";
        }
        if (!ok) {
            Alert alert = new Alert(Alert.AlertType.ERROR, error);
            alert.show();
        }

        return ok;
    }

    /*------------------------------------------------------------------------*/

    private void consultarInteresses() {
        olInteresse.clear();
        olInteresse.addAll(InteresseController.getInstance().getInteresses());
        tableInteresse.setItems(olInteresse);
    }

    private void registrarInteresse(ActionEvent event) {
        Interesse.TipoInteresse tipo = choiceTipoInteresse.getValue();
        Cidade destino = choiceDestinoInteresse.getValue();
        float valorMaximo = Float.parseFloat(textValorInteresse.getCharacters().toString());

        try {
            serverRef.comprarPacote(null);
        }
        catch (RemoteException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Falha na comunicação com o servidor.");
        }
    }

    private void excluirInteresse(ActionEvent event) {

    }

    /*------------------------------------------------------------------------*/
}
