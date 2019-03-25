package ca.ualberta.CMPUT3012019T02.alexandria.cache;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Map;
import java.util.Observable;

import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.BorrowedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.User;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;
import java9.util.Optional;

/**
 * Observable cache for current user
 */
public class ObservableUserCache extends Observable {

    private static ObservableUserCache instance;
    private User user;
    private UserController userController = UserController.getInstance();
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ObservableUserCache getInstance() {
        if (instance == null) {
            instance = new ObservableUserCache();
        }
        return instance;
    }

    private ObservableUserCache() {
        if (!userController.isAuthenticated()) {
            return;
        }

        database.child("users").child(userController.getMyId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                setChanged();
                notifyObservers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Observable", "Error with firebase listener");
            }
        });
    }

    /**
     * Gets borrowed book.
     *
     * @param isbn the isbn
     * @return the borrowed book
     */
    public Optional<BorrowedBook> getBorrowedBook(String isbn) {
        if (user == null || user.getBorrowedBooks() == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(user.getBorrowedBooks().get(isbn));
    }

    /**
     * Gets owned book.
     *
     * @param isbn the isbn
     * @return the owned book
     */
    public Optional<OwnedBook> getOwnedBook(String isbn) {
        if (user == null || user.getOwnedBooks() == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(user.getOwnedBooks().get(isbn));
    }


    /**
     * Gets borrowed books.
     *
     * @return the borrowed books
     */
    public Optional<Map<String, BorrowedBook>> getBorrowedBooks() {
        if (user == null || user.getBorrowedBooks() == null) {
            return Optional.empty();
        }
        return Optional.of(Collections.unmodifiableMap(user.getBorrowedBooks()));
    }

    /**
     * Gets owned books.
     *
     * @return the owned books
     */
    public Optional<Map<String, OwnedBook>> getOwnedBooks() {
        if (user == null || user.getOwnedBooks() == null) {
            return Optional.empty();
        }
        return Optional.of(Collections.unmodifiableMap(user.getOwnedBooks()));
    }

    /**
     * Gets profile.
     *
     * @return the profile
     */
    public Optional<UserProfile> getProfile() {
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user.getProfile());
    }

    /**
     * Gets chat room id.
     *
     * @param userId the user id
     * @return the chat room id
     */
    public Optional<String> getChatRoomId(String userId) {
        if (user == null || user.getChatRooms() == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(user.getChatRooms().get(userId));
    }

    /**
     * Invalidates cache
     */
    public static void invalidate() {
        instance = null;
    }

}
