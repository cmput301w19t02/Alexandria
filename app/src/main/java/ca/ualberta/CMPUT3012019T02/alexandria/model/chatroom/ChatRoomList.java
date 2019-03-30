package ca.ualberta.CMPUT3012019T02.alexandria.model.chatroom;

import java.util.Map;

/**
 * model class for ChatRoomList which stores chat rooms that user is participating with other users
 */
public class ChatRoomList {

    private Map<String, ChatRoomItem> chatRoomItems;

    /**
     * No args constructor to maintain compatibility
     * with Firebase deserializer
     * DO NOT USE
     */
    @Deprecated
    public ChatRoomList(){}

    /**
     * ChatRoomList constructor on id, if id null throws error
     *
     * @throws IllegalArgumentException Id cannot be null
     * @param chatRoomItems Map of ChatRoomItems
     */
    public ChatRoomList(Map<String, ChatRoomItem> chatRoomItems) {
        if (chatRoomItems == null) {
            throw new IllegalArgumentException("chatRoomItems cannot be null or empty");
        }
        this.chatRoomItems = chatRoomItems;
    }

    /**
     * getter for the map of chat rooms in user object
     *
     * @return Map of chat room objects
     */
    public Map<String, ChatRoomItem> getChatRoomItems() {
        return chatRoomItems;
    }

    /**
     * setter for the map of chat rooms in user object
     * it cannot be null
     *
     * @param chatRoomItems
     */

    public void setChatRoomItems(Map<String, ChatRoomItem> chatRoomItems) {
        if (chatRoomItems == null) {
            throw new IllegalArgumentException("chatRoomItems cannot be null or empty");
        }
        this.chatRoomItems = chatRoomItems;
    }
}
