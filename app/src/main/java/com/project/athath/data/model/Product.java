package com.project.athath.data.model;

import com.google.firebase.firestore.DocumentId;

public class Product {

    private String id;                 // Firestore-generated product ID
    private String name;               // Product name
    private String category;           // Product category (e.g., Sofa, Table, Chair)
    private String description;        // Product description
    private double price;              // Product price
    private String color;              // Product color
    private String style;              // Design style (Modern, Classic, etc.)
    private String roomType;           // Suitable room type (Living Room, Bedroom, etc.)
    private double productWidth;       // Product width
    private double productLength;      // Product length
    private String imageUrl;           // Product image URL (Firebase Storage)
    private String storeId;            // Reference to the vendor's store


    public Product(String id, String style, String roomType, double price, double productWidth, double productLength, String imageUrl) {
        this.id = id;
        this.style = style;
        this.roomType = roomType;
        this.price = price;
        this.productWidth = productWidth;
        this.productLength = productLength;
        this.imageUrl = imageUrl;
    }

    public Product(String style, String roomType, double price, double productWidth, double productLength, String imageUrl) {
        this.style = style;
        this.roomType = roomType;
        this.price = price;
        this.productWidth = productWidth;
        this.productLength = productLength;
        this.imageUrl = imageUrl;
    }

    // 🔄 Empty constructor required for Firestore
    public Product() {
    }

    // ✅ Full constructor
    public Product(String id, String name, String category, String description, double price, String color,
                   String style, String roomType, double productWidth, double productLength,
                   String imageUrl) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.color = color;
        this.style = style;
        this.roomType = roomType;
        this.productWidth = productWidth;
        this.productLength = productLength;
        this.imageUrl = imageUrl;
    }

    // ✅ Constructor for product addition (without Firestore ID)
    public Product(String id ,String name, String category, String description, double price, String color,
                   String style, String roomType, double productWidth, double productLength,
                   String imageUrl, String storeId) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.color = color;
        this.style = style;
        this.roomType = roomType;
        this.productWidth = productWidth;
        this.productLength = productLength;
        this.imageUrl = imageUrl;
        this.storeId = storeId;
    }




    // 🔑 Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getProductWidth() {
        return productWidth;
    }

    public void setProductWidth(double productWidth) {
        this.productWidth = productWidth;
    }

    public double getProductLength() {
        return productLength;
    }

    public void setProductLength(double productLength) {
        this.productLength = productLength;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}