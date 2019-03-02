package ca.ualberta.CMPUT3012019T02.alexandria.model.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OwnedBook {

    private String isbn;
    private List<String> usersRequesting;
    private List<String> images;
    private String status;
    private String userBorrowing;

    public OwnedBook(String isbn, String status, String userBorrowing) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("Isbn cannot be null or empty");
        }

        this.isbn = isbn;
        this.status = status;
        this.userBorrowing = userBorrowing;

        usersRequesting = new ArrayList<>();
        images = new ArrayList<>();
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

    public List<String> getImages() {
        return Collections.unmodifiableList(images);
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void addImage(String imageId) {
        this.images.add(imageId);
    }

    public void removeImage(String imageId) {
        this.images.remove(imageId);
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
