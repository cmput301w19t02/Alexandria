package ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.chatroom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.message.Message;

/**
 * model class for ChatRoom which stores message history
 */
public class  ChatRoom {

    private List<Message> messages;
    private String id;

    /**
     * ChatRoom constructor on id, if id null or empty throws error
     *
     * @throws IllegalArgumentException Id cannot be null or empty
     * @param id id
     */
    public ChatRoom(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }

        this.id = id;

        messages = new ArrayList<>();
    }

    /**
     * returns a list of messages
     *
     * @return List<Message> messages
     */
    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    /**
     * sets messages
     *
     * @param messages messages
     */
    public void setMessages(List<Message> messages) {
        if (messages == null) {
            throw new IllegalArgumentException("Messages cannot be null");
        }

        this.messages = messages;
    }

    /**
     * add message to the list
     *
     * @param message message
     */
    public void addMessage(Message message) {
        this.messages.add(message);
    }

    /**
     * removes a message from the list
     *
     * @param message message
     */
    public void removeMessage(Message message) {
        this.messages.remove(message);
    }

    /**
     * gets id of the chatRoom
     *
     * @return String id
     */
    public String getId() {
        return id;
    }

    /**
     * sets id of the chatRoom
     *
     * @param id idw
     */
    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }

        this.id = id;
    }

}
