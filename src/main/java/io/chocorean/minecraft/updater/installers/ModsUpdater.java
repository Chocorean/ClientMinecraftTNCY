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
import java.util.stream.Collectors;

public class ModsUpdater implements Installer<List<Future<File>>> {

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
        try {
            Configuration config = Configuration.getInstance();
            URL modsURL = config.getModsUrl();
            File modsFilename = Paths.get(System.getProperty("java.io.tmpdir"), new File(modsURL.toString()).getName()).toFile();

            ReadableByteChannel rbc = Channels.newChannel(modsURL.openStream());
            FileOutputStream mod_fos = new FileOutputStream(modsFilename);
            mod_fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            BufferedReader br = new BufferedReader(new FileReader(modsFilename));
            String line;
            List<URL> urls = new ArrayList<>();
            while ((line = br.readLine()) != null)
                urls.add(new URL(line.trim()));
            tasks.addAll(this.prepareInstallModTask(urls, this.modsDirectory, this.progressBar));
            return service.invokeAll(tasks);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
        /*if(futureList != null)
            return futureList.stream().map(f -> {
                try {
                    return f.get();
                } catch (InterruptedException | ExecutionException e) { e.printStackTrace(); }
                return null;
            }).collect(Collectors.toList());
        else
            return null;
            */
    }

    private List<Callable<File>> prepareInstallModTask(List<URL> urls, File destination, ProgressBar progress) {
        double increment = 1.0 / urls.size();
        return urls.stream().map(my -> (Callable<File>) () -> {
            File absolutefilePathMod = Paths.get(destination.getAbsolutePath(), new File(my.getFile()).getName()).toFile();
            if (!absolutefilePathMod.exists()) {
                ReadableByteChannel rbc;
                try {
                    rbc = Channels.newChannel(my.openStream());
                    FileOutputStream fos = new FileOutputStream(absolutefilePathMod);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    progress.setProgress(progress.getProgress() + increment);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return absolutefilePathMod;
        }).collect(Collectors.toList());
    }

}

