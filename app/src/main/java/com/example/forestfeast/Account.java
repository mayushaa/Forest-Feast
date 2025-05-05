package com.example.forestfeast;

public class Account {

    private String name;
    private String username;
    private String email;
    private String password;
    private int correctCounter;
    private int currentLevel;

    public Account(String name, String username, String email, String password, int correctCounter, int currentLevel) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.correctCounter = correctCounter;
        this.currentLevel = currentLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCorrectCounter() {
        return correctCounter;
    }

    public void setCorrectCounter(int correctCounter) {
        this.correctCounter = correctCounter;
    }

    public int getCurrentLevel() { return currentLevel; }

    public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }
}
