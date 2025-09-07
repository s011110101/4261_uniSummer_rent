package com.example.a4261_unisummer_rent.model;
import android.media.Image;

import java.text.DateFormat;
import java.util.*;
public class Listing {
    private String id;
    private String title;
    private String description;
    private int status;
    private String posterID;
    private int price;
    //private DateFormat date;
    private int image;

    public Listing(String id, String title, int price, String description, String posterID, int image) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        status = 0;
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

    public void setId(String id) {
        this.id = id;
    }



    public void setPrice(int price) {
        this.price = price;
    }
    public int getPrice(){
        return price;
    }

    public String getId() {
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

    public String getPostedBy() {
        return posterID;
    }

    //public DateFormat getDate() {
    //    return date;
    //}
}
