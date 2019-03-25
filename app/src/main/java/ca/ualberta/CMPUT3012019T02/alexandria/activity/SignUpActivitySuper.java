package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.support.v7.app.AppCompatActivity;

import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;

abstract class SignUpActivitySuper extends AppCompatActivity {
    protected UserController userController = UserController.getInstance();
    protected ImageController imageController = ImageController.getInstance();
    protected String photoId = null;

    protected boolean validateUsername(String username) {
        return username.length() >= 4;
    }

    protected boolean validateName(String username) {
        return username.length() >= 3;
    }

    protected boolean validateEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    protected boolean validatePassword(String password) {
        return password.length() >= 8;
    }
}
