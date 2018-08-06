package io.chocorean.minecraft.updater.controllers;

import io.chocorean.minecraft.updater.Configuration;
import io.chocorean.minecraft.updater.MinecraftUtils;
import io.chocorean.minecraft.updater.core.NotificationCenter;
import io.chocorean.minecraft.updater.core.notifications.NewClientNotification;
import io.chocorean.minecraft.updater.core.notifications.NewModsNotification;
import io.chocorean.minecraft.updater.core.notifications.Notification;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationController {

    @FXML private Label closeNotification;
    @FXML private AnchorPane notificationMenu;
    @FXML private Hyperlink notificationMessage;
    private boolean displayed;
    private Configuration config;
    private NotificationCenter notificationCenter;

    public NotificationController() {
        this.displayed = false;
        this.config = Configuration.getInstance();
        this.notificationCenter = new NotificationCenter();
        this.notificationCenter.register(new NewClientNotification(config.getVersionUrl(), config.getVersion()));
        this.notificationCenter.register(new NewModsNotification(MinecraftUtils.getDefaultModsDirectory().getAbsolutePath()));
    }

    @FXML
    private void initialize() {
        this.displayed = false;
        this.notificationMenu.setCache(true);
        this.notificationMenu.setCacheHint(CacheHint.SPEED);
        this.notificationMenu.setTranslateY(this.notificationMenu.getPrefHeight() + 10);
        this.closeNotification.setOnMouseClicked(event -> {
            if(this.displayed) {
                TranslateTransition slideOut = new TranslateTransition(Duration.millis(333), this.notificationMenu);
                slideOut.setInterpolator(Interpolator.EASE_IN);
                slideOut.setByY(notificationMenu.getPrefHeight() + 10);
                slideOut.play();
                this.displayed = false;
            }
        });
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(() -> Platform.runLater(() -> displayNotifications(notificationCenter.collectNotifications())), 3, TimeUnit.SECONDS);
        this.notificationMessage.setVisited(false);
    }

    private void displayNotification(Notification n) {
        this.notificationMessage.setText(n.getMessage());
        this.notificationMessage.setOnMouseClicked(n.getEvent());
        if(!this.displayed) {
            TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), notificationMenu);
            slideIn.setInterpolator(Interpolator.EASE_BOTH);
            slideIn.setByY(-(notificationMenu.getPrefHeight() + 20));
            slideIn.play();
            displayed = true;
        }
    }

    private void displayNotifications(List<Notification> notifs) {
        if(!notifs.isEmpty())
            this.displayNotification(notifs.get(0));
    }

}
