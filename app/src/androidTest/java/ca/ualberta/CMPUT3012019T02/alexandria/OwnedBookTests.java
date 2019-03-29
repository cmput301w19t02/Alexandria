package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OwnedBookTests {

    private final String TEST_USERNAME = "342c495e_fdfa_44fa_8ab6_a8a74385b1be";
    private final String TEST_PASSWORD = "c32271c9-eb3f-4313-8f1e-db54c0158e5b";

    @Before
    public void authenticate() throws InterruptedException, ExecutionException, TimeoutException {
        UserController.getInstance().authenticate(TEST_USERNAME, TEST_PASSWORD).get(5, TimeUnit.SECONDS);
    }

    @Test
    public void simpleConstructorTest() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        assertEquals("0123456789123", ownedBook.getIsbn());
        assertEquals("available", ownedBook.getStatus());
        assertEquals(UserController.getInstance().getMyId(), ownedBook.getOwner());
        assertNull(ownedBook.getUserBorrowing());
        assertNull(ownedBook.getImageId());
        assertTrue(ownedBook.getRequestingUsers().isEmpty());
    }

    @Test
    public void fullConstructorTest() {
        OwnedBook ownedBook = new OwnedBook("0123456789123", "fake-image-id");
        assertEquals("0123456789123", ownedBook.getIsbn());
        assertEquals("available", ownedBook.getStatus());
        assertEquals(UserController.getInstance().getMyId(), ownedBook.getOwner());
        assertNull(ownedBook.getUserBorrowing());
        assertEquals("fake-image-id", ownedBook.getImageId());
        assertTrue(ownedBook.getRequestingUsers().isEmpty());
    }

    @Test
    public void setImageIdTest() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.setImageId("a-brand-new-image-id");
        assertEquals("a-brand-new-image-id", ownedBook.getImageId());
    }

    @Test
    public void setNullImageIdTest() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.setImageId(null);
        assertNull(ownedBook.getImageId());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetUsersRequestingTest() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.getRequestingUsers().add("testing");
    }

    // Unable to test OwnedBook#updateState with its encapsulation

    @Test
    public void testCoreStateTransitions() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");

        // Default state: available
        assertEquals("available", ownedBook.getStatus());
        assertTrue(ownedBook.getRequestingUsers().isEmpty());
        assertNull(ownedBook.getUserBorrowing());

        // Add a requesting user. Should become: requested
        ownedBook.addRequest("test-user-id");
        assertTrue(ownedBook.getRequestingUsers().contains("test-user-id"));
        assertEquals("requested", ownedBook.getStatus());
        assertNull(ownedBook.getUserBorrowing());

        // Remove the only requesting user. Should become: available
        ownedBook.cancelRequest("test-user-id");
        assertEquals("available", ownedBook.getStatus());
        assertTrue(ownedBook.getRequestingUsers().isEmpty());
        assertNull(ownedBook.getUserBorrowing());

        // Add a requesting user. Should become: requested
        ownedBook.addRequest("test-user-id");
        assertTrue(ownedBook.getRequestingUsers().contains("test-user-id"));
        assertEquals("requested", ownedBook.getStatus());
        assertNull(ownedBook.getUserBorrowing());

        // Add another requesting user. Should stay: requested
        ownedBook.addRequest("test-user-id-two");
        assertTrue(ownedBook.getRequestingUsers().contains("test-user-id-two"));
        assertEquals(2, ownedBook.getRequestingUsers().size());
        assertEquals("requested", ownedBook.getStatus());
        assertNull(ownedBook.getUserBorrowing());

        // Add another requesting user. Should stay: requested
        ownedBook.addRequest("test-user-id-three");
        assertTrue(ownedBook.getRequestingUsers().contains("test-user-id-three"));
        assertEquals(3, ownedBook.getRequestingUsers().size());
        assertEquals("requested", ownedBook.getStatus());
        assertNull(ownedBook.getUserBorrowing());

        // Cancel one of the requests. Should stay: requested
        ownedBook.cancelRequest("test-user-id-two");
        assertTrue(ownedBook.getRequestingUsers().contains("test-user-id-three"));
        assertEquals(2, ownedBook.getRequestingUsers().size());
        assertEquals("requested", ownedBook.getStatus());
        assertNull(ownedBook.getUserBorrowing());

        // Accept a user's request. Should become: accepted
        ownedBook.acceptRequest("test-user-id");
        assertEquals("accepted", ownedBook.getStatus());
        assertTrue(ownedBook.getRequestingUsers().isEmpty());
        assertEquals("test-user-id", ownedBook.getUserBorrowing());

        // Exchange. Should become: borrowed
        ownedBook.setScanned(true);
        ownedBook.exchange(true);
        assertEquals("borrowed", ownedBook.getStatus());
        assertTrue(ownedBook.getRequestingUsers().isEmpty());
        assertEquals("test-user-id", ownedBook.getUserBorrowing());

        // Return. Should become: available
        ownedBook.setScanned(true);
        ownedBook.beReturned(true);
        assertEquals("available", ownedBook.getStatus());
        assertTrue(ownedBook.getRequestingUsers().isEmpty());
        assertNull(ownedBook.getUserBorrowing());

        // Add a requesting user. Should become: requested
        ownedBook.addRequest("test-user-id");
        assertTrue(ownedBook.getRequestingUsers().contains("test-user-id"));
        assertEquals("requested", ownedBook.getStatus());
        assertNull(ownedBook.getUserBorrowing());

        // Accept a user's request. Should become: accepted
        ownedBook.acceptRequest("test-user-id");
        assertEquals("accepted", ownedBook.getStatus());
        assertTrue(ownedBook.getRequestingUsers().isEmpty());
        assertEquals("test-user-id", ownedBook.getUserBorrowing());

        // Cancel the accepted user's request. Should become: available
        ownedBook.cancelRequest("test-user-id");
        assertEquals("available", ownedBook.getStatus());
        assertTrue(ownedBook.getRequestingUsers().isEmpty());
        assertNull(ownedBook.getUserBorrowing());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAvailableCancelRequest() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        // Default state: available
        ownedBook.cancelRequest("test-user-id");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAvailableAcceptRequest() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        // Default state: available
        ownedBook.acceptRequest("test-user-id");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAvailableExchange() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        // Default state: available
        ownedBook.setScanned(true);
        ownedBook.exchange(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAvailableReturn() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        // Default state: available
        ownedBook.setScanned(true);
        ownedBook.beReturned(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestedAddDuplicateRequest() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.addRequest("test-user-id"); // State transition to: requested

        ownedBook.addRequest("test-user-id");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestedCancelNonExistingRequest() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.addRequest("test-user-id"); // State transition to: requested

        ownedBook.cancelRequest("fake-user-id");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestedAcceptNonExistingRequest() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.addRequest("test-user-id"); // State transition to: requested

        ownedBook.acceptRequest("fake-user-id");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRequestedExchange() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.addRequest("test-user-id"); // State transition to: requested

        ownedBook.exchange(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRequestedBeReturned() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.addRequest("test-user-id"); // State transition to: requested

        ownedBook.exchange(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAcceptedAddRequest() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.addRequest("test-user-id"); // State transition to: requested
        ownedBook.acceptRequest("test-user-id"); // State transition to: accepted

        ownedBook.addRequest("another-requester");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAcceptedCancelNonBorrowerRequest() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.addRequest("test-user-id"); // State transition to: requested
        ownedBook.acceptRequest("test-user-id"); // State transition to: accepted

        ownedBook.cancelRequest("another-requester");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAcceptedAcceptRequest() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.addRequest("test-user-id"); // State transition to: requested
        ownedBook.acceptRequest("test-user-id"); // State transition to: accepted

        ownedBook.acceptRequest("test-user-id");
    }

    @Test(expected = IllegalStateException.class)
    public void testAcceptedExchangeWithoutOwnerScan() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.addRequest("test-user-id"); // State transition to: requested
        ownedBook.acceptRequest("test-user-id"); // State transition to: accepted

        ownedBook.exchange(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testAcceptedExchangeWithoutBorrowerScan() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.addRequest("test-user-id"); // State transition to: requested
        ownedBook.acceptRequest("test-user-id"); // State transition to: accepted

        ownedBook.setScanned(true);
        ownedBook.exchange(false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAcceptedBeReturned() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.addRequest("test-user-id"); // State transition to: requested
        ownedBook.acceptRequest("test-user-id"); // State transition to: accepted

        ownedBook.beReturned(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBorrowedAddRequest() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.addRequest("test-user-id"); // State transition to: requested
        ownedBook.acceptRequest("test-user-id"); // State transition to: accepted
        ownedBook.setScanned(true);
        ownedBook.exchange(true); // State transition to: borrowed

        ownedBook.addRequest("new-user");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBorrowedCancelRequest() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.addRequest("test-user-id"); // State transition to: requested
        ownedBook.acceptRequest("test-user-id"); // State transition to: accepted
        ownedBook.setScanned(true);
        ownedBook.exchange(true); // State transition to: borrowed

        ownedBook.cancelRequest("bad-user");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBorrowedAcceptRequest() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.addRequest("test-user-id"); // State transition to: requested
        ownedBook.acceptRequest("test-user-id"); // State transition to: accepted
        ownedBook.setScanned(true);
        ownedBook.exchange(true); // State transition to: borrowed

        ownedBook.acceptRequest("test-user-id");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBorrowedExchange() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.addRequest("test-user-id"); // State transition to: requested
        ownedBook.acceptRequest("test-user-id"); // State transition to: accepted
        ownedBook.setScanned(true);
        ownedBook.exchange(true); // State transition to: borrowed

        ownedBook.exchange(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testBorrowedBeReturnedWithoutOwnerScan() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.addRequest("test-user-id"); // State transition to: requested
        ownedBook.acceptRequest("test-user-id"); // State transition to: accepted
        ownedBook.setScanned(true);
        ownedBook.exchange(true); // State transition to: borrowed

        ownedBook.beReturned(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testBorrowedBeReturnedWithoutBorrowerScan() {
        OwnedBook ownedBook = new OwnedBook("0123456789123");
        ownedBook.addRequest("test-user-id"); // State transition to: requested
        ownedBook.acceptRequest("test-user-id"); // State transition to: accepted
        ownedBook.setScanned(true);
        ownedBook.exchange(true); // State transition to: borrowed

        ownedBook.setScanned(true);
        ownedBook.beReturned(false);
    }
}
