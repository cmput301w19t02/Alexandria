package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ca.ualberta.CMPUT3012019T02.alexandria.cache.BookCache;
import ca.ualberta.CMPUT3012019T02.alexandria.cache.ObservableUserCache;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.BorrowedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserBook;
import java9.util.concurrent.CompletableFuture;

/**
 * This class manages the addition, retrieval, modification, and transactions of books
 */
public class BookController {

    private DatabaseReference database;
    private ObservableUserCache myCache;
    private BookCache bookCache;

    private static BookController instance;

    private BookController() {
        database = FirebaseDatabase.getInstance().getReference();
        myCache = ObservableUserCache.getInstance();
        bookCache = BookCache.getInstance();
    }

    /**
     * Gets the singleton instance of this controller
     *
     * @return instance of BookController
     */
    public static BookController getInstance() {
        if (instance == null) {
            instance = new BookController();
        }
        return instance;
    }


    /* Methods for adding/removing Books to/from the database */


    /**
     * Add a book to the database
     *
     * @param book the book to add
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> addBook(Book book) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        DatabaseReference bookReference = getBookDatabaseReference(book.getIsbn());
        bookReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    bookCache.putBook(book);
                    bookReference.setValue(book)
                            .addOnSuccessListener(future::complete)
                            .addOnFailureListener(future::completeExceptionally);

                } else {
                    future.completeExceptionally(new IllegalArgumentException("Book already exists"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }

    /**
     * Gets a book from the database
     *
     * @param isbn isbn of the book to retrieve
     * @return a CompletableFuture containing a book object from the database with the given isbn
     */
    public CompletableFuture<Book> getBook(@NonNull String isbn) {
        if(bookCache.getBook(isbn)==null) {
            final CompletableFuture<Book> future = new CompletableFuture<>();
            DatabaseReference bookReference = getBookDatabaseReference(isbn);
            bookReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Book book = dataSnapshot.getValue(Book.class);
                        bookCache.putBook(book);
                        future.complete(book);
                    } else {
                        future.completeExceptionally(new IllegalArgumentException("Book does not exist"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    future.completeExceptionally(databaseError.toException());
                }
            });
            return future;
        }
        else {
            return CompletableFuture.completedFuture(bookCache.getBook(isbn));
        }
    }

    /**
     * Update a book in the database, or add one if it does not exist
     *
     * @param book the updated book
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> updateBook(@NonNull Book book) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        bookCache.putBook(book);
        getBookDatabaseReference(book.getIsbn()).setValue(book)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    /**
     * Delete a book in the database
     *
     * @param isbn of the book to delete
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> deleteBook(@NonNull String isbn) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        bookCache.deleteBook(isbn);
        getBookDatabaseReference(isbn).setValue(null)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }


    /* Book transaction methods */


    // TODO: transaction methods for next project part


    // From the perspective of a borrower

    /**
     * As the current user (borrower), request a book from another user
     *
     * @param isbn isbn of the book to request
     * @param id   id of the user to request to
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> requestBook(@NonNull String isbn, @NonNull String id) {
        final CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {

                if (getMyUserId().equals(id)) {
                    throw new IllegalArgumentException("You can not request a book from yourself");
                }

                OwnedBook ownedBook = getUserOwnedBook(id, isbn).get();

                // If the owned book status is not available or requested, then it's not able to be requested.
                if (!(ownedBook.getStatus().equals("available") || ownedBook.getStatus().equals("requested"))) {
                    throw new IllegalStateException("The book is not available to be requested from this owner");
                }
                // Otherwise, proceed with the request

                // Create and add a borrowed book to the current user's collection
                BorrowedBook borrowedBook = new BorrowedBook(isbn, "requested", id);
                addMyBorrowedBook(borrowedBook).get();

                // Set the status and add the current user to the collection of users requesting the book
                ownedBook.setStatus("requested");
                ownedBook.addUserRequesting(getMyUserId());
                updateUserOwnedBook(id, ownedBook).get();

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        return future;
    }

    /**
     * As the current user (borrower), cancel a request for a book from another user
     *
     * @param isbn isbn of the book to cancel the request for
     * @param id   id of the user you sent the request to
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> cancelRequest(@NonNull String isbn, @NonNull String id) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        // TODO: Finish implementation
        return future;
    }

    /**
     * As the current user (borrower), return a book to its owner user
     *
     * @param isbn isbn of the book to return
     * @param id   id of the owner of the book
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> returnBook(@NonNull String isbn, @NonNull String id) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        // TODO: Finish implementation
        return future;
    }


    // From perspective of a book owner

    /**
     * As the current user (book owner), accept a request on your book made by another user
     *
     * @param isbn isbn of your book
     * @param id   id of the user whose request you want to accept
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> acceptBookRequest(@NonNull String isbn, @NonNull String id) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        // TODO: Finish implementation
        return future;
    }

    /**
     * As the current user (book owner), decline a request on your book made by another user
     *
     * @param isbn isbn of your book
     * @param id   id of the user whose request you want to decline
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> declineBookRequest(@NonNull String isbn, @NonNull String id) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        // TODO: Finish implementation
        return future;
    }

    /**
     * As the current user (book owner), exchange a book with another user (if and only if you have accepted
     * their request).
     *
     * @param isbn isbn of your book
     * @param id   id of the user to exchange the book with
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> exchangeBook(@NonNull String isbn, @NonNull String id) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        // TODO: Finish implementation
        return future;
    }


    /* Current User (My) BorrowedBook and OwnedBook queries */


    // Borrowed Books

    private CompletableFuture<Void> addMyBorrowedBook(@NonNull BorrowedBook borrowedBook) {
        return addUserBorrowedBook(getMyUserId(), borrowedBook);
    }

    /**
     * Gets a borrowed book from the current user's collection of borrowed books
     *
     * @param isbn isbn of the borrowed book
     * @return a BorrowedBook with the same isbn
     */
    public CompletableFuture<BorrowedBook> getMyBorrowedBook(@NonNull String isbn) {
        if (myCache.getBorrowedBook(isbn) == null) {
            return getUserBorrowedBook(getMyUserId(), isbn);
        } else {
            return CompletableFuture.completedFuture(myCache.getBorrowedBook(isbn));
        }
    }

    /**
     * Gets the current user's collection of borrowed books
     *
     * @return a HashMap mapping book isbn Strings to BorrowedBook objects
     */
    public CompletableFuture<Map<String, BorrowedBook>> getMyBorrowedBooks() {
        if (myCache.getBorrowedBooks() == null) {
            return getUserBorrowedBooks(getMyUserId());
        }
        else {
            return CompletableFuture.completedFuture(myCache.getBorrowedBooks());
        }
    }

    private CompletableFuture<Void> updateMyBorrowedBook(@NonNull BorrowedBook borrowedBook) {
        return updateUserBorrowedBook(getMyUserId(), borrowedBook);
    }

    private CompletableFuture<Void> deleteMyBorrowedBook(@NonNull String isbn) {
        return deleteUserBorrowedBook(getMyUserId(), isbn);
    }


    // Owned Books

    /**
     * Adds an owned book to the current user's collection of owned books
     *
     * @param ownedBook the owned book to add
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> addMyOwnedBook(@NonNull OwnedBook ownedBook) {
        return addUserOwnedBook(getMyUserId(), ownedBook);
    }

    /**
     * Gets an owned book from the current user's collection of owned books
     *
     * @param isbn isbn of the owned book
     * @return an OwnedBook with the same isbn
     */
    public CompletableFuture<OwnedBook> getMyOwnedBook(@NonNull String isbn) {
        if (myCache.getOwnedBook(isbn) == null) {
            return getUserOwnedBook(getMyUserId(), isbn);
        } else {
            return CompletableFuture.completedFuture(myCache.getOwnedBook(isbn));
        }
    }

    /**
     * Gets the current user's collection of owned books
     *
     * @return a HashMap mapping book isbn Strings to OwnedBook objects
     */
    public CompletableFuture<Map<String, OwnedBook>> getMyOwnedBooks() {
        if (myCache.getOwnedBooks() == null) {
            return getUserOwnedBooks(getMyUserId());
        } else {
            return CompletableFuture.completedFuture(myCache.getOwnedBooks());
        }
    }

    /**
     * Updates the current user's owned book in the database
     *
     * @param ownedBook the owned book to update
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> updateMyOwnedBook(@NonNull OwnedBook ownedBook) {
        return updateUserOwnedBook(getMyUserId(), ownedBook);
    }

    /**
     * Deletes an owned book from the current user's collection of owned books
     *
     * @param isbn isbn of the book to delete
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> deleteMyOwnedBook(@NonNull String isbn) {
        return deleteUserOwnedBook(getMyUserId(), isbn);
    }


    /* User BorrowedBook and OwnedBook queries */


    // Borrowed

    private CompletableFuture<Void> addUserBorrowedBook(@NonNull String id, @NonNull BorrowedBook borrowedBook) {
        return addUserBook("borrowedBooks", BorrowedBook.class, id, borrowedBook);
    }

    /**
     * Gets a user's borrowed book from the collection of their borrowed books
     *
     * @param id   id of the user
     * @param isbn isbn of the book
     * @return a BorrowedBook with the same isbn
     */
    public CompletableFuture<BorrowedBook> getUserBorrowedBook(@NonNull String id, @NonNull String isbn) {
        return getUserBook("borrowedBooks", BorrowedBook.class, id, isbn);
    }

    /**
     * Get's the user's collection of borrowed books
     *
     * @param id id of the user
     * @return a HashMap mapping book isbn Strings to BorrowedBook objects
     */
    public CompletableFuture<Map<String, BorrowedBook>> getUserBorrowedBooks(@NonNull String id) {
        return getUserBooks("borrowedBooks", new GenericTypeIndicator<Map<String, BorrowedBook>>() {
        }, id);
    }

    private CompletableFuture<Void> updateUserBorrowedBook(@NonNull String id, @NonNull BorrowedBook borrowedBook) {
        return updateUserBook("borrowedBooks", id, borrowedBook);
    }

    private CompletableFuture<Void> deleteUserBorrowedBook(@NonNull String id, @NonNull String isbn) {
        return deleteUserBook("borrowedBooks", id, isbn);
    }


    // Owned

    private CompletableFuture<Void> addUserOwnedBook(@NonNull String id, @NonNull OwnedBook ownedBook) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        addUserBook("ownedBooks", OwnedBook.class, id, ownedBook).thenRun(() -> addAvailableOwner(id, ownedBook.getIsbn()).thenRun(() -> future.complete(null)).exceptionally(throwable -> {
            future.completeExceptionally(throwable);
            return null;
        })).exceptionally(throwable -> {
            future.completeExceptionally(throwable);
            return null;
        });
        return future;
    }

    /**
     * Gets an owned book from the user's collection of owned books
     *
     * @param id   id of the user
     * @param isbn isbn of the book
     * @return an OwnedBook with the same isbn
     */
    public CompletableFuture<OwnedBook> getUserOwnedBook(@NonNull String id, @NonNull String isbn) {
        return getUserBook("ownedBooks", OwnedBook.class, id, isbn);
    }

    /**
     * Gets the user's collection of owned books
     *
     * @param id id of the user
     * @return a HashMap mapping book isbn Strings to OwnedBook objects
     */
    public CompletableFuture<Map<String, OwnedBook>> getUserOwnedBooks(@NonNull String id) {
        return getUserBooks("ownedBooks", new GenericTypeIndicator<Map<String, OwnedBook>>() {}, id);
    }

    private CompletableFuture<Void> updateUserOwnedBook(@NonNull String id, @NonNull OwnedBook ownedBook) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        updateUserBook("ownedBooks", id, ownedBook).thenRun(() -> {
            if (ownedBook.getStatus().equals("available") || ownedBook.getStatus().equals("requested")) {
                addAvailableOwner(id, ownedBook.getIsbn()).thenRun(() -> future.complete(null)).exceptionally(throwable -> {
                    future.completeExceptionally(throwable);
                    return null;
                });
            } else {
                removeAvailableOwner(id, ownedBook.getIsbn()).thenRun(() -> future.complete(null)).exceptionally(throwable -> {
                    future.completeExceptionally(throwable);
                    return null;
                });
            }
        }).exceptionally(throwable -> {
            future.completeExceptionally(throwable);
            return null;
        });
        return future;
    }

    private CompletableFuture<Void> deleteUserOwnedBook(@NonNull String id, @NonNull String isbn) {
        return deleteUserBook("ownedBooks", id, isbn).thenCombine(removeAvailableOwner(id, isbn), ((aVoid, aVoid2) -> null));
    }


    /* Methods for querying generic UserBooks */


    private <T extends UserBook> CompletableFuture<Void> addUserBook(@NonNull String userBookPath, @NonNull Class<T> classType, @NonNull String id, @NonNull T userBook) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        DatabaseReference userBookReference = getUserBookReference(userBookPath, id, userBook.getIsbn());
        userBookReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    userBookReference.setValue(userBook)
                            .addOnSuccessListener(future::complete)
                            .addOnFailureListener(future::completeExceptionally);
                } else {
                    future.completeExceptionally(new IllegalArgumentException("A " + classType.getSimpleName() + " of the same isbn already exists"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    private <T extends UserBook> CompletableFuture<T> getUserBook(@NonNull String userBookPath, @NonNull Class<T> classType, @NonNull String id, @NonNull String isbn) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        DatabaseReference ref = getUserBookReference(userBookPath, id, isbn);
        getUserBookReference(userBookPath, id, isbn).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    T book = dataSnapshot.getValue(classType);
                    future.complete(book);
                } else {
                    future.completeExceptionally(new IllegalArgumentException("No " + classType.getSimpleName() + " exists with the given isbn"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }

    private <T extends UserBook> CompletableFuture<Map<String, T>> getUserBooks(@NonNull String userBookPath, @NonNull GenericTypeIndicator<Map<String, T>> genericTypeIndicator, @NonNull String id) {
        final CompletableFuture<Map<String, T>> future = new CompletableFuture<>();
        getUserBooksReference(userBookPath, id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    future.complete(dataSnapshot.getValue(genericTypeIndicator));
                } else {
                    future.complete(new HashMap<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }

    private <T extends UserBook> CompletableFuture<Void> updateUserBook(@NonNull String userBookPath, @NonNull String id, @NonNull T userBook) {
        return updateUserBook(userBookPath, id, userBook.getIsbn(), userBook);
    }

    private <T extends UserBook> CompletableFuture<Void> deleteUserBook(@NonNull String userBookPath, @NonNull String id, @NonNull String isbn) {
        return updateUserBook(userBookPath, id, isbn, null);
    }

    private <T extends UserBook> CompletableFuture<Void> updateUserBook(@NonNull String userBookPath, @NonNull String id, @NonNull String isbn, T userBook) {
        final CompletableFuture<Void> future = new CompletableFuture<>();

        getUserBookReference(userBookPath, id, isbn).setValue(userBook)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }


    /* Updating Available Book Owners */


    private CompletableFuture<Void> addAvailableOwner(String id, String isbn) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                Book book = getBook(isbn).get(5, TimeUnit.SECONDS);
                if (book.getAvailableOwners().contains(id)) {
                    future.complete(null);
                    return;
                }
                book.addAvailableOwners(id);
                updateBook(book).get(5, TimeUnit.SECONDS);
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    private CompletableFuture<Void> removeAvailableOwner(String id, String isbn) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {
                Book book = getBook(isbn).get(5, TimeUnit.SECONDS);
                book.removeAvailableOwners(id);
                updateBook(book).get(5, TimeUnit.SECONDS);
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }


    /* My User ID */


    private String getMyUserId() {
        return UserController.getInstance().getMyId();
    }


    /* Database Referencing */


    private DatabaseReference getBookDatabaseReference(@NonNull String isbn) {
        return database.child("books").child(isbn);
    }

    private DatabaseReference getUserDatabaseReference(@NonNull String id) {
        return database.child("users").child(id);
    }

    private DatabaseReference getUserBooksReference(@NonNull String userBookPath, @NonNull String id) {
        return getUserDatabaseReference(id).child(userBookPath);
    }

    private DatabaseReference getUserBookReference(@NonNull String userBookPath, @NonNull String id, @NonNull String isbn) {
        return getUserBooksReference(userBookPath, id).child(isbn);
    }

}
