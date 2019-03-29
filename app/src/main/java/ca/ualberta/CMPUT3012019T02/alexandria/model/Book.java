package ca.ualberta.CMPUT3012019T02.alexandria.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class holds general information about a book, including its title, author, description, etc.
 */
public class Book {

    private String isbn;
    private String title;
    private String author;

    private String description;
    private String imageId;

    private Map<String, Integer> availableFrom;

    /**
     * No args constructor to maintain compatibility
     * with Firebase deserializer
     * TO BE USED BY FIREBASE ONLY
     */
    @Deprecated
    public Book() {
        availableFrom = new HashMap<>();
    }

    /**
     * Create a new book
     *
     * @param isbn isbn of the book
     * @param title title of the book
     * @param author author of the book
     */
    public Book(@NonNull String isbn, @NonNull String title, @NonNull String author) {
        this(isbn, title, author, null, null);
    }

    /**
     * Create a new book
     *
     * @param isbn isbn of the book
     * @param imageId image id of the cover of the book (null if not available)
     * @param title title of the book
     * @param author author of the book
     * @param description description of the book
     */
    public Book(@NonNull String isbn, @NonNull String title, @NonNull String author, @Nullable String description, @Nullable String imageId) {
        if (isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("Isbn cannot be null or empty");
        }
        this.isbn = isbn;
        setImageId(imageId);
        setTitle(title);
        setAuthor(author);
        setDescription(description);
        availableFrom = new HashMap<>();
    }

    /**
     * Gets the isbn of this book
     *
     * @return the isbn of this book as a string
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Gets the title of this book
     *
     * @return the title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this book
     *
     * @param title the title to set
     */
    public void setTitle(@NonNull String title) {
        if (title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title can not be null or empty");
        }
        this.title = title;
    }

    /**
     * Gets the author of this book
     *
     * @return the author of this book
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of this book
     *
     * @param author the name of the author
     */
    public void setAuthor(@NonNull String author) {
        if (author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author can not be empty");
        }
        this.author = author;
    }

    /**
     * Gets the description of this book
     *
     * @return an optional that may contain the description of this book
     */
    @Nullable
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this book
     *
     * @param description description to set
     */
    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    /**
     *
     * Gets the cover image id of this book
     *
     * @return an optional that may contain the image id
     */
    @Nullable
    public String getImageId() {
        return imageId;
    }

    /**
     * Sets the cover image id of this book
     *
     * @param imageId image id to set to
     */
    public void setImageId(@Nullable String imageId) {
        this.imageId = imageId;
    }

    /**
     * Gets a collection of users (user ids) that have this book available
     *
     * @return an unmodifiable collection of available owners
     */
    @Exclude
    public Collection<String> getAvailableOwners() {
        return Collections.unmodifiableSet(availableFrom.keySet());
    }

    /**
     * Gets a map of user id strings to integers.
     * The keyset of this map is the collection of users that have this book available.
     *
     * @return a map of user id strings to integers
     */
    public Map<String, Integer> getAvailableFrom() {
        return availableFrom;
    }

}
