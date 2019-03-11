package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;

import static org.junit.Assert.assertEquals;

public class OwnedBookTests {

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetImagesTest(){
        OwnedBook book = new OwnedBook("9780545010221", "available", null, null);
        book.getImages().clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetUsersRequestingTest(){
        OwnedBook book = new OwnedBook("9780545010221", "available", null, null);
        book.getUsersRequesting().add("johnsmith");
    }

    @Test
    public void setIsbnTest() {
        OwnedBook book = new OwnedBook("9780545010221", "available", null, null);
        book.setIsbn("1234441242141");
        assertEquals("1234441242141",book.getIsbn());
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetIsbnTest() {
        OwnedBook book = new OwnedBook("9780545010221", "available", null, null);
        book.setIsbn("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetIsbnTest() {
        OwnedBook book = new OwnedBook("9780545010221", "available", null, null);
        book.setIsbn(null);
    }

    @Test
    public void setOwnerTest() {
        OwnedBook book = new OwnedBook("9780545010221", "available", null, null);
        book.setOwner("janesmith@email.com");
        assertEquals("janesmith@email.com",book.getOwner());
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetStatusTest() {
        OwnedBook book = new OwnedBook("9780545010221", "available", null, null);
        book.setStatus("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetStatusTest() {
        OwnedBook book = new OwnedBook("9780545010221", "available", null, null);
        book.setStatus(null);
    }

    @Test
    public void setStatusTest() {
        OwnedBook book = new OwnedBook("9780545010221", "available", null, null);
        book.setStatus("requested");
        assertEquals("requested",book.getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetOwnerTest() {
        OwnedBook book = new OwnedBook("9780545010221", "available", null, null);
        book.setOwner("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetOwnerTest() {
        OwnedBook book = new OwnedBook("9780545010221", "available", null, null);
        book.setOwner(null);
    }
}
