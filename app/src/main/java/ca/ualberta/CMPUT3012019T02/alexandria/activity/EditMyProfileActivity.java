package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;

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

    public static final int RESULT_CAMERA = 1;
    public static final int RESULT_GALLERY = 2;
    public final int REQUEST_PERMISSION_PHONE_STATE = 5;

    private UserController userController;
    private UserProfile myUserProfile;
    private String name;
    private String email;
    private Bitmap userAvatar;
    private String imageID;

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

        // register imageView for menu
        ImageView addImageButton = findViewById(R.id.edit_profile_add_image);
        registerForContextMenu(addImageButton);

        // turn of username editing
        EditText etUsername = findViewById(R.id.edit_profile_username);
        etUsername.setEnabled(false);
        etUsername.setTextColor(getResources().getColor(R.color.colorGrey));

        initializePageAndData();
    }

    /**
     * inflate menu for add photo options based on the presence of the photo
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        if (userAvatar == null) {
            inflater.inflate(R.menu.select_take_image_menu, menu);
        } else {
            inflater.inflate(R.menu.select_take_delete_image_menu, menu);
        }
    }

    /**
     * add photo options
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //menu switch
            case R.id.option_select_photo:
                // Select from gallery
                openGallery();
                break;
            case R.id.option_take_photo:
                // Take camera picture
                addPhotoCamera();
                break;
            case R.id.option_delete_photo:
                // Remove photo
                removePhoto();
                break;
            default:
                throw new RuntimeException("Unknown option");
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * remove bitmap photo, image id still connected unless saved
     */
    public void removePhoto() {
        ImageView ivCover = findViewById(R.id.edit_profile_user_image);
        ivCover.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile));

        userAvatar = null;
    }

    /**
     * choose image from gallery
     */
    public void openGallery() {
        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intentGallery, RESULT_GALLERY);
    }

    public void addPhotoCamera() {
        // Todo: implement other possibilities

        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Camera is required. " +
                    "Please allow camera usage and try again", Toast.LENGTH_LONG).show();

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_PHONE_STATE);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, RESULT_CAMERA);
        }
    }

    /**
     * camera, scan, gallery
     *
     * @param requestCode result code
     * @param resultCode confirmation code
     * @param data output action
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == RESULT_CAMERA) {
            // Camera photo
            Bundle extras = data.getExtras();
            userAvatar = (Bitmap) extras.get("data");
            Bitmap squareBitmap = Bitmap.createBitmap(userAvatar, 0, 0,
                    Math.min(userAvatar.getWidth(), userAvatar.getHeight()),
                    Math.min(userAvatar.getWidth(), userAvatar.getHeight()));

            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                    .create(getResources(), squareBitmap);
            drawable.setCornerRadius(Math.min(
                    userAvatar.getWidth(), userAvatar.getHeight()));
            drawable.setAntiAlias(true);

            ImageView ivAvatar = findViewById(R.id.edit_profile_user_image);
            ivAvatar.setImageDrawable(drawable);
        } else if (requestCode == RESULT_GALLERY && resultCode == RESULT_OK) {
            // Gallery look up
            Uri selectedImage = data.getData();

            try {
                userAvatar = getBitmapFromUri(selectedImage);
                Bitmap squareBitmap = Bitmap.createBitmap(userAvatar, 0, 0,
                        Math.min(userAvatar.getWidth(), userAvatar.getHeight()),
                        Math.min(userAvatar.getWidth(), userAvatar.getHeight()));

                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                        .create(getResources(), squareBitmap);
                drawable.setCornerRadius(Math.min(
                        userAvatar.getWidth(), userAvatar.getHeight()));
                drawable.setAntiAlias(true);

                ImageView ivAvatar = findViewById(R.id.edit_profile_user_image);
                ivAvatar.setImageDrawable(drawable);
            } catch (IOException e) {
                showError(e.getMessage());
            }
        }
    }

    /**
     * https://stackoverflow.com/questions/13023788/how-to-load-an-image-in-image-view-from-gallery
     * returns bitmap image from uri
     */
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    /**
     * locate input fields, controllers. Get current user and set its data to the page
     */
    private void initializePageAndData() {
        editText_name = findViewById(R.id.edit_profile_name);
        editText_username = findViewById(R.id.edit_profile_username);
        //editText_password = findViewById(R.id.editText_password);
        editText_email = findViewById(R.id.edit_profile_email);
        ImageView image_avatar = findViewById(R.id.edit_profile_user_image);

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
                                userAvatar = resultImage;

                                if (userAvatar != null) {
                                    Bitmap squareBitmap = Bitmap.createBitmap(userAvatar, 0, 0,
                                            Math.min(userAvatar.getWidth(), userAvatar.getHeight()),
                                            Math.min(userAvatar.getWidth(), userAvatar.getHeight()));

                                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                                            .create(getResources(), squareBitmap);
                                    drawable.setCornerRadius(Math.min(
                                            userAvatar.getWidth(), userAvatar.getHeight()));
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
    public void onSaveButtonClick(View view) {
        fetchData();
        // checks error on editTexts
        try {
            if (userAvatar != null) {
                saveUserWithImage();
            } else {
                saveUser();
            }

            delayedClose();
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            showError(errorMessage);
        }
    }

    private void saveUserWithImage() {
        final String imageIdToDelete = imageID;
        ImageController.getInstance().addImage(userAvatar).handleAsync((result, error) -> {
            if (error == null) {
                imageID = result;

                runOnUiThread(() -> {
                    saveUser();
                    if (imageIdToDelete != null) {
                        ImageController.getInstance().deleteImage(imageIdToDelete);
                    }
                });
            } else {
                showError(error.getMessage());
            }
            return null;
        });
    }

    private void saveUser() {
        if (imageID != null && userAvatar == null) {
            ImageController.getInstance().deleteImage(imageID);
            imageID = null;
        }
        myUserProfile.setName(name);
        myUserProfile.setEmail(email);
        myUserProfile.setPicture(imageID);
        userController.updateMyProfile(myUserProfile);
    }

    private void fetchData() {
        name = editText_name.getText().toString();
        //String newPassword = editText_password.getText().toString();
        email = editText_email.getText().toString();
    }

    private void delayedClose() {
        startSpinner();


        int finishTime = 3; // x seconds
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                showMessage("Changes Saved");
                finish();
            }
        }, finishTime * 1000);
    }
}
