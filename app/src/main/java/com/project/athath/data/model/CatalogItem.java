package com.project.athath.data.model;

import com.google.firebase.firestore.DocumentId;

public class CatalogItem {
    private String id;  // Firestore auto-generated ID

    private String imageRes;  // Base64 image string

    // Empty constructor required for Firestore deserialization
    public CatalogItem() {}

    public CatalogItem(String imageRes) {
        this.imageRes = imageRes;
    }

    public String getId() {
        return id;
    }

    public String getImageRes() {
        return imageRes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageRes(String imageRes) {
        this.imageRes = imageRes;
    }
}
