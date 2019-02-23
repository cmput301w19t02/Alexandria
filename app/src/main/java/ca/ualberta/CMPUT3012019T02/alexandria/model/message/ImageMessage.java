package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

import android.graphics.Bitmap;

import java.util.Date;

public class ImageMessage extends Message {

    public ImageMessage(String content, String status, Date date, String sender) {
        super("Image", content, status, date, sender);
    }

    public Bitmap getImage() {
        // TODO Finish implementation
        throw new UnsupportedOperationException();
    }

    public void setImage(Bitmap image) {
        // TODO Finish implementation
        throw new UnsupportedOperationException();
    }

}
