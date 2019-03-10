package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;

public class EditMyProfileActivity extends AppCompatActivity {

    private UserProfile myUserProfile;

    private EditText editText_name;
    private EditText editText_username;
    private EditText editText_password;
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
        editText_password = (EditText) findViewById(R.id.editText_password);
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

    //TODO add avatar change options etc
    protected void onSaveButtonClick(View view) {
        //TODO password save + change

        String newUsername = editText_username.getText().toString();
        String newName = editText_name.getText().toString();
        //String newPassword = editText_password.getText().toString();
        String newEmail = editText_email.getText().toString();

        try {
            myUserProfile.setUsername(newUsername);
            myUserProfile.setName(newName);
            // password set
            myUserProfile.setEmail(newEmail);
            Toast.makeText(this , "Changes Saved", Toast.LENGTH_LONG).show();
            finish();
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            Toast.makeText(this , errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
