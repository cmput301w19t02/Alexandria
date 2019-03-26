package ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.user;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User model class that allows to get and set information
 * relevant to a user
 */
public class User {

    private Map<String, String> chatRooms;
    private Map<String, OwnedBook> ownedBooks;
    private Map<String, BorrowedBook> borrowedBooks;
    private UserProfile profile;
    private String id;

    /**
     * No args constructor to maintain compatibility with Firebase deserializer
     * DO NOT USE
     */
    @Deprecated
    public User() {
        chatRooms = new HashMap<>();
        ownedBooks = new HashMap<>();
        borrowedBooks = new HashMap<>();
    }

    /**
     * Gets chat rooms.
     *
     * @return the chat rooms
     */
    public Map<String, String> getChatRooms() {
        return Collections.unmodifiableMap(chatRooms);
    }

    /**
     * Gets owned books.
     *
     * @return the owned books
     */
    public Map<String, OwnedBook> getOwnedBooks() {
        return Collections.unmodifiableMap(ownedBooks);
    }

    /**
     * Gets borrowed books.
     *
     * @return the borrowed books
     */
    public Map<String, BorrowedBook> getBorrowedBooks() {
        return Collections.unmodifiableMap(borrowedBooks);
    }

    /**
     * Returns profile of the user
     *
     * @return UserProfile profile
     */
    public UserProfile getProfile() {
        return profile;
    }

    /**
     * Returns the id of the user
     *
     * @return String id
     */
    public String getId() {
        return id;
    }

}