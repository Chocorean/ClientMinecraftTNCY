package io.chocorean.MinecraftUpdater;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

    private static final String UPDATER_CONFIG_FILE = "/updater.properties";
    private String mods;
    private String changelog;
    private String styles;

    private Configuration(String root, String mods, String changelog, String styles) {
        this.mods = root + "/" + mods;
        this.changelog = root + "/" + changelog;
        this.styles = root + "/" + styles;
    }

    public String getChangelogUrl() {
        return this.changelog;
    }

    public String getModsUrl() {
        return this.mods;
    }

    public String getStylesUrl() {
        return this.styles;
    }

    public static Configuration loadFromFileProperties() {
        Properties prop = new Properties();
        InputStream input;
        try {
            input = Configuration.class.getResourceAsStream(UPDATER_CONFIG_FILE);
            prop.load(input);
            return new Configuration(
                    prop.getProperty("ROOT_URL"),
                    prop.getProperty("MODS_FILE"),
                    prop.getProperty("CHANGELOG_FILE"),
                    prop.getProperty("CSS_FILE")
            );
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "mods='" + mods + '\'' +
                ", changelog='" + changelog + '\'' +
                ", styles='" + styles + '\'' +
                '}';
    }
}
