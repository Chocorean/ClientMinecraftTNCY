package io.chocorean.minecraft.updater.installers;

import io.chocorean.minecraft.updater.core.CallbackByteChannel;
import io.chocorean.minecraft.updater.core.Installer;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Class downloading and running forge installer.
 *
 * @author mcdostone
 */
public class ForgeInstaller implements Installer<Future<?>> {

    private final URL url;
    private final ProgressBar progressBar;
    private final ExecutorService service;
    private final Runnable cb;

    public ForgeInstaller(URL forgeURL, ProgressBar progressBar, Runnable cb) {
        this.url = forgeURL;
        this.progressBar = progressBar;
        this.service = Executors.newFixedThreadPool(1);
        this.cb = cb;
    }

    @Override
    public Future<Integer> install() {
        this.progressBar.setProgress(0);
        return this.service.submit(() -> {
            File forgeFile = Paths.get(System.getProperty("java.io.tmpdir"), new File(this.url.toString()).getName()).toFile();
            try {
                ReadableByteChannel rbc = new CallbackByteChannel(
                        Channels.newChannel(this.url.openStream()),
                        this.contentLength(this.url),
                        this.progressBar
                );
                FileOutputStream fos = new FileOutputStream(forgeFile);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                this.progressBar.setProgress(1);
                Process p = Runtime.getRuntime().exec("java -jar " + forgeFile);
                p.waitFor();
                if(p.exitValue() == 0)
                    Platform.runLater(this.cb);
                return p.exitValue();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            return -1;
        });
    }

    /**
     * @param url URL to download
     * @return The content length of a file
     */
    private int contentLength(URL url) {
        HttpURLConnection connection;
        int contentLength = -1;
        try {
            connection = (HttpURLConnection) url.openConnection();
            contentLength = connection.getContentLength();
        } catch (Exception e) { e.printStackTrace(); }
        return contentLength;
    }

}
