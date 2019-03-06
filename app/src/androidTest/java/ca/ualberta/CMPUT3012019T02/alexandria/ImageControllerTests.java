package ca.ualberta.CMPUT3012019T02.alexandria;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;

public class ImageControllerTests {

    private final String TEST_IMAGE_1 = "ac543736-b355-4eac-b9b8-a11165f0aa43"; // exists in database
    private final String TEST_IMAGE_2 = "f66cde3a-d5cf-4c46-bbe7-cdb3d71f9571"; // exists in database
    private final String TEST_IMAGE_3 = "0c815f8b-6fb9-4721-b278-97cfdb3e9482"; // does not need to exist in database

    @Test
    public void testGetImage() throws ExecutionException, InterruptedException, IOException {

        ImageController imageController = ImageController.getInstance();

        // Gets TEST_IMAGE_1 from the database via URL
        Bitmap expected = BitmapFactory.decodeStream(
                new URL("https://firebasestorage.googleapis.com/v0/b/alexandria-a5aac.appspot.com/o/images%2Fac543736-b355-4eac-b9b8-a11165f0aa43.png?alt=media&token=02c5a1b0-ba82-4128-8665-85bb623d969b")
                        .openConnection().getInputStream());

        // Gets TEST_IMAGE_1 through the ImageController
        Bitmap actual = imageController.getImage(TEST_IMAGE_1).get();

        // Confirm they are the same
        Assert.assertTrue(expected.sameAs(actual));

    }

    @Test
    public void testAddImage() throws ExecutionException, InterruptedException {

        // Depends on ImageController#getImage() working correctly.

        ImageController imageController = ImageController.getInstance();

        // Create a new image and add it to the database
        Bitmap expected = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888);
        String imageId = imageController.addImage(expected).get();

        // Fetch the image from the database and verify it is the same
        Bitmap actual = imageController.getImage(imageId).get();
        Assert.assertTrue(expected.sameAs(actual));

        // Clean up
        imageController.deleteImage(imageId).get();

    }

    @Test
    public void testUpdateImage() throws ExecutionException, InterruptedException {

        // Depends on ImageController#getImage() working correctly.

        Bitmap expected = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888);
        ImageController imageController = ImageController.getInstance();

        // Update an image in the database
        imageController.updateImage(TEST_IMAGE_3, expected).get();

        // Fetch the image from the database and verify it is the same
        Bitmap actual = imageController.getImage(TEST_IMAGE_3).get();
        Assert.assertTrue(expected.sameAs(actual));

        // Clean up
        imageController.deleteImage(TEST_IMAGE_3).get();

    }

    @Test(expected = ExecutionException.class)
    public void testDeleteImage() throws ExecutionException, InterruptedException {

        // Depends ImageController#addImage() and ImageController#getImage() working correctly

        Bitmap bitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888);
        ImageController imageController = ImageController.getInstance();

        // Add an image to the database
        String imageId = imageController.addImage(bitmap).get();

        // Delete it
        imageController.deleteImage(imageId).get();

        // Check if it exists
        imageController.getImage(imageId).get();

    }
}
