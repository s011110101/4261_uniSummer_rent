package com.example.a4261_unisummer_rent.model;
import androidx.annotation.NonNull;

import java.util.*;

public class User {
    private String id;
    private String username;
    private String email;
    //private float rate;

    public String getUsername() {
        return username;
    }

    //public float getRate() {
    //    return rate;
    //}

    public String getId() {
        return id;
    }

    public void setPasswords(String passwords) {
        this.email = passwords;
    }

    public User(String id, String username, String passwords) {
        this.id = id;
        this.username = username;
        this.email = email;
        //this.rate = rate;
    }

    @NonNull
    @Override
    public String toString() {
        return username+id;
    }
}
