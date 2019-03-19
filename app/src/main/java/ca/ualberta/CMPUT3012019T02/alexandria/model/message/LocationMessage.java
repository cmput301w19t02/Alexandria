package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Location;

/**
 * The type Location message. NOT YET USED (11/03/2019).
 */
public class LocationMessage extends Message {

    private String location;

    /**
     * Instantiates a new Location message.
     *
     * @param location the location
     * @param status   the status
     * @param date     the date
     * @param sender   the sender
     */
    public LocationMessage(String location, String status, String/*Date*/ date, String sender) {
        super("location", location, status, date, sender);
        this.setLocation(location);
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(String location) {
        if (location == null) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        this.location = location;
    }
}
