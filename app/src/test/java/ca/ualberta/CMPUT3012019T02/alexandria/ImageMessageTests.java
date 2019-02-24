package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import java.util.Date;
import android.graphics.Bitmap;
import static org.junit.Assert.assertEquals;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;

public class ImageMessageTests {
    // positive constructor tests 
    @Test
    public void constructorTest() {
        Date date = new Date();
        ImageMessage message = new ImageMessage("image_url","unread", date, "johndoe@email.com");
        assertEquals("image_url", message.getImage());
        assertEquals("unread", message.getStatus());
        assertEquals(date, message.getDate());
        assertEquals("johndoe@email.com", message.getSender());
        assertEquals("text", message.getType());
    }
    // negative constructor tests
    @Test(expected = IllegalArgumentException.class)
    public void emptyStatusConstructorTest() {
        new ImageMessage("image_url", "", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySenderConstructorTest() {
        new ImageMessage("image_url", "unread", new Date(),"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullImageConstructorTest() {
        new ImageMessage(null, "unread", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullStatusConstructorTest(){
        new ImageMessage("image_url", null, new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullDateConstructorTest(){
        new ImageMessage("image_url", "unread", null,"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSenderConstructorTest(){
        new ImageMessage("image_url", "unread", new Date(), null);
    }
    // positive setImage
    @Test
    public void setImageTest(){
        //Bitmap picture = Bitmap.createBitmap(32,32, Bitmap.Config.ARGB_8888);
        ImageMessage message = new ImageMessage("image_url", "unread", new Date(), "johndoe@email.com");
        //Bitmap picture2 = Bitmap.createBitmap(34,34, Bitmap.Config.ARGB_8888);
        message.setImage("new_image_url");
        assertEquals("new_image_url", message.getImage());
    }
    // negative setImage
    @Test(expected = IllegalArgumentException.class)
    public void nullSetImageTest(){
        ImageMessage message = new ImageMessage("image_url", "unread", new Date(), "johndoe@email.com");
        message.setImage(null);
    }
}