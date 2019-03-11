package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.BookList;
import ca.ualberta.CMPUT3012019T02.alexandria.model.BookRecyclerViewAdapter;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;

public class ViewUserProfileActivity extends AppCompatActivity {

    //private List<BookList> ownedBooks;
    private String username;
    private String name;
    private UserProfile userProfile;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            userID = null;
        } else {
            userID = extras.getString("USER_ID");
        }

        /*
        //TODO setup data retrieval from Firebase, and remove placeholder lists
        Bitmap aBitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888);
        ownedBooks = new ArrayList<>();
        ownedBooks.add(new BookList
                (aBitmap, "Test Title",
                        "Test Author", "Test ISBN", "accepted"));
        ownedBooks.add(new BookList
                (aBitmap, "Test Title 2",
                        "Test Author", "Test ISBN", "accepted"));
        ownedBooks.add(new BookList
                (aBitmap, "Test Title 3",
                        "Test Author", "Test ISBN", "accepted"));
        ownedBooks.add(new BookList
                (aBitmap, "Test Title 4",
                        "Test Author", "Test ISBN", "accepted"));
        ownedBooks.add(new BookList
                (aBitmap, "Test Title 5",
                        "Test Author", "Test ISBN", "accepted"));
        */

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // remove default title

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //TODO temp
        //getCurrentUserProfile();
        //userProfile = new UserProfile("Joe Example","john@example.com","7801234567",null,"joe_username");
        UserController userController = UserController.getInstance();
        userController.getUserProfile(userID).handleAsync((result, error) -> {
            if(error == null) {
                // Update ui here
                userProfile = result;
                TextView textView_username = (TextView) findViewById(R.id.view_profile_username);
                TextView textView_name = (TextView) findViewById(R.id.view_profile_name);

                username = userProfile.getUsername();
                name = userProfile.getName();

                textView_username.setText(username);
                textView_name.setText(name);
            }
            else {
                // Show error message
                Toast.makeText(this , "Profile is not recognized", Toast.LENGTH_LONG).show();
                userProfile = new UserProfile("Unknown","Unknown","Unknown",null,"Unknown");
            }
            return null;
        });

        //TODO implement book list
        // Recycler View
        //RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.view_user_books_recycler);
        //BookRecyclerViewAdapter bookAdapter =
        //        new BookRecyclerViewAdapter(this, ownedBooks, "UserBookFragment");
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.setAdapter(bookAdapter);
    }

    /**
     * sets user info to the page
     */
   /* @Override
    public void onResume() {
        super.onResume();

        //TODO set image as in the profile if exists

        TextView textView_username = (TextView) findViewById(R.id.view_profile_username);
        TextView textView_name = (TextView) findViewById(R.id.view_profile_name);
        //ImageView image_avatar = (ImageView) findViewById(R.id.user_image);

        username = userProfile.getUsername();
        name = userProfile.getName();
        //String avatarID = userProfile.getPicture();

        textView_username.setText(username);
        textView_name.setText(name);
        //image_avatar. set image
    }
*/
    /** //TODO make use
     * get userProfile of the current user from the database
     */
    private void getCurrentUserProfile() {
        UserController userController = UserController.getInstance();
        userProfile = null;
        userController.getUserProfile(userID).handleAsync((result, error) -> {
            if(error == null) {
                // Set a class variable
                userProfile = result;
            }
            else {
                // Show error message
                Toast.makeText(this , "Profile is not recognized", Toast.LENGTH_LONG).show();
                userProfile = new UserProfile("Unknown","Unknown","Unknown",null,"Unknown");
            }
            return null;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Menu switch, shows warning for blocking user,
     * takes to messaging activity
     * @param item item selected
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //TODO make use of actual user name
            case R.id.message_user_option:
                Intent intentMain = new Intent(this, MainActivity.class);
                String fragment_name = "message";
                intentMain.putExtra("fragment_name", fragment_name);
                startActivity(intentMain);
                break;
            case R.id.block_user_option:
                Toast.makeText(this , "Block user implement", Toast.LENGTH_LONG).show();

                AlertDialog.Builder blockAlert = new AlertDialog.Builder(ViewUserProfileActivity.this, R.style.AlertDialogTheme);

                blockAlert.setCancelable(true);
                blockAlert.setTitle("Block User?");
                String blockMessage = "Are you sure you want to block " +  name;
                blockAlert.setMessage(blockMessage);

                blockAlert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                Context context = this;
                blockAlert.setPositiveButton("BLOCK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // block user
                        Toast.makeText(context , "User Blocked", Toast.LENGTH_LONG).show();
                    }
                });
                blockAlert.show();

                break;
            case R.id.user_profile_setting:
                // open menu
                break;
            default:
                throw new RuntimeException("Unknown option");
        }
        return super.onOptionsItemSelected(item);
    }
}
