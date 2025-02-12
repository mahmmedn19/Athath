package com.project.athath.data.model;


public class Component {
    private String name;
    private int imageResId;

    public Component(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public int getImageResId() { return imageResId; }
}
