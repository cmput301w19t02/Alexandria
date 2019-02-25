package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import ca.ualberta.CMPUT3012019T02.alexandria.model.user.BorrowedBook;

import static org.junit.Assert.assertEquals;

public class BorrowedBookTests {
    @Test
    public void constructorTest() {
        BorrowedBook book = new BorrowedBook("9780545010221", "Available", "johndoe@email.com");
        assertEquals("9780545010221",book.getIsbn());
        assertEquals("Available", book.getStatus());
        assertEquals("johndoe@email.com",book.getOwner());
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyIsbnContructorTest() {
        new BorrowedBook("", "Available", "johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyStatusContructorTest() {
        new BorrowedBook("9780545010221", "", "johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyOwnerContructorTest() {
        new BorrowedBook("9780545010221", "Available", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullIsbnConstructorTest() {
        new BorrowedBook(null, "Available","johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullStatusContructorTest() {
        new BorrowedBook("9780545010221", null, "johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullOwnerConstructorTest() {
        new BorrowedBook("9780545010221", "Available", null);
    }

    @Test
    public void setIsbnTest() {
        BorrowedBook book = new BorrowedBook("9780545010221", "Available", "johndoe@email.com");
        book.setIsbn("1234441242141");
        assertEquals("1234441242141",book.getIsbn());
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetIsbnTest() {
        BorrowedBook BorrowedBook = new BorrowedBook("9780545010221", "Available", "johndoe@email.com");
        BorrowedBook.setIsbn("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetIsbnTest() {
        BorrowedBook BorrowedBook = new BorrowedBook("9780545010221", "Available", "johndoe@email.com");
        BorrowedBook.setIsbn(null);
    }

    @Test
    public void setOwnerTest() {
        BorrowedBook book = new BorrowedBook("9780545010221", "Available", "johndoe@email.com");
        book.setOwner("janesmith@email.com");
        assertEquals("janesmith@email.com",book.getOwner());
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetStatusTest() {
        BorrowedBook BorrowedBook = new BorrowedBook("9780545010221", "Available", "johndoe@email.com");
        BorrowedBook.setStatus("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetStatusTest() {
        BorrowedBook BorrowedBook = new BorrowedBook("9780545010221", "Available", "johndoe@email.com");
        BorrowedBook.setStatus(null);
    }

    @Test
    public void setStatusTest() {
        BorrowedBook book = new BorrowedBook("9780545010221", "Available", "johndoe@email.com");
        book.setStatus("Requested");
        assertEquals("Requested",book.getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetOwnerTest() {
        BorrowedBook BorrowedBook = new BorrowedBook("9780545010221", "Available", "johndoe@email.com");
        BorrowedBook.setOwner("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetOwnerTest() {
        BorrowedBook BorrowedBook = new BorrowedBook("9780545010221", "Available", "johndoe@email.com");
        BorrowedBook.setOwner(null);
    }
}
