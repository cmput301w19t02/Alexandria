package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.BorrowedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;

public class BookControllerTests {


    /* Test methods for adding/removing books */


    @Test
    public void testGetBook() throws InterruptedException, ExecutionException, TimeoutException {
        BookController bookController = BookController.getInstance();

        Book expected = new Book("9780547928227", "ac543736-b355-4eac-b9b8-a11165f0aa43", "The Hobbit", "J.R.R. Tolkien", "Bilbo Baggins is a hobbit who enjoys a comfortable, unambitious life, rarely traveling further than the pantry of his hobbit-hole in Bag End. But his contentment is disturbed when the wizard, Gandalf, and a company of thirteen dwarves arrive on his doorstep one day to whisk him away on an unexpected journey ‘there and back again’. They have a plot to raid the treasure hoard of Smaug the Magnificent, a large and very dangerous dragon.", null);
        Book actual = bookController.getBook("9780547928227").get(5, TimeUnit.SECONDS);

        Assert.assertTrue(expected.equals(actual));
    }

    @Test(expected = ExecutionException.class)
    public void testGetNonExistingBook() throws InterruptedException, ExecutionException, TimeoutException {
        BookController bookController = BookController.getInstance();
        bookController.deleteBook("0000000000000").get(5, TimeUnit.SECONDS); // Shouldn't exist, but delete it anyway to make sure
        bookController.getBook("0000000000000").get(5, TimeUnit.SECONDS); // Should throw an ExecutionException due to book not existing
    }

    @Test
    public void testAddBook() throws InterruptedException, ExecutionException, TimeoutException {
        // Depends on BookController#getBook() working correctly

        BookController bookController = BookController.getInstance();

        Book expected = new Book("0000000000000", null, "Test Title", "Test Author", "Test Description", null);
        bookController.addBook(expected).get(5, TimeUnit.SECONDS);
        Book actual = bookController.getBook("0000000000000").get(5, TimeUnit.SECONDS);

        Assert.assertTrue(expected.equals(actual));

        // Clean up
        bookController.deleteBook("0000000000000").get(5, TimeUnit.SECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testAddDuplicateBook() throws InterruptedException, ExecutionException, TimeoutException {
        // Depends on BookController#getBook() working correctly

        BookController bookController = BookController.getInstance();

        Book existing = new Book("9780547928227", "ac543736-b355-4eac-b9b8-a11165f0aa43", "The Hobbit", "J.R.R. Tolkien", "Bilbo Baggins is a hobbit who enjoys a comfortable, unambitious life, rarely traveling further than the pantry of his hobbit-hole in Bag End. But his contentment is disturbed when the wizard, Gandalf, and a company of thirteen dwarves arrive on his doorstep one day to whisk him away on an unexpected journey ‘there and back again’. They have a plot to raid the treasure hoard of Smaug the Magnificent, a large and very dangerous dragon.", null);
        bookController.addBook(existing).get(5, TimeUnit.SECONDS); // Should throw an ExecutionException due to the book already existing
    }

    @Test
    public void testUpdateBook() throws InterruptedException, ExecutionException, TimeoutException {
        // Depends on BookController#getBook() and BookController#addBook() working correctly

        BookController bookController = BookController.getInstance();

        Book expected = new Book("0000000000000", "", "Test Title", "Test Author", "Test Description", null);
        bookController.addBook(expected).get(5, TimeUnit.SECONDS);

        expected.setAuthor("Test Author 2");
        bookController.updateBook(expected).get(5, TimeUnit.SECONDS);

        Book actual = bookController.getBook("0000000000000").get(5, TimeUnit.SECONDS);

        Assert.assertTrue(expected.equals(actual));

        // Clean up
        bookController.deleteBook("0000000000000").get(5, TimeUnit.SECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testDeleteBook() throws InterruptedException, ExecutionException, TimeoutException {
        // Depends on BookController#getBook() working correctly

        BookController bookController = BookController.getInstance();

        bookController.deleteBook("0000000000000").get(5, TimeUnit.SECONDS);
        bookController.getBook("0000000000000").get(5, TimeUnit.SECONDS); // Should throw an ExecutionException due to the book not existing
    }


    /* Book transaction methods tests */


    // TODO: unit tests for borrowing books to be done in next project part

    @Test
    public void testRequestBook() {
    }

    @Test
    public void testCancelRequest() {
    }

    @Test
    public void testReturnBook() {
    }

    @Test
    public void testAcceptBookRequest() {
    }

    @Test
    public void testDeclineBookRequest() {
    }

    @Test
    public void testExchangeBook() {
    }


    /* Current User (My) BorrowedBook and OwnedBook queries tests */


    // My borrowed books

    @Test
    public void testGetMyBorrowedBook() throws InterruptedException, ExecutionException, TimeoutException {
        UserController userController = UserController.getInstance();
        BookController bookController = BookController.getInstance();

        // Assumes this user has borrowed the book 9780547928227
        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";
        userController.authenticate(username, password).get(5, TimeUnit.SECONDS);

        BorrowedBook borrowedBook = bookController.getMyBorrowedBook("9780547928227").get(5, TimeUnit.SECONDS);
        Assert.assertEquals("9780547928227", borrowedBook.getIsbn());
    }

    @Test(expected = ExecutionException.class)
    public void testGetNullMyBorrowedBook() throws InterruptedException, ExecutionException, TimeoutException {
        UserController userController = UserController.getInstance();
        BookController bookController = BookController.getInstance();

        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";
        userController.authenticate(username, password).get(10, TimeUnit.SECONDS);

        bookController.getMyBorrowedBook("0000000000000").get(10, TimeUnit.SECONDS);
    }

    @Test
    public void testGetMyBorrowedBooks() throws InterruptedException, ExecutionException, TimeoutException {
        UserController userController = UserController.getInstance();
        BookController bookController = BookController.getInstance();

        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";
        userController.authenticate(username, password).get(10, TimeUnit.SECONDS);

        HashMap<String, BorrowedBook> borrowedBookHashMap = bookController.getMyBorrowedBooks().get(5, TimeUnit.SECONDS);
        Assert.assertEquals(1, borrowedBookHashMap.size());
        Assert.assertTrue("9780547928227".equals(borrowedBookHashMap.get("9780547928227").getIsbn()));
    }

    @Test
    public void testGetNullMyBorrowedBooks() throws InterruptedException, ExecutionException, TimeoutException {
        UserController userController = UserController.getInstance();
        BookController bookController = BookController.getInstance();

        String username = "342c495e_fdfa_44fa_8ab6_a8a74385b1be";
        String password = "c32271c9-eb3f-4313-8f1e-db54c0158e5b";
        userController.authenticate(username, password).get(5, TimeUnit.SECONDS);

        HashMap<String, BorrowedBook> borrowedBookHashMap = bookController.getMyBorrowedBooks().get(5, TimeUnit.SECONDS);
        Assert.assertEquals(0, borrowedBookHashMap.size());
    }


    // My owned books

    @Test
    public void testAddMyOwnedBook() throws InterruptedException, ExecutionException, TimeoutException {
        // Depends on BookController#getMyOwnedBook working correctly
        UserController userController = UserController.getInstance();
        BookController bookController = BookController.getInstance();

        String username = "0265747f_72d4_4ea4_a471_8af60f87017e";
        String password = "0ece7cb6-e601-45a1-a93c-0bbd159c31c5";
        userController.authenticate(username, password).get(5, TimeUnit.SECONDS);

        bookController.deleteMyOwnedBook("9781781102459").get(5, TimeUnit.SECONDS);

        OwnedBook expected = new OwnedBook("9781781102459");
        bookController.addMyOwnedBook(expected).get(5, TimeUnit.SECONDS);

        OwnedBook actual = bookController.getMyOwnedBook("9781781102459").get(5, TimeUnit.SECONDS);
        Assert.assertTrue(expected.getIsbn().equals(actual.getIsbn()) && expected.getStatus().equals(actual.getStatus()));

        // Clean up
        bookController.deleteMyOwnedBook("9781781102459").get(5, TimeUnit.SECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testAddDuplicateMyOwnedBook() throws InterruptedException, ExecutionException, TimeoutException {
        UserController userController = UserController.getInstance();
        BookController bookController = BookController.getInstance();

        String username = "0265747f_72d4_4ea4_a471_8af60f87017e";
        String password = "0ece7cb6-e601-45a1-a93c-0bbd159c31c5";
        userController.authenticate(username, password).get(5, TimeUnit.SECONDS);

        OwnedBook ownedBook = new OwnedBook("0000000000000");
        bookController.addMyOwnedBook(ownedBook).get(5, TimeUnit.SECONDS);
        bookController.addMyOwnedBook(ownedBook).get(5, TimeUnit.SECONDS);
    }

    @Test
    public void testGetMyOwnedBook() throws InterruptedException, ExecutionException, TimeoutException {
        UserController userController = UserController.getInstance();
        BookController bookController = BookController.getInstance();

        String username = "0265747f_72d4_4ea4_a471_8af60f87017e";
        String password = "0ece7cb6-e601-45a1-a93c-0bbd159c31c5";
        userController.authenticate(username, password).get(5, TimeUnit.SECONDS);

        OwnedBook ownedBook = bookController.getMyOwnedBook("9780547928227").get(5, TimeUnit.SECONDS);
        Assert.assertTrue("9780547928227".equals(ownedBook.getIsbn()));
    }

    @Test(expected = ExecutionException.class)
    public void testGetNullMyOwnedBook() throws InterruptedException, ExecutionException, TimeoutException {
        UserController userController = UserController.getInstance();
        BookController bookController = BookController.getInstance();

        String username = "0265747f_72d4_4ea4_a471_8af60f87017e";
        String password = "0ece7cb6-e601-45a1-a93c-0bbd159c31c5";
        userController.authenticate(username, password).get(5, TimeUnit.SECONDS);

        bookController.deleteMyOwnedBook("0000000000000").get(5, TimeUnit.SECONDS);
        bookController.getMyOwnedBook("0000000000000").get(5, TimeUnit.SECONDS);
    }

    @Test
    public void testGetMyOwnedBooks() throws InterruptedException, ExecutionException, TimeoutException {
        UserController userController = UserController.getInstance();
        BookController bookController = BookController.getInstance();

        String username = "0265747f_72d4_4ea4_a471_8af60f87017e";
        String password = "0ece7cb6-e601-45a1-a93c-0bbd159c31c5";
        userController.authenticate(username, password).get(5, TimeUnit.SECONDS);

        HashMap<String, OwnedBook> ownedBookHashMap = bookController.getMyOwnedBooks().get(5, TimeUnit.SECONDS);
        Assert.assertEquals(1, ownedBookHashMap.size());
        Assert.assertTrue("9780547928227".equals(ownedBookHashMap.get("9780547928227").getIsbn()));
    }

    @Test
    public void testGetNullMyOwnedBooks() throws InterruptedException, ExecutionException, TimeoutException {
        UserController userController = UserController.getInstance();
        BookController bookController = BookController.getInstance();

        String username = "342c495e_fdfa_44fa_8ab6_a8a74385b1be";
        String password = "c32271c9-eb3f-4313-8f1e-db54c0158e5b";
        userController.authenticate(username, password).get(5, TimeUnit.SECONDS);

        HashMap<String, OwnedBook> ownedBookHashMap = bookController.getMyOwnedBooks().get(5, TimeUnit.SECONDS);
        Assert.assertEquals(0, ownedBookHashMap.size());
    }

    @Test
    public void testUpdateMyOwnedBook() throws InterruptedException, ExecutionException, TimeoutException {
        // Depends on BookController#getMyOwnedBook working correctly
        UserController userController = UserController.getInstance();
        BookController bookController = BookController.getInstance();

        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";
        userController.authenticate(username, password).get(10, TimeUnit.SECONDS);

        OwnedBook expected = new OwnedBook("9781781102459");
        bookController.updateMyOwnedBook(expected).get(10, TimeUnit.SECONDS);
        OwnedBook actual = bookController.getMyOwnedBook("9781781102459").get(5, TimeUnit.SECONDS);

        Assert.assertTrue(expected.getIsbn().equals(actual.getIsbn()) && expected.getStatus().equals(actual.getStatus()));

        // Clean up
        bookController.deleteMyOwnedBook("9781781102459").get(10, TimeUnit.SECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testDeleteMyOwnedBook() throws InterruptedException, ExecutionException, TimeoutException {
        // Depends on BookController#getMyOwnedBook working correctly
        UserController userController = UserController.getInstance();
        BookController bookController = BookController.getInstance();

        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";
        userController.authenticate(username, password).get(5, TimeUnit.SECONDS);

        bookController.deleteMyOwnedBook("9781781102459").get(5, TimeUnit.SECONDS);
        bookController.getMyOwnedBook("9781781102459").get(5, TimeUnit.SECONDS);
    }


    /* User BorrowedBook and OwnedBook queries tests */


    @Test
    public void testGetUserBorrowedBook() throws InterruptedException, ExecutionException, TimeoutException {
        BookController bookController = BookController.getInstance();

        BorrowedBook borrowedBook = bookController.getUserBorrowedBook("eQgZfhN2Yng9TPHcXvfBZs5ZKxj1", "9780547928227").get(5, TimeUnit.SECONDS);
        Assert.assertEquals("9780547928227", borrowedBook.getIsbn());
    }

    @Test(expected = ExecutionException.class)
    public void testGetNullUserBorrowedBook() throws InterruptedException, ExecutionException, TimeoutException {
        BookController bookController = BookController.getInstance();

        bookController.getUserBorrowedBook("eQgZfhN2Yng9TPHcXvfBZs5ZKxj1", "0000000000000").get(5, TimeUnit.SECONDS);
    }

    @Test
    public void testGetUserBorrowedBooks() throws InterruptedException, ExecutionException, TimeoutException {
        BookController bookController = BookController.getInstance();

        HashMap<String, BorrowedBook> borrowedBookHashMap = bookController.getUserBorrowedBooks("eQgZfhN2Yng9TPHcXvfBZs5ZKxj1").get(5, TimeUnit.SECONDS);
        Assert.assertEquals(1, borrowedBookHashMap.size());
        Assert.assertTrue("9780547928227".equals(borrowedBookHashMap.get("9780547928227").getIsbn()));
    }

    @Test
    public void testGetNullUserBorrowedBooks() throws InterruptedException, ExecutionException, TimeoutException {
        BookController bookController = BookController.getInstance();

        HashMap<String, BorrowedBook> borrowedBookHashMap = bookController.getUserBorrowedBooks("bJyo1fjcj0aYJejnJSfz6tugpca2").get(5, TimeUnit.SECONDS);
        Assert.assertEquals(0, borrowedBookHashMap.size());
    }

    @Test
    public void testGetUserOwnedBook() throws InterruptedException, ExecutionException, TimeoutException {
        BookController bookController = BookController.getInstance();

        OwnedBook ownedBook = bookController.getUserOwnedBook("AQiv4J6BTsX5kYHgLChH7xFlir02", "9780547928227").get(5, TimeUnit.SECONDS);
        Assert.assertTrue("9780547928227".equals(ownedBook.getIsbn()));
    }

    @Test(expected = ExecutionException.class)
    public void testGetNullUserOwnedBook() throws InterruptedException, ExecutionException, TimeoutException {
        BookController bookController = BookController.getInstance();

        bookController.getUserOwnedBook("bJyo1fjcj0aYJejnJSfz6tugpca2", "0000000000000").get(5, TimeUnit.SECONDS);
    }

    @Test
    public void testGetUserOwnedBooks() throws InterruptedException, ExecutionException, TimeoutException {
        BookController bookController = BookController.getInstance();

        HashMap<String, OwnedBook> ownedBookHashMap = bookController.getUserOwnedBooks("AQiv4J6BTsX5kYHgLChH7xFlir02").get(5, TimeUnit.SECONDS);
        Assert.assertEquals(1, ownedBookHashMap.size());
        Assert.assertTrue("9780547928227".equals(ownedBookHashMap.get("9780547928227").getIsbn()));
    }

    @Test
    public void testGetNullUserOwnedBooks() throws InterruptedException, ExecutionException, TimeoutException {
        BookController bookController = BookController.getInstance();

        HashMap<String, OwnedBook> ownedBookHashMap = bookController.getUserOwnedBooks("bJyo1fjcj0aYJejnJSfz6tugpca2").get(5, TimeUnit.SECONDS);
        Assert.assertEquals(0, ownedBookHashMap.size());
    }

}

