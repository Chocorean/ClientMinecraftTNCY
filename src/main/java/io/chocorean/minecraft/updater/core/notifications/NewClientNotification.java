package io.chocorean.minecraft.updater.core.notifications;

import com.sun.javafx.application.HostServicesDelegate;
import javafx.event.EventHandler;

public class NewClientNotification implements Notification {

    private final HostServicesDelegate h;
    private final String url;

    public NewClientNotification(HostServicesDelegate h, String url) {
        this.h = h;
        this.url = url;
    }

    @Override
    public String getMessage() {
        return "A new version of the client is available";
    }

    // TODO
    @Override
    public boolean hasNotification() {
        return false;
    }

    @Override
    public EventHandler getEvent() {
        return event -> this.h.showDocument(this.url);
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
