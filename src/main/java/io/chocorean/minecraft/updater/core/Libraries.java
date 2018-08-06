package io.chocorean.minecraft.updater.core;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class Libraries {

    @SerializedName("libraries")
    @Expose
    private List<Library> libraries = null;

    public List<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<Library> libraries) {
        this.libraries = libraries;
    }

    public static List<Library> getLibrariesFromResource() {
        Gson gson = new Gson();
        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(Libraries.class.getResourceAsStream("/libraries.json")));
        Libraries result = gson.fromJson(br, Libraries.class);
        return result.getLibraries();
    }

}
