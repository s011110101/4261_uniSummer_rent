package com.example.a4261_unisummer_rent.model;
import androidx.annotation.NonNull;

import java.util.*;

public class User {
    private UUID id;
    private String username;
    private String passwords;
    //private float rate;

    public String getUsername() {
        return username;
    }

    //public float getRate() {
    //    return rate;
    //}

    public UUID getId() {
        return id;
    }
    public boolean checkPasswords(String passwords){
        return this.passwords.equals(passwords);
    }

    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }

    public User(UUID id, String username, String passwords) {
        this.id = id;
        this.username = username;
        this.passwords = passwords;
        //this.rate = rate;
    }

    @NonNull
    @Override
    public String toString() {
        return username+id;
    }
}
