
package com.example.pictorutinas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Resultado {

    @SerializedName("_id")
    @Expose
    private Integer id;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("downloads")
    @Expose
    private Integer downloads;
    @SerializedName("tags")
    @Expose
    private List<String> tags;
    @SerializedName("synsets")
    @Expose
    private List<String> synsets;
    @SerializedName("sex")
    @Expose
    private Boolean sex;
    @SerializedName("lastUpdated")
    @Expose
    private String lastUpdated;
    @SerializedName("schematic")
    @Expose
    private Boolean schematic;
    @SerializedName("keywords")
    @Expose
    private List<Keyword> keywords;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("categories")
    @Expose
    private List<String> categories;
    @SerializedName("violence")
    @Expose
    private Boolean violence;
    @SerializedName("hair")
    @Expose
    private Boolean hair;
    @SerializedName("skin")
    @Expose
    private Boolean skin;
    @SerializedName("aac")
    @Expose
    private Boolean aac;
    @SerializedName("aacColor")
    @Expose
    private Boolean aacColor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getSynsets() {
        return synsets;
    }

    public void setSynsets(List<String> synsets) {
        this.synsets = synsets;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Boolean getSchematic() {
        return schematic;
    }

    public void setSchematic(Boolean schematic) {
        this.schematic = schematic;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Boolean getViolence() {
        return violence;
    }

    public void setViolence(Boolean violence) {
        this.violence = violence;
    }

    public Boolean getHair() {
        return hair;
    }

    public void setHair(Boolean hair) {
        this.hair = hair;
    }

    public Boolean getSkin() {
        return skin;
    }

    public void setSkin(Boolean skin) {
        this.skin = skin;
    }

    public Boolean getAac() {
        return aac;
    }

    public void setAac(Boolean aac) {
        this.aac = aac;
    }

    public Boolean getAacColor() {
        return aacColor;
    }

    public void setAacColor(Boolean aacColor) {
        this.aacColor = aacColor;
    }

}
