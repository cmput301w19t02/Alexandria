package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import ca.ualberta.CMPUT3012019T02.alexandria.model.User;
import ca.ualberta.CMPUT3012019T02.alexandria.model.UserProfile;

public class UserTests {
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
    public void emptyIdConstructorTest() {
        new User("", new UserProfile("John Smith", "john@example.com", "7801234567", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetIsbnTest() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null));
        user.setId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetIsbnTest() {
        User user = new User("6588a715-1651-4d44-94bc-ee0a40176a93", new UserProfile("John Smith", "john@example.com", "7801234567", null));
        user.setId("");
    }
}
