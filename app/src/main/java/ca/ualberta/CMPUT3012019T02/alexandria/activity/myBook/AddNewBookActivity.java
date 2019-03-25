package ca.ualberta.CMPUT3012019T02.alexandria.activity.myBook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;

/**
 * The Add new book activity.
 */
public class AddNewBookActivity extends AppCompatActivity {
    public static final int CAMERA_CODE = 1;
    private final int REQUEST_PERMISSION_PHONE_STATE = 5;

    private Book book;
    private String title = "";
    private String author = "";
    private String isbn = "";
    private String description = "";
    private Date date;
    private String image;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);

        // toolbar
        Toolbar toolbar = findViewById(R.id.add_new_book_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // remove default title

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * Adds photo from camera
     *
     * @param view the add photo button
     */
    public void addPhoto(View view) {
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
            startActivityForResult(takePictureIntent, CAMERA_CODE);
        }
    }

    /**
     * Saves camera result to database and loads it to the adding page
     *
     * @param requestCode result code
     * @param resultCode confirmation code
     * @param data output of camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CAMERA_CODE) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            ImageController.getInstance().addImage(bitmap).handleAsync((result, error) -> {
                if (error == null) {
                    image = result;

                    Bitmap squareBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight());

                    ImageView bookCover = findViewById(R.id.new_book_image);
                    runOnUiThread(() -> {
                        bookCover.setImageBitmap(squareBitmap);
                    });
                } else {
                    showError(error.getMessage());
                }
                return null;
            });
        }
    }

    /**
     * Scan isbn.
     */
    public void scanISBN(View view) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Adds a book to the database and sets a user as one of the owners for that book. Quits.
     */
    public void addBook(View view) {
        fetchData();
        try {
            book = new Book(isbn, image, title, author, description, date);
            BookController.getInstance().addBook(book);
            OwnedBook myOwnedBook = new OwnedBook(isbn);
            BookController.getInstance().addMyOwnedBook(myOwnedBook);

            Toast.makeText(this , "Book Added", Toast.LENGTH_LONG).show();
            finish();
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            showError(errorMessage);
        }
    }

    /**
     * Collects data from input fields/
     */
    private void fetchData() {
        AppCompatEditText titleField = findViewById(R.id.add_book_add_title_field);
        AppCompatEditText authorField = findViewById(R.id.add_book_add_author_field);
        AppCompatEditText isbnField = findViewById(R.id.add_book_add_ISBN_field);
        date = null;
        description = "default";

        title = titleField.getText().toString();
        author = authorField.getText().toString();
        isbn = isbnField.getText().toString();
    }

    public void showError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }
}
