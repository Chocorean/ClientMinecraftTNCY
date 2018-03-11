package io.chocorean.MinecraftUpdater.controllers;

import io.chocorean.MinecraftUpdater.Configuration;
import io.chocorean.MinecraftUpdater.installers.ForgeInstaller;
import io.chocorean.MinecraftUpdater.installers.ModsUpdater;
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
import java.util.Locale;

/**
 * Controller of the main view
 *
 * @author mcdostone
 */
public class AppController {

    private static final String MODS_DIR =  ".minecraft" + File.separator + "mods";
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

    @FXML
    private void initialize() {
        this.progression.setProgress(0);
        Configuration conf = Configuration.getInstance();
        this.version.setText(conf.getVersion());
        this.updateModsDirectory(this.getDefaultModsDirectory());

        // Event when press 'change' button
        this.changeModsLocation.setOnMouseReleased(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File newPath = chooser.showDialog(((Node) event.getSource()).getScene().getWindow());
            if(newPath != null)
                this.updateModsDirectory(newPath);
        });

        // Event when press 'update mods' button
        this.updateModsButton.setOnMouseReleased(event -> new Thread(() -> {
            Platform.runLater(() -> setMessage("Installing mods..."));
            ModsUpdater updater = new ModsUpdater(this.modsPath, progression);
            List<File> installed = updater.install();
            Platform.runLater(() -> setMessage("Mods installed !"));
            this.progression.setProgress(1);
            List<File> unused = updater.getUnusedMods(this.modsPath, installed);
            this.askForUnusedMods(unused);
        }).start());

        // Event when press 'install forge' button
        this.installForgeButton.setOnMouseReleased(event -> {
            Platform.runLater(() -> setMessage("Downloading forge client..."));
            ForgeInstaller installer = new ForgeInstaller(
                    conf.getForgeUrl(),
                    progression,
                    () -> setMessage("Forge is installed !")
            );
            installer.install();
        });
    }

    private void askForUnusedMods(List<File> unusedMods) {
        if(!unusedMods.isEmpty()) {
            FXMLLoader loader = new FXMLLoader(AppController.class.getResource("/fxml/deleteModsDialog.fxml"));
            try {
                Stage stage = (Stage) this.rootPane.getScene().getWindow();
                loader.setController(new DeleteModsController(unusedMods, this));
                BorderPane root = loader.load();
                root.setMinHeight(Region.USE_PREF_SIZE);
                Platform.runLater(() -> {
                    this.dialog = new Stage();
                    this.dialog.initModality(Modality.WINDOW_MODAL);
                    this.dialog.initOwner(stage);
                    Scene s = new Scene(root);
                    this.dialog.setScene(s);
                    this.dialog.showAndWait();
                });
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

    private void setMessage(String msg) {
        this.message.setText(msg);
    }

    private File getDefaultModsDirectory() {
        String userHomeDir = System.getProperty("user.home", ".");
        String osType = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        File targetDir;
        String mcDir = ".minecraft";
        if (osType.contains("win") && System.getenv("APPDATA") != null)
            targetDir = new File(System.getenv("APPDATA"), mcDir);
        else if (osType.contains("mac"))
            targetDir = new File(new File(new File(userHomeDir, "Library"),"Application Support"),"minecraft");
        else
            targetDir = new File(userHomeDir, mcDir);
        return targetDir;
    }

}
