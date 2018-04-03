package io.chocorean.minecraft.updater.controllers;

import io.chocorean.minecraft.updater.Configuration;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;

/**
 * Controller of the changelog webview
 *
 * @author mcdostone
 */
public class ChangelogController {

    @FXML private WebView changelog;

    @FXML
    private void initialize() {
        Configuration conf = Configuration.getInstance();
        try {
            File changelog = this.download(conf.getChangelogUrl());
            this.download(conf.getStylesUrl());
            this.changelog.getEngine().load(changelog.toURI().toURL().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File download(URL url) throws Exception {
        File file = Paths.get(System.getProperty("java.io.tmpdir"), new File(url.toString()).getName()).toFile();
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        return file;
    }


}