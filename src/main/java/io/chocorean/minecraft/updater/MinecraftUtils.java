package io.chocorean.minecraft.updater;

import java.io.File;
import java.nio.file.Paths;
import java.util.Locale;

public class MinecraftUtils {

    private MinecraftUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static File getDefaultMinecraftDirectory() {
        String minecraftDir = ".minecraft";
        String userHomeDir = System.getProperty("user.home", ".");
        String osType = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        File targetDir;
        if (osType.contains("win") && System.getenv("APPDATA") != null)
            targetDir = new File(System.getenv("APPDATA"), minecraftDir);
        else if (osType.contains("mac"))
            targetDir = Paths.get(userHomeDir, "Library","Application Support", "minecraft").toFile();
        else
            targetDir = new File(userHomeDir, minecraftDir);
        return targetDir;
    }

    public static File getDefaultModsDirectory() {
        return Paths.get(getDefaultMinecraftDirectory().getAbsolutePath(), "mods").toFile();
    }

}
