package ca.ualberta.CMPUT3012019T02.alexandria.activity.myBook;

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
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ISBNLookup;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.SearchController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;
import java9.util.Optional;

import static ca.ualberta.CMPUT3012019T02.alexandria.App.getContext;

/**
 * Activity to add an instance of a book with title, author, and ISBN, optionally image,
 * to the database
 */
public class AddNewBookActivity extends AppCompatActivity {

    /**
     * The RESULT_CAMERA intent constant.
     */
    public static final int RESULT_CAMERA = 1;
    /**
     * The RESULT_GALLERY intent constant.
     */
    public static final int RESULT_GALLERY = 2;
    /**
     * The Result isbn.
     */
    public final int RESULT_ISBN = 3;
    /**
     * The Request permission phone state.
     */
    public final int REQUEST_PERMISSION_PHONE_STATE = 5;

    private Book book;
    private Book searchedBook;
    private String title = "";
    private String author = "";
    private String isbn = "";
    private String description = "";
    private String imageID;
    private Bitmap coverBitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);

        // toolbar
        Toolbar toolbar = findViewById(R.id.add_new_book_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // remove default title

        // back button
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        // register imageView for creating menu of image input
        ImageView addImageButton = findViewById(R.id.add_book_add_image);
        addImageButton.setOnClickListener(this::setPopupMenu);
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
     * confirms there is a cover in current session
     *
     * @return boolean
     */
    public boolean isImageBitmap() {
        return this.coverBitmap != null;
    }

    /**
     * remove bitmap photo, image id still connected unless saved
     */
    public void removePhoto() {
        ImageView ivCover = findViewById(R.id.new_book_image);
        ivCover.setImageDrawable(getResources().getDrawable(R.drawable.ic_image));

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
     * on return to activity with result, deals with camera, scan, gallery
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
            coverBitmap = (Bitmap) extras.get("data");
            ImageView bookCover = findViewById(R.id.new_book_image);
            bookCover.setImageBitmap(coverBitmap);

        } else if (requestCode == RESULT_ISBN && resultCode == RESULT_OK) {

            // ISBN scan
            Bundle extras = data.getExtras();
            String isbn = extras.getString("isbn");

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Fetching book data...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            SearchController.getInstance().searchIsbn(isbn)
                    .handleAsync((searchedBook, throwable) -> {
                this.searchedBook = searchedBook;
                try {
                    coverBitmap = ImageController.getInstance().getImage(searchedBook.getImageId())
                            .get(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> {
                    if (throwable == null) {
                        setBook(searchedBook);
                        if (coverBitmap != null) {
                            ImageView bookCover = findViewById(R.id.new_book_image);
                            bookCover.setImageBitmap(coverBitmap);
                        }
                    } else {
                        throwable.printStackTrace();
                        showError("Failed to scan ISBN");
                    }
                    progressDialog.dismiss();
                });
                return null;
            });

        } else if (requestCode == RESULT_GALLERY && resultCode == RESULT_OK) {

            // Gallery look up
            Uri selectedImage = data.getData();

            try {
                coverBitmap = getBitmapFromUri(selectedImage);
                ImageView ivCover = findViewById(R.id.new_book_image);
                ivCover.setImageBitmap(coverBitmap);
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

    /**
     * opens up a camera for isbn scanning from a book
     */
    public void scanISBN(View view) {
        Intent intentScan = new Intent(this, ISBNLookup.class);
        startActivityForResult(intentScan, RESULT_ISBN);
    }

    /**
     * opens phone files to choose image from the gallery
     */
    public void openGallery() {
        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intentGallery, RESULT_GALLERY);
    }

    /**
     * Adds a book to the database from the input obtained from the user
     * and sets a user as one of the owners for that book. Quits.
     */
    public void addBook(View view) {
        fetchData();
        saveBook();
    }

    /**
     * save book with current details
     */
    private void saveBook() {

        // ISBN, title, author check
        if (isbn.length() != 10 && isbn.length() != 13) {
            showError("Invalid ISBN");
            return;
        }

        if (title.trim().isEmpty()) {
            showError("Title can not be empty");
            return;
        }

        if (author.trim().isEmpty()) {
            showError("Author can not be empty");
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding book...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(() -> {

            // if the user provided a bitmap, use it

            if (coverBitmap != null) {
                try {
                    imageID = ImageController.getInstance()
                            .addImage(coverBitmap).get(5, TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        showError("An error occurred while saving image");
                        progressDialog.dismiss();
                    });
                    return;
                }
            } else {
                imageID = null;
            }

            BookController bookController = BookController.getInstance();
            Optional<Book> bookOptional = Optional.empty();
            try {
                bookOptional = bookController.getBook(isbn).get(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (bookOptional.isPresent()) {

                // if the book already exists, just update the information
                book = new Book(isbn, title, author, description, bookOptional.get().getImageId());

                try {
                    bookController.updateBook(book).get(5, TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        showError("An error occurred while adding book");
                        progressDialog.dismiss();
                    });
                    return;
                }

            } else {

                book = new Book(isbn, title, author, description, null);

                // try to get the image for this book from google
                if (searchedBook == null) {
                    try {
                        searchedBook = SearchController.getInstance()
                                .searchIsbn(isbn).get(5, TimeUnit.SECONDS);
                        book = new Book(isbn, title, author, description,
                                searchedBook.getImageId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    book = new Book(isbn, title, author, description, searchedBook.getImageId());
                }

                // add a new book

                try {
                    bookController.addBook(book).get(5, TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        showError("An error occurred while adding book");
                        progressDialog.dismiss();
                    });
                    return;
                }

            }

            if (author.trim().isEmpty()) {
                runOnUiThread(() -> {
                    showError("Author can not be empty");
                    progressDialog.dismiss();
                });
                return;
            }

            if (title.trim().isEmpty()) {
                runOnUiThread(() -> {
                    showError("Title can not be empty");
                    progressDialog.dismiss();
                });
                return;
            }

            // Add owned book

            OwnedBook myOwnedBook = new OwnedBook(isbn, imageID);
            try {
                bookController.addMyOwnedBook(myOwnedBook).get(5, TimeUnit.SECONDS);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Book Added", Toast.LENGTH_LONG).show();
                    finish();
                });
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    showError("You already own this book");
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    showError("An error occurred while adding book");
                });
            }

            progressDialog.dismiss();

        }).start();
    }

    /**
     * Collects data from input fields
     */
    private void fetchData() {
        AppCompatEditText titleField = findViewById(R.id.add_book_add_title_field);
        AppCompatEditText authorField = findViewById(R.id.add_book_add_author_field);
        AppCompatEditText isbnField = findViewById(R.id.add_book_add_ISBN_field);
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

    /**
     * show error in toast
     *
     * @param message error message
     */
    public void showError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }
}
