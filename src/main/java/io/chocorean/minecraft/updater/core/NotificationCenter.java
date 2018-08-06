package io.chocorean.minecraft.updater.core;

import io.chocorean.minecraft.updater.core.notifications.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationCenter {

    private List<Notification> notificationSources;

    public NotificationCenter() {
        this.notificationSources = new ArrayList<>();
    }

    public void register(Notification n) {
        this.notificationSources.add(n);
    }

    public List<Notification> collectNotifications() {
        return this.notificationSources.stream().filter(Notification::hasNotification)
                .sorted((o1, o2) -> o1.getPriority() > o2.getPriority() ? 1 : -1).collect(Collectors.toList());
    }

}
