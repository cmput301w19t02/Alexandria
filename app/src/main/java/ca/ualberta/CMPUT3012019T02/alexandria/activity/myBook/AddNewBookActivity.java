package ca.ualberta.CMPUT3012019T02.alexandria.activity.myBook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ISBNLookup;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.SearchController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;
import java9.util.concurrent.CompletableFuture;

/**
 * The Add new book activity.
 */
public class AddNewBookActivity extends AppCompatActivity {
    public static final int RESULT_CAMERA = 1;
    public static final int RESULT_GALLERY = 2;
    public final int RESULT_ISBN = 3;
    public final int REQUEST_PERMISSION_PHONE_STATE = 5;

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

        ImageView addImageButton = findViewById(R.id.add_book_add_image);
        registerForContextMenu(addImageButton);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        if (image == null) {
            inflater.inflate(R.menu.select_take_image_menu, menu);
        } else {
            inflater.inflate(R.menu.select_take_delete_image_menu, menu);
        }
    }

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
                addPhoto();
                break;
            case R.id.option_delete_photo:
                // Remove phore
                // todo implement
                break;
            default:
                throw new RuntimeException("Unknown option");
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Adds photo from camera
     *
     */
    public void addPhoto() {
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
        } else if (requestCode == RESULT_ISBN && resultCode == RESULT_OK) {
            // ISBN scan
            Bundle extras = data.getExtras();
            String isbn = extras.getString("isbn");
            CompletableFuture<Book> resultFuture = SearchController.getInstance().searchIsbn(isbn);
            resultFuture.handleAsync((result, error)->{
                if (error == null) {
                    Book book = result;
                    setBook(book);
                } else{
                    showError("No result for the search of " + isbn + ".");
                }
                return null;
            });
        } else if (requestCode == RESULT_GALLERY && resultCode == RESULT_OK) {
            // Gallery look up
            // as described in https://stackoverflow.com/questions/13023788/how-to-load-an-image-in-image-view-from-gallery
            Uri selectedImage = data.getData();

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
                final Bitmap bookCover = bmp;
                ImageController.getInstance().addImage(bookCover).handleAsync((result, error) -> {
                    if (error == null) {
                        ImageView ivCover = (ImageView) findViewById(R.id.new_book_image);
                        runOnUiThread(() -> {
                            ivCover.setImageBitmap(bookCover);
                        });
                    } else {
                        showError(error.getMessage());
                    }
                    return null;
                });
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
     * Scan isbn.
     */
    public void scanISBN(View view) {
        Intent intentScan = new Intent(this, ISBNLookup.class);
        startActivityForResult(intentScan, RESULT_ISBN);
    }

    public void openGallery() {
        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intentGallery, RESULT_GALLERY);
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

    /**
     * sets title, author and ISBN as in the book provided
     *
     * @param book book info to set
     */
    private void setBook(Book book) {
        AppCompatEditText nameField = findViewById(R.id.add_book_add_title_field);
        AppCompatEditText authorField = findViewById(R.id.add_book_add_author_field);
        AppCompatEditText isbnField = findViewById(R.id.add_book_add_ISBN_field);

        nameField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        isbnField.setText(book.getIsbn());
    }


    public void showError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }
}
