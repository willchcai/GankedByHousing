package com.example.gankedbyhousing;

public class Listing {
    private int image;
    private String title, price, location, listingID, userPhoneNumber, email;

    public Listing() {
    }

    public Listing(int image, String title, String price, String location, String listingID, String userPhoneNumber, String email) {
        this.image = image;
        this.title = title;
        this.price = price;
        this.location = location;
        this.listingID = listingID;
        this.userPhoneNumber = userPhoneNumber;
        this.email = email;
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

    public String getListingID() {
        return listingID;
    }

    public String getPhoneNumber() { return userPhoneNumber; }

    public String getEmail() { return email; }

}