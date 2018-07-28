package io.chocorean.minecraft.updater;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

    private Configuration(String root, String mods, String changelog, String styles, String version, String forge, String profile, String forgeVersion) throws MalformedURLException {
        this.mods = new URL(root + "/" + mods);
        this.changelog = new URL(root + "/" + changelog);
        this.styles = new URL(root + "/" + styles);
        this.forge = new URL(forge);
        this.version = version;
        this.profile = profile;
        this.forgeVersion = forgeVersion;
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
            return new Configuration(
                    prop.getProperty("ROOT_URL"),
                    prop.getProperty("MODS_FILE"),
                    prop.getProperty("CHANGELOG_FILE"),
                    prop.getProperty("CSS_FILE"),
                    prop.getProperty("VERSION"),
                    prop.getProperty("FORGE_URL"),
                    prop.getProperty("PROFILE"),
                    prop.getProperty("FORGE_VERSION")
            );
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
