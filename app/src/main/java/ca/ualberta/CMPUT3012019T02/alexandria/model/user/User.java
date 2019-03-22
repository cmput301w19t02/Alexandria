package ca.ualberta.CMPUT3012019T02.alexandria.model.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.ualberta.CMPUT3012019T02.alexandria.model.chatroom.ChatRoomItem;

/**
 * User model class that allows to get and set information
 * relevant to a user
 */
public class User {

    private Map<String, ChatRoomItem> chatRooms;
    private Map<String, OwnedBook> ownedBooks;
    private Map<String, BorrowedBook> borrowedBooks;
    private UserProfile profile;
    private String id;
    private List<String> blockedUsers;


    /**
     * No args constructor to maintain compatibility
     * with Firebase deserializer
     * DO NOT USE
     */
    @Deprecated
    public User(){
        chatRooms = new HashMap<>();
        ownedBooks = new HashMap<>();
        borrowedBooks = new HashMap<>();
        blockedUsers = new ArrayList<>();
    }

    /**
     * User constructor which sets id and user profile,
     * if at least one is null trows an error, throws error if
     * if is empty
     * Initializes HashMaps and blocked user list
     *
     * @param id      user id
     * @param profile user profile
     * @throws IllegalArgumentException ID and profile cannot be null
     */
    public User(String id, UserProfile profile) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }

        this.id = id;
        this.profile = profile;

        chatRooms = new HashMap<>();
        ownedBooks = new HashMap<>();
        borrowedBooks = new HashMap<>();
        blockedUsers = new ArrayList<>();
    }

    /**
     * Gets chat rooms.
     *
     * @return the chat rooms
     */
    public Map<String, ChatRoomItem> getChatRooms() {
        return Collections.unmodifiableMap(chatRooms);
    }

    /**
     * Sets chat rooms.
     *
     * @param chatRooms the chat rooms
     */
    public void setChatRooms(Map<String, ChatRoomItem> chatRooms) {
        if (chatRooms == null){
            throw new IllegalArgumentException("Chat Rooms cannot be null");
        }

        this.chatRooms = chatRooms;
    }

    /**
     * Add chat room.
     *
     * @param user the user id of the other user
     * @param chatRoomItem the uuid
     */
    public void addChatRoom(String user, ChatRoomItem chatRoomItem) {
        this.chatRooms.put(user,chatRoomItem);
    }

    /**
     * Remove chat room.
     *
     * @param user the user id of the other user
     */
    public void removeChatRoom(String user) {
        this.chatRooms.remove(user);
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
     * Sets owned books of the user to the parameter passed,
     * if it is null throws an error
     *
     * @param ownedBooks list of owned books
     * @throws IllegalArgumentException Owned books cannot be null
     */
    public void setOwnedBooks(HashMap<String, OwnedBook> ownedBooks) {
        if (ownedBooks == null){
            throw new IllegalArgumentException("Owned Books cannot be null");
        }

        this.ownedBooks = ownedBooks;
    }

    /**
     * adds an owned book to the user,
     * if it is null throws an error
     *
     * @param ownedBook owned book
     * @throws IllegalArgumentException Owned book cannot be null
     */
    public void addOwnedBook(OwnedBook ownedBook) {
        if (ownedBook == null) {
            throw new IllegalArgumentException("Owned book cannot be null");
        }

        this.ownedBooks.put(ownedBook.getIsbn(), ownedBook);
    }

    /**
     * Removes an owned book from the user,
     * if the book is null throws an error
     *
     * @param ownedBook owned book
     * @throws IllegalArgumentException Owned book cannot be null
     */
    public void removeOwnedBook(OwnedBook ownedBook) {
        if (ownedBook == null) {
            throw new IllegalArgumentException("Owned book cannot be null");
        }

        //TODO should it first check that the book is in the list
        this.ownedBooks.remove(ownedBook.getIsbn());
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
     * Sets borrowed books of the user to the parameter passed,
     * if it is null throws an error
     *
     * @param borrowedBooks list of borrowed books
     * @throws IllegalArgumentException Borrowed books cannot be null
     */
    public void setBorrowedBooks(HashMap<String, BorrowedBook> borrowedBooks) {
        if (borrowedBooks == null){
            throw new IllegalArgumentException("Borrowed Books cannot be null");
        }

        this.borrowedBooks = borrowedBooks;
    }

    /**
     * Adds a borrowed book to the user,
     * if it is null throws an error
     *
     * @param borrowedBook borrowed book
     */
    public void addBorrowedBook(BorrowedBook borrowedBook) {
        if (borrowedBook == null) {
            throw new IllegalArgumentException("Borrowed book cannot be null");
        }

        this.borrowedBooks.put(borrowedBook.getIsbn(), borrowedBook);
    }

    /**
     * Removes a borrowed book from the user,
     * if the book is null throws an error
     *
     * @param borrowedBook borrowed book
     * @throws IllegalArgumentException Borrowed book cannot be null
     */
    public void removeBorrowedBook(BorrowedBook borrowedBook) {
        if (borrowedBook == null) {
            throw new IllegalArgumentException("Borrowed book cannot be null");
        }

        this.borrowedBooks.remove(borrowedBook.getIsbn());
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
     * Sets user profile to the profile passed,
     * if it is null throws an error
     *
     * @param profile user profile
     * @throws IllegalArgumentException Profile cannot be null or empty
     */
    public void setProfile(UserProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null or empty");
        }

        this.profile = profile;
    }

    /**
     * Returns the id of the user
     *
     * @return String id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id of the user
     *
     * @param id id
     */
    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }

        this.id = id;
    }

    /**
     * Returns a list of IDs of users blocked
     *
     * @return List<String>  blocked users id's
     */
    public List<String> getBlockedUsers() {
        return Collections.unmodifiableList(blockedUsers);
    }

    /**
     * Sets the list of blocked users to the value passed,
     * if it is null or empty throws an error
     *
     * @param blockedUsers blocked user id
     * @throws IllegalArgumentException Blocked Users cannot be null or empty
     */
    public void setBlockedUsers(List<String> blockedUsers) {
        if (blockedUsers == null) {
            throw new IllegalArgumentException("Blocked Users cannot be null or empty");
        }

        this.blockedUsers = blockedUsers;
    }

    /**
     * Add an id of the blocked user to the list of the users blocked,
     * if the string is null or empty throws an error
     *
     * @param blockedUser blocked user id
     * @throws IllegalArgumentException Blocked user cannot be null or empty
     */
    public void addBlockedUser(String blockedUser) {
        if (blockedUser == null || blockedUser.trim().isEmpty()) {
            throw new IllegalArgumentException("Blocked user cannot be null or empty");
        }

        if (!isBlockedUser(blockedUser)) {
            this.blockedUsers.add(blockedUser);
        } else {
            System.out.print("User already blocked");
        }
    }

    /**
     * Removes a blocked user from the list if it is there,
     * if the passed value is empty or null throws an errror
     *
     * @param blockedUser blocked user
     * @throws IllegalArgumentException Blocked user cannot be null or empty
     */
    public void removeBlockedUser(String blockedUser) {
        if (blockedUser == null || blockedUser.trim().isEmpty()) {
            throw new IllegalArgumentException("Blocked user cannot be null or empty");
        }

        if (isBlockedUser(blockedUser)) {
            this.blockedUsers.remove(blockedUser);
        } else {
            throw new IllegalArgumentException("User is not blocked");
        }
    }

    /**
     * Checks if the given user appears in the black list
     *
     * @param blockedUser blocked user
     * @return boolean user is blocked
     */
    public boolean isBlockedUser(String blockedUser) {
        if (this.blockedUsers.contains(blockedUser)) {
            return true;
        } else {
            return false;
        }
    }

}