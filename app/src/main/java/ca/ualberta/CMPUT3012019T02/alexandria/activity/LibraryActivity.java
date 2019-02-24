package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

//TODO MOVE METHODS TO LIBRARY FRAGMENT
public class LibraryActivity extends AppCompatActivity {

    private enum Page {ALL, AVAILABLE, LOANED}
    private Page currentPage = Page.ALL;


    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add in activity_library
//        setContentView(R.layout.activity_library);
        filterPage(currentPage);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void filterPage(Page currentPage) {
    }

    public void addBook(View view) {
        // TODO: Implement
        throw new UnsupportedOperationException("Not implemented");
    }

    public void filterAll() {
        if (currentPage != Page.ALL) {
            filterPage(Page.ALL);
        }
    }

    public void filterAvailable() {
        if (currentPage != Page.AVAILABLE) {
            filterPage(Page.AVAILABLE);
        }
    }

    public void filterLoaned() {
        if (currentPage != Page.LOANED) {
            filterPage(Page.LOANED);
        }
    }

    public void addNotification() {
        // TODO: Implement
        throw new UnsupportedOperationException("Not implemented");
    }


}
