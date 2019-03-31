package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.graphics.Bitmap;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.ualberta.CMPUT3012019T02.alexandria.cache.ObservableUserCache;
import ca.ualberta.CMPUT3012019T02.alexandria.model.chatroom.ChatRoomItem;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.LocationMessage;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.TextMessage;
import java9.util.concurrent.CompletableFuture;

/**
 * The controller for chat functionality
 */
public class ChatController {

    private static ChatController instance;

    private UserController userController = UserController.getInstance();
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
     * @param receiverId the person I want to message's id
     * @return the completable future
     */
    public CompletableFuture<String> addChatRoom(String senderId, String receiverId,
                                               String receiverName) {
        return addChatRoomPrivate(senderId, receiverId, receiverName);
    }

    private CompletableFuture<String> addChatRoomPrivate(String senderId, String receiverId,
                                                       String receiverName) {
        CompletableFuture<String> future = new CompletableFuture<>();
        String chatRoomExists = userCache.getChatRoomId(receiverId).orElse(null);

        if (chatRoomExists == null) {
            String senderName = userCache.getProfile().get().getName();
            String chatId = getNewChatRoomId(senderId);
            String senderPicId = userCache.getProfile().get().getPicture();
            ChatRoomItem chatRoomItem = new ChatRoomItem(chatId, senderId, senderName, senderPicId, receiverId, receiverName, "temp", false);

            userController.getUserProfile(receiverId).handleAsync((result, error) -> {
                if (error == null) {
                    chatRoomItem.setUser2UserPic(result.getPicture());
                } else {
                    //handle error
                }
                return null;
            });

            addChatRoomItemToId(senderId, chatId, chatRoomItem).thenCombine(addChatRoomItemToId(receiverId, chatId, chatRoomItem), (result, error) -> {
                future.complete(chatId);
                return null;
            });
            return future;
        } else {
            future.complete(null);
        }
        return future;
    }

    private String getNewChatRoomId(String userId) {
        return getUserChatRoomListReference(userId).push().getKey();
    }

    private CompletableFuture<Void> addChatRoomItemToId(String userId, String chatId,
                                                        ChatRoomItem chatRoomItem) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        getUserChatRoomListReference(userId).child(chatId).setValue(chatRoomItem)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    public void setUserChatRoomReadStatus(String chatId, String otherUserId, boolean status) {
        setChatRoomReadStatusPrivate(chatId, otherUserId, status);
    }

    private void setChatRoomReadStatusPrivate(String chatId, String otherUserId, boolean status) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        getUserChatRoomListReference(otherUserId).child(chatId).child("readStatus").setValue(status)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);
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
