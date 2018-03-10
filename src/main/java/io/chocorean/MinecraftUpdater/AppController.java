package io.chocorean.MinecraftUpdater;

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
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AppController {

    @FXML private AnchorPane rootPane;
    @FXML private TextField modsDirectory;
    @FXML private Button changeModsLocation;
    @FXML private Button updateModsButton;
    @FXML private Button installForgeButton;
    @FXML private Label message;
    @FXML private ProgressBar progression;
    @FXML private Label version;
    private File modsPath;
    private Stage dialog;
    private static final String MODS_DIR =  ".minecraft" + File.separator + "mods";

    @FXML
    private void initialize() {
        this.resetProgression();
        Configuration conf = Configuration.getInstance();
        this.version.setText(conf.getVersion());
        File modsDirectory = new File(System.getProperty("user.home") + File.separator + MODS_DIR);
        if(!modsDirectory.exists())
            modsDirectory = new File(System.getProperty("user.home"));
        this.updateModsDirectory(modsDirectory);
        this.changeModsLocation.setOnMouseReleased(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File newPath = chooser.showDialog(((Node) event.getSource()).getScene().getWindow());
            if(newPath != null)
                this.updateModsDirectory(newPath);
        });
        this.updateModsButton.setOnMouseReleased(event -> {
            new Thread(() -> {
                Platform.runLater(() -> setMessage("Installing mods..."));
                ModsUpdater.update(this.modsPath, progression);
                Platform.runLater(() -> setMessage("Mods installed !"));
            }).start();
        });
        this.installForgeButton.setOnMouseReleased(event -> {
            Platform.runLater(() -> setMessage("Downloading forge client..."));
            ForgeInstaller.install(conf.getForgeUrl(), progression, () -> setMessage("Forge is installed !"));
        });
    }

    private void checkUnusedMods(List<File> installedMods) {
        List<File> unused = ModsUpdater.getUnusedMods(this.modsPath, installedMods);
        if(!unused.isEmpty()) {
            FXMLLoader loader = new FXMLLoader(AppController.class.getResource("/fxml/deleteModsDialog.fxml"));
            try {
                Stage stage = (Stage) this.rootPane.getScene().getWindow();
                loader.setController(new DeleteModsController(unused, this));
                BorderPane root = loader.load();
                root.setMinHeight(Region.USE_PREF_SIZE);
                this.dialog = new Stage();
                this.dialog.initModality(Modality.WINDOW_MODAL);
                this.dialog.initOwner(stage);
                Scene s = new Scene(root);
                this.dialog.setScene(s);
                this.dialog.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateModsDirectory(File newPath) {
        this.modsPath = newPath;
        this.modsDirectory.setText(this.modsPath.getAbsolutePath());
    }

    public void closeDialog() {
        if(this.dialog != null)
            this.dialog.close();
    }

    public void resetProgression() {
        this.progression.setProgress(0);
    }

    public void setMessage(String msg) {
        this.message.setText(msg);
    }

}
