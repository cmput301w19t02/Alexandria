package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

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
        super("location", "temp", status, date, sender);
        this.setLocation(location);
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public Location getLocation() {
        String[] tokens = getContent().split(",");
        double latitude =  Double.parseDouble(tokens[0]);
        double longitude =  Double.parseDouble(tokens[1]);
        return new Location(latitude,longitude);
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(Location location) {
        if(location!=null) {
            String serializedLocation = location.getLatitude() + "," + location.getLongitude();
            setContent(serializedLocation);
        }
        else{
            throw new IllegalArgumentException("Location cannot be null.");
        }
    }
}
