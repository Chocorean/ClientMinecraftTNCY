package io.chocorean.minecraft.updater;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Configuration of the app.
 * THe configuration file is available in /resources/updater.properties.
 *
 * @author mcdostone
 */
public class Configuration {

    private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());
    private static final String UPDATER_CONFIG_FILE = "/updater.properties";
    private static Configuration config;
    private final Map<String, String> properties;


    private Configuration(Map<String, String> properties) {
        this.properties = properties;
    }

    public URL getChangelogUrl() {
        try {
            return new URL(this.properties.get("root") + "/" +  this.properties.get("changelog"));
        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
        return null;
    }

    public URL getModsUrl() {
        try {
            return new URL(this.properties.get("root") + "/mods.txt");
        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
        return null;
    }

    public URL getStylesUrl() {
        try {
            return new URL(this.properties.get("root") + "/" + properties.get("styles"));
        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
        return null;
    }

    public String getProfile() {
        return this.properties.get("profile");
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
            properties.put("versionUrl", prop.getProperty("VERSION_URL"));
            properties.put("forgeVersion", prop.getProperty("FORGE_VERSION"));
            properties.put("githubUrl", prop.getProperty("GITHUB_URL"));
            return new Configuration(properties);
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "mods='" + this.getModsUrl() + '\'' +
                ", changelog='" + this.getChangelogUrl() + '\'' +
                ", styles='" + this.getForgeUrl() + '\'' +
                ", version='" + this.getVersion() + '\'' +
                ", profile='" + this.getProfile() + '\'' +
                ", github='" + this.getGithubUrl() + '\'' +
                '}';
    }

    public static Configuration getInstance() {
        if(Configuration.config == null)
            Configuration.config = loadFromFileProperties();
        return Configuration.config;
    }

    public String getVersion() {
        return this.properties.get("version");
    }

    public URL getForgeUrl() {
        try {
            return new URL(this.properties.get("forge"));
        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
        return null;
    }

    public String getForgeVersion() {
        return this.properties.get("forgeVersion");
    }

    public String getGithubUrl() {
        return this.properties.get("githubUrl");
    }

    public String getVersionUrl() {
        return this.properties.get("versionUrl");
    }
}
