package ca.ualberta.CMPUT3012019T02.alexandria.fragment;

// used in this .java: https://stackoverflow.com/questions/10508363/show-keyboard-for-edittext-when-fragment-starts

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.adapter.BookRecyclerViewAdapter;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.SearchController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import ca.ualberta.CMPUT3012019T02.alexandria.model.BookListItem;
import java9.util.concurrent.CompletableFuture;

/**
 * The type Search fragment.
 */
public class SearchFragment extends Fragment {

    private List<BookListItem> searchBooks = new ArrayList<BookListItem>();
    private EditText searchText;
    private CompletableFuture<ArrayList<Book>> results;
    private BookRecyclerViewAdapter bookAdapter;

    private final SearchController searchController = SearchController.getInstance();
    private final ImageController imageController = ImageController.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, null);
        RecyclerView mRecyclerView = rootView.findViewById(R.id.search_recycler);

        bookAdapter = new BookRecyclerViewAdapter(getContext(), searchBooks, "BookCatalogueFragment");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(bookAdapter);

        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        // toolbar
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        // remove default title
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener((View v) -> {
            imgr.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
            getFragmentManager().popBackStack();
        });

        searchText = rootView.findViewById(R.id.search_input);
        searchText.requestFocus();

        //sets the scroll behaviour for screen, removes navigation bar from bottom
        //absolutely no easy way for me to do this, spent a good time trying to get an alternative
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getActivity().findViewById(R.id.navigation).setVisibility(View.GONE);

        searchText.addTextChangedListener(new TextWatcher() {

            /**
             *
             * @param s the text in the search field
             */
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    // search firebase for the string
                    results = searchController.searchBooks(s.toString());
                    results.handleAsync((books, error) -> {
                        if (error == null) {
                            for (int i = 0; i < books.size(); i++) {
                                Book book = books.get(i);

                                CompletableFuture<Bitmap> imageResult = imageController.getImage(book.getImageId());

                                imageResult.handleAsync((image, imageError)-> {
                                    if (imageError == null) {
                                        searchBooks.add(new BookListItem(
                                                image,
                                                book.getTitle(),
                                                book.getAuthor(),
                                                book.getIsbn())
                                        );
                                        bookAdapter.setmBookListItem(searchBooks);
                                    } else {
                                        imageError.printStackTrace();
                                    }
                                    return null;
                                });
                            }
                        } else {
                            error.printStackTrace();
                        }
                        return null;
                    });
                    bookAdapter.notifyDataSetChanged();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchBooks.clear();
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bookAdapter.setmBookListItem(searchBooks);
                bookAdapter.notifyDataSetChanged();
                handler.postDelayed(this, 500);
            }
        }, 500);
    }

    @Override
    public void onPause() {
        super.onPause();

        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
