package client.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import model.cidade.Cidade;
import model.evento.Interesse;
import model.hotel.InfoHotelRet;
import model.mensagem.Mensagem;
import model.voo.InfoVoo;
import model.voo.TipoPassagem;
import remote.AgencyServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ClientUIController {
    /**
     * Nome de host do serviço de nomes
     */
    private static final String NAMING_SERVICE_HOST = "localhost";

    /**
     * Porta do serviço de nomes
     */
    private static final int NAMING_SERVICE_PORT = 11037;

    /**
     * Referência ao servidor (RMI)
     */
    private AgencyServer serverRef;

    /*------------------------------------------------------------------------*/

    private ObservableList<InfoVoo> olInfoVooIda = FXCollections.observableArrayList();

    private ObservableList<InfoVoo> olInfoVooVolta = FXCollections.observableArrayList();

    private ObservableList<InfoHotelRet> olInfoHotel = FXCollections.observableArrayList();

    private ObservableList<InfoVoo> olInfoVooIdaPac = FXCollections.observableArrayList();

    private ObservableList<InfoVoo> olInfoVooVoltaPac = FXCollections.observableArrayList();

    private ObservableList<InfoHotelRet> olInfoHotelPac = FXCollections.observableArrayList();

    private ObservableList<Interesse> olInteresse = FXCollections.observableArrayList();

    /*------------------------------------------------------------------------*/

    @FXML
    private TabPane tabpane;

    @FXML
    private Tab tabVoo;

    @FXML
    private Tab tabHospedagem;

    @FXML
    private Tab tabPacote;

    @FXML
    private Tab tabInteresse;

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
    private TableView<InfoHotelRet> tableHospedagem;

    @FXML
    private TableColumn<InfoHotelRet, String> columnNomeHosp;

    @FXML
    private TableColumn<InfoHotelRet, String> columnCidadeHosp;

    @FXML
    private TableColumn<InfoHotelRet, String> columnChegadaHosp;

    @FXML
    private TableColumn<InfoHotelRet, Number> columnDiariasHosp;

    @FXML
    private TableColumn<InfoHotelRet, Number> columnQuartosHosp;

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
    private TableView<InfoHotelRet> tableHospedagemPac;

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
    private TableColumn<InfoHotelRet, String> columnNomeHospPac;

    @FXML
    private TableColumn<InfoHotelRet, String> columnCidadeHospPac;

    @FXML
    private TableColumn<InfoHotelRet, String> columnChegadaHospPac;

    @FXML
    private TableColumn<InfoHotelRet, Number> columnDiariasHospPac;

    @FXML
    private TableColumn<InfoHotelRet, Number> columnQuartosHospPac;

    /*------------------------------------------------------------------------*/

    @FXML
    private ChoiceBox<Interesse.TipoInteresse> choiceTipoInteresse;

    @FXML
    private ChoiceBox<Cidade> choiceOrigemInteresse;

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
    private TableView<Interesse> tableNotify;

    @FXML
    private TableColumn<Interesse, Number> columnInteresseId;

    @FXML
    private TableColumn<Interesse, String> columnInteresseOrigem;

    @FXML
    private TableColumn<Interesse, String> columnInteresseDestino;

    @FXML
    private TableColumn<Interesse, String> columnInteresseTipo;

    @FXML
    private TableColumn<Interesse, String> columnInteresseValor;

    @FXML
    private TableColumn<Mensagem, Number> columnNotifyId;

    @FXML
    private TableColumn<Mensagem, String> columnNotifyMsg;

    /*------------------------------------------------------------------------*/

    @FXML
    public void initialize() {
        inicializarVoos();
        inicializarHospedagens();
        inicializarPacotes();
        inicializarInteresse();

        tabpane.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    if (t != t1 && t1 == tabInteresse) {
                        consultarInteresses();
                    }
                }
        );

        try {
            connectToServer();
        } catch (RemoteException | NotBoundException e) {
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
        columnChegadaHosp.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getDataEntrada().toString()));
        columnDiariasHosp.setCellValueFactory(item -> new SimpleLongProperty(item.getValue().getNumDiarias()));
        columnQuartosHosp.setCellValueFactory(item -> new SimpleIntegerProperty(item.getValue().getQuartosDisponiveis()));

        // Configura o botão de consulta
        buttonConsultarHosp.setOnAction(this::consultarHospedagens);

        // Configura o botão de compra
        buttonComprarHosp.setOnAction(this::comprarHospedagem);
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
        columnChegadaHospPac.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getDataEntrada().toString()));
        columnDiariasHospPac.setCellValueFactory(item -> new SimpleLongProperty(item.getValue().getNumDiarias()));
        columnQuartosHospPac.setCellValueFactory(item -> new SimpleIntegerProperty(item.getValue().getQuartosDisponiveis()));

        // Configura o botão de consulta
        buttonConsultarPacote.setOnAction(this::consultarPacotes);

        // Configura o botão de compra
        buttonComprarPacote.setOnAction(this::comprarPacote);
    }

    private void inicializarInteresse() {
        // Inicializa as ChoiceBox com as cidades do enum Cidade
        choiceTipoInteresse.getItems().setAll(Interesse.TipoInteresse.values());
        choiceOrigemInteresse.getItems().setAll(Cidade.values());
        choiceDestinoInteresse.getItems().setAll(Cidade.values());

        // Criar mascara de número float no campo de valor máximo
        textValorInteresse.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d+([\\.|,]\\d+)?")) {
                textValorInteresse.setText(oldValue);
            }
        });

        // Configura as colunas
        columnInteresseId.setCellValueFactory(item -> new SimpleIntegerProperty(item.getValue().getId()));
        columnInteresseOrigem.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getOrigem().toString()));
        columnInteresseDestino.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getDestino().toString()));
        columnInteresseTipo.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getTipo().toString()));
        columnInteresseValor.setCellValueFactory(item -> new SimpleStringProperty(Float.toString(item.getValue().getValorMaximo())));

        // Configura o botão de consulta
        buttonAdicionarInteresse.setOnAction(this::registrarInteresse);
        buttonExcluirInteresse.setOnAction(this::excluirInteresse);

        consultarInteresses();
    }

    /*------------------------------------------------------------------------*/

    private void connectToServer() throws RemoteException, NotBoundException {
        //Registry namingServiceRef = LocateRegistry.getRegistry(
        //        NAMING_SERVICE_HOST, NAMING_SERVICE_PORT);
        //serverRef = (AgencyServer) namingServiceRef.lookup("server");
        RemoteController.getInstance().connect();
        serverRef = RemoteController.getInstance().serverRef;
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
        } catch (RemoteException e) {
            // TODO: Mostrar mensagem de erro
            e.printStackTrace();
        }

        olInfoVooIda.clear();
        olInfoVooVolta.clear();
        if (voos != null) {
            for (InfoVoo v : voos) {
                if (v.getOrigem() == origem) {
                    olInfoVooIda.add(v);
                } else {
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

        TipoPassagem tipoPassagem = (tipoVoo.getSelectedToggle() == radioSomenteIdaVoo)? TipoPassagem.SOMENTE_IDA : TipoPassagem.IDA_E_VOLTA;

        // Verifica se o voo de ida existe e, se for ida e volta, se o voo de volta existe
        if (vooIda == null || (tipoPassagem == TipoPassagem.IDA_E_VOLTA && vooVolta == null)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Selecione os voos que deseja comprar.");
            alert.show();
            return;
        }

        // Pega o ID do voo de volta se for ida e volta
        if (tipoPassagem == TipoPassagem.IDA_E_VOLTA) {
            idVooVolta = vooVolta.getId();
        }

        // TODO: Janela de confirmação

        try {
            if (serverRef.comprarPassagens(tipoPassagem, vooIda.getId(), idVooVolta, numPessoas)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Compra realizada com sucesso!");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Falha na compra.");
                alert.show();
            }
        } catch (RemoteException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Falha na comunicação com o servidor.");
            alert.show();
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

        ArrayList<InfoHotelRet> hospedagens = null;
        try {
            hospedagens = serverRef.consultarHospedagens(cidade, dataEntrada, dataSaida, numQuartos, numPessoas);
        } catch (RemoteException e) {
            // TODO: Mostrar mensagem de erro
            e.printStackTrace();
        }

        olInfoHotel.clear();
        if (hospedagens != null) {
            olInfoHotel.addAll(hospedagens);
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

    private void comprarHospedagem(ActionEvent event) {
        InfoHotelRet hotel = tableHospedagem.getSelectionModel().getSelectedItem();
        if (hotel == null) {
            return;
        }

        int idHotel = hotel.getId();
        LocalDate dataEntrada = dateChegadaHosp.getValue();
        LocalDate dataSaida = dateSaidaHosp.getValue();
        int numQuartos = spinnerNumQuartosHosp.getValue();

        if (dataEntrada != null && dataSaida != null) {
            try {
                if (serverRef.comprarHospedagem(idHotel, dataEntrada, dataSaida, numQuartos)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Compra realizada com sucesso!");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Falha na compra.");
                    alert.show();
                }
            } catch (RemoteException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Falha na comunicação com o servidor.");
                alert.show();
            }
        }
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

        ArrayList<InfoHotelRet> hospedagens = null;
        ArrayList<InfoVoo> voos = null;
        try {
            hospedagens = serverRef.consultarHospedagens(destino, dataIda, dataVolta, numQuartos, numPessoas);
            voos = serverRef.consultarPassagens(TipoPassagem.IDA_E_VOLTA, origem, destino, dataIda, dataVolta, numPessoas);
        } catch (RemoteException e) {
            // TODO: Mostrar mensagem de erro
            e.printStackTrace();
        }

        olInfoHotelPac.clear();
        olInfoVooIdaPac.clear();
        olInfoVooVoltaPac.clear();

        if (hospedagens != null) {
            olInfoHotelPac.addAll(hospedagens);
        }
        if (voos != null) {
            for (InfoVoo v : voos) {
                if (v.getOrigem() == origem) {
                    olInfoVooIdaPac.add(v);
                } else {
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

    private void comprarPacote(ActionEvent event) {
        InfoVoo vooIda = tableVooIdaPac.getSelectionModel().getSelectedItem();
        if (vooIda == null) {
            return;
        }
        int idVooIda = vooIda.getId();

        InfoVoo vooVolta = tableVooVoltaPac.getSelectionModel().getSelectedItem();
        if (vooVolta == null) {
            return;
        }
        int idVooVolta = vooVolta.getId();

        InfoHotelRet hotel = tableHospedagemPac.getSelectionModel().getSelectedItem();
        if (hotel == null) {
            return;
        }
        int idHotel = hotel.getId();

        int numPessoas = spinnerNumPessoasPacote.getValue();
        int numQuartos = spinnerNumQuartosPacote.getValue();
        LocalDate dataEntrada = datePacoteIda.getValue();
        LocalDate dataSaida = datePacoteVolta.getValue();

        if (dataEntrada != null && dataSaida != null) {
            try {
                if (serverRef.comprarPacote(idVooIda, idVooVolta, idHotel, dataEntrada, dataSaida, numQuartos, numPessoas)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Compra realizada com sucesso!");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Falha na compra.");
                    alert.show();
                }
            } catch (RemoteException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Falha na comunicação com o servidor.");
                alert.show();
            }
        }
    }

    /*------------------------------------------------------------------------*/

    private void consultarInteresses() {
        olInteresse.clear();
        olInteresse.addAll(InteresseController.getInstance().getInteresses());
        tableInteresse.setItems(olInteresse);
    }

    private void registrarInteresse(ActionEvent event) {
        Interesse.TipoInteresse tipo = choiceTipoInteresse.getValue();
        Cidade origem = choiceOrigemInteresse.getValue();
        Cidade destino = choiceDestinoInteresse.getValue();
        float valorMaximo = Float.parseFloat(textValorInteresse.getCharacters().toString());

        Interesse interesse = new Interesse(origem, destino, tipo, valorMaximo);

        try {
            InteresseController.getInstance().create(interesse);
            int id = serverRef.registrarInteresse(interesse, RemoteController.getInstance().client);
            interesse.setId(id);
        } catch (RemoteException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Falha na comunicação com o servidor.");
        }
        consultarInteresses();
    }

    private void excluirInteresse(ActionEvent event) {
        Interesse interesse = tableInteresse.getSelectionModel().getSelectedItem();
        if (interesse == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Selecione um registro antes.");
            alert.show();
        } else {
            InteresseController.getInstance().remove(interesse.getId());

            try {
                serverRef.removerInteresse(interesse.getId(), RemoteController.getInstance().client);
            } catch (RemoteException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Falha na comunicação com o servidor.");
                alert.show();
            }
        }
        consultarInteresses();
    }

    /*------------------------------------------------------------------------*/
}
