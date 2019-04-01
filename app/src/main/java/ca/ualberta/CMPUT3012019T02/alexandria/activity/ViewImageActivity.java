package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;

/**
 * Activity to display an image in full screen
 */
public class ViewImageActivity extends AppCompatActivity {

    /**
     * The bundle constant IMAGE_ID.
     */
    public static final String IMAGE_ID = "IMAGE_ID";
    private String image_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        set_toolbar();
        load_imageID();
    }

    /**
     * get image id from extras
     */
    private void load_imageID() {
        Bundle extras = getIntent().getExtras();
        if (extras.getString(IMAGE_ID) == null) {
            image_id = null;
            showError("Image is not found.");
            finish();
        } else {
            image_id = extras.getString(IMAGE_ID);
            setImage();
        }
    }

    /**
     * set toolbar to activity
     */
    private void set_toolbar() {
        // toolbar
        Toolbar toolbar = findViewById(R.id.view_image_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // remove default title

        // back button
        Drawable backIcon = getResources().getDrawable(R.drawable.ic_arrow_back);
        backIcon.setColorFilter(getResources().getColor(R.color.colorWhite),
                PorterDuff.Mode.SRC_IN);

        toolbar.setNavigationIcon(backIcon);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * set image that is being extended from database
     */
    private void setImage() {
        ImageView imageExpand = findViewById(R.id.view_image_expand);

        ImageController imageController = ImageController.getInstance();
        imageController.getImage(image_id).handleAsync((resultImage, errorImage) -> {
            if (errorImage == null) {
                Bitmap bitmap = resultImage;

                if (bitmap != null) {
                    Bitmap squareBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight());

                    runOnUiThread(() -> {
                        imageExpand.setImageBitmap(squareBitmap);
                    });
                }
            } else {
                showError(errorImage.getMessage());
            }
            return null;
        });
    }

    /**
     * show error message on a screen
     * @param message error message
     */
    public void showError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }
}
