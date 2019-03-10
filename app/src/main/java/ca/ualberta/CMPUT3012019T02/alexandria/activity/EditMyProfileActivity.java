package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
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

        //TODO get user details from firebase
        myUserProfile = new UserProfile("John Smith","john@example.com","7801234567",null,"johnsmith");
    }

    @Override
    public void onResume() {
        super.onResume();

        //TODO set image as in the profile if exists
        //TODO password save + change

        editText_name = (EditText) findViewById(R.id.editText_name);
        editText_username = (EditText) findViewById(R.id.editText_username);
        //editText_password = (EditText) findViewById(R.id.editText_password);
        editText_email = (EditText) findViewById(R.id.editText_email);
        //ImageView image_avatar = (ImageView) findViewById(R.id.user_image);

        String username = myUserProfile.getUsername();
        String name = myUserProfile.getName();
        String email = myUserProfile.getEmail();
        //String avatarID = myUserProfile.getPicture();

        editText_name.setText(name);
        editText_username.setText(username);
        editText_email.setText(email);
        //image_avatar. set image
    }

    /** //TODO make use
     * get userProfile of the current user from the database
     */
    private void getCurrentUserProfile() {
        userController = UserController.getInstance();
        myUserProfile = null;
        userController.getMyProfile().handleAsync((result, error) -> {
            if(error == null) {
                // Set a class variable
                myUserProfile = result;
            }
            else {
                // Show error message
                Toast.makeText(this , "Profile is not recognize", Toast.LENGTH_LONG).show();
                myUserProfile = new UserProfile("Unknown","Unknown","Unknown",null,"Unknown");
            }
            return null;
        });
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
