package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.ualberta.CMPUT3012019T02.alexandria.model.chatroom.ChatRoomItem;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.BorrowedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.User;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTests {
    @Test
    public void constructorTest(){
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith");
        User user = new User(id, userProfile);
        assertEquals(user.getProfile(), userProfile);
        assertEquals(user.getId(), id);
    }

    @Test
    public void setProfileTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith");
        User user = new User(id, userProfile);
        UserProfile newUserProfile = new UserProfile("Alexa Smith","alex@example.com","7801112233",null,"johnsmith");
        user.setProfile(newUserProfile);
        assertEquals(user.getProfile(), newUserProfile);
    }

    @Test
    public void setBlockedUsersTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith");
        User user = new User(id, userProfile);
        List<String> blockedUsers = Arrays.asList("userId1", "userId2", "userId3", "userId4", "userId5");
        user.setBlockedUsers(blockedUsers);
        assertEquals(user.getBlockedUsers(), blockedUsers);
    }

    @Test
    public void addBlockedUsersTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith");
        User user = new User(id, userProfile);

        String blockedUser6 = "userId6";
        user.addBlockedUser(blockedUser6);
        List<String> blockedUsers = Collections.singletonList(blockedUser6);
        assertEquals(user.getBlockedUsers(), blockedUsers);
    }

    @Test
    public void removeBlockedUsersTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith");
        User user = new User(id, userProfile);
        List<String> blockedUsers = new ArrayList<>();
        blockedUsers.add("userId1");
        blockedUsers.add("userId2");
        blockedUsers.add("userId3");
        user.setBlockedUsers(blockedUsers);

        String blockedUser = "userId2";
        user.removeBlockedUser(blockedUser);
        blockedUsers.remove(blockedUser);
        assertEquals(user.getBlockedUsers(), blockedUsers);
    }

    @Test
    public void setOwnedBooksTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith");
        User user = new User(id, userProfile);
        HashMap<String, OwnedBook> ownedBooks = new HashMap<>();
        ownedBooks.put("9780545010221", new OwnedBook("9780545010221", "available", id, null));
        ownedBooks.put("9780545012222", new OwnedBook("9780545012222", "available", id, null));
        user.setOwnedBooks(ownedBooks);
        assertEquals(user.getOwnedBooks(), ownedBooks);
    }

    @Test
    public void addOwnedBooksTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith");
        User user = new User(id, userProfile);

        OwnedBook newOwnedBook = new OwnedBook("9780545013333", "available", id, null);
        user.addOwnedBook(newOwnedBook);
        Map<String, OwnedBook> ownedBooks = Collections.singletonMap(newOwnedBook.getIsbn(), newOwnedBook);
        assertEquals(user.getOwnedBooks(), ownedBooks);
    }

    @Test
    public void removeOwnedBooksTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith");
        User user = new User(id, userProfile);
        HashMap<String, OwnedBook> ownedBooks = new HashMap<>();
        ownedBooks.put("9780545010221", new OwnedBook("9780545010221", "available", id, null));
        ownedBooks.put("9780545012222", new OwnedBook("9780545012222", "available", id, null));
        user.setOwnedBooks(ownedBooks);

        OwnedBook lostOwnedBook = new OwnedBook("9780545012222", "available", id, null);
        user.removeOwnedBook(lostOwnedBook);
        ownedBooks.remove(lostOwnedBook.getIsbn());
        assertEquals(user.getOwnedBooks(), ownedBooks);
    }

    @Test
    public void setBorrowedBooksTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith");
        User user = new User(id, userProfile);
        HashMap<String, BorrowedBook> borrowedBooks = new HashMap<>();
        borrowedBooks.put("9780545010221", new BorrowedBook("9780545010221", "available","eQgZfhN2Yng9TPHcXvfBZs5ZKxj1"));
        borrowedBooks.put("9780545012222", new BorrowedBook("9780545012222", "available","eQgZfhN2Yng9TPHcXvfBZs5ZKxj1"));
        user.setBorrowedBooks(borrowedBooks);
        assertEquals(user.getBorrowedBooks(), borrowedBooks);
    }

    @Test
    public void addBorrowedBooksTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith");
        User user = new User(id, userProfile);

        BorrowedBook newBorrowedBook = new BorrowedBook("9780545013333", "available","eQgZfhN2Yng9TPHcXvfBZs5ZKxj1");
        user.addBorrowedBook(newBorrowedBook);
        Map<String, BorrowedBook> borrowedBooks = Collections.singletonMap(newBorrowedBook.getIsbn(), newBorrowedBook);
        assertEquals(user.getBorrowedBooks(), borrowedBooks);
    }

    @Test
    public void removeBorrowedBooksTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith");
        User user = new User(id, userProfile);
        HashMap<String, BorrowedBook> borrowedBooks = new HashMap<>();
        borrowedBooks.put("9780545010221", new BorrowedBook("9780545010221", "available","eQgZfhN2Yng9TPHcXvfBZs5ZKxj1"));
        borrowedBooks.put("9780545012222", new BorrowedBook("9780545012222", "available","eQgZfhN2Yng9TPHcXvfBZs5ZKxj1"));
        user.setBorrowedBooks(borrowedBooks);

        BorrowedBook returnedBorrowedBook = new BorrowedBook("9780545012222", "available","eQgZfhN2Yng9TPHcXvfBZs5ZKxj1");
        user.removeBorrowedBook(returnedBorrowedBook);
        borrowedBooks.remove(returnedBorrowedBook.getIsbn());
        assertEquals(user.getBorrowedBooks(), borrowedBooks);
    }

    @Test
    public void setCharRoomTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith");
        User user = new User(id, userProfile);
        ChatRoomItem chatRoomItem = new ChatRoomItem("TestRoom213141", id, "John Smith", id, "Hit Menshoj", false);

        Map<String, Map<String, ChatRoomItem>> chatRooms = new HashMap<>();
        Map<String, ChatRoomItem> chatRoom = new HashMap<>();
        chatRoom.put("testchatId1",chatRoomItem);
        chatRooms.put("johnsmith",chatRoom);
        chatRooms.put("htimsnhoj",chatRoom);
        user.setChatRooms(chatRooms);
        assertEquals(user.getChatRooms(), chatRooms);
    }

    @Test
    public void addCharRoomTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith");
        User user = new User(id, userProfile);
        ChatRoomItem chatRoomItem = new ChatRoomItem("TestRoom213141", id, "John Smith", id, "Hit Menshoj", false);
        user.addChatRoom("userId", chatRoomItem);
        assertEquals(user.getChatRooms().get("userId"), chatRoomItem);
    }

    @Test
    public void removeCharRoomTest() {
        String id = "6588a715-1651-4d44-94bc-ee0a40176a93";
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith");
        User user = new User(id, userProfile);
        ChatRoomItem chatRoomItem = new ChatRoomItem("TestRoom213141", id, "John Smith", id, "Hit Menshoj", false);

        Map<String, Map<String, ChatRoomItem>> chatRooms = new HashMap<>();
        Map<String, ChatRoomItem> chatRoom = new HashMap<>();
        chatRoom.put("testchatId1",chatRoomItem);
        chatRooms.put("johnsmith",chatRoom);
        chatRooms.put("htimsnhoj",chatRoom);
        user.setChatRooms(chatRooms);
        user.removeChatRoom("htimsnhoj");
        chatRooms.remove("htimsnhoj");
        assertEquals(user.getChatRooms(), chatRooms);
    }

    @Test
    public void isBlockedUserNegativeTest() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith"));
        user.addBlockedUser("userID");
        assertFalse(user.isBlockedUser("janesmith"));
    }

    @Test
    public void isBlockedUserPositiveTest() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith"));
        user.addBlockedUser("userID");
        assertTrue(user.isBlockedUser("userID"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetBlockedUsersTest() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith"));
        user.getBlockedUsers().add("janesmith");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetBorrowedBooksTest() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith"));
        user.getBorrowedBooks().clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetChatRoomsTest() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith"));
        user.getChatRooms().clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetOwnedBooksTest() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith"));
        user.getOwnedBooks().clear();
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullIdConstructorTest() {
        new User(null, new UserProfile("John Smith", "john@example.com", "7801234567", null,"johnsmith"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullProfileConstructorTest() {
        new User("6588a715-1651-4d44-94bc-ee0a40176a93", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyIdConstructorTest() {
        new User("", new UserProfile("John Smith", "john@example.com", "7801234567", null,"johnsmith"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetUserIdTest() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null,"johnsmith"));
        user.setId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetUserIdTest() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null,"johnsmith"));
        user.setId("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullAddOwnedBook() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null,"johnsmith"));
        user.addOwnedBook(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullRemoveOwnedBook() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null,"johnsmith"));
        user.removeOwnedBook(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullAddBorrowedBook() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null,"johnsmith"));
        user.addBorrowedBook(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullRemoveBorrowedBook() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null,"johnsmith"));
        user.removeBorrowedBook(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullAddBlockedUserd() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null,"johnsmith"));
        user.addBlockedUser(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullRemoveBlockedUserd() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null,"johnsmith"));
        user.removeBlockedUser(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetProfile() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null,"johnsmith"));
        user.setProfile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetBlockedUsers() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null,"johnsmith"));
        user.setBlockedUsers(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetBorrowedBooks() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null,"johnsmith"));
        user.setBorrowedBooks(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetChatRooms() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null,"johnsmith"));
        user.setChatRooms(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetOwnedBooks() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null,"johnsmith"));
        user.setOwnedBooks(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeUnexistentBlockedUser() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null,"johnsmith"));
        user.removeBlockedUser("test");
    }

}
