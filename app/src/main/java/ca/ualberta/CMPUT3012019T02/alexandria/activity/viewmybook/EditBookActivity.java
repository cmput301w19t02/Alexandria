package ca.ualberta.CMPUT3012019T02.alexandria.activity.viewmybook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;
import java9.util.function.Consumer;

/**
 * The Edit book activity.
 */
public class EditBookActivity extends AddNewBookActivity {
    private Book myBook;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private Date date;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);

        // toolbar
        Toolbar toolbar = findViewById(R.id.add_new_book_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // remove default title

        // Set new title
        TextView tvTitle = findViewById(R.id.add_book_title);
        String pageTitle = "Edit Book";
        tvTitle.setText(pageTitle);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
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
     * deletes an instance of old book, and adds a new one, if inputs is valid
     *
     * @param view current view
     */
    @Override
    public void addBook(View view) {
        fetchData();
        try {
            Book newBook = new Book(isbn, image, title, author, description, date);
            OwnedBook newOwnedBook = new OwnedBook(isbn);

            BookController.getInstance().deleteMyOwnedBook(myBook.getIsbn());
            BookController.getInstance().deleteBook(myBook.getIsbn());

            BookController.getInstance().addBook(newBook);
            BookController.getInstance().addMyOwnedBook(newOwnedBook);

            Toast.makeText(this, "Book Information Changed", Toast.LENGTH_LONG).show();
            finish();
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            showError(errorMessage);
        }
    }

    /**
     * Delete book.
     */
    public void deleteBook() {
        // TODO: implement
        BookController.getInstance().deleteMyOwnedBook(myBook.getIsbn());
        BookController.getInstance().deleteBook(myBook.getIsbn());
    }

    /**
     * Extracts book info from Book class
     */
    private void extractBookInfo() {
        title = myBook.getTitle();
        author = myBook.getAuthor();
        isbn = myBook.getIsbn();
        description = myBook.getDescription();
        date = myBook.getDate();
        image = myBook.getImageId();
    }

    private void fetchData() {
        AppCompatEditText titleField = findViewById(R.id.add_book_add_title_field);
        AppCompatEditText authorField = findViewById(R.id.add_book_add_author_field);
        AppCompatEditText isbnField = findViewById(R.id.add_book_add_ISBN_field);
        //TODO add image input, date input, description input
        image = null;
        date = null;
        description = "default";

        title = titleField.getText().toString();
        author = authorField.getText().toString();
        isbn = isbnField.getText().toString();
    }

    /**
     * Collects data from input fields/
     */
    private void setData() {
        AppCompatEditText nameField = findViewById(R.id.add_book_add_title_field);
        AppCompatEditText authorField = findViewById(R.id.add_book_add_author_field);
        AppCompatEditText isbnField = findViewById(R.id.add_book_add_ISBN_field);
        //TODO image input, date input, description input

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