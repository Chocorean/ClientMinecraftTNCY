package io.chocorean.minecraft.updater.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller of the main view
 *
 * @author mcdostone
 */
public class AppController {

    private static final Logger LOGGER = Logger.getLogger(AppController.class.getName());
    @FXML private AnchorPane rootPane;

    @FXML
    private void initialize() {
        if(!AppController.checkNetwork())
            this.showNoInternet();
    }

    public static boolean checkNetwork() {
        try {
            final URL url = new URL("http://www.github.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
        } catch (Exception e) { return false; }
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
            LOGGER.log(Level.SEVERE, "", e);
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
            LOGGER.log(Level.SEVERE, "", e);
        }
    }

}
