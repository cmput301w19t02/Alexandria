package ca.ualberta.CMPUT3012019T02.alexandria.cache;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * BasicCache for images
 */
public class ImageCache extends BasicCache {

    private static ImageCache instance;
    private HashMap<String, Bitmap> cache = new HashMap<>();

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ImageCache getInstance() {
        if (instance == null) {
            instance = new ImageCache();
        }
        return instance;
    }

    private ImageCache() {
    }

    /**
     * Gets book from cache
     *
     * @param imageId of the image
     * @return the book
     */
    public Bitmap getImage(String imageId) {
        return cache.get(imageId);
    }

    /**
     * Put book into cache
     *
     * @param imageId of the image
     * @param image the image
     */
    public void putImage(String imageId, Bitmap image) {
        cache.put(imageId, image);
    }

    /**
     * Delete book from cache
     *
     * @param imageId of the image
     */
    public void deleteImage(String imageId) {
        cache.remove(imageId);
    }
}
