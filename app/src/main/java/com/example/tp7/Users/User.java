package com.example.tp7.Users;

public class User {
    private int id;
    private String name;
    private String lastname;
    private String institution;
    private String username;
    private String password;

    // Constructor
    public User(int id, String name, String lastname, String institution, String username, String password) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.institution = institution;
        this.username = username;
        this.password= password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}