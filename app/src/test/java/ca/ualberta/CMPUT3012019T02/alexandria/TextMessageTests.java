package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;

public class TextMessageTests {
    // positive constructor tests 
    @Test
    public void constructorTest() {
        Date date = new Date();
        TextMessage message = new TextMessage("TEST content","unread", date, "johndoe@email.com");
        Assert.assertEquals("TEST content", message.getContent());
        Assert.assertEquals("unread", message.getStatus());
        Assert.assertEquals(date, message.getDate());
        Assert.assertEquals("johndoe@email.com", message.getSender());
        Assert.assertEquals("text", message.getType());
    }
    // negative constructor tests
    @Test(expected = IllegalArgumentException.class)
    public void emptyContentConstructorTest() {
        new TextMessage("", "unread", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyStatusConstructorTest() {
        new TextMessage("TEST content", "", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySenderConstructorTest() {
        new TextMessage("TEST content", "unread", new Date(),"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullContentConstructorTest() {
        new TextMessage(null, "unread", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullStatusConstructorTest(){
        new TextMessage("TEST content", null, new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullDateConstructorTest(){
        new TextMessage("TEST content", "unread", null,"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSenderConstructorTest(){
        new TextMessage("TEST content", "unread", new Date(), null);
    }
    // positive setText
    @Test
    public void setTextTest(){
        TextMessage message = new TextMessage("TEST content", "unread", new Date(), "johndoe@email.com");
        message.setContent("Different content");
        Assert.assertEquals("Different content", message.getText());
    }
    // negative setText
    @Test(expected = IllegalArgumentException.class)
    public void emptySetContentTest(){
        TextMessage message = new TextMessage("TEST content", "unread", new Date(), "johndoe@email.com");
        message.setText("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetContentTest(){
        TextMessage message = new TextMessage("TEST content", "unread", new Date(), "johndoe@email.com");
        message.setText(null);
    }
    // positive setStatus
    @Test
    public void setStatusTest(){
        TextMessage message = new TextMessage("TEST content", "unread", new Date(), "johndoe@email.com");
        Assert.assertEquals("unread", message.getStatus());
        message.setType("read");
        Assert.assertEquals("read", message.getStatus());
    }
    // negative setStatus
    @Test(expected = IllegalArgumentException.class)
    public void emptySetStatusTest(){
        TextMessage message = new TextMessage("TEST content", "unread", new Date(), "johndoe@email.com");
        message.setStatus("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetStatusTest(){
        TextMessage message = new TextMessage("TEST content", "unread", new Date(), "johndoe@email.com");
        message.setStatus(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void badSetStatusTest(){
        TextMessage message = new TextMessage("TEST content", "unread", new Date(), "johndoe@email.com");
        message.setStatus("ardvark");
    }
    // positive setDate
    @Test
    public void setDateTest() {
        Date date = new Date();
        TextMessage message = new TextMessage("TEST content","unread", date, "johndoe@email.com");
        Assert.assertEquals(date, message.getDate());
    }
    // negative setDate
    @Test(expected = IllegalArgumentException.class)
    public void nullSetDateTest(){
        TextMessage message = new TextMessage("TEST content", "unread", new Date(), "johndoe@email.com");
        message.setDate(null);
    }
    // positive setSender
    @Test
    public void setSenderTest() {
        TextMessage message = new TextMessage("TEST content","unread", new Date(), "johndoe@email.com");
        message.setSender("janesomeone@email.com");
        Assert.assertEquals("janesomeone@email.com", message.getSender());
    }
    // negative setSender
    @Test(expected = IllegalArgumentException.class)
    public void emptySetSenderTest(){
        TextMessage message = new TextMessage("TEST content", "unread", new Date(), "johndoe@email.com");
        message.setSender("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetSenderTest(){
        TextMessage message = new TextMessage("TEST content", "unread", new Date(), "johndoe@email.com");
        message.setSender(null);
    }   
}
