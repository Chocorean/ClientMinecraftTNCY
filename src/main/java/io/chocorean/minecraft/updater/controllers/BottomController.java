package io.chocorean.minecraft.updater.controllers;

import io.chocorean.minecraft.updater.Configuration;
import io.chocorean.minecraft.updater.MinecraftUtils;
import io.chocorean.minecraft.updater.core.Libraries;
import io.chocorean.minecraft.updater.core.Library;
import io.chocorean.minecraft.updater.core.Version;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BottomController {

    @FXML private AnchorPane bottomPane;
    @FXML private TextField minecraftDirectory;
    @FXML private TextField username;
    @FXML private ImageView changeMinecraftDir;
    @FXML private Button installForgeButton;
    @FXML private Button saveButton;
    @FXML private Label message;
    @FXML private ProgressBar progression;
    @FXML private Label version;
    @FXML private Button installModsButton;
    private File minecraftPath;
    private Stage dialog;
    private static final Logger LOGGER = Logger.getLogger(BottomController.class.getName());
    private final Configuration conf = Configuration.getInstance();

    public BottomController() {
        this.minecraftPath = MinecraftUtils.getDefaultMinecraftDirectory();
    }

    @FXML
    private void initialize() {
        this.username.setText(Version.getUsername(this.getVersionFile()));
        this.progression.setProgress(0);
        this.version.setText(conf.getVersion());
        this.updateMinecraftDirectory(this.minecraftPath);
        // Event when press 'change' button
        this.changeMinecraftDir.setOnMouseClicked(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File newPath = chooser.showDialog(((Node) event.getSource()).getScene().getWindow());
            if(newPath != null)
                this.updateMinecraftDirectory(newPath);
        });
        this.setOnForgeRealeased();
        this.setOnModsReleased();
        this.setOnSaveRealeased();
    }

    private void askForUnusedMods(List<File> unusedMods) {
        if(!unusedMods.isEmpty()) {
            FXMLLoader loader = new FXMLLoader(AppController.class.getResource("/fxml/deleteModsDialog.fxml"));
            try {
                Stage stage = (Stage) this.bottomPane.getParent().getScene().getWindow();
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
                LOGGER.log(Level.SEVERE, "", e);
            }
        }
    }

    private void setOnSaveRealeased() {
        this.saveButton.setOnMouseReleased(event -> new Thread(() -> {
            File versionFolder = this.getVersionFile();
            if (!versionFolder.exists())
                versionFolder.mkdirs();
            List<Library> libraries = Libraries.getLibrariesFromResource();
            Version profile = new Version(conf.getProfile(), this.username.getText(), conf.getForgeVersion(), libraries);
            BufferedWriter writer;
            try {
                writer = new BufferedWriter(new FileWriter(this.getVersionFile()));
                writer.write(profile.toString());
                Platform.runLater(() -> setMessage("Configuration has been saved"));
                writer.close();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "", e);
            }
        }).start());
    }

    private File getVersionFolder() {
        return Paths.get(this.getMinecraftDirectory().getAbsolutePath(),"versions", conf.getProfile()).toFile();
    }

    private File getVersionFile() {
        return Paths.get(this.getVersionFolder().getAbsolutePath(), conf.getProfile() + ".json").toFile();
    }

    private void setOnForgeRealeased() {
        this.installForgeButton.setOnMouseReleased(event -> new Thread(() -> {
            installForgeButton.setDisable(true);
            installModsButton.setDisable(true);
            Platform.runLater(() -> setMessage("Downloading forge " + conf.getForgeVersion() + "..."));
            ForgeInstaller installer = new ForgeInstaller(
                    conf.getForgeUrl(),
                    progression,
                    () -> setMessage("Forge has been installed")
            );
            Future<Integer> futurExitValue = installer.install();
            try {
                futurExitValue.get();
                installForgeButton.setDisable(false);
                installModsButton.setDisable(false);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "", e);
                Thread.currentThread().interrupt();
            } catch(ExecutionException e) {
                LOGGER.log(Level.SEVERE, "", e);
            }
        }).start());
    }

    private void setOnModsReleased() {
        this.installModsButton.setOnMouseReleased(event -> new Thread(() -> {
            installForgeButton.setDisable(true);
            installModsButton.setDisable(true);
            Platform.runLater(() -> setMessage("Installing mods..."));
            ModsUpdater updater = new ModsUpdater(this.getModsDirectory(), progression);
            List<Future<File>> futureInstalled = updater.install();
            List<File> installed = futureInstalled.stream().map(f -> {
                try {
                    return f.get();
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "", e);
                    Thread.currentThread().interrupt();
                } catch(ExecutionException e) {
                    LOGGER.log(Level.SEVERE, "", e);
                }
                return null;
            }).collect(Collectors.toList());
            installForgeButton.setDisable(false);
            installModsButton.setDisable(false);
            Platform.runLater(() -> setMessage(installed.size() + " mods have been installed"));
            this.progression.setProgress(1);
            List<File> unused = updater.getUnusedMods(this.getModsDirectory(), installed);
            this.askForUnusedMods(unused);
        }).start());
    }

    void closeDialog() {
        if(this.dialog != null)
            this.dialog.close();
    }

    private void updateMinecraftDirectory(File newPath) {
        this.minecraftPath = newPath;
        this.minecraftDirectory.setText(this.minecraftPath.getAbsolutePath());
    }

    private void setMessage(String msg) {
        this.message.setText(msg);
    }

    private File getMinecraftDirectory() {
        return this.minecraftPath;
    }

    private File getModsDirectory() {
        return Paths.get(this.minecraftPath.getAbsolutePath(), "mods").toFile();
    }
}
