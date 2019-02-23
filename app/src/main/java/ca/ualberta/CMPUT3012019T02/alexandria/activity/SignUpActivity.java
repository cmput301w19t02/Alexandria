package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void addPhoto(View view) {
        //TODO Implement
        throw new UnsupportedOperationException("Not implemented");
    }

    public void signUp(View view) {
        //TODO Implement
        throw new UnsupportedOperationException("Not implemented");
    }

    public void showOrHidePassword(View view) {
        //TODO Implement
        throw new UnsupportedOperationException("Not implemented");
    }

    private boolean validateUsername(String username) {
        //TODO: Replace this with better logic
        return true;
    }

    private boolean validateEmail(String email) {
        //TODO: Replace this with better logic
        return email.contains("@") && email.contains(".");
    }

    private boolean validatePassword(String password) {
        //TODO: Replace this with better logic
        return password.length() >= 8;
    }
}
