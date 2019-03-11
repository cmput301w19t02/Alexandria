package ca.ualberta.CMPUT3012019T02.alexandria.model.user;

import java.util.Arrays;
import java.util.List;

/**
 * This class represents some relationship between a User and a Book.
 */
public abstract class UserBook {

    private String isbn;
    private String status;
    private String owner;

    /**
     * Creates a new UserBook given an isbn, status, and owner
     * @param isbn isbn of the book
     * @param status status of the book
     * @param owner owner of the book
     */
    UserBook(String isbn, String status, String owner) {
        this.isbn = isbn;
        this.status = status;
        this.owner = owner;
    }

    /**
     * Gets the isbn of this user book
     * @return the isbn of this user book
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the isbn of this user book
     * TO BE USED BY BOOK CONTROLLER ONLY
     * @param isbn
     */
    @Deprecated
    public void setIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("Isbn cannot be null or empty");
        }

        this.isbn = isbn;
    }

    /**
     * Gets the status of this user book
     * @return status of this book
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of this user book
     * TO BE USED BY BOOK CONTROLLER ONLY
     * @param status status to set this user book to
     */
    @Deprecated
    public void setStatus(String status) {
        List<String> validStatuses = Arrays.asList("available", "requested", "accepted", "borrowed");
        if (!validStatuses.contains(status)) {
            throw new IllegalArgumentException("Invalid status");
        }
        this.status = status;
    }

    /**
     * Gets the owner of this user book
     * @return the user id of the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the owner of this user book
     * TO BE USED BY BOOK CONTROLLER ONLY
     * @param owner the owner user id to set
     */
    @Deprecated
    public void setOwner(String owner) {
        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner cannot be null or empty");
        }
        this.owner = owner;
    }
}
