package ca.ualberta.CMPUT3012019T02.alexandria.model;

/**
 * location model class 
 */
public class Location {

    private double latitude;
    private double longitude;

    /**
     * Location constructor
     *
     * @param latitude latitude
     * @param longitude longtitude
     */
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * gets latitude
     * @return double latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * sets latitude
     *
     * @param latitude latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * gets longtitude
     *
     * @return double longtitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * sets longitude
     *
     * @param longitude longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
