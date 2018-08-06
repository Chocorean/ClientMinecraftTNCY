package io.chocorean.minecraft.updater.core.notifications;

import javafx.event.EventHandler;

public interface Notification {

    String getMessage();
    boolean hasNotification();
    EventHandler getEvent();
    int getPriority();

}
