package io.chocorean.MinecraftUpdater;

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

    private static final int NB_THREADS = 10;


    public static List<File> update(File modsDirectory) {
        ExecutorService service = Executors.newFixedThreadPool(NB_THREADS);
        List<Callable<File>> tasks = new ArrayList<>();
        List<Future<File>> futureList = null;
        try {
            Configuration config = Configuration.getInstance();
            URL modsURL = new URL(config.getModsUrl());
            String modsFilename = new File(modsURL.getFile()).getName();

            ReadableByteChannel rbc = Channels.newChannel(modsURL.openStream());
            FileOutputStream mod_fos = new FileOutputStream(modsFilename);
            mod_fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            BufferedReader br = new BufferedReader(new FileReader(modsFilename));
            String line;
            while ((line = br.readLine()) != null)
                tasks.add(prepareInstallModTask(new URL(line.trim()), modsDirectory));

            futureList = service.invokeAll(tasks);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
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

    public static Callable<File> prepareInstallModTask(URL url, File destination) {
        return () -> {
            File absolutefilePathMod = Paths.get(destination.getAbsolutePath(), new File(url.getFile()).getName()).toFile();
            if(!absolutefilePathMod.exists()) {
                ReadableByteChannel rbc;
                try {
                    rbc = Channels.newChannel(url.openStream());
                    FileOutputStream fos = new FileOutputStream(absolutefilePathMod);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return absolutefilePathMod;
        };
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

