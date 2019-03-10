package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Location;

public class LocationMessage extends Message {

    public LocationMessage(Location location, String status, String/*Date*/ date, String sender) {
        // TODO Finish implementation
        super("location", "temp", status, date, sender);
        this.setLocation(location);
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
