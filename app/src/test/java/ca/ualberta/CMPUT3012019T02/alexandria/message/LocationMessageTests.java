package ca.ualberta.CMPUT3012019T02.alexandria.message;

import org.junit.Test;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Location;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.LocationMessage;

import static org.junit.Assert.assertEquals;

public class LocationMessageTests {
    // positive constructor tests 
    @Test
    public void constructorTest() {
        Date date = new Date();
        Location location = new Location(5,4);
        LocationMessage message = new LocationMessage(location, "unread", date.toString(), "johndoe@email.com");
        assertEquals(location.getLatitude(), message.getLocation().getLatitude(),0.01);
        assertEquals(location.getLongitude(), message.getLocation().getLongitude(),0.01);
        assertEquals("unread", message.getStatus());
        assertEquals(date.toString(), message.getDate());
        assertEquals("johndoe@email.com", message.getSender());
        assertEquals("location", message.getType());
    }

    // negative constructor tests
    @Test(expected = IllegalArgumentException.class)
    public void emptyStatusConstructorTest() {
        Location location = new Location(5,4); 
        new LocationMessage(location, "", new Date().toString(), "johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptySenderConstructorTest() {
        Location location = new Location(5,4); 
        new LocationMessage(location, "unread", new Date().toString(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullLocationConstructorTest() {
        new LocationMessage(null, "unread", new Date().toString(), "johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullStatusConstructorTest() {
        Location location = new Location(5,4); 
        new LocationMessage(location, null, new Date().toString(), "johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullDateConstructorTest() {
        Location location = new Location(5,4); 
        new LocationMessage(location, "unread", null, "johndoe@email.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSenderConstructorTest() {
        Location location = new Location(5,4); 
        new LocationMessage(location, "unread", new Date().toString(), null);
    }

    // positive setLocation
    @Test
    public void setLocationTest() {
        Location location = new Location(5,4); 
        LocationMessage message = new LocationMessage(location, "unread", new Date().toString(), "johndoe@email.com");
        Location location2 = new Location(6,3);
        message.setLocation(location2);
        assertEquals(location2.getLatitude(), message.getLocation().getLatitude(),0.01);
        assertEquals(location2.getLongitude(), message.getLocation().getLongitude(),0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSetLocationTest() {
        Location location = new Location(5,4); 
        LocationMessage message = new LocationMessage(location, "unread", new Date().toString(), "johndoe@email.com");
        message.setLocation(null);
    }
}
