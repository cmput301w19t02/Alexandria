package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import ca.ualberta.CMPUT3012019T02.alexandria.model.chatroom.ChatRoomItem;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChatRoomItemTests {

    // positive constructor tests
    @Test
    public void constructorTest() {
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff" , "temp", "lkjh", "Leff", "temp", true );
        assertEquals("Testasdf1234", chatRoomItem.getChatId());
        assertEquals("asdf", chatRoomItem.getUser1Id());
        assertEquals("Heff", chatRoomItem.getUser1Name());
        assertEquals("temp", chatRoomItem.getUser1UserPic());
        assertEquals("lkjh", chatRoomItem.getUser2Id());
        assertEquals("Leff", chatRoomItem.getUser2Name());
        assertEquals("temp", chatRoomItem.getUser2UserPic());
        assertTrue(chatRoomItem.getReadStatus());
    }

    // negative constructor tests
    @Test(expected = IllegalArgumentException.class)
    public void emptyChatIdConstructorTest(){
        new ChatRoomItem("", "asdf","Heff", "temp", "lkjh", "Leff", "temp", true );
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyUser1IdConstructorTest(){
        new ChatRoomItem("Testasdf1234", "","Heff", "temp", "lkjh", "Leff", "temp",true );
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyUser1NameConstructorTest(){
        new ChatRoomItem("Testasdf1234", "asdf","","temp", "lkjh", "Leff", "temp",true );
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyUser2IdConstructorTest(){
        new ChatRoomItem("Testasdf1234", "asdf","Heff", "temp", "", "Leff", "temp",  true );
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyUser2NameConstructorTest(){
        new ChatRoomItem("Testasdf1234", "asdf","Heff",  "temp","lkjh", "",  "temp",true );
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyUser1UserPicConstructorTest(){
        new ChatRoomItem("Testasdf1234", "asdf","Heff",  "","lkjh", "Leff",  "temp",true );
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyUser2UserPicConstructorTest(){
        new ChatRoomItem("Testasdf1234", "asdf","Heff",  "temp","lkjh", "Leff",  "temp",true );
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullChatIdConstructorTest(){
        new ChatRoomItem(null, "asdf","Heff", "temp","lkjh", "Leff","temp", true );
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUser1IdConstructorTest(){
        new ChatRoomItem("Testasdf1234", null,"Heff","temp", "lkjh", "Leff","temp", true );
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUser1NameConstructorTest(){
        new ChatRoomItem("Testasdf1234", "asdf", null,"temp", "lkjh", "Leff","temp", true );
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUser2IdConstructorTest(){
        new ChatRoomItem("Testasdf1234", "asdf","Heff","temp", null, "Leff", "temp",true );
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUser2NameConstructorTest(){
        new ChatRoomItem("Testasdf1234", "asdf","Heff","temp", "lkjh", null, "temp",true );
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUser1UserPicConstructorTest(){
        new ChatRoomItem("Testasdf1234", "asdf","Heff",  null,"lkjh", "Leff",  "temp",true );
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUser2UserPicConstructorTest(){
        new ChatRoomItem("Testasdf1234", "asdf","Heff",  null,"lkjh", "Leff",  null,true );
    }

    // positive setChatId
    @Test
    public void setChatIdTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff","temp", "lkjh", "Leff","temp", true );
        chatRoomItem.setChatId("asdf;lkj");
        assertEquals("asdf;lkj",chatRoomItem.getChatId());
    }

    // negative setChatId
    @Test(expected = IllegalArgumentException.class)
    public void emptyChatIdTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff","temp", "lkjh", "Leff","temp", true );
        chatRoomItem.setChatId("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullChatIdTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff","temp", "lkjh", "Leff", "temp",true );
        chatRoomItem.setChatId(null);
    }

    //positive setUserId1Test
    @Test
    public void setUser1IdTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                , "temp","lkjh", "Leff", "temp",true );
        chatRoomItem.setUser1Id("asdf;lkj");
        assertEquals("asdf;lkj",chatRoomItem.getUser1Id());
    }

    //negative setUserId1Test
    @Test(expected = IllegalArgumentException.class)
    public void emptyUser1IdTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                ,"temp", "lkjh", "Leff", "temp",true );
        chatRoomItem.setUser1Id("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUser1IdTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                ,"temp", "lkjh", "Leff","temp", true );
        chatRoomItem.setUser1Id(null);
    }

    //positive setUser1Name
    @Test
    public void setUser1NameTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                ,"temp", "lkjh", "Leff","temp", true );
        chatRoomItem.setUser1Id("asdf;lkj");
        assertEquals("asdf;lkj",chatRoomItem.getUser1Id());
    }

    //negative setUser1Name
    @Test(expected = IllegalArgumentException.class)
    public void emptyUser1NameTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                ,"temp", "lkjh", "Leff","temp", true );
        chatRoomItem.setUser1Name("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUser1NameTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                ,"temp", "lkjh", "Leff", "temp",true );
        chatRoomItem.setUser1Name(null);
    }

    //positive setUser2Id
    public void setUser2IdTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                ,"temp", "lkjh", "Leff","temp", true );
        chatRoomItem.setUser2Id("asdf;lkj");
        assertEquals("asdf;lkj",chatRoomItem.getUser2Id());
    }

    //negative setUser2Id
    @Test(expected = IllegalArgumentException.class)
    public void emptyUser2IdTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                ,"temp", "lkjh", "Leff", "temp",true );
        chatRoomItem.setUser2Id("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUser2IdTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                , "temp","lkjh", "Leff","temp", true );
        chatRoomItem.setUser2Id(null);
    }

    //positive setUser2Name
    public void setUser2NameTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                , "temp","lkjh", "Leff","temp", true );
        chatRoomItem.setUser2Name("asdf;lkj");
        assertEquals("asdf;lkj",chatRoomItem.getUser2Name());
    }

    //negative setUser2Name
    @Test(expected = IllegalArgumentException.class)
    public void emptyUser2NameTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                ,"temp", "lkjh", "Leff","temp", true );
        chatRoomItem.setUser2Name("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUser2NameTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                ,"temp", "lkjh", "Leff","temp", true );
        chatRoomItem.setUser2Name(null);
    }

    //positive setUser1UserPicTest
    @Test
    public void setUser1UserPicTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                , "temp","lkjh", "Leff", "temp",true );
        chatRoomItem.setUser1UserPic("asdf;lkj");
        assertEquals("asdf;lkj",chatRoomItem.getUser1UserPic());
    }

    //negative setUserId1Test
    @Test(expected = IllegalArgumentException.class)
    public void emptyUser1PicTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                ,"temp", "lkjh", "Leff", "temp",true );
        chatRoomItem.setUser1UserPic("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUser1PicTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                ,"temp", "lkjh", "Leff","temp", true );
        chatRoomItem.setUser1UserPic(null);
    }

    //positive setUser1UserPicTest
    @Test
    public void setUser2UserPicTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                , "temp","lkjh", "Leff", "temp",true );
        chatRoomItem.setUser2UserPic("asdf;lkj");
        assertEquals("asdf;lkj",chatRoomItem.getUser1UserPic());
    }

    //negative setUserId1Test
    @Test(expected = IllegalArgumentException.class)
    public void emptyUser2PicTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                ,"temp", "lkjh", "Leff", "temp",true );
        chatRoomItem.setUser2UserPic("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUser2PicTest(){
        ChatRoomItem chatRoomItem = new ChatRoomItem("Testasdf1234", "asdf","Heff"
                ,"temp", "lkjh", "Leff","temp", true );
        chatRoomItem.setUser2UserPic(null);
    }
}
