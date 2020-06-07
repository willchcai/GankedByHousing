package com.example.gankedbyhousing;

public class Listing {
    private int image;
    private String title, price, location;

    public Listing() {
    }

    public Listing(int image, String title, String price, String location) {
        this.image = image;
        this.title = title;
        this.price = price;
        this.location = location;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }
}