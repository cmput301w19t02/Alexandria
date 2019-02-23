package ca.ualberta.CMPUT3012019T02.alexandria;

import android.graphics.Bitmap;

import org.junit.Test;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;

import static org.junit.Assert.assertEquals;

public class BookTests {

    @Test
    public void constructorTest(){
        Bitmap image = Bitmap.createBitmap(32,32,Bitmap.Config.ARGB_8888);
        Date date = new Date();
        Book book = new Book("9780545010221", image,"Harry Potter", "J. K. Rowling", new Date());
        assertEquals("9780545010221",book.getIsbn());
        assertEquals(image,book.getImage());
        assertEquals("Harry Potter",book.getTitle());
        assertEquals("J. K. Rowling",book.getAuthor());
        assertEquals(date,book.getDate());
    }

    @Test
    public void setIsbnTest(){
        Book book = new Book("test", null,"Harry Potter", "J. K. Rowling", new Date());
        book.setIsbn("9780545010221");
        assertEquals("9780545010221",book.getIsbn());
    }

    @Test
    public void setImageTest(){
        Book book = new Book("9780545010221", null,"Harry Potter", "J. K. Rowling", new Date());
        Bitmap image = Bitmap.createBitmap(32,32,Bitmap.Config.ARGB_8888);
        book.setImage(image);
        assertEquals(image,book.getImage());
    }

    @Test
    public void setTitleTest(){
        Book book = new Book("9780545010221", null,"Harry Potter Book 1", "J. K. Rowling", new Date());
        book.setTitle("Harry Potter");
        assertEquals("Harry Potter",book.getTitle());
    }

    @Test
    public void setAuthorTest(){
        Book book = new Book("9780545010221", null,"Harry Potter", "J K Rowling", new Date());
        book.setAuthor("J. K. Rowling");
        assertEquals("J. K. Rowling",book.getAuthor());
    }

    @Test
    public void setDateTest(){
        Date date = new Date();
        Book book = new Book("9780545010221", null,"Harry Potter", "J K Rowling", new Date());
        book.setDate(date);
        assertEquals(date,book.getDate());
    }

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
    public void nullTitleConstructorTest(){
        new Book("9780545010221", null,null, "J. K. Rowling", new Date());
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyTitleConstructorTest(){
        new Book("9780545010221", null,"", "J. K. Rowling", new Date());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullAuthorConstructorTest(){
        new Book("9780545010221", null,"Harry Potter", null, new Date());
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyAuthorConstructorTest(){
        new Book("9780545010221", null,"Harry Potter", "", new Date());
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

    @Test(expected = IllegalArgumentException.class)
    public void nullSetTitleTest(){
        Book book = new Book("9780545010221", null,"Harry Potter", "J. K. Rowling", new Date());
        book.setTitle(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetTitleTest(){
        Book book = new Book("9780545010221", null,"Harry Potter", "J. K. Rowling", new Date());
        book.setTitle("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetAuthorTest(){
        Book book = new Book("9780545010221", null,"Harry Potter", "J. K. Rowling", new Date());
        book.setAuthor(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetAuthorTest(){
        Book book = new Book("9780545010221", null,"Harry Potter", "J. K. Rowling", new Date());
        book.setAuthor("");
    }
}
