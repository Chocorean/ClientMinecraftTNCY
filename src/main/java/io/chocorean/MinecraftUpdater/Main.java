package io.chocorean.MinecraftUpdater;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/app.fxml"));
        primaryStage.setTitle("Minecraft Updater for TNCY");
        primaryStage.setScene(new Scene(root, 800, 480));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

