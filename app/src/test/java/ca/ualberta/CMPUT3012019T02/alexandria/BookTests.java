package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.Book;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BookTests {

    @Test
    public void simpleConstructorTest() {
        Book book = new Book("1234567891234", "Title of Book", "Author O. Book");
        assertEquals("1234567891234", book.getIsbn());
        assertEquals("Title of Book", book.getTitle());
        assertEquals("Author O. Book", book.getAuthor());
        assertNull(book.getDescription());
        assertNull(book.getImageId());
    }

    @Test
    public void fullConstructorTest() {
        Book book = new Book("1234567891234", "Title of Book", "Author O. Book", "A book description", "fake-image-id");
        assertEquals("1234567891234", book.getIsbn());
        assertEquals("Title of Book", book.getTitle());
        assertEquals("Author O. Book", book.getAuthor());
        assertEquals("A book description", book.getDescription());
        assertEquals("fake-image-id", book.getImageId());
    }

    @Test
    public void setImageTest() {
        Book book = new Book("1234567891234", "Title of Book", "Author O. Book");
        String imageId = "fake-image-id";
        book.setImageId(imageId);
        assertEquals(imageId, book.getImageId());
    }

    @Test
    public void setTitleTest() {
        Book book = new Book("1234567891234", "Title of Book", "Author O. Book");
        book.setTitle("A New Title");
        assertEquals("A New Title", book.getTitle());
    }

    @Test
    public void setAuthorTest() {
        Book book = new Book("1234567891234", "Title of Book", "Author O. Book");
        book.setAuthor("Author 2.0");
        assertEquals("Author 2.0", book.getAuthor());
    }

    @Test
    public void setDescriptionTest() {
        Book book = new Book("1234567891234", "Title of Book", "Author O. Book");
        book.setDescription("A brand-spankin' new book descriptor");
        assertEquals("A brand-spankin' new book descriptor", book.getDescription());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetAvailableOwnersIdsTest() {
        Book book = new Book("1234567891234", "Title of Book", "Author O. Book");
        book.getAvailableOwners().add("User Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyStringsConstructorTest() {
        new Book(" ", "", "     ", null, null);
    }

}
