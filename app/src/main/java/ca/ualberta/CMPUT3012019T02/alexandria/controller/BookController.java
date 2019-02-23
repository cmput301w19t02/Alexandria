package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;

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

    public Book getBook(String isbn) {
        // TODO: Finish implementation
        // does a call to get a specific book
        throw new UnsupportedOperationException();
    }

    public ArrayList<Book> getMyBorrowedBooks() {
        // TODO: Finish implementation
        // does a call to get my borrowed books using "my" id
        throw new UnsupportedOperationException();
    }

    public ArrayList<Book> getMyOwnedBooks() {
        // TODO: Finish implementation
        // does a call to getOwnedBooks to get my owned books using "my" id
        throw new UnsupportedOperationException();
    }

    public ArrayList<Book> getOwnedBooks(String id) {
        // TODO: Finish implementation
        // does a call to get owned books using user id
        throw new UnsupportedOperationException();
    }

    public void updateMyBorrowedBooks(Book book) {
        // TODO: Finish implementation
        // updates my borrowed books
        // should do a call to getMyBorrowedBooks to get the current borrowed list
        throw new UnsupportedOperationException();
    }

    public void updateMyOwnedBooks(Book book) {
        // TODO: Finish implementation
        // updates my owned books
        // does a call to getMyOwnedBooks to get the current borrowed list
        throw new UnsupportedOperationException();
    }

    public void updateBook(String isbn, Book newBook) {
        // TODO: Finish implementation
        // update a book, if necessary
    }

}

