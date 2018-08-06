package io.chocorean.minecraft.updater.core.notifications;

import com.sun.javafx.application.HostServicesDelegate;
import javafx.event.EventHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewClientNotification implements Notification {

    private final HostServicesDelegate h;
    private final String urlVersion;
    private final String actualVersion;

    public NewClientNotification(HostServicesDelegate h, String urlVersion, String actualVersion) {
        this.h = h;
        this.urlVersion = urlVersion;
        this.actualVersion = actualVersion;
    }

    @Override
    public String getMessage() {
        return "A new version of the client is available";
    }

    // TODO
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
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public EventHandler getEvent() {
        return event -> this.h.showDocument(this.urlVersion);
    }

    @Override
    public int getPriority() {
        return 1;
    }

}
