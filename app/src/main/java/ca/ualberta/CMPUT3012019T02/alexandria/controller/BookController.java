package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ca.ualberta.CMPUT3012019T02.alexandria.cache.ObservableUserCache;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import ca.ualberta.CMPUT3012019T02.alexandria.model.OwnerListItem;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.BorrowedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.User;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;
import java9.util.Optional;
import java9.util.concurrent.CompletableFuture;

/**
 * This class manages the addition, retrieval, modification, and transactions of books
 */
public class BookController {

    private FirebaseDatabase firebase;
    private UserController userController;
    private NotificationController notificationController;
    private ObservableUserCache observableUserCache;

    private static BookController instance;
    private static ImageController imageController = ImageController.getInstance();

    private BookController() {
        firebase = FirebaseDatabase.getInstance();
        userController = UserController.getInstance();
        notificationController = NotificationController.getInstance();
        observableUserCache = ObservableUserCache.getInstance();
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


    /* Methods for adding/removing Books to/from the databaseReference */


    /**
     * Adds a book to the database if it does not exist
     *
     * @param book the book to add
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> addBook(Book book) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        DatabaseReference reference = firebase.getReference(getBookPath(book.getIsbn()));
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    reference.setValue(book)
                            .addOnSuccessListener(future::complete)
                            .addOnFailureListener(future::completeExceptionally);
                } else {
                    future.completeExceptionally(new IllegalArgumentException("Book with the isbn " + book.getIsbn() + "already exists"));
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
     * @return a CompletableFuture that returns an optional that may or may not contain the book
     */
    public CompletableFuture<Optional<Book>> getBook(@NonNull String isbn) {
        CompletableFuture<Optional<Book>> future = new CompletableFuture<>();

        firebase.getReference(getBookPath(isbn)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                future.complete(Optional.ofNullable(dataSnapshot.getValue(Book.class)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    /**
     * Updates a book in the database
     *
     * @param book the updated book
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> updateBook(@NonNull Book book) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        DatabaseReference reference = firebase.getReference(getBookPath(book.getIsbn()));
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    HashMap<String, Object> update = new HashMap<>();
                    update.put("title", book.getTitle());
                    update.put("author", book.getAuthor());
                    update.put("description", book.getDescription());
                    update.put("imageId", book.getImageId());
                    // Does not update availableFrom to preserve data integrity

                    firebase.getReference(getBookPath(book.getIsbn())).updateChildren(update)
                            .addOnSuccessListener(future::complete)
                            .addOnFailureListener(future::completeExceptionally);

                } else {
                    future.completeExceptionally(new IllegalArgumentException("A book with the isbn " + book.getIsbn() + " does not exist"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });


        return future;
    }

    // Deleting books will not be supported. Deleting books that are currently being used in transactions can compromise database integrity.


    /* Book transaction methods */


    // From the perspective of a borrower

    /**
     * As the current user (borrower), request a book from another user
     *
     * @param isbn    isbn of the book to request
     * @param ownerId id of the user to request to
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> requestBook(@NonNull String isbn, @NonNull String ownerId) {
        if (!userController.isAuthenticated()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Not currently logged in"));
        }

        CompletableFuture<Void> future = new CompletableFuture<>();

        getUserOwnedBook(isbn, ownerId).thenAcceptAsync(ownedBookOptional -> {
            if (ownedBookOptional.isPresent()) {
                firebase.getReference(getOwnedBookPath(ownerId, isbn)).runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        OwnedBook ownedBook = mutableData.getValue(OwnedBook.class);
                        if (ownedBook == null) {
                            return Transaction.success(mutableData); // Don't be fooled. This is necessary for a transaction to work correctly. It will not set the value to null in the live database.
                        }
                        ownedBook.updateState();
                        try {
                            ownedBook.addRequest(userController.getMyId());
                        } catch (Exception e) {
                            e.printStackTrace();
                            return Transaction.abort();
                        }
                        mutableData.setValue(ownedBook);
                        return Transaction.success(mutableData);

                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                        if (b) {
                            firebase.getReference(getBorrowedBookPath(userController.getMyId(), isbn))
                                    .setValue(new BorrowedBook(isbn, "requested", ownerId))
                                    .addOnSuccessListener(future::complete)
                                    .addOnFailureListener(future::completeExceptionally); // a potentially problematic error. Leaves ambiguous data in database
                            userController.getMyProfile().handleAsync((myProfile, profileError) -> {
                                if (profileError == null) {
                                    getBook(isbn).handleAsync((book, bookError) -> {
                                        if (bookError == null) {
                                            notificationController.sendNotification(ownerId, myProfile.getUsername(), "Requested " + book.get().getTitle());
                                        }
                                        return null;
                                    });
                                }
                                return null;
                            });
                        } else {
                            if (databaseError == null) {
                                future.completeExceptionally(new IllegalStateException("Book was not able to be requested"));
                            } else {
                                future.completeExceptionally(databaseError.toException());
                            }
                        }
                    }
                });
            } else {
                future.completeExceptionally(new IllegalArgumentException("User " + ownerId + " does not have a book with isbn " + isbn));
            }
        });

        return future;
    }

    /**
     * As the current user (borrower), cancel a request for a book from another user
     *
     * @param isbn    isbn of the book to cancel the request for
     * @param ownerId id of the user you sent the request to
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> cancelRequest(@NonNull String isbn, @NonNull String ownerId) {
        if (!userController.isAuthenticated()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Not currently logged in"));
        }

        CompletableFuture<Void> future = new CompletableFuture<>();
        firebase.getReference(getOwnedBookPath(ownerId, isbn)).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                OwnedBook ownedBook = mutableData.getValue(OwnedBook.class);
                if (ownedBook == null) {
                    return Transaction.success(mutableData);
                }
                ownedBook.updateState();
                try {
                    ownedBook.cancelRequest(userController.getMyId());
                } catch (Exception e) {
                    e.printStackTrace();
                    return Transaction.abort();
                }
                mutableData.setValue(ownedBook);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                if (b) {
                    HashMap<String, Object> update = new HashMap<>();
                    update.put(getBorrowedBookPath(userController.getMyId(), isbn), null);
                    update.put(getBookPath(isbn) + "/availableFrom/" + ownerId, 1);
                    firebase.getReference()
                            .updateChildren(update)
                            .addOnSuccessListener(future::complete)
                            .addOnFailureListener(future::completeExceptionally); // a potentially problematic error. Leaves ambiguous data in database
                    userController.getMyProfile().handleAsync((myProfile, profileError) -> {
                        if (profileError == null) {
                            getBook(isbn).handleAsync((book, bookError) -> {
                                if (bookError == null) {
                                    notificationController.sendNotification(ownerId, myProfile.getUsername(), "Cancelled their request for " + book.get().getTitle());
                                }
                                return null;
                            });
                        }
                        return null;
                    });
                } else {
                    if (databaseError == null) {
                        future.completeExceptionally(new IllegalStateException("Request was not able to be cancelled"));
                    } else {
                        future.completeExceptionally(databaseError.toException());
                    }
                }
            }
        });
        return future;
    }

    public CompletableFuture<ArrayList<OwnerListItem>> getAvailableOwners(String isbn, ArrayList<String> availableOwners, String title, String author) {

        final CompletableFuture<ArrayList<OwnerListItem>> ownersFuture = new CompletableFuture<>();
        ArrayList<OwnerListItem> owners = new ArrayList<>();

//        FirebaseDatabase.getInstance().getReference("books/" + isbn + "/availableFrom").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                OwnerListItem owner = dataSnapshot.getValue(OwnerListItem.class);
//                owners.add(owner);
//                synchronized(owners){
//                    owners.notify();
//                }
////                BookController.this.notifyAll();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        // for each owner id, get their profile
        for ( String owner : availableOwners) {
            CompletableFuture<UserProfile> userFuture = userController.getUserProfile(owner);

            userFuture.handleAsync(
                    (profile, error) -> {
                        if (error == null) {
                            CompletableFuture<Bitmap> profileImage = imageController.getImage(profile.getPicture());

                            // get the profile image of the user, then add it the list
                            profileImage.handleAsync(
                                    (image, imageError) -> {
                                        if (imageError == null){
                                            OwnerListItem completeOwner = new OwnerListItem(
                                                    image,
                                                    profile.getUsername(),
                                                    profile.getUsername(),
                                                    isbn,
                                                    "available",
                                                    title,
                                                    author
                                            );
                                            owners.add(completeOwner);
                                        } else {
                                            imageError.printStackTrace();
                                        }
                                        return null;
                                    }
                            );
                        } else {
                            error.printStackTrace();
                        }
                        return null;
                    }
                );
        }

        ownersFuture.complete(owners);

        return ownersFuture;
    }


    // From perspective of a book owner

    /**
     * As the current user (book owner), accept a request on your book made by another user
     *
     * @param isbn       isbn of your book
     * @param borrowerId id of the user whose request you want to accept
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> acceptRequest(@NonNull String isbn, @NonNull String borrowerId) {
        if (!userController.isAuthenticated()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Not currently logged in"));
        }

        CompletableFuture<Void> future = new CompletableFuture<>();
        firebase.getReference(getOwnedBookPath(userController.getMyId(), isbn)).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                OwnedBook ownedBook = mutableData.getValue(OwnedBook.class);
                if (ownedBook == null) {
                    return Transaction.success(mutableData);
                }
                ownedBook.updateState();
                try {
                    ownedBook.acceptRequest(borrowerId);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Transaction.abort();
                }
                mutableData.setValue(ownedBook);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                if (b) {
                    HashMap<String, Object> update = new HashMap<>();
                    OwnedBook ownedBook = dataSnapshot.getValue(OwnedBook.class);
                    update.put(getBorrowedBookPath(ownedBook.getUserBorrowing(), isbn) + "/status", "accepted");
                    for (String user : ownedBook.getRemovedRequests().keySet()) {
                        update.put(getBorrowedBookPath(user, isbn), null);
                    }
                    update.put(getOwnedBookPath(userController.getMyId(), isbn) + "/removedRequests", null);
                    update.put(getBookPath(isbn) + "/availableFrom/" + userController.getMyId(), null);
                    firebase.getReference()
                            .updateChildren(update)
                            .addOnSuccessListener(future::complete)
                            .addOnFailureListener(future::completeExceptionally); // a potentially problematic error. Leaves ambiguous data in database
                    userController.getMyProfile().handleAsync((myProfile, profileError) -> {
                        if (profileError == null) {
                            getBook(isbn).handleAsync((book, bookError) -> {
                                if (bookError == null) {

                                    notificationController.sendNotification(borrowerId, myProfile.getUsername(), "Accepted your request for " + book.get().getTitle());

                                    for (String user : ownedBook.getRemovedRequests().keySet()) {
                                        notificationController.sendNotification(user, myProfile.getUsername(), "Declined your request for " + book.get().getTitle());
                                    }

                                }
                                return null;
                            });
                        }
                        return null;
                    });
                } else {
                    if (databaseError == null) {
                        future.completeExceptionally(new IllegalStateException("Unable to accept user's request"));
                    } else {
                        future.completeExceptionally(databaseError.toException());
                    }
                }
            }
        });
        return future;
    }

    /**
     * As the current user (book owner), decline a request on your book made by another user
     *
     * @param isbn       isbn of your book
     * @param borrowerId id of the user whose request you want to decline
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> declineRequest(@NonNull String isbn, @NonNull String borrowerId) {
        if (!userController.isAuthenticated()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Not currently logged in"));
        }

        CompletableFuture<Void> future = new CompletableFuture<>();
        firebase.getReference(getOwnedBookPath(userController.getMyId(), isbn)).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                OwnedBook ownedBook = mutableData.getValue(OwnedBook.class);
                if (ownedBook == null) {
                    return Transaction.success(mutableData);
                }
                ownedBook.updateState();
                try {
                    ownedBook.cancelRequest(borrowerId);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Transaction.abort();
                }
                mutableData.setValue(ownedBook);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                if (b) {
                    HashMap<String, Object> update = new HashMap<>();
                    update.put(getBorrowedBookPath(borrowerId, isbn), null);
                    update.put(getBookPath(isbn) + "/availableFrom/" + userController.getMyId(), 1);
                    firebase.getReference()
                            .updateChildren(update)
                            .addOnSuccessListener(future::complete)
                            .addOnFailureListener(future::completeExceptionally); // a potentially problematic error. Leaves ambiguous data in database
                    userController.getMyProfile().handleAsync((myProfile, profileError) -> {
                        if (profileError == null) {
                            getBook(isbn).handleAsync((book, bookError) -> {
                                if (bookError == null) {
                                    notificationController.sendNotification(borrowerId, myProfile.getUsername(), "Declined your request for " + book.get().getTitle());
                                }
                                return null;
                            });
                        }
                        return null;
                    });
                } else {
                    if (databaseError == null) {
                        future.completeExceptionally(new IllegalStateException("Unable to decline user's request"));
                    } else {
                        future.completeExceptionally(databaseError.toException());
                    }
                }
            }
        });
        return future;
    }


    // Scanning

    /**
     * Confirms that you have scanned a book you're borrowing.
     * Required for exchange and return.
     *
     * @param isbn isbn of the book
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> scanMyBorrowedBook(@NonNull String isbn) {
        if (!userController.isAuthenticated()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Not currently logged in"));
        }

        CompletableFuture<Void> future = new CompletableFuture<>();
        firebase.getReference(getBorrowedBookPath(userController.getMyId(), isbn)).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                BorrowedBook borrowedBook = mutableData.getValue(BorrowedBook.class);
                if (borrowedBook == null) {
                    return Transaction.success(mutableData);
                }
                try {
                    borrowedBook.setScanned(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Transaction.abort();
                }
                mutableData.setValue(borrowedBook);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                if (b) {
                    future.complete(null);
                } else {
                    if (databaseError == null) {
                        future.completeExceptionally(new IllegalStateException("Unable to scan borrowed book"));
                    } else {
                        future.completeExceptionally(databaseError.toException());
                    }
                }
            }
        });
        return future;
    }

    public CompletableFuture<Void> scanMyOwnedBook(@NonNull String isbn) {
        if (!userController.isAuthenticated()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Not currently logged in"));
        }

        CompletableFuture<Void> future = new CompletableFuture<>();
        firebase.getReference(getOwnedBookPath(userController.getMyId(), isbn)).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                OwnedBook ownedBook = mutableData.getValue(OwnedBook.class);
                if (ownedBook == null) {
                    return Transaction.success(mutableData);
                }
                try {
                    ownedBook.setScanned(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Transaction.abort();
                }
                mutableData.setValue(ownedBook);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                if (b) {
                    future.complete(null);
                } else {
                    if (databaseError == null) {
                        future.completeExceptionally(new IllegalStateException("Unable to scan owned book"));
                    } else {
                        future.completeExceptionally(databaseError.toException());
                    }
                }
            }
        });
        return future;
    }

    // From perspective of both sides (middleman)

    /**
     * Initiates an exchange of books between an owner and a borrower.
     * Pre-condition:
     * - Both owner and borrower must have scanned the book isbn
     *
     * @param isbn    isbn of the book to exchange
     * @param ownerId id of the owner of the book
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> exchangeBook(@NonNull String isbn, @NonNull String ownerId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        String ownedBookPath = getOwnedBookPath(ownerId, isbn);
        firebase.getReference(ownedBookPath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    OwnedBook ownedBook = dataSnapshot.getValue(OwnedBook.class);
                    ownedBook.updateState();

                    String borrowedBookPath = getBorrowedBookPath(ownedBook.getUserBorrowing(), isbn);
                    firebase.getReference(borrowedBookPath).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                BorrowedBook borrowedBook = dataSnapshot.getValue(BorrowedBook.class);

                                try {
                                    ownedBook.exchange(borrowedBook.getScanned());
                                    borrowedBook.setStatus("borrowed");
                                    borrowedBook.setScanned(false);
                                } catch (IllegalStateException e) {
                                    future.completeExceptionally(e);
                                    return;
                                }

                                Map<String, Object> update = new HashMap<>();
                                update.put(ownedBookPath, ownedBook);
                                update.put(borrowedBookPath, borrowedBook);
                                firebase.getReference().updateChildren(update)
                                        .addOnSuccessListener(future::complete)
                                        .addOnFailureListener(future::completeExceptionally);

                            } else {
                                future.completeExceptionally(new IllegalStateException("Borrowing user " + ownedBook.getUserBorrowing() + " is not borrowing " + isbn));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            future.completeExceptionally(databaseError.toException());
                        }
                    });

                } else {
                    future.completeExceptionally(new IllegalArgumentException("User " + ownerId + " does not have a book with isbn " + isbn));
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
     * As the current user (borrower), return a book to its owner user
     *
     * @param isbn    isbn of the book to return
     * @param ownerId id of the owner of the book
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> returnBook(@NonNull String isbn, @NonNull String ownerId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        String ownedBookPath = getOwnedBookPath(ownerId, isbn);
        firebase.getReference(ownedBookPath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    OwnedBook ownedBook = dataSnapshot.getValue(OwnedBook.class);
                    ownedBook.updateState();

                    String borrowedBookPath = getBorrowedBookPath(ownedBook.getUserBorrowing(), isbn);
                    firebase.getReference(borrowedBookPath).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                BorrowedBook borrowedBook = dataSnapshot.getValue(BorrowedBook.class);

                                try {
                                    ownedBook.beReturned(borrowedBook.getScanned());
                                } catch (IllegalStateException e) {
                                    future.completeExceptionally(e);
                                    return;
                                }

                                HashMap<String, Object> update = new HashMap<>();
                                update.put(ownedBookPath, ownedBook);
                                update.put(borrowedBookPath, null);
                                update.put(getBookPath(isbn) + "/availableFrom/" + ownerId, 1);
                                firebase.getReference().updateChildren(update)
                                        .addOnSuccessListener(future::complete)
                                        .addOnFailureListener(future::completeExceptionally);

                            } else {
                                future.completeExceptionally(new IllegalStateException("Borrowing user " + ownedBook.getUserBorrowing() + " is not borrowing " + isbn));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            future.completeExceptionally(databaseError.toException());
                        }
                    });

                } else {
                    future.completeExceptionally(new IllegalArgumentException("User " + ownerId + " does not have a book with isbn " + isbn));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }


    /* Current User (My) BorrowedBook and OwnedBook queries */


    // Borrowed Books

    /**
     * Gets a borrowed book from the current user's collection of borrowed books
     *
     * @param isbn isbn of the borrowed book
     * @return a CompletableFuture that returns an optional containing the borrowed book
     */
    public CompletableFuture<Optional<BorrowedBook>> getMyBorrowedBook(@NonNull String isbn) {
        if (!userController.isAuthenticated()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Not currently signed in"));
        }

        Optional<BorrowedBook> borrowedBookOptional = observableUserCache.getBorrowedBook(isbn);
        if (borrowedBookOptional.isPresent()) {
            return CompletableFuture.completedFuture(borrowedBookOptional);
        }

        CompletableFuture<Optional<BorrowedBook>> future = new CompletableFuture<>();
        firebase.getReference(getBorrowedBookPath(userController.getMyId(), isbn)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                future.complete(Optional.ofNullable(dataSnapshot.getValue(BorrowedBook.class)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }

    /**
     * Gets the current user's collection of borrowed books
     *
     * @return a CompletableFuture that contains a collection of borrowed books
     */
    public CompletableFuture<Collection<BorrowedBook>> getMyBorrowedBooks() {
        if (!userController.isAuthenticated()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Not currently signed in"));
        }

        Optional<Map<String, BorrowedBook>> borrowedBooksOptional = observableUserCache.getBorrowedBooks();
        if (borrowedBooksOptional.isPresent()) {
            return CompletableFuture.completedFuture(borrowedBooksOptional.get().values());
        }

        CompletableFuture<Collection<BorrowedBook>> future = new CompletableFuture<>();
        firebase.getReference(getBorrowedBooksPath(userController.getMyId())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, BorrowedBook> borrowedBookMap = dataSnapshot.getValue(new GenericTypeIndicator<Map<String, BorrowedBook>>() {
                    });
                    future.complete(borrowedBookMap.values());
                } else {
                    future.complete(Collections.emptySet());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }


    // Owned Books

    /**
     * Adds an owned book to the current user's collection of owned books
     *
     * @param ownedBook the owned book to add
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> addMyOwnedBook(@NonNull OwnedBook ownedBook) {
        if (!userController.isAuthenticated()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Not currently signed in"));
        }

        CompletableFuture<Void> future = new CompletableFuture<>();
        DatabaseReference reference = firebase.getReference(getOwnedBookPath(userController.getMyId(), ownedBook.getIsbn()));
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {

                    HashMap<String, Object> update = new HashMap<>();
                    String ownedBookPath = getOwnedBookPath(userController.getMyId(), ownedBook.getIsbn());
                    update.put(ownedBookPath + "/isbn", ownedBook.getIsbn());
                    update.put(ownedBookPath + "/status", "available");
                    update.put(ownedBookPath + "/owner", userController.getMyId());
                    update.put(ownedBookPath + "/imageId", ownedBook.getImageId());
                    update.put(getBookPath(ownedBook.getIsbn()) + "/availableFrom/" + userController.getMyId(), 1);
                    firebase.getReference().updateChildren(update)
                            .addOnSuccessListener(future::complete)
                            .addOnFailureListener(future::completeExceptionally);

                } else {
                    future.completeExceptionally(new IllegalArgumentException("An owned book with the isbn " + ownedBook.getIsbn() + " already exists"));
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
     * Gets an owned book from the current user's collection of owned books
     *
     * @param isbn isbn of the owned book
     * @return a CompletableFuture that returns an optional containing the owned book
     */
    public CompletableFuture<Optional<OwnedBook>> getMyOwnedBook(@NonNull String isbn) {
        if (!userController.isAuthenticated()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Not currently signed in"));
        }

        Optional<OwnedBook> ownedBookOptional = observableUserCache.getOwnedBook(isbn);
        if (ownedBookOptional.isPresent()) {
            return CompletableFuture.completedFuture(ownedBookOptional);
        }

        CompletableFuture<Optional<OwnedBook>> future = new CompletableFuture<>();
        firebase.getReference(getOwnedBookPath(userController.getMyId(), isbn)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                future.complete(Optional.ofNullable(dataSnapshot.getValue(OwnedBook.class)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }

    /**
     * Gets the current user's collection of owned books
     *
     * @return a CompletableFuture that contains a collection of owned books
     */
    public CompletableFuture<Collection<OwnedBook>> getMyOwnedBooks() {
        if (!userController.isAuthenticated()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Not currently signed in"));
        }

        Optional<Map<String, OwnedBook>> ownedBooksOptional = observableUserCache.getOwnedBooks();
        if (ownedBooksOptional.isPresent()) {
            return CompletableFuture.completedFuture(ownedBooksOptional.get().values());
        }

        CompletableFuture<Collection<OwnedBook>> future = new CompletableFuture<>();
        firebase.getReference(getOwnedBooksPath(userController.getMyId())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, OwnedBook> ownedBookMap = dataSnapshot.getValue(new GenericTypeIndicator<Map<String, OwnedBook>>() {
                    });
                    future.complete(ownedBookMap.values());
                } else {
                    future.complete(Collections.emptySet());
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
     * Updates the current user's owned book in the database
     *
     * @param ownedBook the owned book to update
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> updateMyOwnedBook(@NonNull OwnedBook ownedBook) {
        if (!userController.isAuthenticated()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Not currently logged in"));
        }

        CompletableFuture<Void> future = new CompletableFuture<>();
        DatabaseReference reference = firebase.getReference(getOwnedBookPath(userController.getMyId(), ownedBook.getIsbn()));
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    reference.child("imageId").setValue(ownedBook.getImageId())
                            .addOnSuccessListener(future::complete)
                            .addOnFailureListener(future::completeExceptionally);
                } else {
                    future.completeExceptionally(new IllegalArgumentException("No owned book exists with the given isbn " + ownedBook.getIsbn()));
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
     * Deletes an owned book from the current user's collection of owned books
     *
     * @param isbn isbn of the book to delete
     * @return a CompletableFuture signifying the success/failure of this operation
     */
    public CompletableFuture<Void> deleteMyOwnedBook(@NonNull String isbn) {
        if (!userController.isAuthenticated()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Not currently logged in"));
        }

        CompletableFuture<Void> future = new CompletableFuture<>();
        DatabaseReference reference = firebase.getReference(getOwnedBookPath(userController.getMyId(), isbn));
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    OwnedBook ownedBook = dataSnapshot.getValue(OwnedBook.class);

                    HashMap<String, Object> update = new HashMap<>();
                    update.put(getOwnedBookPath(userController.getMyId(), isbn), null);
                    for (String requester : ownedBook.getRequestingUsers()) {
                        update.put(getBorrowedBookPath(requester, isbn), null); // TODO: notify that the book is no longer available
                    }
                    if (ownedBook.getUserBorrowing() != null) {
                        update.put(getBorrowedBookPath(ownedBook.getUserBorrowing(), isbn), null);
                    }
                    update.put(getBookPath(isbn) + "/availableFrom/" + userController.getMyId(), null);
                    firebase.getReference().updateChildren(update)
                            .addOnSuccessListener(future::complete)
                            .addOnFailureListener(future::completeExceptionally);
                    userController.getMyProfile().handleAsync((myProfile, profileError) -> {
                        if (profileError == null) {
                            getBook(isbn).handleAsync((book, bookError) -> {
                                if (bookError == null) {
                                    for (String requester : ownedBook.getRequestingUsers()) {
                                        notificationController.sendNotification(requester, myProfile.getUsername(), "Declined your request for " + book.get().getTitle());
                                    }
                                }
                                return null;
                            });
                        }
                        return null;
                    });
                } else {
                    future.completeExceptionally(new IllegalArgumentException("No owned book exists with the given isbn " + isbn));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }


    /* User BorrowedBook and OwnedBook queries */


    // Owned

    /**
     * Gets an owned book from the user's collection of owned books
     *
     * @param userId id of the user
     * @param isbn   isbn of the book
     * @return a CompletableFuture that returns an optional that may or may not contain the owned book
     */
    public CompletableFuture<Optional<OwnedBook>> getUserOwnedBook(@NonNull String isbn, @NonNull String userId) {
        CompletableFuture<Optional<OwnedBook>> future = new CompletableFuture<>();
        firebase.getReference(getOwnedBookPath(userId, isbn)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                future.complete(Optional.ofNullable(dataSnapshot.getValue(OwnedBook.class)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }

    /**
     * Gets a user's collection of owned books
     *
     * @return a CompletableFuture that contains a collection of owned books
     */
    public CompletableFuture<Collection<OwnedBook>> getUserOwnedBooks(String userId) {
        CompletableFuture<Collection<OwnedBook>> future = new CompletableFuture<>();
        firebase.getReference(getOwnedBooksPath(userId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, OwnedBook> ownedBookMap = dataSnapshot.getValue(new GenericTypeIndicator<Map<String, OwnedBook>>() {
                    });
                    future.complete(ownedBookMap.values());
                } else {
                    future.complete(Collections.emptySet());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }


    /* Database Paths */


    private String getBookPath(@NonNull String isbn) {
        return "books/" + isbn;
    }

    private String getBorrowedBooksPath(@NonNull String userId) {
        return "users/" + userId + "/borrowedBooks";
    }

    private String getBorrowedBookPath(@NonNull String userId, @NonNull String isbn) {
        return "users/" + userId + "/borrowedBooks/" + isbn;
    }

    private String getOwnedBooksPath(@NonNull String userId) {
        return "users/" + userId + "/ownedBooks";
    }

    private String getOwnedBookPath(@NonNull String userId, @NonNull String isbn) {
        return "users/" + userId + "/ownedBooks/" + isbn;
    }

}
