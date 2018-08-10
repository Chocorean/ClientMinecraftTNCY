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
 * The configuration file is available in /resources/updater.properties.
 *
 * @author mcdostone
 */
public class Configuration {

    private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());
    private static final String UPDATER_CONFIG_FILE = "/updater.properties";
    private static Configuration config;
    private final Map<String, String> properties;

    public Configuration(Map<String, String> properties) {
        this.properties = properties;
    }

    public URL getChangelogUrl() {
        return this.getURL(this.properties.get("root") + "/" +  this.properties.get("changelog"));
    }

    public URL getModsUrl() {
        return this.getURL(this.properties.get("root") + "/mods.txt");
    }

    public URL getStylesUrl() {
        return this.getURL(this.properties.get("root") + "/" + properties.get("styles"));
    }

    public String getProfile() {
        return this.properties.get("profile");
    }

    public String getVersion() {
        return this.properties.get("version");
    }

    public URL getForgeUrl() {
        return this.getURL(this.properties.get("forge"));
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

    private URL getURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) { LOGGER.log(Level.SEVERE, "", e); }
        return null;
    }

    @Override
    public String toString() {
        String s = "Configuration {";
        for(Map.Entry<String, String> entry: this.properties.entrySet()) {
            s = String.format("%s\n\t%s = '%s',", s, entry.getKey(), entry.getValue());
        }
        return s + "\n}";
    }

    private static Configuration loadFromProperties(Properties props) {
        Map<String, String> properties = new HashMap<>();
        properties.put("root", props.getProperty("ROOT_URL"));
        properties.put("changelog", props.getProperty("CHANGELOG_FILE"));
        properties.put("mods", props.getProperty("MODS_FILE"));
        properties.put("styles", props.getProperty("CSS_FILE"));
        properties.put("version", props.getProperty("VERSION"));
        properties.put("forge", props.getProperty("FORGE_URL"));
        properties.put("profile", props.getProperty("PROFILE"));
        properties.put("versionUrl", props.getProperty("VERSION_URL"));
        properties.put("forgeVersion", props.getProperty("FORGE_VERSION"));
        properties.put("githubUrl", props.getProperty("GITHUB_URL"));
        return new Configuration(properties);
    }

    public static Configuration getInstance() {
        return Configuration.getInstance(Configuration.class.getResourceAsStream(UPDATER_CONFIG_FILE));
    }

    public static Configuration getInstance(InputStream input) {
        if (Configuration.config == null) {
            try {
                Properties props = new Properties();
                props.load(input);
                Configuration.config = loadFromProperties(props);
            } catch (IOException e) { LOGGER.log(Level.SEVERE, "", e); }
        }
        return Configuration.config;
    }

}
