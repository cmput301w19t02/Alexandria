package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationController {
    private FirebaseAuth auth;

    private static AuthenticationController instance;

    public static AuthenticationController getInstance() {
        if (instance == null) {
            instance = new AuthenticationController();
        }
        return instance;
    }

    public boolean authenticate(String username, String password) {
        // TODO Implement
        throw new UnsupportedOperationException("Not implemented");
    }

    public void deauthenticate() {
        // TODO Implement
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean createUser(String username, String password) {
        // TODO Implement
        throw new UnsupportedOperationException("Not implemented");
    }

    public String getMyId() {
        return auth.getUid();
    }
}
