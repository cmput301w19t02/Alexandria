package ca.ualberta.CMPUT3012019T02.alexandria.model.user;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;

/**
 * This class represents a relationship between a User and a Book, where the User has ownership of a
 * particular Book. It also contains specific data that a User can attach to a Book they own.
 */
public class OwnedBook extends UserBook {

    private Map<String, Integer> requesting; // Users requesting this book. Only keys are used. Values are ignored. Required to be Map for Firebase deserializer
    private Map<String, Integer> removedRequests;
    private String imageId;
    private String userBorrowing;

    private State state;

    /**
     * No args constructor for Firebase to use when deserializing data
     * TO BE USED BY FIREBASE ONLY
     */
    @Deprecated
    public OwnedBook() {
        super(null, null, null);
        requesting = new HashMap<>();
        removedRequests = new HashMap<>();
        state = new Available();
    }

    /**
     * Create a new OwnedBook owned by the current logged in user with a status of available
     *
     * @param isbn isbn of the book
     */
    public OwnedBook(@NonNull String isbn) {
        super(isbn, "available", UserController.getInstance().getMyId());
        if (isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("Isbn can not be empty");
        }
        requesting = new HashMap<>();
        removedRequests = new HashMap<>();
        state = new Available();
    }

    /**
     * Create a new OwnedBook owned by the current logged in user
     * with a specified image to go along with, and a status of available.
     *
     * @param isbn isbn of the book
     * @param imageId image id for the book
     */
    public OwnedBook(@NonNull String isbn, @Nullable String imageId) {
        super(isbn, "available", UserController.getInstance().getMyId());
        if (isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("Isbn can not be empty");
        }
        requesting = new HashMap<>();
        removedRequests = new HashMap<>();
        this.imageId = imageId;
    }

    /**
     * Gets a collection of users requesting this owned book
     *
     * @return a collection of user ids
     */
    @Exclude
    public Collection<String> getRequestingUsers() {
        return Collections.unmodifiableSet(requesting.keySet());
    }

    /**
     * Gets a map of user ids to integers. The user ids are users who are requesting this book.
     * It is recommended to use {@link OwnedBook#getRequestingUsers()} instead.
     *
     * @return a map of user ids
     */
    public Map<String, Integer> getRequesting() {
        return requesting;
    }

    /**
     * Gets a map of user ids to integers. The user ids are users whose requests are being removed.
     *
     * @return a map of user ids
     */
    public Map<String, Integer> getRemovedRequests() {
        return removedRequests;
    }

    /**
     * Get image id of this owned book
     *
     * @return an optional that may contain image id
     */
    @Nullable
    public String getImageId() {
        return this.imageId;
    }

    /**
     * Sets the list of images of this owned book
     *
     * @param imageId id of the image for this book
     */
    public void setImageId(@Nullable String imageId) {
        this.imageId = imageId;
    }

    /**
     * Gets the id of the user borrowing this owned book
     *
     * @return an optional that may contain the id of the user borrowing this book
     */
    @Nullable
    public String getUserBorrowing() {
        return this.userBorrowing;
    }

    /**
     * Updates the state of this owned book depending on status.
     * Changes the behavior of transaction methods.
     */
    public void updateState() {
        if (getStatus().equals("available")) {
            state = new Available();
        } else if (getStatus().equals("requested")) {
            state = new Requested();
        } else if (getStatus().equals("accepted")) {
            state = new Accepted();
        } else if (getStatus().equals("borrowed")) {
            state = new Borrowed();
        } else {
            throw new IllegalStateException();
        }
    }

    private void setState(State state) {
        this.state = state;
    }

    /**
     * Locally adds a request for this book from a given user. Does not affect the database in any way.
     * Use {@link ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController#requestBook(String, String)}
     * for making book requests.
     *
     * @param userId user id
     */
    public void addRequest(@NonNull String userId) {
        if (state == null) {
            throw new IllegalStateException();
        }
        state.addRequest(userId);
    }

    /**
     * Locally cancels a request for this book from a given user. Does not affect the database in any way.
     * Use {@link ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController#cancelRequest(String, String)}
     * to cancel a request or {@link ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController#declineRequest(String, String)}
     * to decline a request.
     *
     * @param userId user id
     */
    public void cancelRequest(@NonNull String userId) {
        if (state == null) {
            throw new IllegalStateException();
        }
        state.cancelRequest(userId);
    }

    /**
     * Locally accepts a request for this book from a given user. Does not affect the database in any way.
     * Use {@link ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController#acceptRequest(String, String)} to accept a request.
     *
     * @param userId user id
     */
    public void acceptRequest(@NonNull String userId) {
        if (state == null) {
            throw new IllegalStateException();
        }
        state.acceptRequest(userId);
    }

    /**
     * Locally sets the status of this book to borrowed. Does not affect the database in any way.
     * Use {@link ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController#exchangeBook(String, String)} to exchange books.
     *
     * @param borrowerScanned Must be true for this operation to succeed.
     */
    public void exchange(@NonNull boolean borrowerScanned) {
        if (state == null) {
            throw new IllegalStateException();
        }
        state.exchange(borrowerScanned);
    }

    /**
     * Locally sets the status of this book to available. Does not affect the database in any way.
     * Use {@link ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController#returnBook(String, String)} to return books.
     *
     * @param borrowerScanned Must be true for this operation to succeed
     */
    public void beReturned(@NonNull boolean borrowerScanned) {
        if (state == null) {
            throw new IllegalStateException();
        }
        state.beReturned(borrowerScanned);
    }


    /* States */


    private interface State {
        void addRequest(String userId);
        void cancelRequest(String userId);
        void acceptRequest(String userId);
        void exchange(boolean borrowerScanned);
        void beReturned(boolean borrowerScanned);
    }

    private class Available implements State {

        Available() {
            setStatus("available");
        }

        @Override
        public void addRequest(String userId) {
            requesting.put(userId, 1);
            setState(new Requested());
        }

        @Override
        public void cancelRequest(String userId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void acceptRequest(String userId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void exchange(boolean borrowerScanned) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void beReturned(boolean borrowerScanned) {
            throw new UnsupportedOperationException();
        }
    }

    private class Requested implements State {

        Requested() {
            setStatus("requested");
        }

        @Override
        public void addRequest(String userId) {
            if (requesting.containsKey(userId)) {
                throw new IllegalArgumentException("User " + userId + " has already requested this book" + getIsbn());
            }
            requesting.put(userId, 1);
        }

        @Override
        public void cancelRequest(String userId) {
            if (!requesting.containsKey(userId)) {
                throw new IllegalArgumentException("User " + userId + " does not have a request for this book" + getIsbn());
            }
            requesting.remove(userId);

            if (requesting.isEmpty()) {
                setState(new Available());
            }
        }

        @Override
        public void acceptRequest(String userId) {
            if (requesting.containsKey(userId)) {
                userBorrowing = userId;

                requesting.remove(userId);
                removedRequests.putAll(requesting);
                requesting.clear();

                setState(new Accepted());
            } else {
                throw new IllegalArgumentException("User " + userId + " is not currently requesting this book");
            }
        }

        @Override
        public void exchange(boolean borrowerScanned) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void beReturned(boolean borrowerScanned) {
            throw new UnsupportedOperationException();
        }
    }

    private class Accepted implements State {

        Accepted() {
            setStatus("accepted");
        }

        @Override
        public void addRequest(String userId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void cancelRequest(String userId) {
            if (userBorrowing.equals(userId)) {
                userBorrowing = null;
                setState(new Available());
            } else {
                throw new IllegalArgumentException("User " + userId + " is not currently requesting this book");
            }
        }

        @Override
        public void acceptRequest(String userId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void exchange(boolean borrowerScanned) {
            if (getScanned() && borrowerScanned) {
                setScanned(false);
                setState(new Borrowed());
            } else {
                throw new IllegalStateException("Both the owner and borrower need to scan");
            }
        }

        @Override
        public void beReturned(boolean borrowerScanned) {
            throw new UnsupportedOperationException();
        }
    }

    private class Borrowed implements State {

        Borrowed() {
            setStatus("borrowed");
        }

        @Override
        public void addRequest(String userId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void cancelRequest(String userId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void acceptRequest(String userId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void exchange(boolean borrowerScanned) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void beReturned(boolean borrowerScanned) {
            if (getScanned() && borrowerScanned) {
                setScanned(false);
                userBorrowing = null;
                setState(new Available());
            } else {
                throw new IllegalStateException("Both the owner and borrower need to scan");
            }
        }
    }
}
