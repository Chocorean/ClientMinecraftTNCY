package io.chocorean.minecraft.updater.core.notifications;

import javafx.event.EventHandler;

public class MinecraftNotFoundNotification implements Notification {

    @Override
    public String getMessage() {
        return "It seems like the directory doesn't contain minecraft data. Please specify the game directory";
    }

    @Override
    public boolean hasNotification() {
        return true;
    }

    @Override
    public EventHandler getEvent() {
        return event -> { };
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
