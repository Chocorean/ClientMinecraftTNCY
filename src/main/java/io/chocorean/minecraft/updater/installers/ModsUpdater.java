package io.chocorean.minecraft.updater.installers;

import io.chocorean.minecraft.updater.Configuration;
import io.chocorean.minecraft.updater.core.Installer;
import javafx.scene.control.ProgressBar;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ModsUpdater implements Installer<List<Future<File>>> {

    private static final Logger LOGGER = Logger.getLogger(ModsUpdater.class.getName());
    private static final int NB_THREADS = 10;
    private final ProgressBar progressBar;
    private final File modsDirectory;

    public ModsUpdater(File modsDirectory, ProgressBar progressBar) {
        this.modsDirectory = modsDirectory;
        this.progressBar = progressBar;
    }

    public List<File> getUnusedMods(File modsDirectory, List<File> installed) {
        return Arrays.stream(Objects.requireNonNull(modsDirectory.listFiles()))
                .filter(f -> !installed.contains(f) && f.getName().endsWith(".jar"))
                .collect(Collectors.toList());
    }

    @Override
    public List<Future<File>> install() {
        this.progressBar.setProgress(0);
        ExecutorService service = Executors.newFixedThreadPool(NB_THREADS);
        List<Callable<File>> tasks = new ArrayList<>();
        Configuration config = Configuration.getInstance();
        URL modsURL = config.getModsUrl();
        File modsFilename = Paths.get(System.getProperty("java.io.tmpdir"), new File(modsURL.toString()).getName()).toFile();
        try(
                ReadableByteChannel rbc = Channels.newChannel(modsURL.openStream());
                FileOutputStream modFos = new FileOutputStream(modsFilename);
                BufferedReader br = new BufferedReader(new FileReader(modsFilename))
        ) {
            modFos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            String line;
            List<URL> urls = new ArrayList<>();
            while ((line = br.readLine()) != null)
                urls.add(new URL(line.trim()));
            tasks.addAll(this.prepareInstallModTask(urls, this.modsDirectory, this.progressBar));
            return service.invokeAll(tasks);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "", e);
            Thread.currentThread().interrupt();
        }
        return new ArrayList<>();
    }

    private List<Callable<File>> prepareInstallModTask(List<URL> urls, File destination, ProgressBar progress) {
        double increment = 1.0 / urls.size();
        destination.mkdirs();
        return urls.stream().map(my -> (Callable<File>) () -> {
            File absolutefilePathMod = Paths.get(destination.getAbsolutePath(), new File(my.getFile()).getName()).toFile();
            if (!absolutefilePathMod.exists()) {
                ReadableByteChannel rbc;
                try {
                    rbc = Channels.newChannel(my.openStream());
                    FileOutputStream fos = new FileOutputStream(absolutefilePathMod);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    fos.close();
                    rbc.close();
                    progress.setProgress(progress.getProgress() + increment);
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "", e);
                }
            }
            return absolutefilePathMod;
        }).collect(Collectors.toList());
    }

}

