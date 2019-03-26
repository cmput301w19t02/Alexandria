package ca.ualberta.CMPUT3012019T02.alexandria.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_login);
    }

    /**
     * Signs the user in using username and password
     *
     * @param view the signin button
     */
    public void signIn(View view) {
        AppCompatEditText usernameField = findViewById(R.id.login_username_field);
        AppCompatEditText passwordField = findViewById(R.id.login_password_field);
        TextView errorMessage = findViewById(R.id.error_message);

        errorMessage.setText("");

        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString();

        if (!validator(username,password)) return;

        CompletableFuture<Void> future = userController.authenticate(username,password);
        startSpinner();
        future.handleAsync((result, error) -> {
            if (error == null) {
                finish();
            } else {
                runOnUiThread(() -> {
                    stopSpinner();
                    errorMessage.setText(R.string.login_error);
                });
            }
            return null;
        });
    }

    /**
     * Navigates to sign up.
     *
     * @param view the sign up button
     */
    public void navigateToSignUp(View view) {
        Intent startSignUpActivity = new Intent(this, SignUpActivity.class);
        startActivity(startSignUpActivity);
    }

    private Boolean validator(String username, String password) {
        TextView errorMessage = findViewById(R.id.error_message);
        if (username.equals("") || password.equals("")) {
            errorMessage.setText(R.string.login_error);
            return false;
        }
        else return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // or finish();
    }

    /**
     * Removes spinner when data is loaded
     *
     */
    private void stopSpinner() {
        ProgressBar spinner = findViewById(R.id.spinner);
        TextInputLayout tiUsername = findViewById(R.id.login_username_layout);
        TextInputLayout tiPassword = findViewById(R.id.login_password_layout);
        TextView tvSignup = findViewById(R.id.to_sign_up);

        tiUsername.setVisibility(View.VISIBLE);
        tiPassword.setVisibility(View.VISIBLE);
        tvSignup.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.INVISIBLE);

    }

    private void startSpinner() {
        ProgressBar spinner = findViewById(R.id.spinner);
        TextInputLayout tiUsername = findViewById(R.id.login_username_layout);
        TextInputLayout tiPassword = findViewById(R.id.login_password_layout);
        TextView tvSignup = findViewById(R.id.to_sign_up);

        tiUsername.setVisibility(View.INVISIBLE);
        tiPassword.setVisibility(View.INVISIBLE);
        tvSignup.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.VISIBLE);
    }


}

