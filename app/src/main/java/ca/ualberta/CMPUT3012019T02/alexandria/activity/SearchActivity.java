package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;

public class SearchActivity {
    private List<Book> searchResult;

    public void onBookTap(String isbn){}

    public void onDataChange(String data) {
        if (data.length() >= 2){}
    }

    private void loadBookResults() {}
}
