package io.chocorean.minecraft.updater.core;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile implements JsonSerializer<Profile> {

    private HashMap<String, String> properties;
    private String username;
    private List<Library> libraries;

    public Profile(String username) {
        this.properties = new HashMap<>();
        this.libraries = new ArrayList<>();
        this.properties.put("inheritsFrom", "");
        this.properties.put("id", "");
        this.properties.put("type", "release");
        this.properties.put("time", "");
        this.properties.put("releaseTime", "");
        this.properties.put("mainClass", "net.minecraft.launchwrapper.Launch");
        this.properties.put("minimumLauncherVersion", "0");
        this.properties.put("jar", "1.10.2");
        this.username = username;
    }

    public void addLibrary(Library library) {
        this.libraries.add(library);
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
    public JsonElement serialize(Profile profile, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        JsonArray array = new JsonArray();
        for(Map.Entry<String, String> entry: this.properties.entrySet())
            jsonObject.add(entry.getKey(), new JsonPrimitive(entry.getValue()));
        for(Library l: this.libraries)
            array.add(gson.toJsonTree(l));
        jsonObject.add("libraries", array);
        jsonObject.addProperty("minecraftArguments", this.generateMinecraftArguments());
        jsonObject.addProperty("minimumLauncherVersion", 0);
        return jsonObject;
    }
}
