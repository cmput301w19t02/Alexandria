package ca.ualberta.CMPUT3012019T02.alexandria;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.StorageReference;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;

/**
 * This class tests the ImageController
 *
 * NOTE: CURRENTLY NOT WORKING
 */
public class ImageControllerTests {

    @Test
    public void testAddImage() {

        final ImageController imageController = ImageController.getInstance();
        imageController.addImage(Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888),
                new ImageController.ImageUploadCallback() {

            @Override
            public void onFailure(@NonNull Exception exception) {
                Assert.fail("Image upload failed");
                exception.printStackTrace();
            }

            @Override
            public void onSuccess(String imageId) {
                // Check if the file exists by getting the download url
                StorageReference reference = imageController.getImageReference(imageId);
                reference.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Assert.fail("Image retrieval failed");
                        e.printStackTrace();
                    }
                });

            }

        });

    }

    @Test
    public void testGetImage() {
        // TODO: Have an actual image in Firebase Storage at images/16bb3894-be95-4108-9b9d-c6f5283a7920.png
        String imageId = "16bb3894-be95-4108-9b9d-c6f5283a7920"; // exists in database already

        ImageController imageController = ImageController.getInstance();
        imageController.getImage(imageId, new ImageController.ImageDownloadCallback() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Assert.fail("Image download failed");
                exception.printStackTrace();
            }

            @Override
            public void onSuccess(Bitmap image) {
                // Successful retrieval
                // TODO: Have a local copy of the image to compare it to
                URL res = getClass().getClassLoader().getResource("test/test_image.png");
                File file = null;
                try {
                    file = Paths.get(res.toURI()).toFile();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                String absolutePath = file.getAbsolutePath();
                Bitmap bitmap = BitmapFactory.decodeFile(absolutePath);
                Assert.assertEquals(bitmap, image);
            }

        });
    }

    @Test
    public void testUpdateImage() {
        final String imageId = "bd954c35-7755-4a1b-84e3-9cbaff55c516";
        Bitmap bitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888);

        final ImageController imageController = ImageController.getInstance();
        imageController.updateImage(imageId, bitmap, new ImageController.ImageUploadCallback() {

            @Override
            public void onFailure(@NonNull Exception exception) {
                Assert.fail("Image upload failed");
                exception.printStackTrace();
            }

            @Override
            public void onSuccess(String iid) {
                Assert.assertEquals(imageId, iid);
            }

        });
    }

    @Test
    public void testDeleteImage() {
        // Assumes ImageController#updateImage(String, Bitmap, ImageController.ImageUploadCallback) works correctly

        final String imageId = "bd954c35-7755-4a1b-84e3-9cbaff55c516";
        Bitmap bitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888);

        final ImageController imageController = ImageController.getInstance();
        imageController.updateImage(imageId, bitmap, new ImageController.ImageUploadCallback() {

            @Override
            public void onFailure(@NonNull Exception exception) {
                Assert.fail("Image upload failed");
                exception.printStackTrace();
            }

            @Override
            public void onSuccess(String iid) {

                imageController.deleteImage(imageId, new ImageController.ImageDeletionCallback() {

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Assert.fail("Image deletion failed");
                        exception.printStackTrace();
                    }

                    @Override
                    public void onSuccess() {
                        // Success!
                    }

                });

            }

        });
    }

}
