package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java9.util.concurrent.CompletableFuture;

/**
 * The controller for Notifications
 */
public class NotificationController {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseFunctions functions = FirebaseFunctions.getInstance();

    private static NotificationController instance;

    /**
     * Gets the singleton instance of NotificationController
     *
     * @return the NotificationController instance
     */
    public static NotificationController getInstance() {
        if (instance == null) {
            instance = new NotificationController();
        }
        return instance;
    }

    /**
     * Sets device token for current device
     *
     * @return a future with the success of the operation
     */
    public CompletableFuture<Void> setDeviceToken() {
        CompletableFuture<Void> future = new java9.util.concurrent.CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                String projectId = "461628567406";
                String token = FirebaseInstanceId.getInstance().getToken(projectId, "FCM");
                while (token == null) {
                    token = FirebaseInstanceId.getInstance().getToken(projectId, "FCM");
                }
                database.getReference().child("users").child(UserController.getInstance().getMyId()).child("deviceTokens").child(token).setValue(1).addOnCompleteListener(databaseTask -> {
                    if (databaseTask.isSuccessful()) {
                        future.complete(null);
                    } else {
                        future.completeExceptionally(new IOException("Connecting to database"));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    /**
     * Send notification completable future.
     *
     * @param userId  the user id
     * @param title   the title
     * @param message the message
     * @return a future with the success of the operation
     */
    public CompletableFuture<Void> sendNotification(String userId, String title, String message) {
        CompletableFuture<Void> future = new java9.util.concurrent.CompletableFuture<>();
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("body", message);
        data.put("title", title);

        functions.getHttpsCallable("sendNotification").call(data).continueWith(task -> {
            if (task.isSuccessful()) {
                future.complete(null);
            } else {
                future.completeExceptionally(task.getException());
            }
            return null;
        });
        return future;
    }
}
