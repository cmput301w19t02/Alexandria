package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;

public class BookTests {

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetAvailableOwnersTest(){
        Book book = new Book("9780545010221", null,"Harry Potter", "J. K. Rowling", new Date());
        book.getAvailableOwners().add("John Smith");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullIsbnConstructorTest(){
        new Book(null, null,"Harry Potter", "J. K. Rowling", new Date());
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyIsbnConstructorTest(){
        new Book("", null,"Harry Potter", "J. K. Rowling", new Date());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetIsbnTest(){
        Book book = new Book("9780545010221", null,"Harry Potter", "J. K. Rowling", new Date());
        book.setIsbn(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetIsbnTest(){
        Book book = new Book("9780545010221", null,"Harry Potter", "J. K. Rowling", new Date());
        book.setIsbn("");
    }
}
