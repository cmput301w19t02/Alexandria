package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

import java.util.Date;

public class ImageMessage extends Message {

    public ImageMessage(String content, String status, Date date, String sender) {
        super("image", content, status, date, sender);
    }

    public String getImageUrl() {
        return getContent();
    }

    public void setImageUrl(String imageUrl) {
        setContent(imageUrl);
    }

}
