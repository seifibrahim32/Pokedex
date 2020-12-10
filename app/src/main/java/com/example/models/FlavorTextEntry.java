package com.example.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlavorTextEntry {

        @SerializedName("flavor_text")
        @Expose
        private String flavorText;
        @SerializedName("language")
        @Expose
        private Language language;

        public String getFlavorText() {
            return flavorText;
        }

        public void setFlavorText(String flavorText) {
            this.flavorText = flavorText;
        }

        public Language getLanguage() {
            return language;
        }

        public void setLanguage(Language language) {
            this.language = language;
        }

}
