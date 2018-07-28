package io.chocorean.minecraft.updater.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Library {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("serverreq")
    @Expose
    private boolean serverreq;
    @SerializedName("checksums")
    @Expose
    private List<String> checksums = null;
    @SerializedName("clientreq")
    @Expose
    private boolean clientreq;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isServerreq() {
        return serverreq;
    }

    public void setServerreq(boolean serverreq) {
        this.serverreq = serverreq;
    }

    public List<String> getChecksums() {
        return checksums;
    }

    public void setChecksums(List<String> checksums) {
        this.checksums = checksums;
    }

    public boolean isClientreq() {
        return clientreq;
    }

    public void setClientreq(boolean clientreq) {
        this.clientreq = clientreq;
    }

    public String toString() {
        return this.name + ", " + this.url;
    }
}