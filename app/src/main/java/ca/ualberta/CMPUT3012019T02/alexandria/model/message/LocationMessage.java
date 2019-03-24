package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

import java.util.concurrent.ExecutionException;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Location;

/**
 * The type Location message. NOT YET USED (11/03/2019).
 */
public class LocationMessage extends Message {

    private String imageId;
    private Location location;
    /**
     * Instantiates a new Location message.
     *
     * @param location the location
     * @param status   the status
     * @param date     the date
     * @param sender   the sender
     */
    public LocationMessage(Location location, String status, Long date, String sender, String imageId) {
        super("location", "temp", status, date, sender);
        this.setLocation(location);
        this.setImageId(imageId);
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(Location location) {
        if(location!=null) {
            this.location = location;
        }
        else{
            throw new IllegalArgumentException("Location cannot be null.");
        }
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        if (imageId == null || imageId.isEmpty()) {
            throw new IllegalArgumentException("Image id cannot be null or empty");
        }
        this.imageId = imageId;
    }
}
