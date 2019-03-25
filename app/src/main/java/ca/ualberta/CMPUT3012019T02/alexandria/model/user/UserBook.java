package ca.ualberta.CMPUT3012019T02.alexandria.model.user;

import com.google.firebase.database.Exclude;

/**
 * This class represents some relationship between a User and a Book.
 */
public abstract class UserBook {

    private String isbn;
    private String status;
    private String owner;
    private boolean scanned;

    /**
     * Creates a new UserBook given an isbn, status, and owner
     *
     * @param isbn isbn of the book
     * @param status status of the book
     * @param owner owner of the book
     */
    UserBook(String isbn, String status, String owner) {
        this.isbn = isbn;
        this.status = status;
        this.owner = owner;
        this.scanned = false;
    }

    /**
     * Gets the isbn of this user book
     *
     * @return the isbn of this user book
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Gets the status of this user book
     *
     * @return status of this book
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of this user book
     * TO BE USED ONLY BY SUBCLASSES AND BOOK CONTROLLER
     *
     * @param status status to set
     */
    @Deprecated
    public void setStatus(String status) {
        if (status.equals("available") || status.equals("requested") || status.equals("accepted") || status.equals("borrowed")) {
            this.status = status;
            return;
        }
        throw new IllegalArgumentException("Invalid status '" + status + "'");
    }

    /**
     * Gets the owner of this user book
     *
     * @return the user id of the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Whether or not this book has been scanned
     *
     * @return true if this book has been scanned. false otherwise
     */
    public boolean getScanned() {
        return scanned;
    }

    /**
     * Sets 'scanned' status of this book to true
     * TO BE USED ONLY BY SUBCLASSES AND BOOK CONTROLLER
     */
    @Exclude
    @Deprecated
    public void setScanned(boolean b) {
        if (b && (status.equals("accepted") || status.equals("borrowed"))) {
            scanned = true;
        } else if (!b) {
            scanned = false;
        } else {
            throw new UnsupportedOperationException("Can not scan book unless it is accepted or borrowed");
        }
    }
}
