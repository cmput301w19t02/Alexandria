package ca.ualberta.CMPUT3012019T02.alexandria.fragment.exchange;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.model.BookList;
import ca.ualberta.CMPUT3012019T02.alexandria.adapter.BookRecyclerViewAdapter;

/**
 * Fragment for filtering book list that has the status of Accepted
 */

public class AcceptedFragment extends Fragment {

    private List<BookList> acceptedBooks;

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
        BookRecyclerViewAdapter bookAdapter = new BookRecyclerViewAdapter(
                getContext(), acceptedBooks,"UserBookFragment");
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

        //TODO setup data retrieval from Firebase, and remove placeholder lists
        Bitmap aBitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888);
        acceptedBooks = new ArrayList<>();
        acceptedBooks.add(new BookList
                (aBitmap, "Test Title",
                        "Test Author", "Test ISBN", "accepted", "AQiv4J6BTsX5kYHgLChH7xFlir02"));
        acceptedBooks.add(new BookList
                (aBitmap, "Test Title 2",
                        "Test Author", "Test ISBN", "accepted","ZvLVXXLOoWTZ7o6xmW6fT4PP0Wj1"));
        acceptedBooks.add(new BookList
                (aBitmap, "Test Title 3",
                        "Test Author", "Test ISBN", "accepted", "bJyo1fjcj0aYJejnJSfz6tugpca2"));
        acceptedBooks.add(new BookList
                (aBitmap, "Test Title 4",
                        "Test Author", "Test ISBN", "accepted", "eQgZfhN2Yng9TPHcXvfBZs5ZKxj1"));
        acceptedBooks.add(new BookList
                (aBitmap, "Test Title 5",
                        "Test Author", "Test ISBN", "accepted", "tfTIZpx1Y2QwnhBwJeODmem0Mhk2"));

    }
}
