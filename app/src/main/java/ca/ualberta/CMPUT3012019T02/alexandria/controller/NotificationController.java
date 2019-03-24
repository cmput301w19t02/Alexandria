package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import java9.util.concurrent.CompletableFuture;

public class NotificationController {
    FirebaseDatabase database = FirebaseDatabase.getInstance();

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

    public CompletableFuture<Void> setDeviceToken() {
        CompletableFuture<Void> future = new java9.util.concurrent.CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                String projectId = "461628567406";
                String token = FirebaseInstanceId.getInstance().getToken(projectId, "FCM");
                while (token == null) {
                    token = FirebaseInstanceId.getInstance().getToken(projectId, "FCM");
                }
                database.getReference().child("deviceTokens").child(UserController.getInstance().getMyId()).setValue(token).addOnCompleteListener(databaseTask -> {
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

    public CompletableFuture<Void> sendNotification(String userId, String title, String message) {
        CompletableFuture<Void> future = new java9.util.concurrent.CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                String projectId = "461628567406";
                String token = FirebaseInstanceId.getInstance().getToken(projectId, "FCM");
                while (token == null) {
                    token = FirebaseInstanceId.getInstance().getToken(projectId, "FCM");
                }
                database.getReference().child("deviceTokens").child(UserController.getInstance().getMyId()).setValue(token).addOnCompleteListener(databaseTask -> {
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
}
