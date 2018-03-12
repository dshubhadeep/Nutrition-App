package com.example.smith.epsilonhealth.DataModels;

/**
 * Created by SMITH on 12-Mar-18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchItems {

    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("label")
    @Expose
    private String label;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}