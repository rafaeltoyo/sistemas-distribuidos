package server;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainScreen extends Application implements EventHandler<ActionEvent> {

    Button button1;
    Button button2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Title of Window");

        button1 = new Button();
        button1.setText("Click me 1");
        button1.setOnAction(this);

        button2 = new Button();
        button2.setText("Click me 2");

        // This class will handle the button events
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("qeqqeqeqeqeqeqezdasdas");
            }
        });

        button2.setOnAction(e -> System.out.println("euqueuqwewqeqwe"));

        StackPane layout = new StackPane();
        layout.getChildren().addAll(button1, button2);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == button1) {
            System.out.println("Wirue Sarueeee 7 por 40? Sim temos x-burger");
        }
        if (event.getSource() == button2) {
            System.out.println("Peru safadangelo quero pao");
        }
    }

}
