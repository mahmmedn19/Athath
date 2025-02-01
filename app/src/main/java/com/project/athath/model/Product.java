package com.project.athath.model;
import com.google.firebase.firestore.DocumentId;

public class Product {
    @DocumentId
    private String id;
    private String style;
    private String roomType;
    private double budget;
    private double roomWidth;
    private double roomLength;
    private int imageUrl; // Store image URL in Firebase Storage

    public Product() {} // Required empty constructor for Firebase

    public Product(String id ,String style, String roomType, double budget, double roomWidth, double roomLength, int imageUrl) {
        this.id = id;
        this.style = style;
        this.roomType = roomType;
        this.budget = budget;
        this.roomWidth = roomWidth;
        this.roomLength = roomLength;
        this.imageUrl = imageUrl;
    }
    public Product(String style, String roomType, double budget, double roomWidth, double roomLength, int imageUrl) {
        this.style = style;
        this.roomType = roomType;
        this.budget = budget;
        this.roomWidth = roomWidth;
        this.roomLength = roomLength;
        this.imageUrl = imageUrl;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }

    public double getRoomWidth() { return roomWidth; }
    public void setRoomWidth(double roomWidth) { this.roomWidth = roomWidth; }

    public double getRoomLength() { return roomLength; }
    public void setRoomLength(double roomLength) { this.roomLength = roomLength; }

    public int getImageUrl() { return imageUrl; }
    public void setImageUrl(int imageUrl) { this.imageUrl = imageUrl; }
}
