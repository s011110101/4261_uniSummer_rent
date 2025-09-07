package com.example.a4261_unisummer_rent.model;
import android.media.Image;

import java.text.DateFormat;
import java.util.*;
public class Listing {
    private UUID id;
    private String title;
    private String description;
    private int status;
    private UUID posterID;
    private int price;
    //private DateFormat date;
    private int image;

    public Listing(UUID id, String title, int price, String description, int status, UUID posterID, int image) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.status = status;
        this.posterID = posterID;
        //this.date = date;
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    //public void setDate(DateFormat date) {
    //    this.date = date;
    //}


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setId(UUID id) {
        this.id = id;
    }



    public void setPrice(int price) {
        this.price = price;
    }
    public int getPrice(){
        return price;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getStatus() {
        return status;
    }

    public UUID getPostedBy() {
        return posterID;
    }

    //public DateFormat getDate() {
    //    return date;
    //}
}
