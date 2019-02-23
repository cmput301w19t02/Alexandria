package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;

public class MessageTests {
    //positive constructor tests 
    @Test
    public void constructorTest() {
        Date date = new Date();
        Message message = new Message("text","TEST content","unread", date, "johndoe@email.com");
        Assert.assertEquals("text", message.getType());
        Assert.assertEquals("TEST content", message.getContent());
        Assert.assertEquals("unread", message.getStatus());
        Assert.assertEquals(date, message.getDate());
        Assert.assertEquals("johndoe@email.com", message.getSender());
    }
    //negative constructor tests
    @Test(expected = IllegalArgumentException.class)
    public void emptyTypeConstructorTest() {
        new Message("", "TEST content", "unread", new Date(),"johndoe@email.com"); 
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyContentConstructorTest() {
        new Message("text", "", "unread", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyStatusConstructorTest() {
        new Message("text", "TEST content", "", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySenderConstructorTest() {
        new Message("text", "TEST content", "unread", new Date(),"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullTypeConstructorTest() {
        new Message(null, "TEST content", "unread", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullContentConstructorTest() {
        new Message("text", null, "unread", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullStatusConstructorTest(){
        new Message("text", "TEST content", null, new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullDateConstructorTest(){
        new Message("text", "TEST content", "unread", null,"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSenderConstructorTest(){
        new Message("text", "TEST content", "unread", new Date(), null);
    }
    // negative setType 
    @Test(expected = IllegalArgumentException.class)
    public void emptySetTypeTest(){
        Message message = new Message("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setType("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetTypeTest(){
        Message message = new Message("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void badSetTypeTest(){
        Message message = new Message("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setType("ardvark");
    }
    // negative setContent
    @Test(expected = IllegalArgumentException.class)
    public void emptySetContentTest(){
        Message message = new Message("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setContent("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetContentTest(){
        Message message = new Message("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setContent(null);
    }
    // negative setStatus
    @Test(expected = IllegalArgumentException.class)
    public void emptySetStatusTest(){
        Message message = new Message("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setStatus("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetStatusTest(){
        Message message = new Message("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setStatus(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void badSetStatusTest(){
        Message message = new Message("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setStatus("ardvark");
    }
    // negative setDate
    @Test(expected = IllegalArgumentException.class)
    public void nullSetDateTest(){
        Message message = new Message("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setDate(null);
    }
    // negative setSender
    @Test(expected = IllegalArgumentException.class)
    public void emptySetSenderTest(){
        Message message = new Message("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setSender("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetSenderTest(){
        Message message = new Message("text", "TEST content", "unread", new Date(), "johndoe@email.com");
        message.setSender(null);
    }   
}
