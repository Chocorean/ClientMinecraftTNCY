package io.chocorean.MinecraftUpdater.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NoInternetController {

    private final AppController appController;
    @FXML private Button refreshButton;

    public NoInternetController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    private void initialize() {
        this.refreshButton.setOnMouseReleased(e -> {
            if (AppController.checkNetwork()) {
                this.appController.showChangelog();
            }
        });
    }

}
