package com.project.asas.model;

import com.google.firebase.firestore.DocumentId;

public class Furniture {
    @DocumentId
    private String id;
    private String name;
    private String description;
    private String category;
    private String color;
    private String roomType;
    private String style;
    private double price;
    private double length;
    private double width;
    private double height;
    private int imageUrl; // Store image URL in Firebase Storage

    public Furniture() {} // Required empty constructor for Firebase
    // ✅ Add a constructor that includes `id`
    public Furniture(String id, String name, String description, String category, String color, String roomType,
                     String style, double price, double length, double width, double height, int imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.color = color;
        this.roomType = roomType;
        this.style = style;
        this.price = price;
        this.length = length;
        this.width = width;
        this.height = height;
        this.imageUrl = imageUrl;
    }
    public Furniture(String name, String description, String category, String color, String roomType,
                     String style, double price, double length, double width, double height, int imageUrl) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.color = color;
        this.roomType = roomType;
        this.style = style;
        this.price = price;
        this.length = length;
        this.width = width;
        this.height = height;
        this.imageUrl = imageUrl;
    }

    public Furniture(String  name, String description, double length, String color, double width, int imageUrl) {
        this.name = name;
        this.description = description;
        this.length = length;
        this.color = color;
        this.width = width;
        this.imageUrl = imageUrl;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getLength() { return length; }
    public void setLength(double length) { this.length = length; }

    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public int getImageUrl() { return imageUrl; }
    public void setImageUrl(int imageUrl) { this.imageUrl = imageUrl; }
}
