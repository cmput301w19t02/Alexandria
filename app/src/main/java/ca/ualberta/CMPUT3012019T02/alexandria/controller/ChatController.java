package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import ca.ualberta.CMPUT3012019T02.alexandria.model.ChatRoom;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.Message;

public class ChatController {

    private static ChatController instance;

    private ChatController() { }

    public static ChatController getInstance() {
        if (instance == null) {
            instance = new ChatController();
        }
        return instance;
    }

    public ChatRoom getChatRoom(String id) {
        // TODO: Finish implementation
        // gets all chat data from Firebase
        throw new UnsupportedOperationException();
    }

    public ChatRoom updateMessages() {
        // TODO: Finish implementation
        // gets the new messages data stored in Firebase
        throw new UnsupportedOperationException();
    }

    public void postMessage(Message message) {
        // TODO: Finish implementation
        // adds new messages to chatroom in Firebase
        throw new UnsupportedOperationException();
    }

}
