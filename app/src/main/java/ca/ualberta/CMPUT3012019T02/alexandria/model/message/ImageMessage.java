package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

import android.graphics.Bitmap;

import java.util.Date;

public class ImageMessage extends Message {

    public ImageMessage(Bitmap content, String status, Date date, String sender) {
        if (!(content instanceof Bitmap.class)) {
            throw new IllegalArgumentException("Content needs to be a Bitmap object");
        }

        super("image", content, status, date, sender);
    }

    public Bitmap getImage() {
        return this.content;
    }

    public void setImage(Bitmap image) {
        if (!(image instanceof Bitmap.class)) {
            throw new IllegalArgumentException("Content needs to be a Bitmap object");
        }

        this.content = image;
    }

}
