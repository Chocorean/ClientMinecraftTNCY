package io.chocorean.minecraft.updater;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Configuration of the app.
 * THe configuration file is available in /resources/updater.properties.
 *
 * @author mcdostone
 */
public class Configuration {

    private static final String UPDATER_CONFIG_FILE = "/updater.properties";
    private static Configuration config;
    private final URL forge;
    private final URL mods;
    private final URL changelog;
    private final URL styles;
    private final String version;
    private final String profile;
    private final String forgeVersion;

    private Configuration(Map<String, String> entries) throws MalformedURLException {
        String root = entries.get("root");
        this.mods = new URL(root + "/" + entries.get("mods"));
        this.changelog = new URL(root + "/" + entries.get("changelog"));
        this.styles = new URL(root + "/" + entries.get("styles"));
        this.forge = new URL(entries.get("forge"));
        this.version = entries.get("version");
        this.profile = entries.get("profile");
        this.forgeVersion = entries.get("forgeVersion");
    }

    public URL getChangelogUrl() {
        return this.changelog;
    }

    public URL getModsUrl() {
        return this.mods;
    }

    public URL getStylesUrl() {
        return this.styles;
    }

    public String getProfile() {
        return this.profile;
    }

    private static Configuration loadFromFileProperties() {
        Properties prop = new Properties();
        InputStream input;
        try {
            input = Configuration.class.getResourceAsStream(UPDATER_CONFIG_FILE);
            prop.load(input);
            Map<String, String> properties = new HashMap<>();
            properties.put("root", prop.getProperty("ROOT_URL"));
            properties.put("changelog", prop.getProperty("CHANGELOG_FILE"));
            properties.put("mods", prop.getProperty("MODS_FILE"));
            properties.put("styles", prop.getProperty("CSS_FILE"));
            properties.put("version", prop.getProperty("VERSION"));
            properties.put("forge", prop.getProperty("FORGE_URL"));
            properties.put("profile", prop.getProperty("PROFILE"));
            properties.put("forgeVersion", prop.getProperty("FORGE_VERSION"));
            return new Configuration(properties);
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "mods='" + this.mods + '\'' +
                ", changelog='" + this.changelog + '\'' +
                ", styles='" + this.styles + '\'' +
                ", version='" + this.version + '\'' +
                ", profile='" + this.profile + '\'' +
                '}';
    }

    public static Configuration getInstance() {
        if(Configuration.config == null)
            Configuration.config = loadFromFileProperties();
        return Configuration.config;
    }

    public String getVersion() {
        return this.version;
    }

    public URL getForgeUrl() {
        return this.forge;
    }

    public String getForgeVersion() {
        return this.forgeVersion;
    }
}
