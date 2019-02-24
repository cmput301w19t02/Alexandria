package ca.ualberta.CMPUT3012019T02.alexandria;

import android.graphics.Bitmap;

import org.junit.Test;

import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;

import static org.junit.Assert.assertEquals;

public class UserProfileTests {

    @Test
    public void setNameTest() {
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        String name = "Alexa Bliss";
        userProfile.setName(name);
        assertEquals(userProfile.getName(), name);
    }

    @Test
    public void setEmailTest() {
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        String email = "alexa@example.com";
        userProfile.setEmail(email);
        assertEquals(userProfile.getEmail(), email);
    }

    @Test
    public void setPhoneTest() {
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        String phone = "7809991122";
        userProfile.setPhone(phone);
        assertEquals(userProfile.getPhone(), phone);
    }

    @Test
    public void setPictureTest() {
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        Bitmap picture = Bitmap.createBitmap(32,32, Bitmap.Config.ARGB_8888);
        userProfile.setPicture(picture);
        assertEquals(userProfile.getPicture(), picture);
    }

    @Test
    public void constructorTest(){
        UserProfile userProfile = new UserProfile("John Smith","john@example.com","7801234567",null);
        assertEquals(userProfile.getName(), "John Smith");
        assertEquals(userProfile.getEmail(), "john@example.com");
        assertEquals(userProfile.getPhone(), "7801234567");
        assertEquals(userProfile.getPicture(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullNameConstructorTest() {
        new UserProfile(null, "john@example.com", "7801234567", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyNameConstructorTest() {
        new UserProfile("", "john@example.com", "7801234567", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullPhoneConstructorTest() {
        new UserProfile("John Smith", "john@example.com", null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyPhoneConstructorTest() {
        new UserProfile("John Smith", "john@example.com", "", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullEmailConstructorTest() {
        new UserProfile("John Smith", null, "7801234567", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyEmailConstructorTest() {
        new UserProfile("John Smith", "", "7801234567", null);
    }
}
