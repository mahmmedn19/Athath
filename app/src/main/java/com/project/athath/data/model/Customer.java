package com.project.athath.data.model;

public class Customer {
    private String id;
    private String username;
    private String email;
    private String password;
    private String status;
    private String role = "Customer";


    public Customer() {
    } // Required empty constructor for Firebase

    public Customer(String username, String email, String status, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
        this.role = "Customer";
    }

    public Customer(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
        this.role = "Customer";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
