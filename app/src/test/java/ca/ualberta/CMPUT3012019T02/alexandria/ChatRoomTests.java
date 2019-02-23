package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.model.ChatRoom;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.TextMessage;

public class ChatRoomTests {
    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiableGetMessagesTest(){
        ChatRoom chatRoom = new ChatRoom("6588a715-1651-4d44-94bc-ee0a40176a93");
        chatRoom.getMessages().add(new TextMessage("Hello","Read",new Date(),"johnsmith"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullIdConstructorTest(){
        new ChatRoom(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyIdConstructorTest(){
        new ChatRoom("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetIsbnTest(){
        ChatRoom chatRoom = new ChatRoom("6588a715-1651-4d44-94bc-ee0a40176a93");
        chatRoom.setId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySetIsbnTest(){
        ChatRoom chatRoom = new ChatRoom("6588a715-1651-4d44-94bc-ee0a40176a93");
        chatRoom.setId("");
    }
}
