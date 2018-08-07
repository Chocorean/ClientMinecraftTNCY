package io.chocorean.minecraft.updater.core.notifications;

import io.chocorean.minecraft.updater.Configuration;
import javafx.event.EventHandler;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class NewModsNotification implements Notification {

    private static final Logger LOGGER = Logger.getLogger(NewModsNotification.class.getName());
    private final String modsPath;
    private List<String> uninstalled;

    public NewModsNotification(String modsPath) {
        this.modsPath = modsPath;
        this.uninstalled = new ArrayList<>();
    }

    @Override
    public String getMessage() {
        if(this.uninstalled.size() == 1)
            return this.uninstalled.get(0) + " must be installed";
        return this.uninstalled.isEmpty() ? "Your setup is up-to-date" :
            String.format("%d mods must be installed", this.uninstalled.size());
    }

    @Override
    public boolean hasNotification() {
        try {
            List<String> mods = this.getLatestMods();
            this.uninstalled = mods.stream().filter(m -> !Paths.get(modsPath, m).toFile().exists()).collect(Collectors.toList());
            return !uninstalled.isEmpty();
        } catch (IOException e) { LOGGER.log(Level.SEVERE, "", e); }
        return false;
    }

    @Override
    public EventHandler getEvent() {
        return event -> { };
    }

    @Override
    public int getPriority() {
        return 0;
    }

    private List<String> getLatestMods() throws IOException {
        URL modsURL = Configuration.getInstance().getModsUrl();
        File modsFilename = Paths.get(System.getProperty("java.io.tmpdir"), new File(modsURL.toString()).getName()).toFile();
        try (FileOutputStream modFos = new FileOutputStream(modsFilename);
             ReadableByteChannel rbc = Channels.newChannel(modsURL.openStream());
             BufferedReader br = new BufferedReader(new FileReader(modsFilename))
        ) {
            modFos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            String line;
            List<String> mods = new ArrayList<>();
            while ((line = br.readLine()) != null)
                mods.add(new File(line).getName().trim());
            br.close();
            return mods;
        }
    }

}
