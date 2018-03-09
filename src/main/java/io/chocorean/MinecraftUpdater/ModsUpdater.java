package io.chocorean.MinecraftUpdater;

import javafx.scene.control.ProgressBar;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ModsUpdater {

    private static final int NB_THREADS = 5;


    public static List<File> update(File modsDirectory, ProgressBar progression) {
        progression.setProgress(0);
        ExecutorService service = Executors.newFixedThreadPool(NB_THREADS);
        List<Callable<File>> tasks = new ArrayList<>();
        List<Future<File>> futureList = null;
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

            tasks.addAll(prepareInstallModTask(urls, modsDirectory, progression));
            futureList = service.invokeAll(tasks);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        progression.setProgress(100);
        if(futureList != null)
            return futureList.stream().map(f -> {
                try {
                    return f.get();
                } catch (InterruptedException | ExecutionException e) { e.printStackTrace(); }
                return null;
            }).collect(Collectors.toList());
        else
            return null;
    }

    public static List<Callable<File>> prepareInstallModTask(List<URL> urls, File destination, ProgressBar progress) {
        double increment = 100 / urls.size();
        return urls.stream().map(my -> (Callable<File>) () -> {
            File absolutefilePathMod = Paths.get(destination.getAbsolutePath(), new File(my.getFile()).getName()).toFile();
            if (!absolutefilePathMod.exists()) {
                ReadableByteChannel rbc;
                try {
                    rbc = Channels.newChannel(my.openStream());
                    FileOutputStream fos = new FileOutputStream(absolutefilePathMod);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    progress.setProgress(progress.getProgress() + 0.1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return absolutefilePathMod;
        }).collect(Collectors.toList());
    }

        public static List<File> getUnusedMods(File modsDirectory, List<File> installed) {
            return Arrays.stream(modsDirectory.listFiles())
                    .filter(f -> !installed.contains(f) && f.getName().endsWith(".jar"))
                    .collect(Collectors.toList());
        }

        public static void deleteFile(List<File> toDelete) {
            for(File m: toDelete)
                m.delete();

        }
    }

