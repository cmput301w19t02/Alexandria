package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import java9.util.concurrent.CompletableFuture;

import static ca.ualberta.CMPUT3012019T02.alexandria.App.getContext;

/**
 * The sign up screen
 */
public class SignUpActivity extends AppCompatActivity {

    /**
     * The RESULT_CAMERA intent constant.
     */
    public static final int RESULT_CAMERA = 1;
    /**
     * The RESULT_GALLERY intent constant.
     */
    public static final int RESULT_GALLERY = 2;
    /**
     * The Request permission phone state.
     */
    public final int REQUEST_PERMISSION_PHONE_STATE = 5;

    private UserController userController = UserController.getInstance();
    private ImageController imageController = ImageController.getInstance();
    private Bitmap coverBitmap;

    private ProgressDialog progressDialog;

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

        // register imageView for creating menu of image input
        ImageView addImageButton = findViewById(R.id.sign_up_plus);
        addImageButton.setOnClickListener(this::setPopupMenu);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating profile...");
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    /**
     * opens phone files to choose image from the gallery
     */
    public void openGallery() {
        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intentGallery, RESULT_GALLERY);
    }

    /**
     * remove bitmap photo, image id still connected unless saved
     */
    public void removePhoto() {
        ImageView ivCover = findViewById(R.id.sign_up_user_image);
        ivCover.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile));

        coverBitmap = null;
    }

    /**
     * Adds photo from camera
     * asks for permission if not granted already
     *
     */
    public void addPhotoCamera() {
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
     * confirms there is a cover in current session
     *
     * @return boolean
     */
    public boolean isImageBitmap() {
        return this.coverBitmap != null;
    }

    /**
     * Creates popup menu for adding photos
     * @param v
     */
    private void setPopupMenu(View v){
        PopupMenu popup = new PopupMenu(getContext(), v);

        if (!this.isImageBitmap()) {
            popup.getMenuInflater().inflate(R.menu.select_take_image_menu, popup.getMenu());
        } else {
            popup.getMenuInflater().inflate(R.menu.select_take_delete_image_menu, popup.getMenu());
        }

        popup.setOnMenuItemClickListener((MenuItem item) -> {
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
            return true;
        });
        popup.show();
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

        progressDialog.show();
        new Thread(() -> {

            String photoId = null;
            if (coverBitmap != null) {
                try {
                    photoId = imageController.addImage(coverBitmap).get(5, TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> showError("Failed to upload image"));
                }
            }

            try {
                userController.createUser(name, email, null, photoId, username, password).get(5, TimeUnit.SECONDS);
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Intent startMainActivity = new Intent(this, MainActivity.class);
                    startMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(startMainActivity);
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(()-> {
                    showError(e.getMessage());
                    progressDialog.dismiss();
                });
            }

        }).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == RESULT_CAMERA) {

            // Camera photo
            Bundle extras = data.getExtras();
            coverBitmap = (Bitmap) extras.get("data");
            ImageView imageView = findViewById(R.id.sign_up_user_image);

            Bitmap squareBitmap = Bitmap.createBitmap(coverBitmap, 0, 0, Math.min(coverBitmap.getWidth(), coverBitmap.getHeight()), Math.min(coverBitmap.getWidth(), coverBitmap.getHeight()));

            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), squareBitmap);
            drawable.setCornerRadius(Math.min(coverBitmap.getWidth(), coverBitmap.getHeight()));
            drawable.setAntiAlias(true);

            imageView.setImageDrawable(drawable);

        } else if (requestCode == RESULT_GALLERY && resultCode == RESULT_OK) {

            // Gallery look up
            Uri selectedImage = data.getData();

            try {
                coverBitmap = getBitmapFromUri(selectedImage);
                ImageView imageView = findViewById(R.id.sign_up_user_image);

                Bitmap squareBitmap = Bitmap.createBitmap(coverBitmap, 0, 0, Math.min(coverBitmap.getWidth(), coverBitmap.getHeight()), Math.min(coverBitmap.getWidth(), coverBitmap.getHeight()));

                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), squareBitmap);
                drawable.setCornerRadius(Math.min(coverBitmap.getWidth(), coverBitmap.getHeight()));
                drawable.setAntiAlias(true);

                imageView.setImageDrawable(drawable);
            } catch (IOException e) {
                showError(e.getMessage());
            }

        }
    }


    /**
     * https://stackoverflow.com/questions/13023788/how-to-load-an-image-in-image-view-from-gallery
     * returns bitmap image from uri on return from gallery
     */
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
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
