package io.chocorean.MinecraftUpdater;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ForgeInstaller {

    private final AppController controller;

    public ForgeInstaller(AppController ctrl) {
        this.controller = ctrl;
    }

    public static void install(URL forgeURL, ProgressBar progress, Runnable callback) {
        progress.setProgress(0);
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(() -> {
            File forgeFile = Paths.get(System.getProperty("java.io.tmpdir"), new File(forgeURL.toString()).getName()).toFile();
            try {
                CallbackByteChannel rbc = new CallbackByteChannel(Channels.newChannel(forgeURL.openStream()), contentLength(forgeURL), progress);
                FileOutputStream mod_fos;
                mod_fos = new FileOutputStream(forgeFile);
                progress.setProgress(0.1);
                mod_fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                progress.setProgress(1);
                Process p = Runtime.getRuntime().exec("java -jar " + forgeFile);
                p.waitFor();
                if(p.exitValue() == 0)
                    Platform.runLater(callback);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


    private static int contentLength(URL url) {
        HttpURLConnection connection;
        int contentLength = -1;
        try {
            connection = (HttpURLConnection) url.openConnection();
            contentLength = connection.getContentLength();
        } catch (Exception e) {
        }
        return contentLength;
    }

    static class CallbackByteChannel implements ReadableByteChannel {
        private final ProgressBar progression;
        long size;
        ReadableByteChannel rbc;
        long sizeRead;

        CallbackByteChannel(ReadableByteChannel rbc, long expectedSize, ProgressBar bar) {
            this.size = expectedSize;
            this.rbc = rbc;
            this.progression = bar;
        }

        public void close() throws IOException {
            rbc.close();
        }

        public long getReadSoFar() {
            return sizeRead;
        }

        public boolean isOpen() {
            return rbc.isOpen();
        }

        @Override
        public int read(ByteBuffer bb) throws IOException {
            int n;
            double progress;
            if ((n = rbc.read(bb)) > 0) {
                sizeRead += n;
                progress = size > 0 ? (double) sizeRead / (double) size : -1.0;
                this.progression.setProgress(progress);
            }
            return n;
        }
    }

}
