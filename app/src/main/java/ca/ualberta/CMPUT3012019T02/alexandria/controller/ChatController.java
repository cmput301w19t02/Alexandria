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
     * @param id a string that is the id of the current user
     * @return a CompletableFuture with Map type of
     */
    public CompletableFuture<Map<String, Object>> getChatRoomList(String id){
        return getChatRoomsListPrivate(id);
    }

    private CompletableFuture<Map<String, Object>> getChatRoomsListPrivate(String id) {
        // TODO: Finish implementation
        // gets list of my chat rooms
        final CompletableFuture<Map<String, Object>> resultFuture = new CompletableFuture<>();

        database.getReference().child("users").child(id).child("chatRoomList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> chatRoomIds = new ArrayList<String>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    chatRoomIds.add(childSnapshot.getValue(String.class));
                }
                resultFuture.complete();
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
        return addChatRoomPrivate(senderId, recieverId);
    }

    private CompletableFuture<Void> addChatRoomPrivate(String senderId, String recieverId){
        final CompletableFuture<Void> resultFuture = new CompletableFuture<>(); // might not be necessary

        DatabaseReference senderListRef = database.getReference().child("users").child(senderId).child("chatRoomList");
        DatabaseReference recieverListRef = database.getReference().child("users").child(recieverId).child("chatRoomList");
        DatabaseReference chatroomsRef = database.getReference().child("chatrooms");


        // TODO: generate a unique identifier for chat room, following a specific schema/ordering
        String chatId1 = "chat" + senderId + "_" + recieverId;
        String chatId2 = "chat" + recieverId + "_" + senderId;
        CompletableFuture<String> checkSendChatId1 = checkChatRoomExists(senderListRef, chatId1);
        CompletableFuture<String> checkSendChatId2 = checkChatRoomExists(senderListRef, chatId2);
        CompletableFuture<String> checkReciChatId1 = checkChatRoomExists(recieverListRef, chatId1);
        CompletableFuture<String> checkReciChatId2 = checkChatRoomExists(recieverListRef, chatId2);
        CompletableFuture<String> checkChatRoomListId1 = checkChatRoomExists(chatroomsRef, chatId1);
        CompletableFuture<String> checkChatRoomListId2 = checkChatRoomExists(chatroomsRef, chatId2);
        boolean check1 = checkSendChatId1.equals("true");
        boolean check2 = checkSendChatId2.equals("true");
        boolean check3 = checkReciChatId1.equals("true");
        boolean check4 = checkReciChatId2.equals("true");
        boolean checkList1 = checkChatRoomListId1.equals("true");
        boolean checkList2 = checkChatRoomListId2.equals("true");
        if (check1 || check2 || check3 || check4 || checkList1 || checkList2) {
            // check if a chat room is shared between the two ids
            // add the chat room to the missing one
        } else {
            // if a chat room is in neither id lists
            // add chat room to both ids
            senderListRef.push().setValue(chatId1);
            recieverListRef.push().setValue(chatId1);
            chatroomsRef.push().setValue(chatId1);
        }
        return resultFuture;
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
                    boolean doesExists = false;
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        String key = childSnapshot.getKey();
                        String idFromList = childSnapshot.child(key).getValue(String.class);
                        if (chatRoomId.equals(idFromList)){
                            doesExists = true;
                        }
                    }
                    if (!doesExists){
                        resultFuture.complete("false");
                    } else {
                        resultFuture.complete("true");
                    }
                } else {
                    resultFuture.complete("false");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                resultFuture.completeExceptionally(databaseError.toException());
            }
        });
        return resultFuture;
    }
}
