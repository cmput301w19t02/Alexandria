package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;

/**
 * Activity for my profile of a current user
 * Shows its name, username, email address,
 * provides navigation to blocked users activity
 * and edit my profile activity
 */
public class MyProfileActivity extends AppCompatActivity {

    private UserProfile myUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // remove default title

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // TODO make uses of loggen in user whem it is done
        //getCurrentUserProfile();
        myUserProfile = new UserProfile("Unknown","Unknown","Unknown",null,"Unknown"); //TODO temp
    }

    /**
     * get userProfile of the current user from the database
     */
    private void getCurrentUserProfile() {
        UserController userController = UserController.getInstance();
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
     * sets  user details as in user profile
     */
    @Override
    public void onResume() {
        super.onResume();

        //TODO set image as in the profile if exists

        TextView textView_username = (TextView) findViewById(R.id.textView_username);
        TextView textView_name = (TextView) findViewById(R.id.textView_name);
        TextView textView_email = (TextView) findViewById(R.id.textView_email);
        //ImageView image_avatar = (ImageView) findViewById(R.id.user_image);

        String username = myUserProfile.getUsername();
        String name = myUserProfile.getName();
        String email = myUserProfile.getEmail();
        //String avatarID = myUserProfile.getPicture();

        textView_username.setText(username);
        textView_name.setText(name);
        textView_email.setText(email);
        //image_avatar. set image
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //menu switch
            case R.id.edit_profile:
                // edit activity
                Intent startEditProfile = new Intent(this, EditMyProfileActivity.class);
                startActivity(startEditProfile);
                break;
            case R.id.blocked_users:
                // black list activity
                Intent startBlockedUsers = new Intent(this, BlockedUsersActivity.class);
                startActivity(startBlockedUsers);
                break;
            case R.id.profile_setting:
                // open menu
                break;
            default:
                throw new RuntimeException("Unknown option");
        }
        return super.onOptionsItemSelected(item);
    }
}
