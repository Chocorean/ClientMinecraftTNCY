package io.chocorean.minecraft.updater.core;

import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class Version {

    private final static Logger LOGGER = Logger.getLogger(Version.class.getName());
    private final HashMap<String, String> properties;
    private final String username;
    private List<Library> libraries;

    public Version(String id, String username, String forgeVersion, List<Library> libraries) {
        this.properties = new HashMap<>();
        this.libraries = new ArrayList<>();
        this.properties.put("inheritsFrom", forgeVersion);
        this.properties.put("id", id);
        this.properties.put("type", "release");
        this.properties.put("time", "2016-12-05T23:29:04+0000");
        this.properties.put("releaseTime", "1960-01-01T00:00:00-0700");
        this.properties.put("mainClass", "net.minecraft.launchwrapper.Launch");
        this.properties.put("jar", forgeVersion);
        this.username = username;
        this.libraries = libraries;
    }

    private String generateMinecraftArguments() {
        return String.format(
                "--username %s " +
                "--version ${version_name} " +
                "--gameDir ${game_directory} " +
                "--assetsDir ${assets_root} " +
                "--assetIndex ${assets_index_name} " +
                "--uuid ${auth_uuid} " +
                "--accessToken ${auth_access_token} " +
                "--userType ${user_type} " +
                "--tweakClass net.minecraftforge.fml.common.launcher.FMLTweaker " +
                "--versionType Forge", this.username
        );
    }

    @Override
    public String toString() {
        JsonObject jsonObject = new JsonObject();
        for(Map.Entry<String, String> entry: this.properties.entrySet())
            jsonObject.add(entry.getKey(), new JsonPrimitive(entry.getValue()));
        JsonArray array = new JsonArray();
        for(Library l: this.libraries)
            array.add(new Gson().toJsonTree(l, Library.class));
        jsonObject.add("minecraftArguments", new JsonPrimitive(this.generateMinecraftArguments()));
        jsonObject.add("libraries", array);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }

    public static String getUsername(File f) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())), StandardCharsets.UTF_8);
            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(json).getAsJsonObject();
            String[] params = root.get("minecraftArguments").toString().substring(1).split(" ");
            int index=  IntStream.range(0, params.length)
                    .filter(i -> params[i].equals("--username"))
                    .findFirst()
                    .orElse(-1) + 1;
            return params[index];
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
        return "Player";
    }

}
