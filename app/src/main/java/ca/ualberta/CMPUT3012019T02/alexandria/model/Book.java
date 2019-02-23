package ca.ualberta.CMPUT3012019T02.alexandria.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Book {
    private String isbn;
    private Bitmap image;
    private String title;
    private String author;
    private Date date;
    private List<String> availableOwners = new ArrayList<>();

    public Book(String isbn, Bitmap image, String title, String author, Date date) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("Isbn cannot be null or empty");
        }

        this.isbn = isbn;
        this.image = image;
        this.title = title;
        this.author = author;
        this.date = date;
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getAvailableOwners() {
        return Collections.unmodifiableList(availableOwners);
    }

    public void setAvailableOwners(List<String> availableOwners) {
        this.availableOwners = availableOwners;
    }

    public void addAvailableOwners(String avaliableOwner) {
        this.availableOwners.add(avaliableOwner);
    }

    public void removeAvailableOwners(String avaliableOwner) {
        this.availableOwners.remove(avaliableOwner);
    }
}
