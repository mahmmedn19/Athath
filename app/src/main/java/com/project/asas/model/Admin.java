package com.project.asas.model;

import com.google.firebase.firestore.DocumentId;

public class Admin {
    @DocumentId
    private String id;
    private String username;
    private String email;
    private String password;

    public Admin() {} // Required empty constructor for Firebase

    public Admin(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
