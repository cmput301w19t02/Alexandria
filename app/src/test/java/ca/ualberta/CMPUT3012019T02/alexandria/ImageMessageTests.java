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
        ImageMessage message = new ImageMessage("gs://image_url","unread", date, "johndoe@email.com");
        assertEquals("gs://image_url", message.getImageUrl());
        assertEquals("unread", message.getStatus());
        assertEquals(date, message.getDate());
        assertEquals("johndoe@email.com", message.getSender());
        assertEquals("image", message.getType());
    }
    // negative constructor tests
    @Test(expected = IllegalArgumentException.class)
    public void emptyStatusConstructorTest() {
        new ImageMessage("gs://image_url", "", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySenderConstructorTest() {
        new ImageMessage("gs://image_url", "unread", new Date(),"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullImageConstructorTest() {
        new ImageMessage(null, "unread", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullStatusConstructorTest(){
        new ImageMessage("gs://image_url", null, new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullDateConstructorTest(){
        new ImageMessage("gs://image_url", "unread", null,"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSenderConstructorTest(){
        new ImageMessage("gs://image_url", "unread", new Date(), null);
    }
    // positive setImage
    @Test
    public void setImageTest(){
        ImageMessage message = new ImageMessage("gs://image_url", "unread", new Date(), "johndoe@email.com");
        message.setImageUrl("gs://new_image_url");
        assertEquals("gs://new_image_url", message.getImageUrl());
    }
    // negative setImage
    @Test(expected = IllegalArgumentException.class)
    public void nullSetImageTest(){
        ImageMessage message = new ImageMessage("gs://image_url", "unread", new Date(), "johndoe@email.com");
        message.setImageUrl(null);
    }
}