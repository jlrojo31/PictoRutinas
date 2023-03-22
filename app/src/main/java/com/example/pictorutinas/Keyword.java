
package com.example.pictorutinas;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Keyword {

    @SerializedName("keyword")
    @Expose
    private String keyword;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("meaning")
    @Expose
    private String meaning;
    @SerializedName("plural")
    @Expose
    private String plural;
    @SerializedName("hasLocution")
    @Expose
    private Boolean hasLocution;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    public Boolean getHasLocution() {
        return hasLocution;
    }

    public void setHasLocution(Boolean hasLocution) {
        this.hasLocution = hasLocution;
    }

}
