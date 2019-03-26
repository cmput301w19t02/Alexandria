package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Test;

import ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.Location;

import static org.junit.Assert.assertEquals;

public class LocationTests {
    @Test
    public void constructorTest(){
        Location location = new Location(5,4);
        assertEquals(5,location.getLatitude(),0);
        assertEquals(4,location.getLongitude(),0);
    }

    @Test
    public void setLatitudeTest(){
        Location location = new Location(0,0);
        location.setLatitude(12);
        assertEquals(12,location.getLatitude(),0);
    }

    @Test
    public void setLongitudeTest(){
        Location location = new Location(0,0);
        location.setLongitude(9);
        assertEquals(9,location.getLongitude(),0);
    }

}
