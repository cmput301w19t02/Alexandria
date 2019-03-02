package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.google.firebase.storage.FirebaseStorage;

import java.util.UUID;

/**
 * This class manages the addition, retrieval, and modification of images in Firebase Storage
 */
public class ImageController {

    private FirebaseStorage storage;

    private static ImageController instance;

    private ImageController() { }

    public static ImageController getInstance() {
        if (instance == null) {
            instance = new ImageController();
        }
        return instance;
    }

    /**
     * Add an image to the database and get its unique id for later retrieval
     * @param image the image to add
     * @return the unique id of the image in the database
     */
    public String addImage(Bitmap image) {
        String imageId = UUID.randomUUID().toString();
        updateImage(imageId, image);
        return imageId;
    }

    /**
     * Get an image from the database
     * @param imageId the unique id of the image in the database
     * @return a bitmap of the image
     * @throws Resources.NotFoundException if the image was not found
     */
    public Bitmap getImage(String imageId) throws Resources.NotFoundException {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

    /**
     * Update an image in the database
     * @param imageId the unique id of the image in the database
     * @param image the new image to be associated with the imageId
     */
    public void updateImage(String imageId, Bitmap image) {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

    /**
     * Remove an image from the database
     * @param imageId the unique id of the image in the database
     */
    public void removeImage(String imageId) {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

}
