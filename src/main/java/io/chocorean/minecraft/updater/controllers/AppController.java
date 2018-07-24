package io.chocorean.minecraft.updater.controllers;

import io.chocorean.minecraft.updater.Configuration;
import io.chocorean.minecraft.updater.installers.ForgeInstaller;
import io.chocorean.minecraft.updater.installers.ModsUpdater;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

/**
 * Controller of the main view
 *
 * @author mcdostone
 */
public class AppController {

    @FXML private AnchorPane rootPane;

    @FXML
    private void initialize() {

        if(!this.checkNetwork())
            this.showNoInternet();
    }

    public static boolean checkNetwork() {
        try {
            final URL url = new URL("http://www.github.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void showChangelog() {
            FXMLLoader loader = new FXMLLoader(AppController.class.getResource("/fxml/changelog.fxml"));
            try {
                AnchorPane changelog = loader.load();
                BorderPane pane = (BorderPane) this.rootPane.getChildren().get(0);
                pane.setCenter(changelog);
                pane.getBottom().setDisable(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    private void showNoInternet() {
        FXMLLoader loader = new FXMLLoader(AppController.class.getResource("/fxml/no-internet.fxml"));
        try {
            loader.setController(new NoInternetController(this));
            StackPane character = loader.load();
            BorderPane pane = (BorderPane) this.rootPane.getChildren().get(0);
            pane.setCenter(character);
            pane.getBottom().setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
