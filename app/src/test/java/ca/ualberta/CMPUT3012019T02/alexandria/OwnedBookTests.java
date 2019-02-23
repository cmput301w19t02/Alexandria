package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import ca.ualberta.CMPUT3012019T02.alexandria.model.OwnedBook;

public class OwnedBookTests {

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetImagesTest(){
        OwnedBook ownedBook = new OwnedBook("9780545010221", "Available",null);
        ownedBook.getImages().clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetUsersRequestingTest(){
        OwnedBook ownedBook = new OwnedBook("9780545010221", "Available",null);
        ownedBook.getUsersRequesting().add("johnsmith");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullIsbnConstructorTest(){
        new OwnedBook(null, "Available",null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyIsbnConstructorTest(){
        new OwnedBook("", "Available",null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetIsbnTest(){
        OwnedBook ownedBook = new OwnedBook("9780545010221", "Available",null);
        ownedBook.setIsbn(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetIsbnTest(){
        OwnedBook ownedBook = new OwnedBook("9780545010221", "Available",null);
        ownedBook.setIsbn("");
    }
}
