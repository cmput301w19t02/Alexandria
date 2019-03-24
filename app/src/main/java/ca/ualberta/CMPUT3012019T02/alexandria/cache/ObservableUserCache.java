package ca.ualberta.CMPUT3012019T02.alexandria.cache;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.chatroom.ChatRoomItem;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.BorrowedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.User;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;

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
    public BorrowedBook getBorrowedBook(String isbn) {
        if (user != null && user.getBorrowedBooks() != null) {
            return user.getBorrowedBooks().get(isbn);
        }
        return null;
    }

    /**
     * Gets owned book.
     *
     * @param isbn the isbn
     * @return the owned book
     */
    public OwnedBook getOwnedBook(String isbn) {
        if (user != null && user.getOwnedBooks() != null) {
            return user.getOwnedBooks().get(isbn);
        }
        return null;
    }


    /**
     * Gets borrowed books.
     *
     * @return the borrowed books
     */
    public Map<String, BorrowedBook> getBorrowedBooks() {
        if (user != null && user.getBorrowedBooks() != null) {
            return Collections.unmodifiableMap(user.getBorrowedBooks());
        }
        return null;
    }

    /**
     * Gets owned books.
     *
     * @return the owned books
     */
    public Map<String, OwnedBook> getOwnedBooks() {
        if (user != null && user.getOwnedBooks() != null) {
            return Collections.unmodifiableMap(user.getOwnedBooks());
        }
        return null;
    }

    /**
     * Gets profile.
     *
     * @return the profile
     */
    public UserProfile getProfile() {
        if (user != null) {
            return user.getProfile();
        }
        return null;
    }

    /**
     * Gets blocked users.
     *
     * @return the blocked users
     */
    public List<String> getBlockedUsers() {
        if (user != null) {
            return user.getBlockedUsers();
        }
        return null;
    }

    /**
     * Gets chat room id.
     *
     * @param userId the user id
     * @return the chat room id
     */
    public String getChatRoomId(String userId) {
        if (user != null && user.getChatRooms() != null && user.getChatRooms().size() > 0) {
            if (user.getChatRooms().get(userId).getUser1Id().equals(userId) ||
                    user.getChatRooms().get(userId).getUser2Id().equals(userId)) {
                return user.getChatRooms().get(userId).getChatId();
            }
            return null;
        }
        return null;
    }

    /**
     * Invalidates cache
     */
    public static void invalidate() {
        instance = null;
    }

}
