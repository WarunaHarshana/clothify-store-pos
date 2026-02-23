package com.clothify;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Clothify Store POS");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}

