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


        // TODO: generate a unique identifier for chat room, following a specific schema/ordering
        String chatId1 = "chat" + senderId + "_" + recieverId;
        String chatId2 = "chat" + recieverId + "_" + senderId;
        CompletableFuture<String> checkSendChatId1 = checkChatRoomExists(senderRef, chatId1);
        CompletableFuture<String> checkSendChatId2 = checkChatRoomExists(senderRef, chatId2);
        CompletableFuture<String> checkReciChatId1 = checkChatRoomExists(senderRef, chatId1);
        CompletableFuture<String> checkReciChatId2 = checkChatRoomExists(senderRef, chatId2);
        boolean check1 = checkSendChatId1.equals("true");
        boolean check2 = checkSendChatId2.equals("true");
        boolean check3 = checkReciChatId1.equals("true");
        boolean check4 = checkReciChatId2.equals("true");

        if (check1 || check2 || check3 || check4) {
            // check if a chat room is shared between the two ids
            // add the chat room to the missing one
        } else {
            // if a chat room is in neither id lists
            // add chat room to both ids

        }
        return resultFuture.complete();
    }

    /**
     * A check if a chat room exists at databaseReference.child(userId).child("chatRoomList")
     * @param ref
     * @param chatRoomId
     * @return boolean, true for the chat room exists in both database references, false if not
     */
    private CompletableFuture<String> checkChatRoomExists(DatabaseReference ref, String chatRoomId){
        final CompletableFuture<String> resultFuture = new CompletableFuture<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // check list
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        String key = childSnapshot.getKey();
                        String idFromList = childSnapshot.child(key).getValue(String.class);
                        if (chatRoomId.equals(idFromList)){
                            resultFuture.complete("true");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                resultFuture.completeExceptionally(databaseError.toException());
            }
        });
        String chatRoomExists = resultFuture.get();
        if (chatRoomExists.equals("true") ){
            return true;
        } else {
            return false;
        }
    }

    /* My User ID */

    private String getMyUserId() {
        return UserController.getInstance().getMyId();
    }
}
