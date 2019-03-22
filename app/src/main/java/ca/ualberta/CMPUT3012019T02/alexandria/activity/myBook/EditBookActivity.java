package ca.ualberta.CMPUT3012019T02.alexandria.activity.myBook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import java9.util.function.Consumer;

/**
 * The Edit book activity.
 */
public class EditBookActivity extends AddNewBookActivity {
    private Book myBook;
    private String title;
    private String author;
    private String isbn;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);

        // toolbar
        Toolbar toolbar = findViewById(R.id.add_new_book_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // remove default title

        hideIsbn();

        // Set new title
        TextView tvTitle = findViewById(R.id.add_book_title);
        String pageTitle = "Edit Book";
        tvTitle.setText(pageTitle);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        Bundle extras = getIntent().getExtras();
        if (extras.getString("BOOK_ISBN") == null) {
            isbn = null;
            showError("The ISBN is not provided.");
            finish();
        } else {
            isbn = extras.getString("BOOK_ISBN");
            BookController.getInstance().getBook(isbn).thenAccept(new Consumer<Book>() {
                @Override
                public void accept(Book book) {
                    myBook = book;
                    if (myBook == null) {
                        showError("The book is not found.");
                        finish();
                    }
                    extractBookInfo();
                    setData();
                }
            });
        }
    }

    /**
     * hides isbn input from the page, as it cannot be changed in
     * a pre-existent book.
     */
    private void hideIsbn() {
        TextInputLayout ilIsbn = findViewById(R.id.add_book_add_ISBN_layout);
        ilIsbn.setEnabled(false);

        ImageView ivScan = findViewById(R.id.image_location);
        ivScan.setVisibility(View.GONE);
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
        if (resultCode == RESULT_OK && requestCode == 1) {
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
     * updates author, title and cover information for the selected book
     *
     * @param view current view
     */
    @Override
    public void addBook(View view) {
        fetchData();
        try {
            myBook.setAuthor(author);
            myBook.setTitle(title);
            myBook.setImageId(image);

            BookController.getInstance().updateBook(myBook);

            Toast.makeText(this, "Book Information Changed", Toast.LENGTH_LONG).show();
            finish();
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            showError(errorMessage);
        }
    }

    /**
     * Extracts book info from Book class
     */
    private void extractBookInfo() {
        title = myBook.getTitle();
        author = myBook.getAuthor();
        isbn = myBook.getIsbn();
        image = myBook.getImageId();
    }

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

    private void setImage() {
        // sets user image
        ImageView bookCover = findViewById(R.id.new_book_image);

        ImageController imageController = ImageController.getInstance();
        imageController.getImage(image).handleAsync((resultImage, errorImage) -> {
            if (errorImage == null) {
                Bitmap bitmap = resultImage;

                if (bitmap != null) {
                    Bitmap squareBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight());

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