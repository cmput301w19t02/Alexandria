package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.BorrowedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserBook;
import java9.util.concurrent.CompletableFuture;

class UserBookType<T extends UserBook> {

    public static final UserBookType<BorrowedBook> BORROWED = new UserBookType<>("borrowedBooks", BorrowedBook.class, new GenericTypeIndicator<HashMap<String, BorrowedBook>>() { });
    public static final UserBookType<OwnedBook> OWNED = new UserBookType<>("ownedBooks", OwnedBook.class, new GenericTypeIndicator<HashMap<String, OwnedBook>>() { });

    private String dataPath;
    private Class<T> genericClass;
    private GenericTypeIndicator<HashMap<String, T>> hashMapGenericTypeIndicator;

    private UserBookType(String dataPath, Class<T> classType, GenericTypeIndicator<HashMap<String, T>> hashMapGenericTypeIndicator) {
        this.dataPath = dataPath;
        this.genericClass = classType;
        this.hashMapGenericTypeIndicator = hashMapGenericTypeIndicator;
    }

    public String getDataPath() {
        return dataPath;
    }

    public Class<T> getGenericClass() {
        return genericClass;
    }

    public GenericTypeIndicator<HashMap<String, T>> getHashMapType() {
        return hashMapGenericTypeIndicator;
    }

}

/**
 * This class manages the addition, retrieval, modification, and transactions of books
 */
public class BookController {

    /*
        Notes:
        Finish transactions
        Make tests
        What if data changes in the middle of a transaction?
     */

    private DatabaseReference database;

    private static BookController instance;

    private BookController() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Gets the singleton instance of this controller
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
     * @param isbn isbn of the book to retrieve
     * @return a CompletableFuture containing a book object from the database with the given isbn
     */
    public CompletableFuture<Book> getBook(@NonNull String isbn) {
        final CompletableFuture<Book> future = new CompletableFuture<>();
        DatabaseReference bookReference = getBookDatabaseReference(isbn);
        bookReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    future.complete(dataSnapshot.getValue(Book.class));
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

    /**
     * Update a book in the database, or add one if it does not exist
     * @param book the updated book
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> updateBook(@NonNull Book book) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        getBookDatabaseReference(book.getIsbn()).setValue(book)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    /**
     * Delete a book in the database, or add one if it does not exist
     * @param isbn of the book to delete
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> deleteBook(@NonNull String isbn) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        getBookDatabaseReference(isbn).setValue(null)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }


    /* Book transaction methods */


    // From the perspective of a borrower

    /**
     * As the current user (borrower), request a book from another user
     * @param isbn isbn of the book to request
     * @param id id of the user to request to
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> requestBook(@NonNull String isbn, @NonNull String id) {
        final CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {

                OwnedBook ownedBook = getUserOwnedBook(id, isbn).get();

                // If the owned book status is not available or requested, then it's not able to be requested.
                if (!(ownedBook.getStatus() == "available" || ownedBook.getStatus() == "requested")) {
                    throw new IllegalStateException("The book is not available to be requested from this owner");
                }
                // Otherwise, proceed with the request

                // Set the status and add the current user to the collection of users requesting the book
                ownedBook.setStatus("requested");
                ownedBook.addUserRequesting(getMyUserId());

                // Create and add a borrowed book to the current user's collection
                BorrowedBook borrowedBook = new BorrowedBook(isbn, "requested", id);
                addMyBorrowedBook(borrowedBook).get();

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        return future;
    }

    /**
     * As the current user (borrower), cancel a request for a book from another user
     * @param isbn isbn of the book to cancel the request for
     * @param id id of the user you sent the request to
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> cancelRequest(@NonNull String isbn, @NonNull String id) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        // TODO: Finish implementation
        return future;
    }

    /**
     * As the current user (borrower), return a book to its owner user
     * @param isbn isbn of the book to return
     * @param id id of the owner of the book
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
     * @param isbn isbn of your book
     * @param id id of the user whose request you want to accept
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> acceptBookRequest(@NonNull String isbn, @NonNull String id) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        // TODO: Finish implementation
        return future;
    }

    /**
     * As the current user (book owner), decline a request on your book made by another user
     * @param isbn isbn of your book
     * @param id id of the user whose request you want to decline
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
     * @param isbn isbn of your book
     * @param id id of the user to exchange the book with
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
     * @param isbn isbn of the borrowed book
     * @return a BorrowedBook with the same isbn
     */
    public CompletableFuture<BorrowedBook> getMyBorrowedBook(@NonNull String isbn) {
        return getUserBorrowedBook(getMyUserId(), isbn);
    }

    /**
     * Gets the current user's collection of borrowed books
     * @return a HashMap mapping book isbn Strings to BorrowedBook objects
     */
    public CompletableFuture<HashMap<String, BorrowedBook>> getMyBorrowedBooks() {
        return getUserBorrowedBooks(getMyUserId());
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
     * @param ownedBook the owned book to add
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> addMyOwnedBook(@NonNull OwnedBook ownedBook) {
        return addUserOwnedBook(getMyUserId(), ownedBook);
    }

    /**
     * Gets an owned book from the current user's collection of owned books
     * @param isbn isbn of the owned book
     * @return an OwnedBook with the same isbn
     */
    public CompletableFuture<OwnedBook> getMyOwnedBook(@NonNull String isbn) {
        return getUserOwnedBook(getMyUserId(), isbn);
    }

    /**
     * Gets the current user's collection of owned books
     * @return a HashMap mapping book isbn Strings to OwnedBook objects
     */
    public CompletableFuture<HashMap<String, OwnedBook>> getMyOwnedBooks() {
        return getUserOwnedBooks(getMyUserId());
    }

    /**
     * Updates the current user's owned book in the database
     * @param ownedBook the owned book to update
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> updateMyOwnedBook(@NonNull OwnedBook ownedBook) {
        return updateUserOwnedBook(getMyUserId(), ownedBook);
    }

    /**
     * Deletes an owned book from the current user's collection of owned books
     * @param isbn isbn of the book to delete
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> deleteMyOwnedBook(@NonNull String isbn) {
        return deleteUserOwnedBook(getMyUserId(), isbn);
    }


    /* User BorrowedBook and OwnedBook queries */


    // Borrowed

    private CompletableFuture<Void> addUserBorrowedBook(@NonNull String id, @NonNull BorrowedBook borrowedBook) {
        return addUserBook(UserBookType.BORROWED, id, borrowedBook);
    }

    /**
     * Gets a user's borrowed book from the collection of their borrowed books
     * @param id id of the user
     * @param isbn isbn of the book
     * @return a BorrowedBook with the same isbn
     */
    public CompletableFuture<BorrowedBook> getUserBorrowedBook(@NonNull String id, @NonNull String isbn) {
        return getUserBook(UserBookType.BORROWED, id, isbn);
    }

    /**
     * Get's the user's collection of borrowed books
     * @param id id of the user
     * @return a HashMap mapping book isbn Strings to BorrowedBook objects
     */
    public CompletableFuture<HashMap<String, BorrowedBook>> getUserBorrowedBooks(@NonNull String id) {
        return getUserBooks(UserBookType.BORROWED, id);
    }

    private CompletableFuture<Void> updateUserBorrowedBook(@NonNull String id, @NonNull BorrowedBook borrowedBook) {
        return updateUserBook(UserBookType.BORROWED, id, borrowedBook);
    }
    private CompletableFuture<Void> deleteUserBorrowedBook(@NonNull String id, @NonNull String isbn) {
        return deleteUserBook(UserBookType.BORROWED, id, isbn);
    }


    // Owned

    private CompletableFuture<Void> addUserOwnedBook(@NonNull String id, @NonNull OwnedBook ownedBook) {
        return addUserBook(UserBookType.OWNED, id, ownedBook);
    }

    /**
     * Gets an owned book from the user's collection of owned books
     * @param id id of the user
     * @param isbn isbn of the book
     * @return an OwnedBook with the same isbn
     */
    public CompletableFuture<OwnedBook> getUserOwnedBook(@NonNull String id, @NonNull String isbn) {
        return getUserBook(UserBookType.OWNED, id, isbn);
    }

    /**
     * Gets the user's collection of owned books
     * @param id id of the user
     * @return a HashMap mapping book isbn Strings to OwnedBook objects
     */
    public CompletableFuture<HashMap<String, OwnedBook>> getUserOwnedBooks(@NonNull String id) {
        return getUserBooks(UserBookType.OWNED, id);
    }

    private CompletableFuture<Void> updateUserOwnedBook(@NonNull String id, @NonNull OwnedBook ownedBook) {
        return updateUserBook(UserBookType.OWNED, id, ownedBook);
    }
    private CompletableFuture<Void> deleteUserOwnedBook(@NonNull String id, @NonNull String isbn) {
        return deleteUserBook(UserBookType.OWNED, id, isbn);
    }


    /* Methods for querying generic UserBooks */


    private <T extends UserBook> CompletableFuture<Void> addUserBook(@NonNull UserBookType<T> userBookType, @NonNull String id, @NonNull T userBook) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        DatabaseReference userBookReference = getUserBookReference(userBookType, id, userBook.getIsbn());
        userBookReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    userBookReference.setValue(userBook)
                            .addOnSuccessListener(future::complete)
                            .addOnFailureListener(future::completeExceptionally);
                } else {
                    future.completeExceptionally(new IllegalArgumentException("A " + userBookType.getGenericClass().getSimpleName() + " of the same isbn already exists"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    private <T extends UserBook> CompletableFuture<T> getUserBook(@NonNull UserBookType<T> userBookType, @NonNull String id, @NonNull String isbn) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        getUserBookReference(userBookType, id, isbn).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    future.complete(dataSnapshot.getValue(userBookType.getGenericClass()));
                } else {
                    future.completeExceptionally(new IllegalArgumentException("No " + userBookType.getGenericClass().getSimpleName() + " exists with the given isbn"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }

    private <T extends UserBook> CompletableFuture<HashMap<String, T>> getUserBooks(@NonNull UserBookType<T> userBookType, @NonNull String id) {
        final CompletableFuture<HashMap<String, T>> future = new CompletableFuture<>();
        getUserBooksReference(userBookType, id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    future.complete(dataSnapshot.getValue(userBookType.getHashMapType()));
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

    private <T extends UserBook> CompletableFuture<Void> updateUserBook(@NonNull UserBookType<T> userBookType, @NonNull String id, @NonNull T userBook) {
        return updateUserBook(userBookType, id, userBook.getIsbn(), userBook);
    }

    private <T extends UserBook> CompletableFuture<Void> deleteUserBook(@NonNull UserBookType<T> userBookType, @NonNull String id, @NonNull String isbn) {
        return updateUserBook(userBookType, id, isbn, null);
    }

    private <T extends UserBook> CompletableFuture<Void> updateUserBook(@NonNull UserBookType<T> userBookType, @NonNull String id, @NonNull String isbn, T userBook) {
        final CompletableFuture<Void> future = new CompletableFuture<>();

        getUserBookReference(userBookType, id, isbn).setValue(userBook)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);

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

    private DatabaseReference getUserBooksReference(@NonNull UserBookType userBookType, @NonNull String id) {
        return getUserDatabaseReference(id).child(userBookType.getDataPath());
    }

    private DatabaseReference getUserBookReference(@NonNull UserBookType userBookType, @NonNull String id, @NonNull String isbn) {
        return getUserBooksReference(userBookType, id).child(isbn);
    }

}
