package ca.ualberta.CMPUT3012019T02.alexandria.model.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;

/**
 * This class represents a relationship between a User and a Book, where the User has ownership of a
 * particular Book. It also contains specific data that a User can attach to a Book they own.
 */
public class OwnedBook extends UserBook {

    private List<String> usersRequesting;
    private List<String> images;
    private String userBorrowing;

    /**
     * No args constructor to maintain compatibility with Firebase deserializer
     * TO BE USED BY BOOK CONTROLLER ONLY
     */
    @Deprecated
    public OwnedBook() {
        super(null, null, null);
        usersRequesting = new ArrayList<>();
        images = new ArrayList<>();
    }

    /**
     * Create a new OwnedBook owned by the current logged in user
     * @param isbn isbn of the book
     */
    public OwnedBook(String isbn) {
        super(isbn, "available", UserController.getInstance().getMyId());

        this.userBorrowing = null;
        this.usersRequesting = new ArrayList<>();
        this.images = new ArrayList<>();
    }

    /**
     * Creates a new OwnedBook with the given isbn, status, owner, and user borrowing
     * @param isbn isbn of the book
     * @param status status of the book
     * @param owner owner of the book
     * @param userBorrowing user borrowing this book
     */
    public OwnedBook(String isbn, String status, String owner, String userBorrowing) {
        super(isbn, status, owner);
        this.userBorrowing = userBorrowing;
        this.usersRequesting = new ArrayList<>();
        this.images = new ArrayList<>();
    }

    /**
     * Gets a list of users requesting this owned book
     * @return a list of user ids
     */
    public List<String> getUsersRequesting() {
        return Collections.unmodifiableList(usersRequesting);
    }

    /**
     * Sets the list of users requesting this owned book
     * TO BE USED BY BOOK CONTROLLER ONLY
     * @param usersRequesting a list of user ids
     */
    @Deprecated
    public void setUsersRequesting(List<String> usersRequesting) {
        this.usersRequesting = usersRequesting;
    }

    /**
     * Sets the list of users requesting this owned book
     * TO BE USED BY BOOK CONTROLLER ONLY
     * @param userRequesting a user id
     */
    @Deprecated
    public void addUserRequesting(String userRequesting) {
        this.usersRequesting.add(userRequesting);
    }

    /**
     * Sets the list of users requesting this owned book
     * TO BE USED BY BOOK CONTROLLER ONLY
     * @param userRequesting a user id
     */
    @Deprecated
    public void removeUserRequesting(String userRequesting) {
        this.usersRequesting.remove(userRequesting);
    }

    /**
     * Get images of this owned book
     * @return a list of image ids
     */
    public List<String> getImages() {
        return Collections.unmodifiableList(images);
    }

    /**
     * Sets the list of images of this owned book
     * @param images a list of image ids
     */
    public void setImages(List<String> images) {
        this.images = images;
    }

    /**
     * Add an image to the list of images of this owned book
     * @param imageId image id to add
     */
    public void addImage(String imageId) {
        this.images.add(imageId);
    }

    /**
     * Remove an image from the list of images of this owned book
     * @param imageId image id of the image to remove
     */
    public void removeImage(String imageId) {
        this.images.remove(imageId);
    }

    /**
     * Gets the id of the user borrowing this owned book
     * @return a user id
     */
    public String getUserBorrowing() {
        return userBorrowing;
    }

    /**
     * Sets the id of the user borrowing this owned book
     * TO BE USED BY BOOK CONTROLLER ONLY
     * @param userBorrowing
     */
    @Deprecated
    public void setUserBorrowing(String userBorrowing) {
        this.userBorrowing = userBorrowing;
    }

}
