package com.project.athath.data.model;

public class Customer {
    private String id;
    private String username;
    private String email;
    private String status;
    private String role = "Customers";


    public Customer() {
    } // Required empty constructor for Firebase

    public Customer(String username, String email, String status) {
        this.username = username;
        this.email = email;
        this.status = status;
        this.role = "Customers";
    }

    public Customer(String username, String email) {
        this.username = username;
        this.email = email;
        this.role = "Customers";
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



    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
