package ca.ualberta.CMPUT3012019T02.alexandria.activity.viewmybook;

import android.os.Bundle;
import android.view.View;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;

/**
 * The Edit book activity.
 */
public class EditBookActivity extends AddNewBookActivity {
    private Book book;
    private String title ;
    private String author;
    private String isbn;
    private String description;
    private Date date;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_book);
        // TODO: set information from book being edited
    }

    @Override
    public void addBook(View view) {
        book = new Book(isbn, image, title, author, description, date);
        // TODO: implement
        // make sure the old book is deleted from the user profile
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Delete book.
     */
    public void deleteBook() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }
}
