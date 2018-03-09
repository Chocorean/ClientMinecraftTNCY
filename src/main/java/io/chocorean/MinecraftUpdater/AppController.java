package io.chocorean.MinecraftUpdater;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class AppController {

    @FXML private TextField modsDirectory;
    @FXML private Button changeModsLocation;
    @FXML private Label version;
    private String modsPath;

    @FXML
    private void initialize() {
        this.version.setText("v1.0");
        this.updateModsDirectory(System.getProperty("user.home") + File.separator + ".minecraft" + File.separator + "mods");
        this.changeModsLocation.setOnMouseReleased(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setInitialDirectory(new File(System.getProperty("user.home")));
            this.updateModsDirectory(chooser.showDialog(((Node) event.getSource()).getScene().getWindow()).getAbsolutePath());
        });
    }

    private void updateModsDirectory(String newPath) {
        this.modsPath = newPath;
        this.modsDirectory.setText(this.modsPath);
    }


}
