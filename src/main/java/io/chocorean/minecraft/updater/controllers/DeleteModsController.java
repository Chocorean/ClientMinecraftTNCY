package io.chocorean.minecraft.updater.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller of the dialog view
 *
 * @author mcdostone
 */
public class DeleteModsController {

    private static final Logger LOGGER = Logger.getLogger(DeleteModsController.class.getName());
    private final List<File> unusedMods;
    @FXML private Label alert;
    @FXML private ScrollPane grid;
    @FXML private Button delete;
    @FXML private Button cancel;
    @FXML private final BottomController appController;

    public DeleteModsController(List<File> unusedMods, BottomController controller) {
        this.unusedMods = unusedMods;
        this.appController = controller;
    }

    @FXML
    private void initialize() {
        String message = null;
        if(this.unusedMods.size() == 1)
            message = unusedMods.size() + " mod is unused, do you want to delete it?";
        else
            message = unusedMods.size() + " mods are unused, do you want to delete them?";
        this.alert.setText(message);
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
        try {
            for (File m : this.unusedMods) {
                Files.delete(m.toPath());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
        this.appController.closeDialog();
    }

}
