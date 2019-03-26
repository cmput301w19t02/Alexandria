package ca.ualberta.CMPUT3012019T02.alexandria.activity.myBook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;

/**
 * The Edit book activity.
 */
public class EditBookActivity extends AddNewBookActivity {
    public static final String EDIT_BOOK_TITLE = "Edit Book";
    public static final String ISBN_KEY = "BOOK_ISBN";
    private Book myBook;
    private String title;
    private String author;
    private String description = "default";
    private String isbn;
    private String imageID;
    private Bitmap coverBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);

        // toolbar
        Toolbar toolbar = findViewById(R.id.add_new_book_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // remove default title

        updateUItoEdit();

        // register imageView for menu
        ImageView addImageButton = findViewById(R.id.add_book_add_image);
        registerForContextMenu(addImageButton);

        // back button
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        Bundle extras = getIntent().getExtras();
        if (extras.getString(ISBN_KEY) == null) {
            isbn = null;
            showError("The ISBN is not provided.");
            finish();
        } else {
            isbn = extras.getString("BOOK_ISBN");
            BookController.getInstance().getBook(isbn).thenAccept(bookOptional -> {
                if (bookOptional.isPresent()) {
                    myBook = bookOptional.get();
                    extractBookInfo();
                    setData();
                } else {
                    showError("The book is not found.");
                    finish();
                }
            });
        }
    }

    /**
     * confirms there is a cover in current session
     * @return
     */
    @Override
    public boolean isImageBitmap() {
        return this.coverBitmap != null;
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
    @Override
    public void removePhoto() {
        ImageView ivCover = findViewById(R.id.new_book_image);
        ivCover.setImageDrawable(getResources().getDrawable(R.drawable.ic_image));

        coverBitmap = null;
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
            coverBitmap = (Bitmap) extras.get("data");
            ImageView bookCover = findViewById(R.id.new_book_image);
            bookCover.setImageBitmap(coverBitmap);
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
     * Sets new title. Disallows isbn changes, makes colour grey. Updates save button text
     */
    private void updateUItoEdit() {
        AppCompatEditText etIsbn = findViewById(R.id.add_book_add_ISBN_field);
        etIsbn.setEnabled(false);
        etIsbn.setTextColor(getResources().getColor(R.color.colorGrey));

        ImageView ivScan = findViewById(R.id.image_location);
        ivScan.setVisibility(View.GONE);

        // Set new title
        TextView tvTitle = findViewById(R.id.add_book_title);
        tvTitle.setText(EDIT_BOOK_TITLE);

        Button saveButton = findViewById(R.id.add_book_save);
        saveButton.setText(getString(R.string.edit_book_save));
    }

    /**
     * Adds a book to the database and sets a user as one of the owners for that book. Quits.
     */
    @Override
    public void addBook(View view) {
        fetchData();
        try {
            if (coverBitmap != null) {
                saveWithImage();
            } else {
                saveBook();
            }
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            showError(errorMessage);
        }
    }

    /**
     * save book with image saving and attaching, removes old id if exists
     */
    private void saveWithImage() {
        final String imageIdToDelete = imageID;
        ImageController.getInstance().addImage(coverBitmap).handleAsync((result, error) -> {
            if (error == null) {
                imageID = result;

                runOnUiThread(() -> {
                    saveBook();
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

    /**
     * save book with current details, remove cover id if no bitmap saved
     */
    private void saveBook() {
        if (imageID != null && coverBitmap == null) {
            ImageController.getInstance().deleteImage(imageID);
            imageID = null;
        }
        Book book = new Book(isbn, title, author, description, imageID);
        BookController.getInstance().updateBook(book);
        OwnedBook myOwnedBook;
        if (coverBitmap != null) {
            myOwnedBook = new OwnedBook(isbn, imageID);
        } else {
            myOwnedBook = new OwnedBook(isbn);
        }
        BookController.getInstance().updateMyOwnedBook(myOwnedBook);

        Toast.makeText(this, "Book Information Updated", Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * Extracts book info from Book class
     */
    private void extractBookInfo() {
        title = myBook.getTitle();
        author = myBook.getAuthor();
        isbn = myBook.getIsbn();
        imageID = myBook.getImageId();
    }

    /**
     * get data from input fields
     */
    private void fetchData() {
        AppCompatEditText titleField = findViewById(R.id.add_book_add_title_field);
        AppCompatEditText authorField = findViewById(R.id.add_book_add_author_field);

        title = titleField.getText().toString();
        author = authorField.getText().toString();
    }

    /**
     * Collects data from input fields/
     */
    private void setData() {
        AppCompatEditText nameField = findViewById(R.id.add_book_add_title_field);
        AppCompatEditText authorField = findViewById(R.id.add_book_add_author_field);
        AppCompatEditText isbnField = findViewById(R.id.add_book_add_ISBN_field);

        nameField.setText(title);
        authorField.setText(author);
        isbnField.setText(isbn);
        setImage();
    }

    /**
     * set image for cover from imageId
     */
    private void setImage() {
        // sets user image
        ImageView bookCover = findViewById(R.id.new_book_image);

        ImageController imageController = ImageController.getInstance();
        imageController.getImage(imageID).handleAsync((resultImage, errorImage) -> {
            if (errorImage == null) {
                coverBitmap = resultImage;

                if (coverBitmap != null) {
                    Bitmap squareBitmap = Bitmap.createBitmap(coverBitmap, 0, 0,
                            coverBitmap.getWidth(), coverBitmap.getHeight());

                    runOnUiThread(() -> {
                        bookCover.setImageBitmap(squareBitmap);
                    });
                }
            } else {
                showError(errorImage.getMessage());
            }
            return null;
        });
    }
}