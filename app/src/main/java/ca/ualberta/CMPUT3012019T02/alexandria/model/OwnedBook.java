package ca.ualberta.CMPUT3012019T02.alexandria.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OwnedBook {
    private String isbn;
    private List<String> usersRequesting = new ArrayList<>();
    private List<Bitmap> images = new ArrayList<>();
    private String status;
    private String userBorrowing;

    public OwnedBook(String isbn, String status, String userBorrowing) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("Isbn cannot be null or empty");
        }

        this.isbn = isbn;
        this.status = status;
        this.userBorrowing = userBorrowing;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("Isbn cannot be null or empty");
        }

        this.isbn = isbn;
    }

    public List<String> getUsersRequesting() {
        return Collections.unmodifiableList(usersRequesting);
    }

    public void setUsersRequesting(List<String> usersRequesting) {
        this.usersRequesting = usersRequesting;
    }

    public void addUserRequesting(String userRequesting) {
        this.usersRequesting.add(userRequesting);
    }

    public void removeUserRequesting(String userRequesting) {
        this.usersRequesting.remove(userRequesting);
    }

    public List<Bitmap> getImages() {
        return Collections.unmodifiableList(images);
    }

    public void setImages(List<Bitmap> images) {
        this.images = images;
    }

    public void addImage(Bitmap image) {
        this.images.add(image);
    }

    public void removeImage(Bitmap image) {
        this.images.remove(image);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserBorrowing() {
        return userBorrowing;
    }

    public void setUserBorrowing(String userBorrowing) {
        this.userBorrowing = userBorrowing;
    }
}
