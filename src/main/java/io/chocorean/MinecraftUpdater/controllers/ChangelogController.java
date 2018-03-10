package io.chocorean.MinecraftUpdater.controllers;

import io.chocorean.MinecraftUpdater.Configuration;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
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
        File changelog = this.download(conf.getChangelogUrl());
        this.download(conf.getStylesUrl());
        try {
            this.changelog.getEngine().load(changelog.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private File download(URL url) {
        File file = Paths.get(System.getProperty("java.io.tmpdir"), new File(url.toString()).getName()).toFile();
        try {
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
