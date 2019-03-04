package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

import ca.ualberta.CMPUT3012019T02.alexandria.model.user.User;
import java9.util.concurrent.CompletableFuture;

import ca.ualberta.CMPUT3012019T02.alexandria.model.ChatRoom;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.Message;

public class ChatController {
    private FirebaseDatabase database;

    private static ChatController instance;

    private ChatController() {
        database = FirebaseDatabase.getInstance();
    }

    public static ChatController getInstance() {
        if (instance == null) {
            instance = new ChatController();
        }
        return instance;
    }

    // MessageFragment necessary methods

    private <T extends Map<String, String>> CompletableFuture<T> getChatRoomsList(String id) {
        // TODO: Finish implementation
        // gets all chat data from Firebase
        final CompletableFuture<Void> resultFuture = new CompletableFuture<>();

        database.getReference().child("users").child(id).child("chatrooms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Map<String, String> chatRooms = user.getChatRooms();
                resultFuture.complete(chatRooms);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                resultFuture.completeExceptionally(databaseError.toException());
            }
        });

        return resultFuture;
    }

    // ChatActivity necessary methods
    public CompletableFuture<Void> getMessages() {
        // TODO: Finish implementation
        // gets the new messages data stored in Firebase
        throw new UnsupportedOperationException();
    }

    public void postMessage(Message message) {
        // TODO: Finish implementation
        // adds new messages to chatroom in Firebase
        throw new UnsupportedOperationException();
    }

    /* My User ID */


    private String getMyUserId() {
        return UserController.getInstance().getMyId();
    }


    /* Database Referencing */

    // TODO: Change theses to chat references
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
