package ca.ualberta.CMPUT3012019T02.alexandria.activity.viewmybook;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import java9.util.function.Consumer;

/**
 * The Edit book activity.
 */
public class EditBookActivity extends AddNewBookActivity {
    private Book myBook;
    private String title ;
    private String author;
    private String isbn;
    private String description;
    private Date date;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);
        // TODO: set information from book being edited

        TextView tvTitle = findViewById(R.id.add_book_title);
        String pageTitle = "Edit Book";
        tvTitle.setText(pageTitle);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            // quit
            isbn = null;
            Toast.makeText(this , "ISBN is incorrect", Toast.LENGTH_LONG).show();
            finish();
        } else {
            isbn = extras.getString("BOOK_ISBN");
            BookController.getInstance().getBook(isbn).thenAccept(new Consumer<Book>() {
                @Override
                public void accept(Book book) {
                    myBook = book;
                    extractBookInfo();
                    setData();
                }
            });
        }

        //runOnUiThread(() -> {

        //});
    }

    @Override
    public void addBook(View view) {
        Book newBook = new Book(isbn, image, title, author, description, date);
        // TODO: implement
        // make sure the old book is deleted from the user profile
        Toast.makeText(this , "Book Information Changed", Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * Delete book.
     */
    public void deleteBook() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    private void extractBookInfo() {
        title = myBook.getTitle();
        author = myBook.getAuthor();
        isbn = myBook.getIsbn();
        description = myBook.getDescription();
        date = myBook.getDate();
        image = myBook.getImageId();
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
    }
}
