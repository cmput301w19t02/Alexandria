package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.model.message.Message;

import static org.junit.Assert.assertEquals;

public class MessageTests {

    //Needed since Message is abstract
    private class TestMessage extends Message{

        public TestMessage(String type, String content, String status, Date date, String sender) {
            super(type, content, status, date, sender);
        }
    }

    // positive constructor tests
    @Test
    public void constructorTest() {
        Date date = new Date();
        Message message = new TestMessage("text","TEST content","unread", date, "johndoe@email.com");
        assertEquals("text", message.getType());
        assertEquals("TEST content", message.getContent());
        assertEquals("unread", message.getStatus());
        assertEquals(date, message.getDate());
        assertEquals("johndoe@email.com", message.getSender());
    }
    // negative constructor tests
    @Test(expected = IllegalArgumentException.class)
    public void emptyTypeConstructorTest() {
        new TestMessage("", "TEST content", "unread", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyContentConstructorTest() {
        new TestMessage("text", "", "unread", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyStatusConstructorTest() {
        new TestMessage("text", "TEST content", "", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySenderConstructorTest() {
        new TestMessage("text", "TEST content", "unread", new Date(),"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullTypeConstructorTest() {
        new TestMessage(null, "TEST content", "unread", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullContentConstructorTest() {
        new TestMessage("text", null, "unread", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullStatusConstructorTest(){
        new TestMessage("text", "TEST content", null, new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullDateConstructorTest(){
        new TestMessage("text", "TEST content", "unread", null,"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSenderConstructorTest(){
        new TestMessage("text", "TEST content", "unread", new Date(), null);
    }
    // positive setType
    @Test
    public void setTypeTest(){
        Message message = new TestMessage("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setType("image");
        assertEquals("image", message.getType());
        message.setType("location");
        assertEquals("location", message.getType());
        message.setType("text");
        assertEquals("text", message.getType());
    }
    // negative setType
    @Test(expected = IllegalArgumentException.class)
    public void emptySetTypeTest(){
        Message message = new TestMessage("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setType("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetTypeTest(){
        Message message = new TestMessage("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void badSetTypeTest(){
        Message message = new TestMessage("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setType("ardvark");
    }
    // positive setContent
    @Test
    public void setContentTest(){
        Message message = new TestMessage("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setContent("Different content");
        assertEquals("Different content", message.getContent());
    }
    // negative setContent
    @Test(expected = IllegalArgumentException.class)
    public void emptySetContentTest(){
        Message message = new TestMessage("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setContent("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetContentTest(){
        Message message = new TestMessage("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setContent(null);
    }
    // positive setStatus
    @Test
    public void setStatusTest(){
        Message message = new TestMessage("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        assertEquals("unread", message.getStatus());
        message.setType("read");
        assertEquals("read", message.getStatus());
    }
    // negative setStatus
    @Test(expected = IllegalArgumentException.class)
    public void emptySetStatusTest(){
        Message message = new TestMessage("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setStatus("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetStatusTest(){
        Message message = new TestMessage("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setStatus(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void badSetStatusTest(){
        Message message = new TestMessage("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setStatus("ardvark");
    }
    // positive setDate
    @Test
    public void setDateTest() {
        Date date = new Date();
        Message message = new TestMessage("text","TEST content","unread", date, "johndoe@email.com");
        assertEquals(date, message.getDate());
    }
    // negative setDate
    @Test(expected = IllegalArgumentException.class)
    public void nullSetDateTest(){
        Message message = new TestMessage("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setDate(null);
    }
    // positive setSender
    @Test
    public void setSenderTest() {
        Message message = new TestMessage("text","TEST content","unread", new Date(), "johndoe@email.com");
        message.setSender("janesomeone@email.com")
        assertEquals("janesomeone@email.com", message.getSender());
    }
    // negative setSender
    @Test(expected = IllegalArgumentException.class)
    public void emptySetSenderTest(){
        Message message = new TestMessage("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setSender("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetSenderTest(){
        Message message = new TestMessage("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setSender(null);
    }
}
