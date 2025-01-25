package com.project.asas.model;


public class Product {
    private String title;
    private String description;
    private String price;
    private int imageResId;
    private boolean isFavorite;

    public Product(String title, String description, String price, int imageResId, boolean isFavorite) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
        this.isFavorite = isFavorite;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }
    public boolean isFavorite() {
        return isFavorite;
    }
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

}