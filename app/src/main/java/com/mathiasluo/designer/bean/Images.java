package com.mathiasluo.designer.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Images extends RealmObject {

    @SerializedName("hidpi")
    @Expose
    private String hidpi;
    @SerializedName("normal")
    @Expose
    @PrimaryKey
    private String normal;
    @SerializedName("teaser")
    @Expose
    private String teaser;

    /**
     * @return The hidpi
     */
    public String getHidpi() {
        return hidpi;
    }

    /**
     * @param hidpi The hidpi
     */
    public void setHidpi(String hidpi) {
        this.hidpi = hidpi;
    }

    /**
     * @return The normal
     */
    public String getNormal() {
        return normal;
    }

    /**
     * @param normal The normal
     */
    public void setNormal(String normal) {
        this.normal = normal;
    }

    /**
     * @return The teaser
     */
    public String getTeaser() {
        return teaser;
    }

    /**
     * @param teaser The teaser
     */
    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

}
