package io.chocorean.minecraft.updater.controllers;

import com.google.gson.Gson;
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

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
                // path to .minecraft
                String path = this.getDefaultModsDirectory().getAbsolutePath().substring(0, (this.getDefaultModsDirectory().getAbsolutePath().length()-4));

                // creating folder if it doesnt exist
                File versionFolder = new File(path + "versions/1.10.2-TNCY");
                if (!versionFolder.exists()) {
                    if (!versionFolder.mkdir()) {
                        this.message.setText("Unable to create a directory '1.10.2-TNCY' at path "+ path +"versions/");

                        this.javaArguments.setDisable(false);
                        this.username.setDisable(false);
                        this.playButton.setDisable(false);

                        return;
                    }
                }

                // defining the content of jsonFile
                String content = "{\n" +
                        "  \"inheritsFrom\": \"1.10.2\",\n" +
                        "  \"id\": \"1.10.2-TNCY\",\n" +
                        "  \"time\": \"2018-07-25T18:09:00+01:00\",\n" +
                        "  \"releaseTime\": \"1960-01-01T08:00:00+01:00\",\n" +
                        "  \"type\": \"release\",\n" +
                        "  \"minecraftArguments\": \"--username " + this.username.getText() + " --version ${version_name} --gameDir ${game_directory} --assetsDir ${assets_root} --assetIndex ${assets_index_name} --uuid ${auth_uuid} --accessToken ${auth_access_token} --userType ${user_type} --tweakClass net.minecraftforge.fml.common.launcher.FMLTweaker --versionType Forge\",\n" +
                        "  \"libraries\": [\n" +
                        "    {\n" +
                        "      \"name\": \"net.minecraftforge:forge:1.10.2-12.18.3.2185\",\n" +
                        "      \"url\": \"http://files.minecraftforge.net/maven/\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"net.minecraft:launchwrapper:1.12\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"org.ow2.asm:asm-all:5.0.3\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"jline:jline:2.13\",\n" +
                        "      \"url\": \"http://files.minecraftforge.net/maven/\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"com.typesafe.akka:akka-actor_2.11:2.3.3\",\n" +
                        "      \"url\": \"http://files.minecraftforge.net/maven/\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"com.typesafe:config:1.2.1\",\n" +
                        "      \"url\": \"http://files.minecraftforge.net/maven/\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"org.scala-lang:scala-actors-migration_2.11:1.1.0\",\n" +
                        "      \"url\": \"http://files.minecraftforge.net/maven/\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"org.scala-lang:scala-compiler:2.11.1\",\n" +
                        "      \"url\": \"http://files.minecraftforge.net/maven/\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"org.scala-lang.plugins:scala-continuations-library_2.11:1.0.2\",\n" +
                        "      \"url\": \"http://files.minecraftforge.net/maven/\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"org.scala-lang.plugins:scala-continuations-plugin_2.11.1:1.0.2\",\n" +
                        "      \"url\": \"http://files.minecraftforge.net/maven/\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"org.scala-lang:scala-library:2.11.1\",\n" +
                        "      \"url\": \"http://files.minecraftforge.net/maven/\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"org.scala-lang:scala-parser-combinators_2.11:1.0.1\",\n" +
                        "      \"url\": \"http://files.minecraftforge.net/maven/\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"org.scala-lang:scala-reflect:2.11.1\",\n" +
                        "      \"url\": \"http://files.minecraftforge.net/maven/\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"org.scala-lang:scala-swing_2.11:1.0.1\",\n" +
                        "      \"url\": \"http://files.minecraftforge.net/maven/\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"org.scala-lang:scala-xml_2.11:1.0.2\",\n" +
                        "      \"url\": \"http://files.minecraftforge.net/maven/\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"lzma:lzma:0.0.1\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"net.sf.jopt-simple:jopt-simple:4.6\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"java3d:vecmath:1.5.2\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"net.sf.trove4j:trove4j:3.0.3\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"net.minecraftforge:MercuriusUpdater:1.10.2\",\n" +
                        "      \"url\": \"http://files.minecraftforge.net/maven/\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"mainClass\": \"net.minecraft.launchwrapper.Launch\",\n" +
                        "  \"minimumLauncherVersion\": 0,\n" +
                        "  \"jar\": \"1.10.2\",\n" +
                        "  \"downloads\": {}\n" +
                        "}";

                // creating json file if it doesnt exist
                File jsonFile = new File(path + "versions/1.10.2-TNCY/1.10.2-TNCY.json");
                if (!jsonFile.exists()) {
                    if (!jsonFile.createNewFile()) {

                        this.message.setText("Unable to create a file '1.10.2-TNCY.json' at path "+ path +"versions/1.10.2-TNCY/");

                        this.javaArguments.setDisable(false);
                        this.username.setDisable(false);
                        this.playButton.setDisable(false);

                        return;
                    }
                    BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFile));

                    writer.write(content);
                    writer.close();
                } else {
                    // extract jsonFile's content
                    FileInputStream fis = new FileInputStream(jsonFile);
                    byte[] data = new byte[(int) jsonFile.length()];
                    fis.read(data);
                    fis.close();
                    String jsonContent = new String(data, "UTF-8");
                    // if jsonFile exists, replace username in field "minecraftArguments"
                    Gson gson = new Gson();
                    Map<String, String> jsonMap = gson.fromJson(jsonContent, Map.class);
                    
                    if (!this.username.getText().equals(jsonMap.get("minecraftArguments").split(" ")[1])) {
                        jsonMap.remove("minecraftArguments");
                        jsonMap.put("minecraftArguments", "--username " + this.username.getText() + " --version ${version_name} --gameDir ${game_directory} --assetsDir ${assets_root} --assetIndex ${assets_index_name} --uuid ${auth_uuid} --accessToken ${auth_access_token} --userType ${user_type} --tweakClass net.minecraftforge.fml.common.launcher.FMLTweaker --versionType Forge");

                        // saved jsonFile
                        FileWriter writer = new FileWriter(jsonFile.getAbsolutePath());
                        writer.write(gson.toJson(jsonMap));
                        writer.close();
                    }
                }


                // launch minecraft launcher
                ArrayList<String> cmd = new ArrayList<>();
                cmd.add("java");
                cmd.add("-jar");
                cmd.add(path + "/launcher.jar");
                ProcessBuilder pb = new ProcessBuilder(cmd);
                pb.redirectErrorStream(true);
                pb.redirectOutput(new File("output.log"));
                Process p = pb.start();

                // wait for Minecraft to be closed to enable textfields and buttons
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
