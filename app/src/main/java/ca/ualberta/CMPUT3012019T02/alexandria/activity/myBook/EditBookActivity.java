package ca.ualberta.CMPUT3012019T02.alexandria.activity.myBook;

import android.app.ProgressDialog;
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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;
import java9.util.Optional;

import static ca.ualberta.CMPUT3012019T02.alexandria.App.getContext;

/**
 * Takes isbn of a book, and allows editing its title, author and image
 */
public class EditBookActivity extends AddNewBookActivity {

    private static final String ISBN_KEY = "BOOK_ISBN";

    private Book myBook;
    private OwnedBook myOwnedBook;
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
        addImageButton.setOnClickListener(this::setPopupMenu);

        // back button
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        // get ISBN number of the book being edited
        Bundle extras = getIntent().getExtras();
        if (extras.getString(ISBN_KEY) == null) {
            isbn = null;
            showError("The ISBN is not provided.");
            finish();
        } else {

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            isbn = extras.getString("BOOK_ISBN");
            new Thread(() -> {
                Optional<Book> bookOptional = null;
                try {
                    myBook = BookController.getInstance().getBook(isbn)
                            .get(5, TimeUnit.SECONDS).get();
                    myOwnedBook = BookController.getInstance().getMyOwnedBook(isbn)
                            .get(5, TimeUnit.SECONDS).get();
                    runOnUiThread(() -> {
                        extractBookInfo();
                        setData();
                        progressDialog.dismiss();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        showError("Book not found");
                        progressDialog.dismiss();
                        finish();
                    });
                }
            }).start();

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
     * Creates popup menu for adding photos
     * @param v view
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
     * remove bitmap photo, image id still connected unless saved
     */
    @Override
    public void removePhoto() {
        ImageView ivCover = findViewById(R.id.new_book_image);
        ivCover.setImageDrawable(getResources().getDrawable(R.drawable.ic_image));

        coverBitmap = null;
    }

    /**
     * on return to activity with result, deals with camera, gallery
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
        // preset ISBN
        AppCompatEditText etIsbn = findViewById(R.id.add_book_add_ISBN_field);
        etIsbn.setEnabled(false);
        etIsbn.setTextColor(getResources().getColor(R.color.colorGrey));

        // remove scan button
        ImageView ivScan = findViewById(R.id.image_location);
        ivScan.setVisibility(View.GONE);

        // Set new title
        TextView tvTitle = findViewById(R.id.add_book_title);
        tvTitle.setText(R.string.edit_book);

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
     * save book with image saving and attaching, removes old image of the cover if exists
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

        // author and title check
        if (author.trim().isEmpty()) {
            showError("Author can not be empty");
            return;
        }

        if (title.trim().isEmpty()) {
            showError("Title can not be empty");
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving changes...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(() -> {

            // update imageID

            if (coverBitmap != null) {
                try {
                    imageID = ImageController.getInstance().addImage(coverBitmap)
                            .get(5, TimeUnit.SECONDS);
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

            myBook = new Book(isbn, title, author, description, myBook.getImageId());
            myOwnedBook = new OwnedBook(isbn, imageID);

            // update book info

            BookController bookController = BookController.getInstance();
            try {
                bookController.updateBook(myBook).get(5, TimeUnit.SECONDS);
                bookController.updateMyOwnedBook(myOwnedBook).get(5, TimeUnit.SECONDS);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Book Information Updated",
                            Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    finish();
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    showError("An error occurred while saving changes");
                    progressDialog.dismiss();
                });
            }

        }).start();
    }

    /**
     * Extracts book info from Book class
     */
    private void extractBookInfo() {
        title = myBook.getTitle();
        author = myBook.getAuthor();
        isbn = myBook.getIsbn();
        imageID = myOwnedBook.getImageId();
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
        if (imageID == null) {
            return;
        }

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