package com.project.asas.model;

public class Product {
    private int id;
    private String style;
    private String roomType;
    private double budget;
    private double roomWidth;
    private double roomLength;
    private int image; // Storing image as a byte array

    public Product(int id, String style, String roomType, double budget, double roomWidth, double roomLength, int image) {
        this.id = id;
        this.style = style;
        this.roomType = roomType;
        this.budget = budget;
        this.roomWidth = roomWidth;
        this.roomLength = roomLength;
        this.image = image;
    }

    // Getters
    public int getId() { return id; }
    public String getStyle() { return style; }
    public String getRoomType() { return roomType; }
    public double getBudget() { return budget; }
    public double getRoomWidth() { return roomWidth; }
    public double getRoomLength() { return roomLength; }
    public int getImage() { return image; }

    // Methods from UML
    public void getRecommendation() {
        System.out.println("Generating furniture recommendation based on budget and style...");
    }

    public void getImageDetails() {
        System.out.println("Fetching image for product...");
    }
}
