package ca.ualberta.CMPUT3012019T02.alexandria.activity.viewmybook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.model.book.Book;

/**
 * The Add new book activity.
 */
public class AddNewBookActivity extends AppCompatActivity {
    private Book book;
    private String title = "";
    private String author = "";
    private String isbn = "";
    private String description = "";
    private Date date;
    private String image;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_book);
        // TODO: set information from book being edited
    }

    /**
     * Validate title boolean.
     *
     * @param title the title
     * @return the validation result
     */
    public boolean validateTitle(String title) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Validate author boolean.
     *
     * @param author the author
     * @return the validation result
     */
    public boolean validateAuthor(String author) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Validate isbn boolean.
     *
     * @param isbn the isbn
     * @return the validation result
     */
    public boolean validateISBN(String isbn) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Add photo.
     */
    public void addPhoto() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Scan isbn.
     */
    public void scanISBN() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Add book.
     */
    public void addBook() {
        book = new Book(isbn, image, title, author, description, date);
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }
}
