package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;

/**
 * The shows the a user's profile.
 */
public class ViewUserProfileActivity extends AppCompatActivity {

    private String username;
    private String name;
    private String photoId;
    private UserProfile userProfile;
    private String userID;

    private ImageController imageController = ImageController.getInstance();
    private UserController userController = UserController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_profile);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            userID = null;
        } else {
            userID = extras.getString("USER_ID");
        }

        // Toolbar
        Toolbar toolbar = findViewById(R.id.user_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // remove default title

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        //TODO implement book list
    }

    /**
     * sets user info to the page
     */
    @Override
    public void onResume() {
        super.onResume();

        userController.getUserProfile(userID).handleAsync((result, error) -> {
            if (error == null) {
                userProfile = result;
                TextView textViewUsername = findViewById(R.id.user_profile_username);
                TextView textViewName = findViewById(R.id.user_profile_name);

                username = userProfile.getUsername();
                name = userProfile.getName();
                photoId = userProfile.getPicture();
                runOnUiThread(() -> {
                    textViewUsername.setText(username);
                    textViewName.setText(name);
                });
                if (photoId != null) {
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

                                ImageView imageView = findViewById(R.id.user_profile_image);
                                runOnUiThread(() -> {
                                    imageView.setImageDrawable(drawable);
                                });
                            }
                        } else {
                            showError(errorImage.getMessage());
                        }
                        return null;
                    });
                }
            } else {
                // Show error message
                Toast.makeText(this, "Profile is not recognized",
                        Toast.LENGTH_LONG).show();
                userProfile = new UserProfile("Unknown", "Unknown",
                        "Unknown", null, "Unknown");
            }
            return null;
        });
    }

    private void showError(String message) {
        Toast.makeText(ViewUserProfileActivity.this,
                "Error: " + message, Toast.LENGTH_LONG).show();
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
     *
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
                Toast.makeText(this, "Block user implement", Toast.LENGTH_LONG).show();

                AlertDialog.Builder blockAlert = new AlertDialog.Builder(
                        ViewUserProfileActivity.this, R.style.AlertDialogTheme);

                blockAlert.setCancelable(true);
                blockAlert.setTitle("Block User?");
                String blockMessage = "Are you sure you want to block " + name;
                blockAlert.setMessage(blockMessage);

                blockAlert.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
                Context context = this;
                blockAlert.setPositiveButton("BLOCK", (dialog, which) -> {
                    // block user
                    Toast.makeText(context, "User Blocked", Toast.LENGTH_LONG).show();
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
