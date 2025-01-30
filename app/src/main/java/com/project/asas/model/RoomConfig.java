package com.project.asas.model;

public class RoomConfig {
    private String id;
    private String name;
    private String colorType;
    private boolean hasPlants;
    private boolean hasTV;
    private String floorType;
    private String roomType;
    private String designType;
    private int imageUrl;

    public RoomConfig() {} // Required for Firebase

    public RoomConfig(String id, String name, String colorType, boolean hasPlants, boolean hasTV,
                      String floorType, String roomType, String designType, int imageUrl) {
        this.id = id;
        this.name = name;
        this.colorType = colorType;
        this.hasPlants = hasPlants;
        this.hasTV = hasTV;
        this.floorType = floorType;
        this.roomType = roomType;
        this.designType = designType;
        this.imageUrl = imageUrl;
    }
    public RoomConfig(String name, String roomType, int imageUrl) {
        this.name = name;
        this.roomType = roomType;
        this.imageUrl = imageUrl;
    }


    public String getId() { return id; }
    public String getName() { return name; }
    public String getColorType() { return colorType; }
    public boolean hasPlants() { return hasPlants; }
    public boolean hasTV() { return hasTV; }
    public String getFloorType() { return floorType; }
    public String getRoomType() { return roomType; }
    public String getDesignType() { return designType; }
    public int getImageUrl() { return imageUrl; }
}
