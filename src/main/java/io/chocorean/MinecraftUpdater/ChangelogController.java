package io.chocorean.MinecraftUpdater;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;

public class ChangelogController {

    @FXML private WebView changelog;

    @FXML
    private void initialize() {
        this.changelog.getEngine().load(ChangelogController.class.getResource("/changelog/index.html").toExternalForm());
    }

}
