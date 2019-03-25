package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import ca.ualberta.CMPUT3012019T02.alexandria.cache.ObservableUserCache;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;
import java9.util.concurrent.CompletableFuture;


/**
 * This class is for controlling user profile and authentication
 */
public class UserController {

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    private static UserController instance;

    private UserController() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    /**
     * Gets the singleton instance of UserController
     *
     * @return the UserController instance
     */
    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    /**
     * Determines and returns if a user is authenticated
     *
     * @return if the user is authenticated
     */
    public boolean isAuthenticated() {
        return auth.getCurrentUser() != null;
    }

    /**
     * Logs the current user in
     *
     * @param username the user's username
     * @param password the user's password
     * @return a CompletableFuture signifying this operation's success/failure
     */
    public CompletableFuture<Void> authenticate(String username, final String password) {
        ObservableUserCache.invalidate();

        final CompletableFuture<Void> future = new CompletableFuture<>();

        CompletableFuture<String> emailFuture = getUserEmail(username);
        emailFuture.thenAccept(email -> auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                NotificationController.getInstance().setDeviceToken().handleAsync((result,error)->{
                    if(error==null){
                        future.complete(null);
                    }
                    else {
                        future.completeExceptionally(error);
                    }
                    return null;
                });
            } else {
                future.completeExceptionally(task.getException());
            }
        }));

        emailFuture.exceptionally(throwable -> {
            future.completeExceptionally(throwable);
            return null;
        });

        return future;
    }

    /**
     * Signs the current user out
     */
    public void deauthenticate() {
        auth.signOut();
        ObservableUserCache.invalidate();
        CompletableFuture.runAsync(()->{
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Creates a new user account
     *
     * @param username the user's new username
     * @param email    the user's email
     * @param password the user's password
     * @return a CompletableFuture signifying this operation's success/failure
     */
    public CompletableFuture<Void> createUser(String name, String email, String phone, String picture, String username, String password) {
        final CompletableFuture<Void> resultFuture = new CompletableFuture<>();

        try {
            UserProfile profile = new UserProfile(name, email, phone, picture, username);
            database.getReference().child("usernameToEmail").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(authTask -> {
                            if (authTask.isSuccessful()) {
                                database.getReference().child("usernameToEmail").child(username).setValue(email).addOnCompleteListener(databaseTask -> {
                                    if (databaseTask.isSuccessful()) {
                                        CompletableFuture<Void> profileFuture = updateMyProfile(profile);
                                        profileFuture.handleAsync((profileResult,profileError)->{
                                            if(profileError==null){
                                                NotificationController.getInstance().setDeviceToken().handleAsync((result,error)->{
                                                    if(error==null){
                                                        resultFuture.complete(null);
                                                    }
                                                    else {
                                                        resultFuture.completeExceptionally(error);
                                                    }
                                                    return null;
                                                });
                                            }
                                            else {
                                                resultFuture.completeExceptionally(profileError);
                                            }
                                            return null;
                                        });
                                    } else {
                                        database.getReference().child("usernameToEmail").child(username).removeValue();
                                        resultFuture.completeExceptionally(databaseTask.getException());
                                    }
                                });
                            } else {
                                resultFuture.completeExceptionally(authTask.getException());
                            }
                        });
                    } else {
                        resultFuture.completeExceptionally(new IllegalArgumentException("Username already exists"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    resultFuture.completeExceptionally(databaseError.toException());
                }
            });
        } catch (IllegalArgumentException exception) {
            resultFuture.completeExceptionally(exception);
        }

        return resultFuture;
    }

    /**
     * Gets and return the current user's id
     *
     * @return the current user's id
     */
    @Nullable
    public String getMyId() {
        return auth.getUid();
    }

    /**
     * Gets the current user's email address
     *
     * @param username the user's username
     * @return a CompletableFuture signifying this operation's success/failure
     */
    private CompletableFuture<String> getUserEmail(String username) {

        final CompletableFuture<String> resultFuture = new CompletableFuture<>();

        database.getReference().child("usernameToEmail").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String email = (String) dataSnapshot.getValue();
                    resultFuture.complete(email);

                } else {
                    resultFuture.completeExceptionally(new IllegalArgumentException("Username does not exist"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                resultFuture.completeExceptionally(databaseError.toException());
            }

        });

        return resultFuture;
    }

    /**
     * Gets a user's profile
     *
     * @param id of the user
     * @return a CompletableFuture signifying this operation's success/failure
     */
    public CompletableFuture<UserProfile> getUserProfile(String id) {
        final CompletableFuture<UserProfile> resultFuture = new CompletableFuture<>();

        database.getReference().child("users").child(id).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile profile = dataSnapshot.getValue(UserProfile.class);
                resultFuture.complete(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                resultFuture.completeExceptionally(databaseError.toException());
            }
        });
        return resultFuture;
    }

    /**
     * Gets the current user's profile
     *
     * @return a CompletableFuture signifying this operation's success/failure
     */
    public CompletableFuture<UserProfile> getMyProfile() {
        ObservableUserCache cache = ObservableUserCache.getInstance();
        if (cache.getProfile().isEmpty()) {
            return getUserProfile(getMyId());
        } else {
            return CompletableFuture.completedFuture(cache.getProfile().get());
        }
    }

    /**
     * Updates the current user's profile
     *
     * @param userProfile the updated profile of the current user
     * @return a CompletableFuture signifying this operation's success/failure
     */
    public CompletableFuture<Void> updateMyProfile(UserProfile userProfile) {

        if (!isAuthenticated()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Currently not signed in"));
        }

        final CompletableFuture<Void> resultFuture = new CompletableFuture<>();
        auth.getCurrentUser().updateEmail(userProfile.getEmail()).addOnCompleteListener(updateEmailTask -> {
            if (updateEmailTask.isSuccessful()) {
                database.getReference().child("usernameToEmail").child(userProfile.getUsername()).setValue(userProfile.getEmail()).addOnCompleteListener(mappingTask -> {
                    if (mappingTask.isSuccessful()) {
                        database.getReference().child("users").child(getMyId()).child("profile").setValue(userProfile).addOnCompleteListener(updateUserProfileTask -> {
                            if (updateUserProfileTask.isSuccessful()) {
                                resultFuture.complete(null);
                            } else {
                                resultFuture.completeExceptionally(updateUserProfileTask.getException());
                            }
                        });
                    } else {
                        resultFuture.completeExceptionally(mappingTask.getException());
                    }
                });
            } else {
                resultFuture.completeExceptionally(updateEmailTask.getException());
            }
        });
        return resultFuture;
    }
}
