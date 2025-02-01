package com.project.athath.model;

import com.google.firebase.firestore.DocumentId;

public class Vendor {
    @DocumentId
    private String id;
    private String name;
    private String storeName;
    private String phone;
    private String address;
    private String email;
    private String status;
    private String password;

    public Vendor() {} // Required empty constructor for Firebase

    public Vendor(String name, String storeName, String phone, String address, String email, String status , String password) {
        this.name = name;
        this.storeName = storeName;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.status = status;
        this.password = password;
    }
    public Vendor(String name, String status ) {
        this.name = name;
        this.status = status;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
