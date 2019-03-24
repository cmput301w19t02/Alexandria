package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.model.chatroom.ChatRoom;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.Message;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.TextMessage;

import static org.junit.Assert.assertEquals;

public class ChatRoomTests {

    @Test
    public void constructorTest() {
        ChatRoom chatRoom = new ChatRoom("6588a715-1651-4d44-94bc-ee0a40176a93");
        assertEquals("6588a715-1651-4d44-94bc-ee0a40176a93", chatRoom.getId());
    }

    @Test
    public void setIdTest() {
        ChatRoom chatRoom = new ChatRoom("6588a715-1651-4d44-94bc-ee0a40176a93");
        chatRoom.setId("1aa825b3-6f00-42cf-8a32-805a483329dc");
        assertEquals("1aa825b3-6f00-42cf-8a32-805a483329dc", chatRoom.getId());
    }

    @Test
    public void setMessagesTest() {
        ChatRoom chatRoom = new ChatRoom("6588a715-1651-4d44-94bc-ee0a40176a93");
        List<Message> messages = new ArrayList<>();
        messages.add(new TextMessage("Hello", "read", new Date().getTime(), "johnsmith"));
        chatRoom.setMessages(messages);
        assertEquals(messages, chatRoom.getMessages());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetMessagesTest() {
        ChatRoom chatRoom = new ChatRoom("6588a715-1651-4d44-94bc-ee0a40176a93");
        chatRoom.getMessages().add(new TextMessage("Hello", "read", new Date().getTime(), "johnsmith"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullIdConstructorTest() {
        new ChatRoom(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyIdConstructorTest() {
        new ChatRoom("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetIdTest() {
        ChatRoom chatRoom = new ChatRoom("6588a715-1651-4d44-94bc-ee0a40176a93");
        chatRoom.setId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetIdTest() {
        ChatRoom chatRoom = new ChatRoom("6588a715-1651-4d44-94bc-ee0a40176a93");
        chatRoom.setId("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetMessagesTest() {
        ChatRoom chatRoom = new ChatRoom("6588a715-1651-4d44-94bc-ee0a40176a93");
        chatRoom.setMessages(null);
    }
}
