package io.chocorean.minecraft.updater;


import io.chocorean.minecraft.updater.installers.ForgeInstaller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main class
 *
 * @author mcdostone
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/app.fxml"));
        primaryStage.setTitle("Minecraft Updater for telecomnancy.net");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/icon.png")));
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.setMinWidth(root.minWidth(-1));
        primaryStage.setMinHeight(root.minHeight(-1));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            ForgeInstaller.destroy();
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}

