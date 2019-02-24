package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void signIn(View view) {
        //TODO Implement
        throw new UnsupportedOperationException("Not implemented");
    }

    public void navigateToSignUp(View view) {
        //TODO Implement
        throw new UnsupportedOperationException("Not implemented");
    }


    private boolean validateUsername(String username) {
        //TODO: Replace this with better logic
        return true;
    }

    private boolean validatePassword(String password) {
        //TODO: Replace this with better logic
        return password.length() >= 8;
    }

    public void loadInfo() {
        //TODO Implement
        throw new UnsupportedOperationException("Not implemented");
    }

}

