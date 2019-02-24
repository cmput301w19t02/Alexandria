package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Location;

public class LocationMessage extends Message {

    public LocationMessage(Location content, String status, Date date, String sender) {
        super("location", content, status, date, sender);
    }

    public Location getLocation() {
        // TODO Finish implementation
        throw new UnsupportedOperationException();
    }

    public void setLocation(Location location) {
        // TODO Finish implementation
        throw new UnsupportedOperationException();
    }
}
