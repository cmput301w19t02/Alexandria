package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.model.BookList;

/**
 * Provides lists of borrowed and owned books in a UI-friendly format
 */
public interface BookListProvider {
    List<BookList> getBorrowedBookList();
    List<BookList> getOwnedBookList();
}
