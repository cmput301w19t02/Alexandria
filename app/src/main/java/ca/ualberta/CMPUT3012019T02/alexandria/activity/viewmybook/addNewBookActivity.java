package ca.ualberta.CMPUT3012019T02.alexandria.activity.viewmybook;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;

public class addNewBookActivity extends AppCompatActivity {
    private Book book;
    private String title = "";
    private String author = "";
    private String isbn = "";
    private Date date;
    private Bitmap image;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_book);
        // TODO: set information from book being edited
    }

    public boolean validateTitle(String title) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean validateAuthor(String author) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean validateISBN(String isbn) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    public void addPhoto() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    public void scanISBN() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    public void addBook() {
        book = new Book(isbn, image, title, author, date);
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }
}
