package com.example.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FlavorTextEntries {

    @SerializedName("flavor_text_entries")
    @Expose
    private List<FlavorTextEntry> flavorTextEntries = null;

    public List<FlavorTextEntry> getFlavorTextEntries() {
        return flavorTextEntries;
    }

    public void setFlavorTextEntries(List<FlavorTextEntry> flavorTextEntries) {
        this.flavorTextEntries = flavorTextEntries;
    }
}