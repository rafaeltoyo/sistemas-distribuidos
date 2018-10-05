package client.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.hotel.InfoHotel;

import java.util.ArrayList;

public class ListarHotel {

    /** Referência para a tela principal */
    private Stage window;

    /** Agrupador dos elementos dessa tela */
    private Scene root;

    /** Listagem dos Hotéis */
    private TableView table;

    /** Lista dos dados a serem listados */
    private ArrayList<InfoHotel> dados;

    public ListarHotel(Stage window) {
        this.window = window;

        window.setTitle("Hotéis");
        window.setWidth(450);
        window.setHeight(300);

        root = new Scene((new Group()));


        final Label label = new Label();
        label.setText("Lista de Hotéis:");

        // TODO: 05/10/18 https://docs.oracle.com/javafx/2/ui_controls/table-view.htm

        TableColumn column1 = new TableColumn("ID");
        TableColumn column2 = new TableColumn("ID");
        TableColumn column3 = new TableColumn("ID");
        table = new TableView();
        table.getColumns().addAll(column1, column2, column3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        ((Group) root.getRoot()).getChildren().addAll(vbox);

        dados = new ArrayList<>();
    }

    public ArrayList<InfoHotel> getDados() {
        return dados;
    }

    public void show() {
        window.setScene(root);
        window.show();
    }

}
