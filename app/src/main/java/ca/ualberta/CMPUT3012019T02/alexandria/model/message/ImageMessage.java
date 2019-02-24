package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

import android.graphics.Bitmap;

import java.util.Date;

public class ImageMessage extends Message {

    public ImageMessage(Bitmap content, String status, Date date, String sender) {
        super("image", content, status, date, sender);
    }

    public String getImage() {
        return this.content;
    }

    public void setImage(String imageUrl) {
        this.content = imageUrl;
    }

}
