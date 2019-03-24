package ca.ualberta.CMPUT3012019T02.alexandria.model.user;

import android.support.annotation.NonNull;

/**
 * This class represents the relationship between a User and an OwnedBook, where the User has made
 * some sort of transaction/interaction with the OwnedBook (e.g. requested, exchanged, etc.)
 */
public class BorrowedBook extends UserBook {

    /**
     * No args constructor for Firebase deserializer
     * TO BE USED BY FIREBASE ONLY
     */
    @Deprecated
    public BorrowedBook() {
        super(null, null, null);
    }

    /**
     * Creates a new borrowed book with isbn, status, and owner
     * TO BE USED BY BOOK CONTROLLER ONLY
     *
     * @param isbn
     * @param status
     * @param owner
     */
    @Deprecated
    public BorrowedBook(@NonNull String isbn, @NonNull String status, @NonNull String owner) {
        super(isbn, status, owner);
    }
}
