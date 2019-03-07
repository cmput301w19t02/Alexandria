package ca.ualberta.CMPUT3012019T02.alexandria.model.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private List<String> chatRoomList;
    private HashMap<String, OwnedBook> ownedBooks;
    private HashMap<String, BorrowedBook> borrowedBooks;
    private UserProfile profile;
    private String id;
    private List<String> blockedUsers;

    public User(String id, UserProfile profile) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null or empty");
        }

        this.id = id;
        this.profile = profile;

        chatRoomList = new ArrayList<String>();
        ownedBooks = new HashMap<>();
        borrowedBooks = new HashMap<>();
        blockedUsers = new ArrayList<>();
    }

    public List<String> getChatRooms() {
        return chatRoomList;
    }

    public void setChatRooms(List<String> chatRooms) {
        if (chatRooms == null){
            throw new IllegalArgumentException("Chat Rooms cannot be null");
        }

        this.chatRoomList = chatRooms;
    }

    public void addChatRoom(String uuid) {
        this.chatRoomList.add(uuid);
    }

    public void removeChatRoom(String user) {
        this.chatRoomList.remove(user);
    }

    public Map<String, OwnedBook> getOwnedBooks() {
        return Collections.unmodifiableMap(ownedBooks);
    }

    public void setOwnedBooks(HashMap<String, OwnedBook> ownedBooks) {
        if (ownedBooks == null){
            throw new IllegalArgumentException("Owned Books cannot be null");
        }

        this.ownedBooks = ownedBooks;
    }

    public void addOwnedBook(OwnedBook ownedBook) {
        if (ownedBook == null) {
            throw new IllegalArgumentException("Owned book cannot be null");
        }

        this.ownedBooks.put(ownedBook.getIsbn(), ownedBook);
    }

    public void removeOwnedBook(OwnedBook ownedBook) {
        if (ownedBook == null) {
            throw new IllegalArgumentException("Owned book cannot be null");
        }

        this.ownedBooks.remove(ownedBook.getIsbn());
    }

    public Map<String, BorrowedBook> getBorrowedBooks() {
        return Collections.unmodifiableMap(borrowedBooks);
    }

    public void setBorrowedBooks(HashMap<String, BorrowedBook> borrowedBooks) {
        if (borrowedBooks == null){
            throw new IllegalArgumentException("Borrowed Books cannot be null");
        }

        this.borrowedBooks = borrowedBooks;
    }

    public void addBorrowedBook(BorrowedBook borrowedBook) {
        if (borrowedBook == null) {
            throw new IllegalArgumentException("Borrowed book cannot be null");
        }

        this.borrowedBooks.put(borrowedBook.getIsbn(), borrowedBook);
    }

    public void removeBorrowedBook(BorrowedBook borrowedBook) {
        if (borrowedBook == null) {
            throw new IllegalArgumentException("Borrowed book cannot be null");
        }

        this.borrowedBooks.remove(borrowedBook.getIsbn());
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null or empty");
        }

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
        if (blockedUsers == null) {
            throw new IllegalArgumentException("Blocked Users cannot be null or empty");
        }

        this.blockedUsers = blockedUsers;
    }

    public void addBlockedUser(String blockedUser) {
        if (blockedUser == null || blockedUser.trim().isEmpty()) {
            throw new IllegalArgumentException("Blocked user cannot be null or empty");
        }

        this.blockedUsers.add(blockedUser);
    }

    public void removeBlockedUser(String blockedUser) {
        if (blockedUser == null || blockedUser.trim().isEmpty()) {
            throw new IllegalArgumentException("Blocked user cannot be null or empty");
        }
        
        this.blockedUsers.remove(blockedUser);
    }

}