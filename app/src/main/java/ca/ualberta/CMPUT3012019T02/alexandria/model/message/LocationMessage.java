package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Location;

public class LocationMessage extends Message {

    public LocationMessage(Location content, String status, Date date, String sender) {
        if (!(content instanceof Location.class)) {
            throw new IllegalArgumentException("location content needs to be a Location object");
        }

        super("location", content, status, date, sender);
    }

    public Location getLocation() {
        return this.content;
    }

    public void setLocation(Location location) {
        if (!(content instanceof Location.class)) {
            throw new IllegalArgumentException("location content needs to be a Location object");
        }
        this.content = location;
    }
}
