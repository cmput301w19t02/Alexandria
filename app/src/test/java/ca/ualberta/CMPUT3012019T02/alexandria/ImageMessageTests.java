package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import java.util.Date;
import android.graphics.Bitmap;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;

public class TextMessageTests {
    // positive constructor tests 
    @Test
    public void constructorTest() {
        Date date = new Date();
        Bitmap picture = Bitmap.createBitmap(32,32, Bitmap.Config.ARGB_8888);
        ImageMessage message = new ImageMessage(picture,"unread", date, "johndoe@email.com");
        Assert.assertEquals(picture, message.getImage());
        Assert.assertEquals("unread", message.getStatus());
        Assert.assertEquals(date, message.getDate());
        Assert.assertEquals("johndoe@email.com", message.getSender());
        Assert.assertEquals("text", message.getType());
    }
    // negative constructor tests
    @Test(expected = IllegalArgumentException.class)
    public void emptyStatusConstructorTest() {
        Bitmap picture = Bitmap.createBitmap(32,32, Bitmap.Config.ARGB_8888);
        new ImageMessage(picture, "", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySenderConstructorTest() {
        Bitmap picture = Bitmap.createBitmap(32,32, Bitmap.Config.ARGB_8888);
        new ImageMessage(picture, "unread", new Date(),"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullImageConstructorTest() {
        new ImageMessage(null, "unread", new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullStatusConstructorTest(){
        Bitmap picture = Bitmap.createBitmap(32,32, Bitmap.Config.ARGB_8888);
        new ImageMessage(picture, null, new Date(),"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullDateConstructorTest(){
        Bitmap picture = Bitmap.createBitmap(32,32, Bitmap.Config.ARGB_8888);
        new ImageMessage(picture, "unread", null,"johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSenderConstructorTest(){
        Bitmap picture = Bitmap.createBitmap(32,32, Bitmap.Config.ARGB_8888);
        new ImageMessage(picture, "unread", new Date(), null);
    }
    // positive setImage
    @Test
    public void setImageTest(){
        Bitmap picture = Bitmap.createBitmap(32,32, Bitmap.Config.ARGB_8888);
        ImageMessage message = new ImageMessage(picture, "unread", new Date(), "johndoe@email.com");
        Bitmap picture2 = Bitmap.createBitmap(34,34, Bitmap.Config.ARGB_8888);
        message.setImage(picture2);
        assertEquals(picture2, message.getImage());
    }
    // negative setImage
    @Test(expected = IllegalArgumentException.class)
    public void nullSetImageTest(){
        Bitmap picture = Bitmap.createBitmap(32,32, Bitmap.Config.ARGB_8888);
        ImageMessage message = new ImageMessage(picture, "unread", new Date(), "johndoe@email.com");
        message.setImage(null);
    }
}