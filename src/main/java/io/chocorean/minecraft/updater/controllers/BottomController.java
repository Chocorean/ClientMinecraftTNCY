package io.chocorean.minecraft.updater.controllers;

import com.google.gson.Gson;
import io.chocorean.minecraft.updater.Configuration;
import io.chocorean.minecraft.updater.core.Profile;
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
import java.util.Locale;

public class BottomController {
    @FXML private AnchorPane bottomPane;
    @FXML private TextField minecraftDirectory;
    @FXML private TextField javaArguments;
    @FXML private TextField username;
    @FXML private ImageView changeMinecraftDir;
    @FXML private Button installForgeButton;
    @FXML private Button saveButton;
    @FXML private Label message;
    @FXML private ProgressBar progression;
    @FXML private Label version;
    private File minecraftPath;
    private Stage dialog;

    @FXML
    private void initialize() {
        this.progression.setProgress(0);
        Configuration conf = Configuration.getInstance();
        this.version.setText(conf.getVersion());
        this.updateMinecraftDirectory(this.getDefaultMinecraftDirectory());

        // Event when press 'change' button
        this.changeMinecraftDir.setOnMouseClicked(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File newPath = chooser.showDialog(((Node) event.getSource()).getScene().getWindow());
            if(newPath != null)
                this.updateMinecraftDirectory(newPath);
        });

        // Event when press 'update client' button
        this.installForgeButton.setOnMouseReleased(event -> new Thread(() -> {
            Platform.runLater(() -> setMessage("Downloading forge client..."));
            ForgeInstaller installer = new ForgeInstaller(
                    conf.getForgeUrl(),
                    progression,
                    () -> setMessage("Forge is installed !")
            );
            installer.install();
            Platform.runLater(() -> setMessage("Installing mods..."));
            ModsUpdater updater = new ModsUpdater(this.getModsDirectory(), progression);
            List<File> installed = updater.install();
            Platform.runLater(() -> setMessage("Mods installed !"));
            this.progression.setProgress(1);
            List<File> unused = updater.getUnusedMods(this.getModsDirectory(), installed);
            this.askForUnusedMods(unused);

        }).start());

        // Event when press 'play' button
        this.saveButton.setOnMouseReleased(event -> new Thread(() -> {
            Gson gson = new Gson();
            String path = this.getMinecraftDirectory().getAbsolutePath();
            // creating folder if it doesnt exist
            File versionFolder = Paths.get(path,"versions", "1.10.2-TNCY").toFile();
            if (!versionFolder.exists())
                versionFolder.mkdirs();
            Profile profile = new Profile(this.username.getText());
            BufferedWriter writer;
            try {
                writer = new BufferedWriter(new FileWriter(Paths.get(path, "versions", conf.getProfile(), conf.getProfile() + ".json").toFile()));
                writer.write(gson.toJson(profile));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start());
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
                e.printStackTrace();
            }
        }
    }

    public void closeDialog() {
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

    private File getDefaultMinecraftDirectory() {
        String minecraftDir = ".minecraft";
        String userHomeDir = System.getProperty("user.home", ".");
        String osType = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        File targetDir;
        if (osType.contains("win") && System.getenv("APPDATA") != null)
            targetDir = new File(System.getenv("APPDATA"), minecraftDir);
        else if (osType.contains("mac"))
            targetDir = Paths.get(userHomeDir, "Library","Application Support", "minecraft").toFile();
        else
            targetDir = new File(userHomeDir, minecraftDir);
        return targetDir;
    }

    private File getMinecraftDirectory() {
        return this.minecraftPath;
    }


    private File getModsDirectory() {
        return Paths.get(this.minecraftPath.getAbsolutePath(), "mods").toFile();
    }
}
