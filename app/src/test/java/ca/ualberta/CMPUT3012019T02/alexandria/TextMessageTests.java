package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.message.TextMessage;

import static org.junit.Assert.assertEquals;

public class TextMessageTests {
    // positive constructor tests 
    @Test
    public void constructorTest() {
        Date date = new Date();
        TextMessage message = new TextMessage("TEST content", "unread", date.toString(), "johndoe@email.com");
        assertEquals("TEST content", message.getText());
        assertEquals("unread", message.getStatus());
        assertEquals(date.toString(), message.getDate());
        assertEquals("johndoe@email.com", message.getSender());
        assertEquals("text", message.getType());
    }

    // negative constructor tests
    @Test(expected = IllegalArgumentException.class)
    public void emptyTextConstructorTest() {
        new TextMessage("", "unread", new Date().toString(), "johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyStatusConstructorTest() {
        new TextMessage("TEST content", "", new Date().toString(), "johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySenderConstructorTest() {
        new TextMessage("TEST content", "unread", new Date().toString(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullTextConstructorTest() {
        new TextMessage(null, "unread", new Date().toString(), "johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullStatusConstructorTest() {
        new TextMessage("TEST content", null, new Date().toString(), "johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullDateConstructorTest() {
        new TextMessage("TEST content", "unread", null, "johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSenderConstructorTest() {
        new TextMessage("TEST content", "unread", new Date().toString(), null);
    }

    // positive setText
    @Test
    public void setTextTest() {
        TextMessage message = new TextMessage("TEST content", "unread", new Date().toString(), "johndoe@email.com");
        message.setContent("Different content");
        assertEquals("Different content", message.getText());
    }

    // negative setText
    @Test(expected = IllegalArgumentException.class)
    public void emptySetTextTest() {
        TextMessage message = new TextMessage("TEST content", "unread", new Date().toString(), "johndoe@email.com");
        message.setText("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetTextTest() {
        TextMessage message = new TextMessage("TEST content", "unread", new Date().toString(), "johndoe@email.com");
        message.setText(null);
    }
}
