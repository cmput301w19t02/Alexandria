package ca.ualberta.CMPUT3012019T02.alexandria.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.adapter.BookRecyclerViewAdapter;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.SearchController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import ca.ualberta.CMPUT3012019T02.alexandria.model.BookList;
import java9.util.concurrent.CompletableFuture;

public class SearchFragment extends Fragment {

    private ArrayList<BookList> searchBooks = new ArrayList<BookList>();
    private EditText searchText;
    private CompletableFuture<ArrayList<Book>> results;

    private final SearchController searchController = SearchController.getInstance();
    private final ImageController imageController = ImageController.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search,null);
        RecyclerView mRecyclerView = rootView.findViewById(R.id.search_recycler);

        BookRecyclerViewAdapter bookAdapter =
                new BookRecyclerViewAdapter(getContext(), searchBooks,"UserBookFragment");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(bookAdapter);

        searchText = (EditText) rootView.findViewById(R.id.search_input);

        searchText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    // search firebase for the string
                    try {
                        results = searchController.searchBooks(s.toString());
                        ArrayList<Book> books = results.get();
                        for (int i = 0; i < books.size(); i++) {
                            Book book = books.get(i);

                            CompletableFuture<Bitmap> imageResult = imageController.getImage(book.getImageId());

                            Bitmap image = imageResult.get();

                            searchBooks.add(new BookList
                                    (image, book.getTitle(), book.getAuthor(),
                                            book.getIsbn()));
                        }
                    } catch (ExecutionException e) {
                        System.out.println("Error: failed to execute get in search: " + e);
                    } catch (InterruptedException e) {
                        System.out.println("Error: interrupted get in search: " + e);
                    }
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        return rootView;
    }
}
