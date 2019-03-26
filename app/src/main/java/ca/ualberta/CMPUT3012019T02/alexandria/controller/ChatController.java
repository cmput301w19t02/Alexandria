package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.chatroom.ChatRoomItem;
import ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.user.UserProfile;
import java9.util.concurrent.CompletableFuture;

/**
 * The controller for chat functionality
 */
public class ChatController {
    private FirebaseDatabase database;

    private static ChatController instance;

    private ChatController() {
        database = FirebaseDatabase.getInstance();
    }

    /**
     * Gets the singleton instance of this controller
     * @return instance of ChatController
     */
    public static ChatController getInstance() {
        if (instance == null) {
            instance = new ChatController();
        }
        return instance;
    }

    /**
     * Adds new chat room to both me and the person I want to message. Also checks if there is
     * already a chat between the two.
     *
     * @param senderId   my user id
     * @param recieverId the person I want to message's id
     * @return the completable future
     */
    public CompletableFuture<Void> addChatRoom(String senderId, String recieverId){
        return addChatRoomPrivate(senderId, recieverId);
    }

    private CompletableFuture<Void> addChatRoomPrivate(String senderId, String recieverId){
        final CompletableFuture<Void> resultFuture = new CompletableFuture<>(); // might not be necessary

        DatabaseReference senderListRef = database.getReference().child("users").child(senderId).child("chatRoomList");
        DatabaseReference recieverListRef = database.getReference().child("users").child(recieverId).child("chatRoomList");
        DatabaseReference chatroomsRef = database.getReference().child("chatrooms");

        CompletableFuture<UserProfile> user1Future = UserController.getInstance().getUserProfile(senderId);
        CompletableFuture<UserProfile> user2Future = UserController.getInstance().getUserProfile(recieverId);

        //UserProfile user1 = user1Future.getNow();
        //UserProfile user2 = user2Future.getNow();

        String user1Name = "Joe";
        String user2Name = "Sandy";
        //String user1Name = user1.getUsername();
        //String user2Name = user2.getUsername();

        String chatId1 = "chat" + senderId + "_" + recieverId;
        ChatRoomItem chatRoomItem = new ChatRoomItem(chatId1, senderId, user1Name, recieverId, user2Name, false);

        senderListRef.child(chatId1).child("chatId").setValue(chatRoomItem.getChatId());
        senderListRef.child(chatId1).child("user1Id").setValue(chatRoomItem.getUser1Id());
        senderListRef.child(chatId1).child("user1Name").setValue(chatRoomItem.getUser1Name());
        senderListRef.child(chatId1).child("user2Id").setValue(chatRoomItem.getUser2Id());
        senderListRef.child(chatId1).child("user2Name").setValue(chatRoomItem.getUser2Name());
        senderListRef.child(chatId1).child("readStatus").setValue(chatRoomItem.getReadStatus());

        // TODO: generate a unique identifier for chat room, following a specific schema/ordering
        // String chatId2 = "chat" + recieverId + "_" + senderId;
        /** TODO: check if chat already exists between the two users
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
            chatroomsRef.child().setValue(chatId1);
        }
        */


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
