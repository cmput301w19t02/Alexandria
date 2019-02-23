package ca.ualberta.CMPUT3012019T02.alexandria.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User{
    private HashMap<String,String> chatRooms = new HashMap<>();
    private List<OwnedBook> ownedBooks = new ArrayList();
    private List<BorrowedBook> borrowedBooks = new ArrayList();
    private UserProfile profile;
    private String id;
    private List<String> blockedUsers = new ArrayList<>();

    public User(String id,UserProfile profile) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }

        this.id = id;
        this.profile = profile;
    }

    public Map<String, String> getChatRooms() {
        return Collections.unmodifiableMap(chatRooms);
    }

    public void setChatRooms(HashMap<String, String> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public void addChatRoom(String user, String uuid) {
        this.chatRooms.put(user,uuid);
    }

    public void removeChatRoom(String user) {
        this.chatRooms.remove(user);
    }

    public List<OwnedBook> getOwnedBooks() {
        return Collections.unmodifiableList(ownedBooks);
    }

    public void setOwnedBooks(List<OwnedBook> ownedBooks) {
        this.ownedBooks = ownedBooks;
    }

    public void addOwnedBook(OwnedBook ownedBook) {
        this.ownedBooks.add(ownedBook);
    }

    public void removeOwnedBook(OwnedBook ownedBook) {
        this.ownedBooks.remove(ownedBook);
    }

    public List<BorrowedBook> getBorrowedBooks() {
        return Collections.unmodifiableList(borrowedBooks);
    }

    public void setBorrowedBooks(List<BorrowedBook> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public void addBorrowedBook(BorrowedBook borrowedBook) {
        this.borrowedBooks.add(borrowedBook);
    }

    public void removeBorrowedBook(BorrowedBook borrowedBook) {
        this.borrowedBooks.remove(borrowedBook);
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }

        this.id = id;
    }

    public List<String> getBlockedUsers() {
        return Collections.unmodifiableList(blockedUsers);
    }

    public void setBlockedUsers(List<String> blockedUsers) {
        this.blockedUsers = blockedUsers;
    }

    public void addBlockedUser(String blockedUser) {
        this.blockedUsers.add(blockedUser);
    }

    public void removeBlockedUser(String blockedUser) {
        this.blockedUsers.remove(blockedUser);
    }
}