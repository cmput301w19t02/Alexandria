package ca.ualberta.CMPUT3012019T02.alexandria.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * This class holds general information about a book, including its title, author, description, etc.
 */
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
     * TO BE USED BY BOOK CONTROLLER ONLY
     */
    @Deprecated
    public Book() {
        availableOwners = new ArrayList<>();
    }

    /**
     * Create a new book
     * @param isbn isbn of the book
     * @param imageId image id of the cover of the book (null if not available)
     * @param title title of the book
     * @param author author of the book
     * @param description description of the book
     * @param date date of the book
     */
    public Book(@NonNull String isbn, String imageId, @NonNull String title, @NonNull String author, @NonNull String description, Date date) {
        setIsbn(isbn);
        setImageId(imageId);
        setTitle(title);
        setAuthor(author);
        setDate(date);
        setDescription(description);

        availableOwners = new ArrayList<>();
    }

    /**
     * Gets the isbn of this book
     * @return
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the isbn of this book
     * @param isbn
     */
    public void setIsbn(@NonNull String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("Isbn cannot be null or empty");
        }

        this.isbn = isbn;
    }

    /**
     * Gets the cover image id of this book
     * @return image id
     */
    @Nullable
    public String getImageId() {
        return imageId;
    }

    /**
     * Sets the cover image id of this book
     * @param imageId image id to set to
     */
    public void setImageId(@Nullable String imageId) {
        this.imageId = imageId;
    }

    /**
     * Gets the title of this book
     * @return the title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this book
     * @param title the title to set
     */
    public void setTitle(@NonNull String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        this.title = title;
    }

    /**
     * Gets the author of this book
     * @return the name of the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of this book
     * @param author the name of the author
     */
    public void setAuthor(@NonNull String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }

        this.author = author;
    }

    /**
     * Gets the date of this book
     * @return the date of this book
     */
    @Nullable
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date of this book
     * @param date date of this book
     */
    public void setDate(@Nullable Date date) {
        this.date = date;
    }

    /**
     * Gets a list of owner user ids that have this book available for borrowing
     * @return a list of user ids
     */
    public List<String> getAvailableOwners() {
        return Collections.unmodifiableList(availableOwners);
    }

    /**
     * Sets the list of available owners
     * TO BE USED BY BOOK CONTROLLER ONLY
     * @param availableOwners list of available owners
     */
    @Deprecated
    public void setAvailableOwners(List<String> availableOwners) {
        this.availableOwners = availableOwners;
    }

    /**
     * Adds a user to the list of available owners for this book
     * TO BE USED BY BOOK CONTROLLER ONLY
     * @param avaliableOwner user id
     */
    @Deprecated
    public void addAvailableOwners(String avaliableOwner) {
        this.availableOwners.add(avaliableOwner);
    }

    /**
     * Removes a user from the list of available owners for this book
     * TO BE USED BY BOOK CONTROLLER ONLY
     * @param avaliableOwner
     */
    @Deprecated
    public void removeAvailableOwners(String avaliableOwner) {
        this.availableOwners.remove(avaliableOwner);
    }

    /**
     * Gets the description of this book
     * @return the description of this book
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this book
     * @param description description to set
     */
    public void setDescription(@NonNull String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }

        this.description = description;
    }

    /**
     * Checks of the contents of this book matches another
     * (Note: ignores checks on date and image)
     * @param book book to compare to
     * @return True if the book contents match. False otherwise
     */
    public boolean equals(Book book) {
        return  this.isbn.equals(book.getIsbn()) &&
                this.title.equals(book.getTitle()) &&
                this.author.equals(book.getAuthor()) &&
                this.description.equals(book.getDescription());
    }
}
