package ca.ualberta.CMPUT3012019T02.alexandria.fragment.exchange;

import android.content.Context;
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
import ca.ualberta.CMPUT3012019T02.alexandria.activity.BookListProvider;
import ca.ualberta.CMPUT3012019T02.alexandria.adapter.BookRecyclerViewAdapter;
import ca.ualberta.CMPUT3012019T02.alexandria.model.BookListItem;

/**
 * Fragment for filtering book list that has the status of Accepted
 */

public class AcceptedFragment extends Fragment {

    private List<BookListItem> bookListings = new ArrayList<>();
    private BookRecyclerViewAdapter bookAdapter;
    private BookListProvider bookListProvider;

    /**
     * Sets up the RecyclerView for the Fragment
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_exchange_accepted, null);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.accepted_recycler);
        bookAdapter = new BookRecyclerViewAdapter(
                getContext(), bookListings, "UserBookFragment");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(bookAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.bookListProvider = (BookListProvider) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BookListProvider");
        }
        if (bookAdapter != null) {
            update();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    public void update() {
        List<BookListItem> books = bookListProvider.getBorrowedBookList();
        bookListings.clear();
        for (BookListItem book : books) {
            if (book.getStatus().equals("accepted")) {
                bookListings.add(book);
            }
        }
        bookAdapter.setmBookListItem(bookListings);
        bookAdapter.notifyDataSetChanged();
    }
}




