package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.content.res.Resources;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.BorrowedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;

/**
 * This class manages the addition, retrieval, modification, and transactions of books
 */
public class BookController {

    private FirebaseDatabase database;

    private static BookController instance;

    private BookController() { }

    public static BookController getInstance() {
        if (instance == null) {
            instance = new BookController();
        }
        return instance;
    }


    /* Methods for adding/removing Books to/from the database */


    /**
     * Add a book to the database
     * @param book the book to add
     */
    public void addBook(Book book) {
        updateBook(book);
    }

    /**
     * Retrieve a book from the database
     * @param isbn isbn of the book to retrieve
     * @return a book object from the database with the given isbn
     * @throws Resources.NotFoundException if book was not found in the database with matching isbn
     */
    public Book getBook(String isbn) throws Resources.NotFoundException {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

    /**
     * Update a book in the database
     * @param book the updated book
     */
    public void updateBook(Book book) {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }


    /* Book transaction methods */


    /**
     * As the current user (borrower), request a book from another user
     * @param isbn isbn of the book to request
     * @param id id of the user to request to
     */
    public void requestBook(String isbn, String id) {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

    /**
     * As the current user (book owner), accept a request on your book made by another user
     * @param isbn isbn of your book
     * @param id id of the user whose request you want to accept
     */
    public void acceptBookRequest(String isbn, String id) {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

    /**
     * As the current user (book owner), decline a request on your book made by another user
     * @param isbn isbn of your book
     * @param id id of the user whose request you want to decline
     */
    public void declineBookRequest(String isbn, String id) {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

    /**
     * As the current user (book owner), exchange a book with another user (if and only if you have accepted
     * their request).
     * @param isbn isbn of your book
     * @param id id of the user to exchange the book with
     */
    public void exchangeBook(String isbn, String id) {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

    /**
     * As the current user (borrower), return a book to its owner user
     * @param isbn isbn of the book to return
     * @param id id of the owner of the book
     */
    public void returnBook(String isbn, String id) {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }


    /* Methods for querying the current user's BorrowedBooks and OwnedBooks */


    /**
     * Retrieve a list of borrowed books from the current user
     * @return a list of BorrowedBook objects
     */
    public ArrayList<BorrowedBook> getMyBorrowedBooks() {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

    /**
     * Add a book to the current user's collection of owned books
     * @param ownedBook the OwnedBook to add
     */
    public void addToMyOwnedBooks(OwnedBook ownedBook) {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

    /**
     * Retrieve a particular owned book from the current user, by isbn
     * @param isbn isbn of the book to retrieve
     */
    public void getMyOwnedBook(String isbn) {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

    /**
     * Retrieve an unmodifiable list of owned books from the current user
     * @return an unmodifiable list of OwnedBook objects
     */
    public ArrayList<OwnedBook> getMyOwnedBooks() {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

    /**
     * Update an owned book of the current user
     * @param ownedBook the OwnedBook to update
     */
    public void updateMyOwnedBook(OwnedBook ownedBook) {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

    /**
     * Remove a book from the current user's owned books
     * @param ownedBook the OwnedBook to remove
     */
    public void removeFromMyOwnedBooks(OwnedBook ownedBook) {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }


    /* Methods for querying user books */


    /**
     * Retrieve a user's list of owned books
     * @param id id of the user
     * @return a list of the user's owned books
     */
    public ArrayList<OwnedBook> getOwnedBooks(String id) {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

}

