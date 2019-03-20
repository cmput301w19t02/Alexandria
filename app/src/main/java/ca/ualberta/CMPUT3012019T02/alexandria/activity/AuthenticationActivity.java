package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.support.v7.app.AppCompatActivity;

public abstract class AuthenticationActivity extends AppCompatActivity {

    protected boolean validateUsername(String username) {
        return username.length() >= 4;
    }

    protected boolean validatePassword(String password) {
        return password.length() >= 8;
    }
}
