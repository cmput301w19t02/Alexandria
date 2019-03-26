package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.Book;
import ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.user.BorrowedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.user.OwnedBook;
import java9.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BookControllerTests {

    private class LoginInfo {
        private final String username;
        private final String password;
        LoginInfo(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    private final Book theHobbit = new Book("9780547928227", "The Hobbit", "J.R.R. Tolkien", "Bilbo Baggins is a hobbit who enjoys a comfortable, unambitious life, rarely traveling further than the pantry of his hobbit-hole in Bag End. But his contentment is disturbed when the wizard, Gandalf, and a company of thirteen dwarves arrive on his doorstep one day to whisk him away on an unexpected journey ‘there and back again’. They have a plot to raid the treasure hoard of Smaug the Magnificent, a large and very dangerous dragon.", "ac543736-b355-4eac-b9b8-a11165f0aa43");
    private final Book chamberOfSecrets = new Book("0747538492", "Harry Potter and the Chamber of Secrets", "J. K. Rowling", "Harry Potter and the Chamber of Secrets is a fantasy novel written by British author J. K. Rowling and the second novel in the Harry Potter series. The plot follows Harry's second year at Hogwarts School of Witchcraft and Wizardry, during which a series of messages on the walls of the school's corridors warn that the \"Chamber of Secrets\" has been opened and that the \"heir of Slytherin\" would kill all pupils who do not come from all-magical families. These threats are found after attacks which leave residents of the school petrified. Throughout the year, Harry and his friends Ron and Hermione investigate the attacks.", "a4d0131b-ec92-4f7d-90cf-be1fb3f6427d");
    private final Book prisonerOfAzkaban = new Book("0747542155", "Harry Potter and the Prisoner of Azkaban", "J. K. Rowling", "Harry Potter and the Prisoner of Azkaban is a fantasy novel written by British author J. K. Rowling and the third in the Harry Potter series. The book follows Harry Potter, a young wizard, in his third year at Hogwarts School of Witchcraft and Wizardry. Along with friends Ronald Weasley and Hermione Granger, Harry investigates Sirius Black, an escaped prisoner from Azkaban who they believe is one of Lord Voldemort's old allies.", "209f71c9-2f68-4d8b-9ac9-ee7d68202474");
    private final Book gobletOfFire = new Book("074754624X", "Harry Potter and the Goblet of Fire", "J. K. Rowling", "Harry Potter and the Goblet of Fire is a fantasy book written by British author J. K. Rowling and the fourth novel in the Harry Potter series. It follows Harry Potter, a wizard in his fourth year at Hogwarts School of Witchcraft and Wizardry and the mystery surrounding the entry of Harry's name into the Triwizard Tournament, in which he is forced to compete.", "e0fb4b87-13aa-4126-b530-b3d296eb5440");

    private BookController bookController = BookController.getInstance();
    private UserController userController = UserController.getInstance();

    /* Utility methods */

    private LoginInfo generateRandomUser() throws InterruptedException, ExecutionException, TimeoutException {
        // Assumes that UserController is working correctly
        String username = "test_" + UUID.randomUUID().toString().replace('-', '_');
        String password = UUID.randomUUID().toString();
        String email = username + "@example.com";
        userController.createUser("John Smith", email, "7801234567", null, username, password).get(5, TimeUnit.SECONDS);
        return new LoginInfo(username, password);
    }

    private void authenticate(LoginInfo loginInfo) throws InterruptedException, ExecutionException, TimeoutException {
        userController.deauthenticate(); // Invalidates cache as well
        userController.authenticate(loginInfo.username, loginInfo.password).get(5, TimeUnit.SECONDS);
    }

    private void assertBooksEqual(Book expected, Book actual) {
        assertEquals(expected.getIsbn(), actual.getIsbn());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getImageId(), actual.getImageId());
    }

    private Book generateRandomBook() {
        return new Book("test_" + UUID.randomUUID().toString().replace('-', '_'), "Test Book", "BookControllerTests.java", "This book was automatically generated by a unit test for testing purposes. This book can safely be deleted.", null);
    }

    @Before
    public void prepare() {
        userController.deauthenticate();
    }

    @After
    public void cleanUp() {
        userController.deauthenticate();
    }

    @Test
    public void testCoreTransactionProcess() throws InterruptedException, ExecutionException, TimeoutException {
        /* Methods tested
            -   BookController#addBook
            -   BookController#getBook
            -   BookController#getMyBorrowedBook
            -   BookController#addMyOwnedBook
            -   BookController#getUserOwnedBook
            -   BookController#requestBook
            -   BookController#cancelRequest
            -   BookController#acceptRequest
            -   BookController#declineRequest
            -   BookController#scanMyBorrowedBook
            -   BookController#scanMyOwnedBook
            -   BookController#exchange
            -   BookController#returnBook
        */

        // Create users to test with
        LoginInfo owner = generateRandomUser();
        LoginInfo borrower1 = generateRandomUser();
        LoginInfo borrower2 = generateRandomUser();
        LoginInfo borrower3 = generateRandomUser();
        LoginInfo borrower4 = generateRandomUser();

        // Get user ids
        authenticate(owner);
        String ownerId = userController.getMyId();
        authenticate(borrower1);
        String borrower1Id = userController.getMyId();
        authenticate(borrower2);
        String borrower2Id = userController.getMyId();
        authenticate(borrower3);
        String borrower3Id = userController.getMyId();
        authenticate(borrower4);
        String borrower4Id = userController.getMyId();

        // Create new book to test with
        Book book = generateRandomBook();

        bookController.addBook(book).get(5, TimeUnit.SECONDS);
        Book verify = bookController.getBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
        assertBooksEqual(book, verify); // Tests BookController#addBook and BookController#getBook

        /* Add an owned book. Make a book request. Borrowed and Owned Book status transition from 'available' to 'requested' */

        authenticate(owner); { // Tests BookController#addMyOwnedBook
            bookController.addMyOwnedBook(new OwnedBook(book.getIsbn())).get(5, TimeUnit.SECONDS);
        }

        authenticate(borrower1); { // Tests BookController#requestBook with the book being available
            bookController.requestBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS);
        }

        /* Verify State */ {

            /* authenticate(borrower1); */ { // Tests BookController#getMyBorrowedBook
                BorrowedBook borrowedBook = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
                assertEquals(ownerId, borrowedBook.getOwner());
                assertEquals("requested", borrowedBook.getStatus());
                assertFalse(borrowedBook.getScanned());
            }

            authenticate(owner); { // Tests BookController#getMyOwnedBook
                OwnedBook ownedBook = bookController.getMyOwnedBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
                assertEquals(ownerId, ownedBook.getOwner());
                assertEquals("requested", ownedBook.getStatus());
                assertFalse(ownedBook.getScanned());
                assertNull(ownedBook.getUserBorrowing());
                assertTrue(ownedBook.getRequestingUsers().contains(borrower1Id));
                assertEquals(1, ownedBook.getRequestingUsers().size());
                assertTrue(ownedBook.getRemovedRequests().isEmpty());
            }

        }

        /* Cancel the only request. Borrowed and Owned Book status transition from 'requested' to 'available' */

        authenticate(borrower1); { // Tests BookController#cancelRequest with the book only having one request
            bookController.cancelRequest(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS);
        }

        /* Verify State */ {
            /* authenticate(borrower1); */ {
                Optional<BorrowedBook> borrowedBookOptional = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS);
                assertTrue(borrowedBookOptional.isEmpty());
            }

            OwnedBook ownedBook = bookController.getUserOwnedBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS).get(); // Tests BookController#getUserOwnedBook
            assertEquals(ownerId, ownedBook.getOwner());
            assertEquals("available", ownedBook.getStatus());
            assertFalse(ownedBook.getScanned());
            assertNull(ownedBook.getUserBorrowing());
            assertTrue(ownedBook.getRequestingUsers().isEmpty());
            assertTrue(ownedBook.getRemovedRequests().isEmpty());
        }

        /* Make a book request. Borrowed and Owned Book status transition from 'available' to 'requested' */

        authenticate(borrower1); {
            bookController.requestBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS);
        }

        /* Verify State */ {

            /* authenticate(borrower1); */ { // Tests BookController#getMyBorrowedBook
                BorrowedBook borrowedBook = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
                assertEquals(ownerId, borrowedBook.getOwner());
                assertEquals("requested", borrowedBook.getStatus());
                assertFalse(borrowedBook.getScanned());
            }

            authenticate(owner); { // Tests BookController#getMyOwnedBook
                OwnedBook ownedBook = bookController.getMyOwnedBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
                assertEquals(ownerId, ownedBook.getOwner());
                assertEquals("requested", ownedBook.getStatus());
                assertFalse(ownedBook.getScanned());
                assertNull(ownedBook.getUserBorrowing());
                assertTrue(ownedBook.getRequestingUsers().contains(borrower1Id));
                assertEquals(1, ownedBook.getRequestingUsers().size());
                assertTrue(ownedBook.getRemovedRequests().isEmpty());
            }

        }

        /* Decline the only request. Borrowed and Owned Book status transition from 'requested' to 'available' */

        authenticate(owner); { // Tests BookController#declineRequest with the book only having one request
            bookController.declineRequest(book.getIsbn(), borrower1Id).get(5, TimeUnit.SECONDS);
        }

        /* Verify State */ {
            /* authenticate(borrower1); */ {
                Optional<BorrowedBook> borrowedBookOptional = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS);
                assertTrue(borrowedBookOptional.isEmpty());
            }

            OwnedBook ownedBook = bookController.getUserOwnedBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS).get(); // Tests BookController#getUserOwnedBook
            assertEquals(ownerId, ownedBook.getOwner());
            assertEquals("available", ownedBook.getStatus());
            assertFalse(ownedBook.getScanned());
            assertNull(ownedBook.getUserBorrowing());
            assertTrue(ownedBook.getRequestingUsers().isEmpty());
            assertTrue(ownedBook.getRemovedRequests().isEmpty());
        }

        /* Make multiple requests. Borrowed and Owned Book status transition from 'available' to 'requested' */

        // Tests BookController#requestBook with the book having multiple requests

        authenticate(borrower1); {
            bookController.requestBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS);
        }

        authenticate(borrower2); {
            bookController.requestBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS);
        }

        authenticate(borrower3); {
            bookController.requestBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS);
        }

        authenticate(borrower4); {
            bookController.requestBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS);
        }

        /* Verify State */ {
            /* authenticate(borrower4); */ {
                BorrowedBook borrowedBook = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
                assertEquals(ownerId, borrowedBook.getOwner());
                assertEquals("requested", borrowedBook.getStatus());
                assertFalse(borrowedBook.getScanned());
            }

            authenticate(borrower1); {
                BorrowedBook borrowedBook = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
                assertEquals(ownerId, borrowedBook.getOwner());
                assertEquals("requested", borrowedBook.getStatus());
                assertFalse(borrowedBook.getScanned());
            }

            authenticate(borrower2); {
                BorrowedBook borrowedBook = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
                assertEquals(ownerId, borrowedBook.getOwner());
                assertEquals("requested", borrowedBook.getStatus());
                assertFalse(borrowedBook.getScanned());
            }

            authenticate(borrower3); {
                BorrowedBook borrowedBook = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
                assertEquals(ownerId, borrowedBook.getOwner());
                assertEquals("requested", borrowedBook.getStatus());
                assertFalse(borrowedBook.getScanned());
            }

            OwnedBook ownedBook = bookController.getUserOwnedBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS).get();
            assertEquals(ownerId, ownedBook.getOwner());
            assertEquals("requested", ownedBook.getStatus());
            assertFalse(ownedBook.getScanned());
            assertNull(ownedBook.getUserBorrowing());
            assertTrue(ownedBook.getRequestingUsers().contains(borrower1Id));
            assertTrue(ownedBook.getRequestingUsers().contains(borrower2Id));
            assertTrue(ownedBook.getRequestingUsers().contains(borrower3Id));
            assertTrue(ownedBook.getRequestingUsers().contains(borrower4Id));
            assertEquals(4, ownedBook.getRequestingUsers().size());
            assertTrue(ownedBook.getRemovedRequests().isEmpty());
        }

        /* Cancel one of the requests. Borrowed and Owned Book status stay the same */

        authenticate(borrower4); { // Tests BookController#cancelRequest when there are multiple users requesting the book
            bookController.cancelRequest(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS);
        }

        /* Verify State */ {
            /* authenticate(borrower4); */ {
                Optional<BorrowedBook> borrowedBookOptional = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS);
                assertTrue(borrowedBookOptional.isEmpty());
            }

            authenticate(borrower1); {
                BorrowedBook borrowedBook = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
                assertEquals(ownerId, borrowedBook.getOwner());
                assertEquals("requested", borrowedBook.getStatus());
                assertFalse(borrowedBook.getScanned());
            }

            authenticate(borrower2); {
                BorrowedBook borrowedBook = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
                assertEquals(ownerId, borrowedBook.getOwner());
                assertEquals("requested", borrowedBook.getStatus());
                assertFalse(borrowedBook.getScanned());
            }

            authenticate(borrower3); {
                BorrowedBook borrowedBook = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
                assertEquals(ownerId, borrowedBook.getOwner());
                assertEquals("requested", borrowedBook.getStatus());
                assertFalse(borrowedBook.getScanned());
            }

            OwnedBook ownedBook = bookController.getUserOwnedBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS).get();
            assertEquals(ownerId, ownedBook.getOwner());
            assertEquals("requested", ownedBook.getStatus());
            assertFalse(ownedBook.getScanned());
            assertNull(ownedBook.getUserBorrowing());
            assertTrue(ownedBook.getRequestingUsers().contains(borrower1Id));
            assertTrue(ownedBook.getRequestingUsers().contains(borrower2Id));
            assertTrue(ownedBook.getRequestingUsers().contains(borrower3Id));
            assertEquals(3, ownedBook.getRequestingUsers().size());
            assertTrue(ownedBook.getRemovedRequests().isEmpty());
        }

        /* Decline one of the requests. Borrowed and Owned Book status stay the same */

        authenticate(owner); { // Tests BookController#declineRequest while having more than one request on the book
            bookController.declineRequest(book.getIsbn(), borrower3Id).get(5, TimeUnit.SECONDS);
        }

        /* Verify State */ {
            authenticate(borrower1); {
                BorrowedBook borrowedBook = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
                assertEquals(ownerId, borrowedBook.getOwner());
                assertEquals("requested", borrowedBook.getStatus());
                assertFalse(borrowedBook.getScanned());
            }

            authenticate(borrower2); {
                BorrowedBook borrowedBook = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
                assertEquals(ownerId, borrowedBook.getOwner());
                assertEquals("requested", borrowedBook.getStatus());
                assertFalse(borrowedBook.getScanned());
            }

            authenticate(borrower3); {
                Optional<BorrowedBook> borrowedBookOptional = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS);
                assertTrue(borrowedBookOptional.isEmpty());
            }

            OwnedBook ownedBook = bookController.getUserOwnedBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS).get();
            assertEquals(ownerId, ownedBook.getOwner());
            assertEquals("requested", ownedBook.getStatus());
            assertFalse(ownedBook.getScanned());
            assertNull(ownedBook.getUserBorrowing());
            assertTrue(ownedBook.getRequestingUsers().contains(borrower1Id));
            assertTrue(ownedBook.getRequestingUsers().contains(borrower2Id));
            assertEquals(2, ownedBook.getRequestingUsers().size());
            assertTrue(ownedBook.getRemovedRequests().isEmpty());
        }

        /* Accept one of the requests. Borrowed and Owned Book status transition from 'requested' to 'accepted' */

        authenticate(owner); { // Tests BookController#acceptRequest
            bookController.acceptRequest(book.getIsbn(), borrower1Id).get(5, TimeUnit.SECONDS);
        }

        /* Verify State */ {
            authenticate(borrower1); {
                BorrowedBook borrowedBook = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
                assertEquals(ownerId, borrowedBook.getOwner());
                assertEquals("accepted", borrowedBook.getStatus());
                assertFalse(borrowedBook.getScanned());
            }

            authenticate(borrower2); {
                Optional<BorrowedBook> borrowedBookOptional = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS);
                assertTrue(borrowedBookOptional.isEmpty());
            }

            OwnedBook ownedBook = bookController.getUserOwnedBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS).get();
            assertEquals(ownerId, ownedBook.getOwner());
            assertEquals("accepted", ownedBook.getStatus());
            assertFalse(ownedBook.getScanned());
            assertEquals(borrower1Id, ownedBook.getUserBorrowing());
            assertTrue(ownedBook.getRequestingUsers().isEmpty());
            assertTrue(ownedBook.getRemovedRequests().isEmpty());
        }

        /* Scan Borrowed Book. Borrowed Book becomes scanned */

        authenticate(borrower1); { // Tests BookController#scanMyBorrowedBook on an accepted borrowed book
            bookController.scanMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS);
        }

        /* Verify State */ {
            /* authenticate(borrower1); */ {
                BorrowedBook borrowedBook = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
                assertEquals(ownerId, borrowedBook.getOwner());
                assertEquals("accepted", borrowedBook.getStatus());
                assertTrue(borrowedBook.getScanned());
            }
        }

        /* Scan Owned Book. Owned Book becomes scanned */

        authenticate(owner); { // Tests BookController#scanMyOwnedBook on an accepted owned book
            bookController.scanMyOwnedBook(book.getIsbn()).get(5, TimeUnit.SECONDS);
        }

        /* Verify State */ {
            OwnedBook ownedBook = bookController.getUserOwnedBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS).get();
            assertEquals(ownerId, ownedBook.getOwner());
            assertEquals("accepted", ownedBook.getStatus());
            assertTrue(ownedBook.getScanned());
            assertEquals(borrower1Id, ownedBook.getUserBorrowing());
            assertTrue(ownedBook.getRequestingUsers().isEmpty());
            assertTrue(ownedBook.getRemovedRequests().isEmpty());
        }

        /* Exchange books. Borrowed and Owned Book status transitions from 'accepted' to 'borrowed' */

        bookController.exchangeBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS);

        /* Verify State */ {
            authenticate(borrower1); {
                BorrowedBook borrowedBook = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
                assertEquals(ownerId, borrowedBook.getOwner());
                assertEquals("borrowed", borrowedBook.getStatus());
                assertFalse(borrowedBook.getScanned());
            }

            OwnedBook ownedBook = bookController.getUserOwnedBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS).get();
            assertEquals(ownerId, ownedBook.getOwner());
            assertEquals("borrowed", ownedBook.getStatus());
            assertFalse(ownedBook.getScanned());
            assertEquals(borrower1Id, ownedBook.getUserBorrowing());
            assertTrue(ownedBook.getRequestingUsers().isEmpty());
            assertTrue(ownedBook.getRemovedRequests().isEmpty());
        }

        /* Scan Borrowed Book. Borrowed Book becomes scanned */

        authenticate(borrower1); { // Tests BookController#scanMyBorrowedBook on a borrowed borrowed book
            bookController.scanMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS);
        }

        /* Verify State */ {
            /* authenticate(borrower1); */ {
                BorrowedBook borrowedBook = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
                assertEquals(ownerId, borrowedBook.getOwner());
                assertEquals("borrowed", borrowedBook.getStatus());
                assertTrue(borrowedBook.getScanned());
            }
        }

        /* Scan Owned Book. Owned Book becomes scanned */

        authenticate(owner); { // Tests BookController#scanMyOwnedBook on a borrowed owned book
            bookController.scanMyOwnedBook(book.getIsbn()).get(5, TimeUnit.SECONDS);
        }

        /* Verify State */ {
            OwnedBook ownedBook = bookController.getUserOwnedBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS).get();
            assertEquals(ownerId, ownedBook.getOwner());
            assertEquals("borrowed", ownedBook.getStatus());
            assertTrue(ownedBook.getScanned());
            assertEquals(borrower1Id, ownedBook.getUserBorrowing());
            assertTrue(ownedBook.getRequestingUsers().isEmpty());
            assertTrue(ownedBook.getRemovedRequests().isEmpty());
        }

        /* Return book. Borrowed Book is gone. Owned Book status transitions from 'borrowed' to 'available' */

        bookController.returnBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS);

        /* Verify State */ {
            authenticate(borrower1); {
                Optional<BorrowedBook> borrowedBookOptional = bookController.getMyBorrowedBook(book.getIsbn()).get(5, TimeUnit.SECONDS);
                assertTrue(borrowedBookOptional.isEmpty());
            }

            OwnedBook ownedBook = bookController.getUserOwnedBook(book.getIsbn(), ownerId).get(5, TimeUnit.SECONDS).get();
            assertEquals(ownerId, ownedBook.getOwner());
            assertEquals("available", ownedBook.getStatus());
            assertFalse(ownedBook.getScanned());
            assertNull(ownedBook.getUserBorrowing());
            assertTrue(ownedBook.getRequestingUsers().isEmpty());
            assertTrue(ownedBook.getRemovedRequests().isEmpty());
        }

    }


    /* Other "core" methods not covered by testCoreTransactions */


    @Test
    public void testUpdateBook() throws InterruptedException, ExecutionException, TimeoutException {
        /* Dependencies (these methods are assumed to work correctly)
            -   BookController#getBook
            -   BookController#addBook
         */

        Book book = generateRandomBook();
        bookController.addBook(book).get(5, TimeUnit.SECONDS);

        book.setDescription("A test to update description. This book is safe to delete.");
        book.setAuthor("Book Controller Test");
        book.setTitle("An Updated Test Book");
        book.setImageId("test-image-id");

        bookController.updateBook(book).get(5, TimeUnit.SECONDS);

        Book actual = bookController.getBook(book.getIsbn()).get(5, TimeUnit.SECONDS).get();
        assertBooksEqual(book, actual);
    }

    /* Edge cases */

    @Test
    public void testGetNonExistingBook() throws InterruptedException, ExecutionException, TimeoutException {
        Book book = generateRandomBook();
        assertTrue(bookController.getBook(book.getIsbn()).get(5, TimeUnit.SECONDS).isEmpty());
    }

    @Test(expected = ExecutionException.class)
    public void testAddDuplicateBook() throws InterruptedException, ExecutionException, TimeoutException {
        Book book = generateRandomBook();
        bookController.addBook(book).get(5, TimeUnit.SECONDS);
        bookController.addBook(book).get(5, TimeUnit.SECONDS); // Should throw an ExecutionException due to the book already existing
    }

    @Test
    public void testGetMyOwnedBooks() throws InterruptedException, ExecutionException, TimeoutException {
        /* Dependencies (these methods are assumed to work correctly)
            -   BookController#addBook
            -   BookController#addMyOwnedBook
         */

        authenticate(generateRandomUser());

        Book book1 = generateRandomBook();
        Book book2 = generateRandomBook();
        Book book3 = generateRandomBook();

        bookController.addBook(book1).get(5, TimeUnit.SECONDS);
        bookController.addBook(book2).get(5, TimeUnit.SECONDS);
        bookController.addBook(book3).get(5, TimeUnit.SECONDS);

        bookController.addMyOwnedBook(new OwnedBook(book1.getIsbn())).get(5, TimeUnit.SECONDS);
        bookController.addMyOwnedBook(new OwnedBook(book2.getIsbn())).get(5, TimeUnit.SECONDS);
        bookController.addMyOwnedBook(new OwnedBook(book3.getIsbn())).get(5, TimeUnit.SECONDS);

        Collection<OwnedBook> ownedBookCollection = bookController.getMyOwnedBooks().get(5, TimeUnit.SECONDS);
        assertEquals(3, ownedBookCollection.size());
    }

    // TODO: more tests

//
//    /* Current User (My) BorrowedBook and OwnedBook queries tests */
//
//
//    // My borrowed books
//
//    @Test
//    public void testGetMyBorrowedBook() throws InterruptedException, ExecutionException, TimeoutException {
//        // Assumes this user has borrowed The Hobbit
//        userController.authenticate(borrower.username, borrower.password).get(5, TimeUnit.SECONDS);
//        BorrowedBook borrowedBook = bookController.getMyBorrowedBook(theHobbit.getIsbn()).get(5, TimeUnit.SECONDS).get();
//        Assert.assertEquals(theHobbit.getIsbn(), borrowedBook.getIsbn());
//    }
//
//    @Test(expected = ExecutionException.class)
//    public void testGetNullMyBorrowedBook() throws InterruptedException, ExecutionException, TimeoutException {
//        userController.authenticate(borrower.username, borrower.password).get(5, TimeUnit.SECONDS);
//        Optional<BorrowedBook> borrowedBookOptional = bookController.getMyBorrowedBook(generateRandomBook().getIsbn()).get(5, TimeUnit.SECONDS);
//        assertTrue(borrowedBookOptional.isEmpty());
//    }
//
//    @Test
//    public void testGetMyBorrowedBooks() throws InterruptedException, ExecutionException, TimeoutException {
//        userController.authenticate(username, password).get(10, TimeUnit.SECONDS);
//
//        HashMap<String, BorrowedBook> borrowedBookHashMap = bookController.getMyBorrowedBooks().get(5, TimeUnit.SECONDS);
//        Assert.assertEquals(1, borrowedBookHashMap.size());
//        Assert.assertTrue("9780547928227".equals(borrowedBookHashMap.get("9780547928227").getIsbn()));
//    }
//
//    @Test
//    public void testGetNullMyBorrowedBooks() throws InterruptedException, ExecutionException, TimeoutException {
//        UserController userController = UserController.getInstance();
//        BookController bookController = BookController.getInstance();
//
//        String username = "342c495e_fdfa_44fa_8ab6_a8a74385b1be";
//        String password = "c32271c9-eb3f-4313-8f1e-db54c0158e5b";
//        userController.authenticate(username, password).get(5, TimeUnit.SECONDS);
//
//        HashMap<String, BorrowedBook> borrowedBookHashMap = bookController.getMyBorrowedBooks().get(5, TimeUnit.SECONDS);
//        Assert.assertEquals(0, borrowedBookHashMap.size());
//    }
//
//
//    // My owned books
//
//    @Test
//    public void testAddMyOwnedBook() throws InterruptedException, ExecutionException, TimeoutException {
//        // Depends on BookController#getMyOwnedBook working correctly
//        UserController userController = UserController.getInstance();
//        BookController bookController = BookController.getInstance();
//
//        String username = "0265747f_72d4_4ea4_a471_8af60f87017e";
//        String password = "0ece7cb6-e601-45a1-a93c-0bbd159c31c5";
//        userController.authenticate(username, password).get(5, TimeUnit.SECONDS);
//
//        bookController.deleteMyOwnedBook("9781781102459").get(5, TimeUnit.SECONDS);
//
//        OwnedBook expected = new OwnedBook("9781781102459");
//        bookController.addMyOwnedBook(expected).get(5, TimeUnit.SECONDS);
//
//        OwnedBook actual = bookController.getMyOwnedBook("9781781102459").get(5, TimeUnit.SECONDS);
//        Assert.assertTrue(expected.getIsbn().equals(actual.getIsbn()) && expected.getStatus().equals(actual.getStatus()));
//
//        // Clean up
//        bookController.deleteMyOwnedBook("9781781102459").get(5, TimeUnit.SECONDS);
//    }
//
//    @Test(expected = ExecutionException.class)
//    public void testAddDuplicateMyOwnedBook() throws InterruptedException, ExecutionException, TimeoutException {
//        UserController userController = UserController.getInstance();
//        BookController bookController = BookController.getInstance();
//
//        String username = "0265747f_72d4_4ea4_a471_8af60f87017e";
//        String password = "0ece7cb6-e601-45a1-a93c-0bbd159c31c5";
//        userController.authenticate(username, password).get(5, TimeUnit.SECONDS);
//
//        OwnedBook ownedBook = new OwnedBook("0000000000000");
//        bookController.addMyOwnedBook(ownedBook).get(5, TimeUnit.SECONDS);
//        bookController.addMyOwnedBook(ownedBook).get(5, TimeUnit.SECONDS);
//    }
//
//    @Test
//    public void testGetMyOwnedBook() throws InterruptedException, ExecutionException, TimeoutException {
//        UserController userController = UserController.getInstance();
//        BookController bookController = BookController.getInstance();
//
//        String username = "0265747f_72d4_4ea4_a471_8af60f87017e";
//        String password = "0ece7cb6-e601-45a1-a93c-0bbd159c31c5";
//        userController.authenticate(username, password).get(5, TimeUnit.SECONDS);
//
//        OwnedBook ownedBook = bookController.getMyOwnedBook("9780547928227").get(5, TimeUnit.SECONDS);
//        Assert.assertTrue("9780547928227".equals(ownedBook.getIsbn()));
//    }
//
//    @Test(expected = ExecutionException.class)
//    public void testGetNullMyOwnedBook() throws InterruptedException, ExecutionException, TimeoutException {
//        UserController userController = UserController.getInstance();
//        BookController bookController = BookController.getInstance();
//
//        String username = "0265747f_72d4_4ea4_a471_8af60f87017e";
//        String password = "0ece7cb6-e601-45a1-a93c-0bbd159c31c5";
//        userController.authenticate(username, password).get(5, TimeUnit.SECONDS);
//
//        bookController.deleteMyOwnedBook("0000000000000").get(5, TimeUnit.SECONDS);
//        bookController.getMyOwnedBook("0000000000000").get(5, TimeUnit.SECONDS);
//    }
//
//    @Test
//    public void testGetMyOwnedBooks() throws InterruptedException, ExecutionException, TimeoutException {
//        UserController userController = UserController.getInstance();
//        BookController bookController = BookController.getInstance();
//
//        String username = "0265747f_72d4_4ea4_a471_8af60f87017e";
//        String password = "0ece7cb6-e601-45a1-a93c-0bbd159c31c5";
//        userController.authenticate(username, password).get(5, TimeUnit.SECONDS);
//
//        HashMap<String, OwnedBook> ownedBookHashMap = bookController.getMyOwnedBooks().get(5, TimeUnit.SECONDS);
//        Assert.assertEquals(1, ownedBookHashMap.size());
//        Assert.assertTrue("9780547928227".equals(ownedBookHashMap.get("9780547928227").getIsbn()));
//    }
//
//    @Test
//    public void testGetNullMyOwnedBooks() throws InterruptedException, ExecutionException, TimeoutException {
//        UserController userController = UserController.getInstance();
//        BookController bookController = BookController.getInstance();
//
//        String username = "342c495e_fdfa_44fa_8ab6_a8a74385b1be";
//        String password = "c32271c9-eb3f-4313-8f1e-db54c0158e5b";
//        userController.authenticate(username, password).get(5, TimeUnit.SECONDS);
//
//        HashMap<String, OwnedBook> ownedBookHashMap = bookController.getMyOwnedBooks().get(5, TimeUnit.SECONDS);
//        Assert.assertEquals(0, ownedBookHashMap.size());
//    }
//
//    @Test
//    public void testUpdateMyOwnedBook() throws InterruptedException, ExecutionException, TimeoutException {
//        // Depends on BookController#getMyOwnedBook working correctly
//        UserController userController = UserController.getInstance();
//        BookController bookController = BookController.getInstance();
//
//        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
//        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";
//        userController.authenticate(username, password).get(10, TimeUnit.SECONDS);
//
//        OwnedBook expected = new OwnedBook("9781781102459");
//        bookController.updateMyOwnedBook(expected).get(10, TimeUnit.SECONDS);
//        OwnedBook actual = bookController.getMyOwnedBook("9781781102459").get(5, TimeUnit.SECONDS);
//
//        Assert.assertTrue(expected.getIsbn().equals(actual.getIsbn()) && expected.getStatus().equals(actual.getStatus()));
//
//        // Clean up
//        bookController.deleteMyOwnedBook("9781781102459").get(10, TimeUnit.SECONDS);
//    }
//
//    @Test(expected = ExecutionException.class)
//    public void testDeleteMyOwnedBook() throws InterruptedException, ExecutionException, TimeoutException {
//        // Depends on BookController#getMyOwnedBook working correctly
//        UserController userController = UserController.getInstance();
//        BookController bookController = BookController.getInstance();
//
//        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
//        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";
//        userController.authenticate(username, password).get(10, TimeUnit.SECONDS);
//
//        bookController.deleteMyOwnedBook("9781781102459").get(10, TimeUnit.SECONDS);
//        bookController.getMyOwnedBook("9781781102459").get(10, TimeUnit.SECONDS);
//    }
//
//
//    /* User BorrowedBook and OwnedBook queries tests */
//
//
//    @Test
//    public void testGetUserBorrowedBook() throws InterruptedException, ExecutionException, TimeoutException {
//        BookController bookController = BookController.getInstance();
//
//        BorrowedBook borrowedBook = bookController.getUserBorrowedBook("eQgZfhN2Yng9TPHcXvfBZs5ZKxj1", "9780547928227").get(5, TimeUnit.SECONDS);
//        Assert.assertEquals("9780547928227", borrowedBook.getIsbn());
//    }
//
//    @Test(expected = ExecutionException.class)
//    public void testGetNullUserBorrowedBook() throws InterruptedException, ExecutionException, TimeoutException {
//        BookController bookController = BookController.getInstance();
//
//        bookController.getUserBorrowedBook("eQgZfhN2Yng9TPHcXvfBZs5ZKxj1", "0000000000000").get(5, TimeUnit.SECONDS);
//    }
//
//    @Test
//    public void testGetUserBorrowedBooks() throws InterruptedException, ExecutionException, TimeoutException {
//        BookController bookController = BookController.getInstance();
//
//        HashMap<String, BorrowedBook> borrowedBookHashMap = bookController.getUserBorrowedBooks("eQgZfhN2Yng9TPHcXvfBZs5ZKxj1").get(5, TimeUnit.SECONDS);
//        Assert.assertEquals(1, borrowedBookHashMap.size());
//        Assert.assertTrue("9780547928227".equals(borrowedBookHashMap.get("9780547928227").getIsbn()));
//    }
//
//    @Test
//    public void testGetNullUserBorrowedBooks() throws InterruptedException, ExecutionException, TimeoutException {
//        BookController bookController = BookController.getInstance();
//
//        HashMap<String, BorrowedBook> borrowedBookHashMap = bookController.getUserBorrowedBooks("bJyo1fjcj0aYJejnJSfz6tugpca2").get(5, TimeUnit.SECONDS);
//        Assert.assertEquals(0, borrowedBookHashMap.size());
//    }
//
//    @Test
//    public void testGetUserOwnedBook() throws InterruptedException, ExecutionException, TimeoutException {
//        BookController bookController = BookController.getInstance();
//
//        OwnedBook ownedBook = bookController.getUserOwnedBook("AQiv4J6BTsX5kYHgLChH7xFlir02", "9780547928227").get(5, TimeUnit.SECONDS);
//        Assert.assertTrue("9780547928227".equals(ownedBook.getIsbn()));
//    }
//
//    @Test(expected = ExecutionException.class)
//    public void testGetNullUserOwnedBook() throws InterruptedException, ExecutionException, TimeoutException {
//        BookController bookController = BookController.getInstance();
//
//        bookController.getUserOwnedBook("bJyo1fjcj0aYJejnJSfz6tugpca2", "0000000000000").get(5, TimeUnit.SECONDS);
//    }
//
//    @Test
//    public void testGetUserOwnedBooks() throws InterruptedException, ExecutionException, TimeoutException {
//        BookController bookController = BookController.getInstance();
//
//        HashMap<String, OwnedBook> ownedBookHashMap = bookController.getUserOwnedBooks("AQiv4J6BTsX5kYHgLChH7xFlir02").get(5, TimeUnit.SECONDS);
//        Assert.assertEquals(1, ownedBookHashMap.size());
//        Assert.assertTrue("9780547928227".equals(ownedBookHashMap.get("9780547928227").getIsbn()));
//    }
//
//    @Test
//    public void testGetNullUserOwnedBooks() throws InterruptedException, ExecutionException, TimeoutException {
//        BookController bookController = BookController.getInstance();
//
//        HashMap<String, OwnedBook> ownedBookHashMap = bookController.getUserOwnedBooks("bJyo1fjcj0aYJejnJSfz6tugpca2").get(5, TimeUnit.SECONDS);
//        Assert.assertEquals(0, ownedBookHashMap.size());
//    }

}


