package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This class manages the addition, retrieval, and modification of images in Firebase Storage
 */
public class ImageController {

    private final String IMAGE_FORMAT = "png";

    private StorageReference storage;

    private static ImageController instance;

    private ImageController() {
        this.storage = FirebaseStorage.getInstance().getReference().child("images");
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
     * @return a CompletableFuture that returns the image id
     */
    public CompletableFuture<String> addImage(Bitmap image) {
        final String imageId = UUID.randomUUID().toString();
        final CompletableFuture<String> future = new CompletableFuture<>();
        updateImage(imageId, image).thenRun(new Runnable() {
            @Override
            public void run() {
                future.complete(imageId);
            }
        });
        return future;
    }

    /**
     * Get an image from the database
     * @param imageId the unique id of the image in the database
     * @return a CompletableFuture that returns the image bitmap
     */
    public CompletableFuture<Bitmap> getImage(String imageId) {
        // Based off of https://firebase.google.com/docs/storage/android/download-files#download_to_a_local_file

        StorageReference imageReference = getImageReference(imageId);

        final CompletableFuture<Bitmap> future = new CompletableFuture<>();

        try {

            final File localFile = File.createTempFile("images", IMAGE_FORMAT);

            final FileDownloadTask fileDownloadTask = imageReference.getFile(localFile);
            fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    future.complete(bitmap);
                }

            }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    future.completeExceptionally(exception);
                }

            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return future;
    }

    /**
     * Update an image in the database
     * @param imageId the unique id of the image in the database
     * @param image the new image to be associated with the imageId
     * @return a CompletableFuture signifying this operation's success/failture
     */
    public CompletableFuture<Void> updateImage(String imageId, Bitmap image) {
        // Based off of https://firebase.google.com/docs/storage/android/upload-files#upload_from_data_in_memory

        StorageReference imageReference = getImageReference(imageId);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        final CompletableFuture<Void> future = new CompletableFuture<>();

        final UploadTask uploadTask = imageReference.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                future.complete(null);
            }

        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                future.completeExceptionally(exception);
            }

        });

        return future;
    }

    /**
     * Remove an image from the database
     * @param imageId the unique id of the image in the database
     * @return a CompletableFuture signifying this operation's success/failture
     */
    public CompletableFuture<Void> deleteImage(String imageId) {
        // Based off of https://firebase.google.com/docs/storage/android/delete-files#delete_a_file

        final CompletableFuture<Void> future = new CompletableFuture<>();

        StorageReference imageReference = getImageReference(imageId);
        imageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                future.complete(aVoid);
            }

        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                future.completeExceptionally(exception);
            }

        });

        return future;
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
