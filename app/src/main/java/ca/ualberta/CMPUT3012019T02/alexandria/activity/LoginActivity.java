package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import java9.util.concurrent.CompletableFuture;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private UserController userController = UserController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void signIn(View view) {
        AppCompatEditText usernameField = findViewById(R.id.login_usernname_field);
        AppCompatEditText passwordField = findViewById(R.id.login_password_field);

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if (!validateUsername(username)) {
            showError("Username is invalid! Username must contain at least 4 character.");
            return;
        }
        if (!validatePassword(password)) {
            showError("Password is invalid! Password must contain at least 8 characters.");
            return;
        }

        CompletableFuture<Void> future = userController.authenticate(username,password);

        future.handleAsync((result, error) -> {
            if (error == null) {
                finish();
            } else {
                showError(error.getMessage());
            }
            return null;
        });
    }

    public void navigateToSignUp(View view) {
        Intent startSignUpActivity = new Intent(this, SignUpActivity.class);
        startActivity(startSignUpActivity);
    }

    private void showError(String message) {
        Toast.makeText(LoginActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
    }

    private boolean validateUsername(String username) {
        return username.length() >= 4;
    }

    private boolean validatePassword(String password) {
        return password.length() >= 8;
    }

}

