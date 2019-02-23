package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.model.user.BorrowedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.User;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;

import static org.junit.Assert.assertEquals;

public class UserTests {
    @Test
    public void constructorTest(){
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        User user = new User(id, userProfile);
        assertEquals(user.getProfile(), userProfile);
        assertEquals(user.getId(), id);
    }

    @Test
    public void setProfileTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        User user = new User(id, userProfile);
        UserProfile newUserProfile = new UserProfile("Alexa Smith","alex@example.com","7801112233",null);
        user.setProfile(newUserProfile);
        assertEquals(user.getProfile(), newUserProfile);
    }

    @Test
    public void setBlockedUsersTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        User user = new User(id, userProfile);
        List<String> blockedUsers = Arrays.asList("userId1", "userId2", "userId3", "userId4", "userId5");
        user.setBlockedUsers(blockedUsers);
        assertEquals(user.getBlockedUsers(), blockedUsers);
    }

    @Test
    public void addBlockedUsersTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        User user = new User(id, userProfile);
        List<String> blockedUsers = Arrays.asList("userId1", "userId2", "userId3", "userId4", "userId5");
        user.setBlockedUsers(blockedUsers);
        String blockedUser6 = "userId6";
        user.addBlockedUser(blockedUser6);
        blockedUsers.add(blockedUser6);
        assertEquals(user.getBlockedUsers(), blockedUsers);
    }

    @Test
    public void removeBlockedUsersTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        User user = new User(id, userProfile);
        List<String> blockedUsers = Arrays.asList("userId1", "userId2", "userId3", "userId4", "userId5");
        user.setBlockedUsers(blockedUsers);
        user.removeBlockedUser("userId2");
        blockedUsers.remove("userId2");
        assertEquals(user.getBlockedUsers(), blockedUsers);
    }

    @Test
    public void setOwnedBooksTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        User user = new User(id, userProfile);
        List<OwnedBook> ownedBooks = Arrays.asList(new OwnedBook("9780545010221", "Available",null),
                new OwnedBook("9780545012222", "Available",null));
        user.setOwnedBooks(ownedBooks);
        assertEquals(user.getOwnedBooks(), ownedBooks);
    }

    @Test
    public void addOwnedBooksTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        User user = new User(id, userProfile);

        OwnedBook newOwnedBook = new OwnedBook("9780545013333", "Available",null);
        user.addOwnedBook(newOwnedBook);
        List<OwnedBook> ownedBooks = Collections.singletonList(newOwnedBook);
        assertEquals(user.getOwnedBooks(), ownedBooks);
    }

    @Test
    public void removeOwnedBooksTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        User user = new User(id, userProfile);
        List<OwnedBook> ownedBooks = Arrays.asList(new OwnedBook("9780545010221", "Available",null),
                new OwnedBook("9780545012222", "Available",null));
        user.setOwnedBooks(ownedBooks);

        OwnedBook lostOwnedBook = new OwnedBook("9780545012222", "Available",null);
        user.removeOwnedBook(lostOwnedBook);
        ownedBooks.remove(lostOwnedBook);
        assertEquals(user.getOwnedBooks(), ownedBooks);
    }

    @Test
    public void setBorrowedBooksTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        User user = new User(id, userProfile);
        List<BorrowedBook> borrowedBooks = Arrays.asList(new BorrowedBook("9780545010221", "Available",null),
                new BorrowedBook("9780545012222", "Available",null));
        user.setBorrowedBooks(borrowedBooks);
        assertEquals(user.getBorrowedBooks(), borrowedBooks);
    }

    @Test
    public void addBorrowedBooksTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        User user = new User(id, userProfile);

        BorrowedBook newBorrowedBook = new BorrowedBook("9780545013333", "Available",null);
        user.addBorrowedBook(newBorrowedBook);
        List<BorrowedBook> borrowedBooks = Collections.singletonList(newBorrowedBook);
        assertEquals(user.getBorrowedBooks(), borrowedBooks);
    }

    @Test
    public void removeBorrowedBooksTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        User user = new User(id, userProfile);
        List<BorrowedBook> borrowedBooks = Arrays.asList(new BorrowedBook("9780545010221", "Available",null),
                new BorrowedBook("9780545012222", "Available",null));
        user.setBorrowedBooks(borrowedBooks);

        BorrowedBook returnedBorrowedBook = new BorrowedBook("9780545012222", "Available",null);
        user.removeBorrowedBook(returnedBorrowedBook);
        borrowedBooks.remove(returnedBorrowedBook);
        assertEquals(user.getBorrowedBooks(), borrowedBooks);
    }

    @Test
    public void setCharRoomTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        User user = new User(id, userProfile);

        HashMap<String,String> chatRooms = new HashMap<>();
        chatRooms.put("johnsmith","6588a715-1651-4d44-94bc-ee0a40176a93");
        chatRooms.put("htimsnhoj","6588a715-4d44-94bc-1651-ee0a40176a93");
        user.setChatRooms(chatRooms);
        assertEquals(user.getChatRooms(), chatRooms);
    }

    @Test
    public void addCharRoomTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        User user = new User(id, userProfile);

        user.addChatRoom("userId", "6588a715-1651-4d44-94bc-ee0a40176a93");
        assertEquals(user.getChatRooms().get("userId"), "6588a715-1651-4d44-94bc-ee0a40176a93");
    }

    @Test
    public void removeCharRoomTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        User user = new User(id, userProfile);

        HashMap<String,String> chatRooms = new HashMap<>();
        chatRooms.put("johnsmith","6588a715-1651-4d44-94bc-ee0a40176a93");
        chatRooms.put("htimsnhoj","6588a715-4d44-94bc-1651-ee0a40176a93");
        user.setChatRooms(chatRooms);
        user.removeChatRoom("htimsnhoj");
        chatRooms.remove("htimsnhoj");
        assertEquals(user.getChatRooms(), chatRooms);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetBlockedUsersTest() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith","john@example.com","7801234567",null));
        user.getBlockedUsers().add("janesmith");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetBorrowedBooksTest() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith","john@example.com","7801234567",null));
        user.getBorrowedBooks().clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetChatRoomsTest() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith","john@example.com","7801234567",null));
        user.getChatRooms().clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetOwnedBooksTest() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith","john@example.com","7801234567",null));
        user.getOwnedBooks().clear();
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullIdConstructorTest() {
        new User(null, new UserProfile("John Smith", "john@example.com", "7801234567", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullProfileConstructorTest() {
        new User("6588a715-1651-4d44-94bc-ee0a40176a93", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyIdConstructorTest() {
        new User("", new UserProfile("John Smith", "john@example.com", "7801234567", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetUserIdTest() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null));
        user.setId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetUserIdTest() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null));
        user.setId("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullAddOwnedBook() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null));
        user.addOwnedBook(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullRemoveOwnedBook() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null));
        user.removeOwnedBook(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullAddBorrowedBook() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null));
        user.addBorrowedBook(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullRemoveBorrowedBook() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null));
        user.removeBorrowedBook(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullAddBlockedUserd() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null));
        user.addBlockedUser(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullRemoveBlockedUserd() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null));
        user.removeBlockedUser(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetProfile() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null));
        user.setProfile(null);
    }
}
