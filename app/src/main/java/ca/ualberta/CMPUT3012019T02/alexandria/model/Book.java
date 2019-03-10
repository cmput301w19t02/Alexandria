package ca.ualberta.CMPUT3012019T02.alexandria.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Book {

    private String isbn;
    private String imageId;
    private String title;
    private String author;
    private String description;
    private Date date;
    private List<String> availableOwners;

    /**
     * No args constructor to maintain compatibility
     * with Firebase deserializer
     * DO NOT USE
     */
    @Deprecated
    public Book() {
        availableOwners = new ArrayList<>();
    }

    public Book(String isbn, String imageId, String title, String author, String description, Date date) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("Isbn cannot be null or empty");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }

        this.isbn = isbn;
        this.imageId = imageId;
        this.title = title;
        this.author = author;
        this.date = date;
        this.description = description;

        availableOwners = new ArrayList<>();
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

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description==null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }

        this.description = description;
    }

    public boolean equals(Book book) {
        return  this.isbn.equals(book.getIsbn()) &&
                this.imageId.equals(book.getImageId()) &&
                this.title.equals(book.getTitle()) &&
                this.author.equals(book.getAuthor()) &&
                this.description.equals(book.getDescription());
    }
}
