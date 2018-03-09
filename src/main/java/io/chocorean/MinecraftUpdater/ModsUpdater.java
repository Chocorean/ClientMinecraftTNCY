package io.chocorean.MinecraftUpdater;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModsUpdater {


    public static List<File> update(String modsDirectory) {
        List<File> installedMods = new ArrayList<>();
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
                installedMods.add(ModsUpdater.installMod(new URL(line.trim()), modsDirectory));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return installedMods;
    }

    public static File installMod(URL url, String destination) {
        File absolutefilePathMod = Paths.get(destination, new File(url.getFile()).getName()).toFile();
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
    }

    public static List<File> getUnusedMods(String modsDirectory, List<File> installed) {
        File folder = new File(modsDirectory);
        return Arrays.stream(folder.listFiles())
                .filter(f -> !installed.contains(f))
                .collect(Collectors.toList());
    }

    public static void deleteFile(List<File> toDelete) {

    }
}

