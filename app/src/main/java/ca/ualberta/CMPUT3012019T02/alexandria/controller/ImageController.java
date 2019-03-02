package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class manages the addition, retrieval, and modification of images in Firebase Storage
 */
public class ImageController {

    /* Callbacks for various asynchronous actions */

    /**
     * This interface handles what is done after successful or failed Firebase Storage
     * image data upload. It is an asynchronous callback.
     */
    public interface ImageUploadCallback {

        /**
         * Handles unsuccessful upload of an image
         * @param exception the exception that caused the failure
         */
        void onFailure(@NonNull Exception exception);

        /**
         * Handles the image after successful upload
         * @param imageId the unique id of the image
         */
        void onSuccess(String imageId);

    }

    /**
     * This interface handles what is done after successful or failed Firebase Storage
     * image data download. It is an asynchronous callback.
     */
    public interface ImageDownloadCallback {

        /**
         * Handles unsuccessful download of an image
         * @param exception the exception that caused the failure
         */
        void onFailure(@NonNull Exception exception);

        /**
         * Handles the image after successful download
         * @param image the image as a bitmap
         */
        void onSuccess(Bitmap image);

    }

    /**
     * This interface handles what is done after successful or failed Firebase Storage
     * image data deletion. It is an asynchronous callback.
     */
    public interface ImageDeletionCallback {

        /**
         * Handles unsuccessful deletion of an image
         * @param exception the exception that caused the failure
         */
        void onFailure(@NonNull Exception exception);

        /**
         * Handles the successful deletion of an image
         */
        void onSuccess();

    }


    /* ImageController code */


    private final String IMAGE_FORMAT = "png";

    private StorageReference storage;
    private List<StorageTask> tasks;

    private static ImageController instance;

    private ImageController() {
        this.storage = FirebaseStorage.getInstance().getReference().child("images");
        tasks = new ArrayList<>();
    }

    public static ImageController getInstance() {
        if (instance == null) {
            instance = new ImageController();
        }
        return instance;
    }

    /**
     * Add an image to the database and get its unique id for later retrieval
     * @param image the image to add
     * @param imageUploadCallback
     */
    public void addImage(Bitmap image, ImageUploadCallback imageUploadCallback) {
        String imageId = UUID.randomUUID().toString();
        updateImage(imageId, image, imageUploadCallback);
    }

    /**
     * Get an image from the database
     * @param imageId the unique id of the image in the database
     * @return a bitmap of the image
     */
    public void getImage(String imageId, final ImageDownloadCallback imageDownloadCallback) {
        // Based off of https://firebase.google.com/docs/storage/android/download-files#download_to_a_local_file

        StorageReference imageReference = getImageReference(imageId);

        try {

            final File localFile = File.createTempFile("images", IMAGE_FORMAT);

            final FileDownloadTask fileDownloadTask = imageReference.getFile(localFile);
            fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    tasks.remove(fileDownloadTask);
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageDownloadCallback.onSuccess(bitmap);
                }

            }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    tasks.remove(fileDownloadTask);
                    imageDownloadCallback.onFailure(exception);
                }

            });
            tasks.add(fileDownloadTask);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update an image in the database
     * @param imageId the unique id of the image in the database
     * @param image the new image to be associated with the imageId
     */
    public void updateImage(final String imageId, final Bitmap image, final ImageUploadCallback callback) {
        // Based off of https://firebase.google.com/docs/storage/android/upload-files#upload_from_data_in_memory

        StorageReference imageReference = getImageReference(imageId);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        final UploadTask uploadTask = imageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                tasks.remove(uploadTask);
                callback.onFailure(exception);
            }

        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                tasks.remove(uploadTask);
                callback.onSuccess(imageId);
            }

        });
        tasks.add(uploadTask);
    }

    /**
     * Remove an image from the database
     * @param imageId the unique id of the image in the database
     */
    public void deleteImage(String imageId, final ImageDeletionCallback imageDeletionCallback) {
        // Based off of https://firebase.google.com/docs/storage/android/delete-files#delete_a_file

        StorageReference imageReference = getImageReference(imageId);
        imageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                imageDeletionCallback.onSuccess();
            }

        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                imageDeletionCallback.onFailure(exception);
            }

        });
    }

    /**
     * Cancels all download and upload tasks for images
     */
    public void cancelUploadDownloadTasks() {
        for (StorageTask task : tasks) {
            task.cancel();
        }
        tasks.clear();
    }

    /**
     * Get the reference to an image in Firebase Storage
     * @param imageId the unique id of the image in the database
     * @return a reference to the image
     */
    public StorageReference getImageReference(String imageId) {
        return storage.child(imageId + "." + IMAGE_FORMAT);
    }

}
