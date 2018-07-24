package io.chocorean.minecraft.updater.controllers;

import io.chocorean.minecraft.updater.Configuration;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BottomController {
    @FXML private AnchorPane bottomPane;
    @FXML private TextField modsDirectory;
    @FXML private TextField javaArguments;
    @FXML private TextField username;
    @FXML private Button changeModsLocation;
    @FXML private Button installForgeButton;
    @FXML private Button playButton;
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

        // Event when press 'update client' button
        this.installForgeButton.setOnMouseReleased(event -> new Thread(() -> {
            this.playButton.setDisable(true);
            Platform.runLater(() -> setMessage("Downloading forge client..."));
            ForgeInstaller installer = new ForgeInstaller(
                    conf.getForgeUrl(),
                    progression,
                    () -> setMessage("Forge is installed !")
            );
            installer.install();
            this.playButton.setDisable(false);
        }).start());

        // Event when press 'play' button
        this.playButton.setOnMouseReleased(event -> new Thread(() -> {
            this.playButton.setDisable(true);
            this.javaArguments.setDisable(true);
            this.username.setDisable(true);
            Platform.runLater(() -> setMessage("Installing mods..."));
            ModsUpdater updater = new ModsUpdater(this.modsPath, progression);
            List<File> installed = updater.install();
            Platform.runLater(() -> setMessage("Mods installed !"));
            this.progression.setProgress(1);
            List<File> unused = updater.getUnusedMods(this.modsPath, installed);
            this.askForUnusedMods(unused);
            try {
                String path = this.getDefaultModsDirectory().getAbsolutePath().substring(0, (int) (this.getDefaultModsDirectory().getAbsolutePath().length()-4));

                // launch minecraft forge
                ArrayList<String> cmd = new ArrayList<>();
                cmd.add("java");
                cmd.add(this.javaArguments.getText().split(" ")[1]);
                cmd.add("-XX:+UseConcMarkSweepGC");
                cmd.add("-XX:-UseAdaptiveSizePolicy");
                cmd.add(this.javaArguments.getText().split(" ")[0]);
                cmd.add("-Djava.library.path=" + path + "versions/1.10.2-forge1.10.2-12.18.3.2185/1.10.2-forge1.10.2-12.18.3.2185-natives-45923197036750");
                cmd.add("-Dminecraft.launcher.brand=java-minecraft-launcher");
                cmd.add("-Dminecraft.launcher.version=1.6.89-j");
                cmd.add("-Dminecraft.client.jar=" +path + "versions/1.10.2/1.10.2.jar");
                cmd.add("-cp");
                cmd.add(path + "libraries/net/minecraftforge/forge/1.10.2-12.18.3.2185/forge-1.10.2-12.18.3.2185.jar:" + path +
                        "libraries/net/minecraft/launchwrapper/1.12/launchwrapper-1.12.jar:" + path +
                        "libraries/org/ow2/asm/asm-all/5.0.3/asm-all-5.0.3.jar:" + path + "libraries/jline/jline/2.13/jline-2.13.jar:" +
                        path + "libraries/com/typesafe/akka/akka-actor_2.11/2.3.3/akka-actor_2.11-2.3.3.jar:" + path +
                        "libraries/com/typesafe/config/1.2.1/config-1.2.1.jar:" + path +
                        "libraries/org/scala-lang/scala-actors-migration_2.11/1.1.0/scala-actors-migration_2.11-1.1.0.jar:" + path +
                        "libraries/org/scala-lang/scala-compiler/2.11.1/scala-compiler-2.11.1.jar:" + path +
                        "libraries/org/scala-lang/plugins/scala-continuations-library_2.11/1.0.2/scala-continuations-library_2.11-1.0.2.jar:" +
                        path + "libraries/org/scala-lang/plugins/scala-continuations-plugin_2.11.1/1.0.2/scala-continuations-plugin_2.11.1-1.0.2.jar:" +
                        path + "libraries/org/scala-lang/scala-library/2.11.1/scala-library-2.11.1.jar:" + path +
                        "libraries/org/scala-lang/scala-parser-combinators_2.11/1.0.1/scala-parser-combinators_2.11-1.0.1.jar:" + path +
                        "libraries/org/scala-lang/scala-reflect/2.11.1/scala-reflect-2.11.1.jar:" + path +
                        "libraries/org/scala-lang/scala-swing_2.11/1.0.1/scala-swing_2.11-1.0.1.jar" + path +
                        "libraries/org/scala-lang/scala-xml_2.11/1.0.2/scala-xml_2.11-1.0.2.jar:" + path +
                        "libraries/lzma/lzma/0.0.1/lzma-0.0.1.jar" + path + "libraries/net/sf/jopt-simple/jopt-simple/4.6/jopt-simple-4.6.jar:" +
                        path + "libraries/java3d/vecmath/1.5.2/vecmath-1.5.2.jar:" + path +
                        "libraries/net/sf/trove4j/trove4j/3.0.3/trove4j-3.0.3.jar:" + path +
                        "libraries/net/minecraftforge/MercuriusUpdater/1.10.2/MercuriusUpdater-1.10.2.jar:" + path +
                        "libraries/com/mojang/netty/1.6/netty-1.6.jar:" + path + "libraries/oshi-project/oshi-core/1.1/oshi-core-1.1.jar:" +
                        path + "libraries/net/java/dev/jna/jna/3.4.0/jna-3.4.0.jar:" + path +
                        "libraries/net/java/dev/jna/platform/3.4.0/platform-3.4.0.jar:" + path +
                        "libraries/com/ibm/icu/icu4j-core-mojang/51.2/icu4j-core-mojang-51.2.jar:" + path +
                        "libraries/net/sf/jopt-simple/jopt-simple/4.6/jopt-simple-4.6.jar:" + path +
                        "libraries/com/paulscode/codecjorbis/20101023/codecjorbis-20101023.jar:" + path +
                        "libraries/com/paulscode/codecwav/20101023/codecwav-20101023.jar:" + path +
                        "libraries/com/paulscode/libraryjavasound/20101123/libraryjavasound-20101123.jar:" + path +
                        "libraries/com/paulscode/librarylwjglopenal/20100824/librarylwjglopenal-20100824.jar:" + path +
                        "libraries/com/paulscode/soundsystem/20120107/soundsystem-20120107.jar:" + path +
                        "libraries/io/netty/netty-all/4.0.23.Final/netty-all-4.0.23.Final.jar:" + path +
                        "libraries/com/google/guava/guava/17.0/guava-17.0.jar:" + path +
                        "libraries/org/apache/commons/commons-lang3/3.3.2/commons-lang3-3.3.2.jar:" + path +
                        "libraries/commons-io/commons-io/2.4/commons-io-2.4.jar:" + path +
                        "libraries/commons-codec/commons-codec/1.9/commons-codec-1.9.jar:" + path +
                        "libraries/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar:" + path +
                        "libraries/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar:" + path +
                        "libraries/com/google/code/gson/gson/2.2.4/gson-2.2.4.jar:" + path +
                        "libraries/com/mojang/authlib/1.5.22/authlib-1.5.22.jar:" + path + "libraries/com/mojang/realms/1.9.8/realms-1.9.8.jar:" +
                        path + "libraries/org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar:" + path +
                        "libraries/org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar:" + path +
                        "libraries/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar:" + path +
                        "libraries/org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar:" + path +
                        "libraries/it/unimi/dsi/fastutil/7.0.12_mojang/fastutil-7.0.12_mojang.jar:" + path +
                        "libraries/org/apache/logging/log4j/log4j-api/2.0-beta9/log4j-api-2.0-beta9.jar:" + path +
                        "libraries/org/apache/logging/log4j/log4j-core/2.0-beta9/log4j-core-2.0-beta9.jar:" + path +
                        "libraries/org/lwjgl/lwjgl/lwjgl/2.9.4-nightly-20150209/lwjgl-2.9.4-nightly-20150209.jar:" + path +
                        "libraries/org/lwjgl/lwjgl/lwjgl_util/2.9.4-nightly-20150209/lwjgl_util-2.9.4-nightly-20150209.jar:" + path +
                        "versions/1.10.2/1.10.2.jar");
                cmd.add("net.minecraft.launchwrapper.Launch");
                cmd.add("--username");
                cmd.add(username.getText());
                cmd.add("--version");
                cmd.add("1.10.2-forge1.10.2-12.18.3.2185");
                cmd.add("--gameDir");
                cmd.add(path.substring(0, path.length()-1));
                cmd.add("--assetsDir");
                cmd.add(path + "assets");
                cmd.add("--assetIndex");
                cmd.add("1.10");
                cmd.add("--uuid");
                cmd.add("3fcd6d05a5ac473bb245776a5e89b40b");
                cmd.add("--accessToken");
                cmd.add("029a7354fcb142e08d2b7c3023c71260");
                cmd.add("--userType");
                cmd.add("legacy");
                cmd.add("--tweakClass");
                cmd.add("net.minecraftforge.fml.common.launcher.FMLTweaker");
                cmd.add("--versionType");
                cmd.add("Forge");

                ProcessBuilder pb = new ProcessBuilder(cmd);
                pb.redirectErrorStream(true);
                pb.redirectOutput(new File("output.log"));
                Process p = pb.start();

                // wait for Minecraft to be closed to enable textiputs and buttons
                //while (p.isAlive()) {}

                this.javaArguments.setDisable(false);
                this.username.setDisable(false);
                this.playButton.setDisable(false);
            } catch (Exception e) {
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

    private void updateModsDirectory(File newPath) {
        this.modsPath = newPath;
        this.modsDirectory.setText(this.modsPath.getAbsolutePath());
    }

    private void setMessage(String msg) {
        this.message.setText(msg);
    }

    private File getDefaultModsDirectory() {
        String modsDir =  ".minecraft" + File.separator + "mods";
        String userHomeDir = System.getProperty("user.home", ".");
        String osType = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        File targetDir;
        if (osType.contains("win") && System.getenv("APPDATA") != null)
            targetDir = new File(System.getenv("APPDATA"), modsDir);
        else if (osType.contains("mac"))
            targetDir = Paths.get(userHomeDir, "Library","Application Support", "minecraft" + File.separator + "mods").toFile();
        else
            targetDir = new File(userHomeDir, modsDir);
        return targetDir;
    }
}
