package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
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

    /**
     * Gets a chat room ids from database as a list of strings.
     * @param id
     * @return List of id strings
     */
    public List<String> getChatRoomList(String id){
        return getChatRoomsListPrivate(id);
    }
    private CompletableFuture<List<String>> getChatRoomsListPrivate(String id) {
        // TODO: Finish implementation
        // gets list of my chat rooms
        final CompletableFuture<User> resultFuture = new CompletableFuture<>();

        database.getReference().child("users").child(id).child("chatRoomList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> chatRoomIds = new ArrayList<String>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    chatRoomIds.add(childSnapshot.getValue(String.class));
                }
                resultFuture.complete(chatRoomIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                resultFuture.completeExceptionally(databaseError.toException());
            }
        });

        return resultFuture;
    }

    /**
     * Adds new chat room to both me and the person I want to message. Also checks if there is
     * already a chat between the two.
     * @param senderId my user id
     * @param recieverId the person I want to message's id
     */
    public CompletableFuture<Void> addChatRoom(String senderId, String recieverId){
        final CompletableFuture<User> resultFuture = new CompletableFuture<>();
        addChatRoomPrivate(senderId, recieverId);
        return resultFuture;
    }

    private CompletableFuture<Void> addChatRoomPrivate(String senderId, String recieverId){
        final CompletableFuture<User> resultFuture = new CompletableFuture<>();
        DatabaseReference senderRef = database.getReference().child("users").child(senderId).child("chatRoomList");
        DatabaseReference recieverRef = database.getReference().child("users").child(recieverId).child("chatRoomList");


        // TODO: generate a unique identifier for chat room, following a specific schema
        String chatId1 = "chat" + senderId + "_" + recieverId;
        String chatId2 = "chat" + recieverId + "_" + senderId;
        boolean checkChatId1 = checkChatRoomExists(senderRef, recieverRef, chatId1);
        boolean checkChatId2 = checkChatRoomExists(senderRef, recieverRef, chatId2);
        //check if a chat room is shared between the two ids
        if (checkChatId1 || checkChatId2) {

        } else {

        }

        // add chat room to both ids

    }

    private boolean checkChatRoomExists(DatabaseReference ref1, DatabaseReference ref2, String chatRoomId){
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /* My User ID */

    private String getMyUserId() {
        return UserController.getInstance().getMyId();
    }
}
