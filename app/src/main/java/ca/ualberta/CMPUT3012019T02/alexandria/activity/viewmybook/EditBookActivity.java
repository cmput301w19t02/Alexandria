package ca.ualberta.CMPUT3012019T02.alexandria.activity.viewmybook;

import android.graphics.Bitmap;
import android.os.Bundle;

import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;

public class EditBookActivity extends AddNewBookActivity {
    private Book book;
    private String title ;
    private String author;
    private String isbn;
    private Date date;
    private String imageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_book);
    }

    @Override
    public void addBook() {
        book = new Book(isbn, imageId, title, author, date);
        // TODO: implement
        // make sure the old book is deleted from the user profile
        throw new UnsupportedOperationException("Not implemented");
    }

    public void deleteBook() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }
}
