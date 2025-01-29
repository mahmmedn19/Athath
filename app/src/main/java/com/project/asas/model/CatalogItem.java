package com.project.asas.model;

import androidx.annotation.DrawableRes;

public class CatalogItem {
    private final String title;
    private final String description;
    @DrawableRes
    private final int imageRes;

    public CatalogItem(String title, String description, int imageRes) {
        this.title = title;
        this.description = description;
        this.imageRes = imageRes;
    }

    public CatalogItem(int imageRes) {
        this.title = "Default Title"; // Default value for title
        this.description = "Default Description"; // Default value for description
        this.imageRes = imageRes;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImageRes() {
        return imageRes;
    }
}
