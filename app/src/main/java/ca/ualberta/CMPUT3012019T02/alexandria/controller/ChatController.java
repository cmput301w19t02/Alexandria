package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import ca.ualberta.CMPUT3012019T02.alexandria.cache.ObservableUserCache;
import ca.ualberta.CMPUT3012019T02.alexandria.model.chatroom.ChatRoomItem;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.LocationMessage;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.TextMessage;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;
import java9.util.concurrent.CompletableFuture;

/**
 * The controller for chat functionality
 */
public class ChatController {

    private static ChatController instance;
    private FirebaseDatabase database;
    private ObservableUserCache userCache;


    private ChatController() {
        database = FirebaseDatabase.getInstance();
        userCache = ObservableUserCache.getInstance();
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
    public CompletableFuture<Void> addChatRoom(String senderId, String recieverId, String recieverName) {
        return addChatRoomPrivate(senderId, recieverId, recieverName);
    }

    private CompletableFuture<Void> addChatRoomPrivate(String senderId, String recieverId, String recieverName) {

        String chatRoomExists = userCache.getChatRoomId(recieverId);

        // chatRoomExists will be null string if there is NOT a chat room between the two
        if (chatRoomExists == null) {

            String senderName = userCache.getProfile().getName();
            // TODO: implement ExecutionException and InterruptedException catches
            // for each lambda function add try/catch  Exception e
            CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() ->
                    database.getReference().child("users")
                            .child(senderId).child("chatRoomList").push().getKey());
            CompletableFuture<ChatRoomItem> future2 = future1.thenApply(chatId -> {
                ChatRoomItem chatRoom = new ChatRoomItem(chatId, senderId, senderName, recieverId, recieverName, false);
                addChatRoomItemToId(senderId, chatId, chatRoom);
                return chatRoom;
            });
            CompletableFuture<Void> future3 = future2.thenAccept(chatRoom ->
                addChatRoomItemToId(recieverId, chatRoom.getChatId(), chatRoom)
            );
            return future3;
        } else {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.complete(null);
            return future;
        }
    }

    private CompletableFuture<Void> addChatRoomItemToId(String userId, String chatId, ChatRoomItem chatRoomItem) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                getUserChatRoomListReference(userId).child(chatId).setValue(chatRoomItem)
                        .addOnSuccessListener(future::complete)
                        .addOnFailureListener(future::completeExceptionally);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    /* Messaging */

    public void addTextMessage(String chatId, TextMessage textMessage){
        addTextMessagePrivate(chatId, textMessage);
    }

    private void addTextMessagePrivate(String chatId, TextMessage textMessage) {
        DatabaseReference chatMessagesReference = getChatMessagesReference(chatId);
        chatMessagesReference.push().setValue(textMessage);
    }

    public void addLocationMessage(String chatId, LocationMessage locationMessage){
        addLocationMessagePrivate(chatId, locationMessage);
    }

    private void addLocationMessagePrivate(String chatId, LocationMessage locationMessage) {
        DatabaseReference chatMessagesReference = getChatMessagesReference(chatId);
        chatMessagesReference.push().setValue(locationMessage);
    }

    /* Database Reference Methods */
    private DatabaseReference getUserChatRoomListReference(String userId) {
        return database.getReference().child("users").child(userId).child("chatRoomList");
    }

    private DatabaseReference getChatMessagesReference(String chatId) {
        return database.getReference().child("chatMessages").child(chatId);
    }
}
