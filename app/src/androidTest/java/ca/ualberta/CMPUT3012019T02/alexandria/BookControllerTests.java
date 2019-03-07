package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;

public class BookControllerTests {

    @Test
    public void testGetBook() throws InterruptedException, ExecutionException, TimeoutException {
        BookController bookController = BookController.getInstance();

        Book expected = new Book("9780547928227", "ac543736-b355-4eac-b9b8-a11165f0aa43", "The Hobbit", "J.R.R. Tolkien", "Bilbo Baggins is a hobbit who enjoys a comfortable, unambitious life, rarely traveling further than the pantry of his hobbit-hole in Bag End. But his contentment is disturbed when the wizard, Gandalf, and a company of thirteen dwarves arrive on his doorstep one day to whisk him away on an unexpected journey ‘there and back again’. They have a plot to raid the treasure hoard of Smaug the Magnificent, a large and very dangerous dragon.", null);
        Book actual = bookController.getBook("9780547928227").get(5, TimeUnit.SECONDS);

        Assert.assertTrue(expected.equals(actual));
    }

    @Test
    public void testAddBook() throws InterruptedException, ExecutionException, TimeoutException {
        // Depends on BookController#getBook() working correctly

        BookController bookController = BookController.getInstance();

        Book expected = new Book("0000000000000", "", "Test Title", "Test Author", "Test Description", null);
        bookController.addBook(expected).get(5, TimeUnit.SECONDS);
        Book actual = bookController.getBook("0000000000000").get(5, TimeUnit.SECONDS);

        Assert.assertTrue(expected.equals(actual));

        // Clean up
        bookController.deleteBook("0000000000000").get(5, TimeUnit.SECONDS);
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
        bookController.getBook("0000000000000").get(5, TimeUnit.SECONDS);
    }

}
