package com.project.asas.model;

import com.google.firebase.firestore.DocumentId;

public class Vendor {
    @DocumentId
    private String id;
    private String storeName;
    private String phone;
    private String address;
    private String email;
    private String password;

    public Vendor() {} // Required empty constructor for Firebase

    public Vendor(String storeName, String phone, String address, String email, String password) {
        this.storeName = storeName;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.password = password;
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

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
