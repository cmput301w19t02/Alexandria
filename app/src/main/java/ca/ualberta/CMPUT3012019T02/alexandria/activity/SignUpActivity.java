package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import java9.util.concurrent.CompletableFuture;

/**
 * The sign up screen
 */
public class SignUpActivity extends AppCompatActivity {

    private UserController userController = UserController.getInstance();
    private ImageController imageController = ImageController.getInstance();
    private String photoId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // toolbar
        Toolbar toolbar = findViewById(R.id.sign_up_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // remove default title

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * Adds photo from camera
     *
     * @param view the add photo button
     */
    public void addPhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }

    /**
     * Signs the user up using provided credentials
     *
     * @param view the sign up button
     */
    public void signUp(View view) {
        AppCompatEditText nameField = findViewById(R.id.sign_up_name_field);
        AppCompatEditText usernameField = findViewById(R.id.sign_up_usernname_field);
        AppCompatEditText passwordField = findViewById(R.id.sign_up_password_field);
        AppCompatEditText emailField = findViewById(R.id.sign_up_email_field);

        String name = nameField.getText().toString().trim();
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString();
        String email = emailField.getText().toString().trim();

        if (!validateName(name)) {
            showError("Name is invalid! Name must contain at least 4 character.");
            return;
        }
        if (!validateUsername(username)) {
            showError("Username is invalid! Username must contain at least 4 character.");
            return;
        }
        if (!validatePassword(password)) {
            showError("Password is invalid! Password must contain at least 8 characters.");
            return;
        }
        if (!validateEmail(email)) {
            showError("Email is invalid! Email must be valid email.");
            return;
        }

        CompletableFuture<Void> future = userController.createUser(name, email, null, photoId, username, password);

        future.handleAsync((result, error) -> {
            if (error == null) {
                Intent startMainActivity = new Intent(this, MainActivity.class);
                startMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startMainActivity);
            } else {
                runOnUiThread(()->showError(error.getMessage()));
            }
            return null;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            imageController.addImage(bitmap).handleAsync((result, error) -> {
                if (error == null) {
                    photoId = result;

                    Bitmap squareBitmap = Bitmap.createBitmap(bitmap, 0, 0, Math.min(bitmap.getWidth(), bitmap.getHeight()), Math.min(bitmap.getWidth(), bitmap.getHeight()));

                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), squareBitmap);
                    drawable.setCornerRadius(Math.min(bitmap.getWidth(), bitmap.getHeight()));
                    drawable.setAntiAlias(true);

                    ImageView imageView = findViewById(R.id.sign_up_user_image);
                    imageView.setImageDrawable(drawable);
                } else {
                    runOnUiThread(()->showError(error.getMessage()));
                }
                return null;
            });
        }
    }

    private void showError(String message) {
        Toast.makeText(getBaseContext(), "Error: " + message, Toast.LENGTH_LONG).show();
    }

    private boolean validateUsername(String username) {
        return username.length() >= 4;
    }

    private boolean validateName(String username) {
        return username.length() >= 3;
    }

    private boolean validateEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean validatePassword(String password) {
        return password.length() >= 8;
    }
}
