package ca.ualberta.CMPUT3012019T02.alexandria.model.user;

/**
 * This class represents the relationship between a User and an OwnedBook, where the User has made
 * some sort of transaction/interaction with the OwnedBook (e.g. requested, exchanged, etc.)
 */
public class BorrowedBook extends UserBook {

    /**
     * No args constructor to maintain compatibility with Firebase deserializer
     * TO BE USED BY BOOK CONTROLLER ONLY
     */
    @Deprecated
    public BorrowedBook() {
        super(null, null, null);
    }

    /**
     * Creates a new BorrowedBook from isbn, status, and owner
     * TO BE USED BY BOOK CONTROLLER ONLY
     * @param isbn isbn of the borrowed book
     * @param status status of the borrowed book
     * @param owner owner of the borrowed book
     */
    @Deprecated
    public BorrowedBook(String isbn, String status, String owner) {
        super(isbn, status, owner);
    }

}
