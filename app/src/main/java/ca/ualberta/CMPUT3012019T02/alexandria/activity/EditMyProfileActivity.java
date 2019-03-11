package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_my_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // remove default title

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //TODO password save + change

        editText_name = (EditText) findViewById(R.id.editText_name);
        editText_username = (EditText) findViewById(R.id.editText_username);
        //editText_password = (EditText) findViewById(R.id.editText_password);
        editText_email = (EditText) findViewById(R.id.editText_email);
        ImageView image_avatar = (ImageView) findViewById(R.id.user_image);

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
                    editText_name.setText(username);
                    editText_username.setText(name);
                    editText_email.setText(email);

                    ImageController imageController = ImageController.getInstance();
                    imageController.getImage(photoId).handleAsync((resultImage, errorImage) -> {
                        if (errorImage == null) {
                            Bitmap bitmap = resultImage;

                            if (bitmap != null) {
                                Bitmap squareBitmap = Bitmap.createBitmap(bitmap, 0, 0, Math.min(bitmap.getWidth(), bitmap.getHeight()), Math.min(bitmap.getWidth(), bitmap.getHeight()));

                                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), squareBitmap);
                                drawable.setCornerRadius(Math.min(bitmap.getWidth(), bitmap.getHeight()));
                                drawable.setAntiAlias(true);

                                runOnUiThread(() -> {
                                    image_avatar.setImageDrawable(drawable);
                                });
                            }
                        } else {
                            showError(errorImage.getMessage());
                        }
                        return null;
                    });
                });
            }
            else {
                // Show error message
                Toast.makeText(this , "Profile is not recognized", Toast.LENGTH_LONG).show();
                myUserProfile = new UserProfile("Unknown","Unknown","Unknown",null,"Unknown");
                editText_name.setText(myUserProfile.getName());
                editText_username.setText(myUserProfile.getUsername());
                editText_email.setText(myUserProfile.getEmail());
            }
            return null;
        });
    }

    private void showError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }

    /**
     * attempts to set editTexts to values entered, if no error
     * saves userProfile, otherwise, shows an error
     * @param view View
     */
    //TODO add avatar change options etc
    protected void onSaveButtonClick(View view) {
        //TODO decide if to have password

        String newUsername = editText_username.getText().toString();
        String newName = editText_name.getText().toString();
        //String newPassword = editText_password.getText().toString();
        String newEmail = editText_email.getText().toString();

        try {
            myUserProfile.setUsername(newUsername);
            myUserProfile.setName(newName);
            // password set
            myUserProfile.setEmail(newEmail);
            // TODO update on firebase userprofile
            Toast.makeText(this , "Changes Saved", Toast.LENGTH_LONG).show();
            finish();
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            Toast.makeText(this , errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
