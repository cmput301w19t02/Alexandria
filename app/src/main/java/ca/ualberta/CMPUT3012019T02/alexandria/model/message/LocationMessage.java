package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Location;

/**
 * The type Location message. NOT YET USED (11/03/2019).
 */
public class LocationMessage extends Message {

    /**
     * Instantiates a new Location message.
     *
     * @param location the location
     * @param status   the status
     * @param date     the date
     * @param sender   the sender
     */
    public LocationMessage(Location location, String status, String/*Date*/ date, String sender) {
        // TODO Finish implementation
        super("location", "temp", status, date, sender);
        this.setLocation(location);
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public Location getLocation() {
        // TODO Finish implementation
        throw new UnsupportedOperationException();
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(Location location) {
        // TODO Finish implementation
        throw new UnsupportedOperationException();
    }
}
