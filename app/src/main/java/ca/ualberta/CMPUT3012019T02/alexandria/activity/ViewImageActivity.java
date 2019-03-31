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

public class ViewImageActivity extends AppCompatActivity {

    public static final String IMAGE_ID = "IMAGE_ID";
    private String image_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        set_toolbar();
        load_imageID();
    }

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
     *
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

    public void showError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }
}
