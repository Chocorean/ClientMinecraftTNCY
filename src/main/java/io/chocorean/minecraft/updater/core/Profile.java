package io.chocorean.minecraft.updater.core;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile {

    private final HashMap<String, String> properties;
    private final String username;
    private List<Library> libraries;

    public Profile(String id, String username, String forgeVersion, List<Library> libraries) {
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
        Gson gson = new Gson();
        for(Map.Entry<String, String> entry: this.properties.entrySet())
            jsonObject.add(entry.getKey(), new JsonPrimitive(entry.getValue()));
        JsonArray array = new JsonArray();
        for(Library l: this.libraries)
            array.add(gson.toJsonTree(l, Library.class));
        jsonObject.add("minecraftArguments", new JsonPrimitive(this.generateMinecraftArguments()));
        jsonObject.add("minimumLauncherVersion", new JsonPrimitive(0));
        jsonObject.add("libraries", array);
        return jsonObject.toString();
    }
    
}
