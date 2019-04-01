package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.model.message.ImageMessage;

import static org.junit.Assert.assertEquals;

public class ImageMessageTests {
    // positive constructor tests 
    @Test
    public void constructorTest() {
        Date date = new Date();
        ImageMessage message = new ImageMessage("16bb3894-be95-4108-9b9d-c6f5283a7920","unread", date.getTime(), "johndoe@email.com");
        assertEquals("16bb3894-be95-4108-9b9d-c6f5283a7920", message.getImage());
        assertEquals("unread", message.getStatus());
        assertEquals(Long.valueOf(date.getTime()), message.getDate());
        assertEquals("johndoe@email.com", message.getSender());
        assertEquals("image", message.getType());
    }
    // negative constructor tests
    @Test(expected = IllegalArgumentException.class)
    public void emptyStatusConstructorTest() {
        new ImageMessage("gs://image_url", "", new Date().getTime(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySenderConstructorTest() {
        new ImageMessage("gs://image_url", "unread", new Date().getTime(),"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullImageConstructorTest() {
        new ImageMessage(null, "unread", new Date().getTime(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullStatusConstructorTest(){
        new ImageMessage("gs://image_url", null, new Date().getTime(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullDateConstructorTest(){
        new ImageMessage("gs://image_url", "unread", null,"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSenderConstructorTest(){
        new ImageMessage("gs://image_url", "unread", new Date().getTime(), null);
    }
    // positive setImage
    @Test
    public void setImageTest(){
        ImageMessage message = new ImageMessage("16bb3894-be95-4108-9b9d-c6f5283a7920", "unread", new Date().getTime(), "johndoe@email.com");
        message.setImage("16bb3894-be95-4108-9b9d-c6f5283a7920");
        assertEquals("16bb3894-be95-4108-9b9d-c6f5283a7920", message.getImage());
    }
    // negative setImage
    @Test(expected = IllegalArgumentException.class)
    public void nullSetImageTest(){
        ImageMessage message = new ImageMessage("gs://image_url", "unread", new Date().getTime(), "johndoe@email.com");
        message.setImage(null);
    }
}