package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.model.chatroom.ChatRoomList;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.Message;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.TextMessage;

import static org.junit.Assert.assertEquals;

public class ChatRoomListTests {

    @Test
    public void constructorTest() {
        ChatRoomList chatRoomList = new ChatRoomList("6588a715-1651-4d44-94bc-ee0a40176a93");
        assertEquals("6588a715-1651-4d44-94bc-ee0a40176a93", chatRoomList.getId());
    }

    @Test
    public void setIdTest() {
        ChatRoomList chatRoomList = new ChatRoomList("6588a715-1651-4d44-94bc-ee0a40176a93");
        chatRoomList.setId("1aa825b3-6f00-42cf-8a32-805a483329dc");
        assertEquals("1aa825b3-6f00-42cf-8a32-805a483329dc", chatRoomList.getId());
    }

    @Test
    public void setMessagesTest() {
        ChatRoomList chatRoomList = new ChatRoomList("6588a715-1651-4d44-94bc-ee0a40176a93");
        List<Message> messages = new ArrayList<>();
        messages.add(new TextMessage("Hello", "read", new Date().getTime(), "johnsmith"));
        chatRoomList.setMessages(messages);
        assertEquals(messages, chatRoomList.getMessages());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetMessagesTest() {
        ChatRoomList chatRoomList = new ChatRoomList("6588a715-1651-4d44-94bc-ee0a40176a93");
        chatRoomList.getMessages().add(new TextMessage("Hello", "read", new Date().getTime(), "johnsmith"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullIdConstructorTest() {
        new ChatRoomList(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyIdConstructorTest() {
        new ChatRoomList("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetIdTest() {
        ChatRoomList chatRoomList = new ChatRoomList("6588a715-1651-4d44-94bc-ee0a40176a93");
        chatRoomList.setId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetIdTest() {
        ChatRoomList chatRoomList = new ChatRoomList("6588a715-1651-4d44-94bc-ee0a40176a93");
        chatRoomList.setId("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetMessagesTest() {
        ChatRoomList chatRoomList = new ChatRoomList("6588a715-1651-4d44-94bc-ee0a40176a93");
        chatRoomList.setMessages(null);
    }
}
