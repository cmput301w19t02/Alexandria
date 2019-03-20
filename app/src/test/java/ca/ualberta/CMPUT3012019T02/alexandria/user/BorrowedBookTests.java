package ca.ualberta.CMPUT3012019T02.alexandria.user;

import org.junit.Test;

import ca.ualberta.CMPUT3012019T02.alexandria.model.user.BorrowedBook;

import static org.junit.Assert.assertEquals;

public class BorrowedBookTests {

    @Test
    public void constructorTest() {
        BorrowedBook book = new BorrowedBook("9780545010221", "available", "johndoe@email.com");
        assertEquals("9780545010221",book.getIsbn());
        assertEquals("available", book.getStatus());
        assertEquals("johndoe@email.com",book.getOwner());
    }

    @Test
    public void setIsbnTest() {
        BorrowedBook book = new BorrowedBook("9780545010221", "available", "johndoe@email.com");
        book.setIsbn("1234441242141");
        assertEquals("1234441242141",book.getIsbn());
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetIsbnTest() {
        BorrowedBook book = new BorrowedBook("9780545010221", "available", "johndoe@email.com");
        book.setIsbn("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetIsbnTest() {
        BorrowedBook book = new BorrowedBook("9780545010221", "available", "johndoe@email.com");
        book.setIsbn(null);
    }

    @Test
    public void setOwnerTest() {
        BorrowedBook book = new BorrowedBook("9780545010221", "available", "johndoe@email.com");
        book.setOwner("janesmith@email.com");
        assertEquals("janesmith@email.com",book.getOwner());
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetStatusTest() {
        BorrowedBook book = new BorrowedBook("9780545010221", "available", "johndoe@email.com");
        book.setStatus("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetStatusTest() {
        BorrowedBook book = new BorrowedBook("9780545010221", "available", "johndoe@email.com");
        book.setStatus(null);
    }

    @Test
    public void setStatusTest() {
        BorrowedBook book = new BorrowedBook("9780545010221", "available", "johndoe@email.com");
        book.setStatus("requested");
        assertEquals("requested",book.getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetOwnerTest() {
        BorrowedBook book = new BorrowedBook("9780545010221", "available", "johndoe@email.com");
        book.setOwner("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetOwnerTest() {
        BorrowedBook book = new BorrowedBook("9780545010221", "available", "johndoe@email.com");
        book.setOwner(null);
    }
}
