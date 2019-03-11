package ca.ualberta.CMPUT3012019T02.alexandria.fragment.exchange;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookParser;
import ca.ualberta.CMPUT3012019T02.alexandria.model.BookList;
import ca.ualberta.CMPUT3012019T02.alexandria.model.BookRecyclerViewAdapter;

/**
 * Fragment for filtering book list that has the status of Requested
 */

public class RequestedFragment extends Fragment {

    private List<BookList> bookListings = new ArrayList<>();
    private BookRecyclerViewAdapter bookAdapter;

    /**
     * Sets up the RecyclerView for the Fragment
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_exchange_accepted,null);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.accepted_recycler);
        bookAdapter = new BookRecyclerViewAdapter(
                getContext(), bookListings,"UserBookFragment");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(bookAdapter);

        return rootView;
    }

    /**
     * Temporary creation of lists until the firebase connection is made
     * {@inheritDoc}
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BookController bookController = BookController.getInstance();
        bookController.getMyBorrowedBooks().thenAcceptAsync(stringBorrowedBookHashMap -> {
            try {

                // Gets accepted book list items
                List<BookList> bookLists = BookParser.UserBooksToBookList(stringBorrowedBookHashMap).get(5, TimeUnit.SECONDS);
                bookListings.clear();
                for (BookList bookList : bookLists) {
                    if (bookList.getStatus().equals("requested")) {
                        bookListings.add(bookList);
                    }
                }

                // Sort by alphabetical order of book titles
                Collections.sort(bookListings, (o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getTitle(), o2.getTitle()));

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bookAdapter.setmBookList(bookListings);
                bookAdapter.notifyDataSetChanged();
                handler.postDelayed(this, 2000);
            }
        }, 2000);

    }

}


