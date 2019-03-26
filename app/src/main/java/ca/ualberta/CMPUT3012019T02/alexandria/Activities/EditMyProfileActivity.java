package ca.ualberta.CMPUT3012019T02.alexandria.Activities;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.user.UserProfile;

/**
 * Edit current user profile activity
 * sets edit text boxes to current value as in user profile
 * allows to enter new values and save them.
 * If new values are valid saves them
 */
public class EditMyProfileActivity extends AppCompatActivity {

    private UserController userController;
    private UserProfile myUserProfile;

    private EditText editText_name;
    private EditText editText_username;
    // private EditText editText_password;
    private EditText editText_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);

        // toolbar
        Toolbar toolbar = findViewById(R.id.edit_my_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // remove default title

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * Sets user details on the profile page
     */
    @Override
    public void onResume() {
        super.onResume();

        //TODO password

        editText_name = findViewById(R.id.editText_name);
        editText_username = findViewById(R.id.editText_username);
        //editText_password = findViewById(R.id.editText_password);
        editText_email = findViewById(R.id.editText_email);
        ImageView image_avatar = findViewById(R.id.user_image);

        // sets user info
        userController = UserController.getInstance();
        userController.getMyProfile().handleAsync((result, error) -> {
            if(error == null) {
                // Update ui here
                myUserProfile = result;

                String username = myUserProfile.getUsername();
                String name = myUserProfile.getName();
                String email = myUserProfile.getEmail();
                String photoId = myUserProfile.getPicture();
                runOnUiThread(() -> {
                    editText_name.setText(name);
                    editText_username.setText(username);
                    editText_email.setText(email);

                    // sets user image if any
                    if (photoId == null) {
                        stopSpinner();
                    } else {
                        ImageController imageController = ImageController.getInstance();
                        imageController.getImage(photoId).handleAsync((resultImage, errorImage) -> {
                            if (errorImage == null) {
                                Bitmap bitmap = resultImage;

                                if (bitmap != null) {
                                    Bitmap squareBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                                            Math.min(bitmap.getWidth(), bitmap.getHeight()),
                                            Math.min(bitmap.getWidth(), bitmap.getHeight()));

                                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                                            .create(getResources(), squareBitmap);
                                    drawable.setCornerRadius(Math.min(
                                            bitmap.getWidth(), bitmap.getHeight()));
                                    drawable.setAntiAlias(true);

                                    runOnUiThread(() -> {
                                        image_avatar.setImageDrawable(drawable);
                                        stopSpinner();
                                    });
                                }
                            } else {
                                showError(errorImage.getMessage());
                            }
                            return null;
                        });
                    }
                });
            }
            else {
                // Show error message
                showError("Profile is not recognized");
                myUserProfile = new UserProfile("Unknown","Unknown",
                        "Unknown",null,"Unknown");
                editText_name.setText(myUserProfile.getName());
                editText_username.setText(myUserProfile.getUsername());
                editText_email.setText(myUserProfile.getEmail());
            }
            return null;
        });
    }

    /**
     * Removes spinner when data is loaded
     */
    private void stopSpinner() {
        ProgressBar spinner = findViewById(R.id.edit_my_profile_spinner);
        spinner.setVisibility(View.GONE);

        ConstraintLayout mainContent = findViewById(R.id.edit_my_profile_main_content);
        mainContent.setVisibility(View.VISIBLE);
    }

    private void startSpinner() {
        ProgressBar spinner = findViewById(R.id.edit_my_profile_spinner);
        spinner.setVisibility(View.VISIBLE);

        ConstraintLayout mainContent = findViewById(R.id.edit_my_profile_main_content);
        mainContent.setVisibility(View.GONE);
    }

    /**
     * shows toast of an error
     * @param message error message
     */
    private void showError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }

    /**
     * shows message
     * @param message  message
     */
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * attempts to set editTexts to values entered, if no error
     * saves userProfile, otherwise, shows an error
     * @param view View
     */
    //TODO add avatar change options etc
    public void onSaveButtonClick(View view) {
        //TODO decide if to have password

        String newUsername = editText_username.getText().toString();
        String newName = editText_name.getText().toString();
        //String newPassword = editText_password.getText().toString();
        String newEmail = editText_email.getText().toString();

        // checks error on editTexts
        try {
            myUserProfile.setUsername(newUsername);
            myUserProfile.setName(newName);
            // todo password set
            myUserProfile.setEmail(newEmail);
            userController.updateMyProfile(myUserProfile);

            delayedClose();
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            showError(errorMessage);
        }
    }

    private void delayedClose() {
        startSpinner();


        int finishTime = 2; //1 sec
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                showMessage("Changes Saved");
                finish();
            }
        }, finishTime * 1000);
    }
}
