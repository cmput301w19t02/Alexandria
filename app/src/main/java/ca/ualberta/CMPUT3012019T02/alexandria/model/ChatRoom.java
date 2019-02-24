package ca.ualberta.CMPUT3012019T02.alexandria.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.model.message.Message;

public class ChatRoom {

    private List<Message> messages;
    private String id;

    public ChatRoom(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }

        this.id = id;

        messages = new ArrayList<>();
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public void setMessages(List<Message> messages) {
        if (messages == null) {
            throw new IllegalArgumentException("Messages cannot be null");
        }

        this.messages = messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public void removeMessage(Message message) {
        this.messages.remove(message);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }

        this.id = id;
    }

}
