package com.geekbrains;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class ClientApp extends Application {

    public static Logger log = Logger.getLogger("stdout");


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client_window.fxml"));
        Parent parent = fxmlLoader.load();
        stage.setTitle("Cloud Storage");
        stage.setScene(new Scene(parent, 600,400));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
