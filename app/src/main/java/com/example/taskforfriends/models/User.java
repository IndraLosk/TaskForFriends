package com.example.taskforfriends.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name, email, password, friends, numOfTask;
    private int score;

    public  User() {}

    public String getNumOfTask() {
        return numOfTask;
    }

    public void setNumOfTask(String numOfTask) {
        this.numOfTask = numOfTask;
    }

    public  User(String name, String email, String password, int score, String friends, String numOfTask) {
        this.score = score;
        this.name = name;
        this.email = email;
        this.password = password;
        this.friends = friends;
        this.numOfTask = numOfTask;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }
}
