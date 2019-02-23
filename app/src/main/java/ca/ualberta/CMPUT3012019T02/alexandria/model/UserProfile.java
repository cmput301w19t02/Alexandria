package ca.ualberta.CMPUT3012019T02.alexandria.model;

import android.graphics.Bitmap;

public class UserProfile {
    private String name;
    private String email;
    private String phone;
    private Bitmap picture;

    public UserProfile(String name, String email, String phone, Bitmap picture) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.picture = picture;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
