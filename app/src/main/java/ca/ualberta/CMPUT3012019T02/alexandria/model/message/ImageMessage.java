package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

import java.util.Date;

/**
 * The type Image message. NOT YET USED (11/03/2019).
 */
public class ImageMessage extends Message {

    /**
     * Instantiates a new Image message.
     *
     * @param content the content
     * @param status  the status
     * @param date    the date
     * @param sender  the sender
     */
    public ImageMessage(String content, String status, String /*Date*/date, String sender) {
        super("image", content, status, date, sender);
    }

    /**
     * Gets image.
     *
     * @return the image
     */
    public String getImage() {
        return getContent();
    }

    /**
     * Sets image.
     *
     * @param imageId the image id
     */
    public void setImage(String imageId) {
        setContent(imageId);
    }

}
