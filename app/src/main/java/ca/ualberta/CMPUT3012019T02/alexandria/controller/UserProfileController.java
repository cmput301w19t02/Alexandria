package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;

public class UserProfileController {

    private static UserProfileController instance;

    private UserProfileController() { }

    public static UserProfileController getInstance() {
        if (instance == null) {
            instance = new UserProfileController();
        }
        return instance;
    }

    public UserProfile getUserProfile(String id) {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

    public UserProfile getMyProfile() {
        // TODO: Finish implementation
        // does a call to getUserProfile() with the "my" id
        throw new UnsupportedOperationException();
    }

    public void updateMyProfile(UserProfile userProfile) {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

}
