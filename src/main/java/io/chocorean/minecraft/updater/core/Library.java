package io.chocorean.minecraft.updater.core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class Library implements JsonSerializer<Library> {

    private String name;
    private String url;

    public Library(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public JsonElement serialize(Library library, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("name", this.name);
        object.addProperty("url", this.url);
        return object;
    }
}
