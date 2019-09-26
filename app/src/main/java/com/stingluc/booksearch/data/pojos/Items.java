package com.stingluc.booksearch.data.pojos;

import com.google.gson.annotations.SerializedName;

public class Items {

    @SerializedName("kind")
    private String kind;
    @SerializedName("id")
    private String id;
    @SerializedName("etag")
    private String etag;
    @SerializedName("selfLink")
    private String selfLink;
    @SerializedName("volumeInfo")
    private VolumeInfo volumeInfo;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }
}
