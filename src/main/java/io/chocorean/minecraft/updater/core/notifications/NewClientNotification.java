package io.chocorean.minecraft.updater.core.notifications;

import javafx.event.EventHandler;

import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewClientNotification implements Notification {

    private static final Logger LOGGER = Logger.getLogger(NewClientNotification.class.getName());
    private final String urlVersion;
    private final String actualVersion;

    public NewClientNotification(String urlVersion, String actualVersion) {
        this.urlVersion = urlVersion;
        this.actualVersion = actualVersion;
    }

    @Override
    public String getMessage() {
        return "A new version of the client is available";
    }

    @Override
    public boolean hasNotification() {
        try {
            URL url = new URL(this.urlVersion);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            con.getInputStream();
            String latestVersion = con.getURL().toString().split("/tag/")[1];
            return !latestVersion.equals(this.actualVersion);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
        return false;
    }

    @Override
    public EventHandler getEvent() {
        return event -> new Thread(() -> {
            try {
                Desktop.getDesktop().browse(new URI(this.urlVersion));
            } catch (IOException | URISyntaxException e) {
                LOGGER.log(Level.SEVERE, "", e);
            }
        }).start();
    }

    @Override
    public int getPriority() {
        return 1;
    }

}
