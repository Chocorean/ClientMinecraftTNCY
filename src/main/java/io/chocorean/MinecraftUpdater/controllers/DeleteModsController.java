package io.chocorean.MinecraftUpdater.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.List;

/**
 * Controller of the dialog view
 *
 * @author mcdostone
 */
public class DeleteModsController {

    private final List<File> unusedMods;
    @FXML private Label alert;
    @FXML private ScrollPane grid;
    @FXML private Button delete;
    @FXML private Button cancel;
    @FXML private final AppController appController;

    public DeleteModsController(List<File> unusedMods, AppController controller) {
        this.unusedMods = unusedMods;
        this.appController = controller;
    }

    @FXML
    private void initialize() {
        this.alert.setText(unusedMods.size() + " mods are unused, do you want to delete them?");
        this.grid.setContent(this.generateVBoxList());
        this.delete.setOnMouseReleased(event -> this.deleteUnusedMods());
        this.cancel.setOnMouseReleased(event -> this.appController.closeDialog());
    }

    private VBox generateVBoxList() {
        VBox parent = new VBox();
        parent.getStylesheets().add("/styles.css");
        parent.getStyleClass().add("transparent");
        parent.setAlignment(Pos.CENTER);
        for(File unused: this.unusedMods) {
            parent.getChildren().add(new Label(unused.getName()));
        }
        return parent;
    }

    private void deleteUnusedMods() {
        for(File m: this.unusedMods)
            m.delete();
        this.appController.closeDialog();
    }

}
