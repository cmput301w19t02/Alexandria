package ca.ualberta.CMPUT3012019T02.alexandria.fragment.library;

import android.content.Context;
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
import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.BookListProvider;
import ca.ualberta.CMPUT3012019T02.alexandria.model.BookListItem;
import ca.ualberta.CMPUT3012019T02.alexandria.adapter.BookRecyclerViewAdapter;

/**
 * Fragment for listing all own books
 */
public class AllTabFragment extends Fragment {

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
        View rootView = inflater.inflate(R.layout.fragment_library_all_tab,null);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.all_recycler);
        bookAdapter = new BookRecyclerViewAdapter(
                getContext(), bookListings,"MyBookFragment");
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
    }

    @Override
    public void onResume() {
        super.onResume();

        // TODO: dynamic loading/updating of books
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<BookListItem> books = bookListProvider.getOwnedBookList();
                bookListings.clear();
                bookListings.addAll(books);
                bookAdapter.setmBookListItem(bookListings);
                bookAdapter.notifyDataSetChanged();
                handler.postDelayed(this, 2000);
            }
        }, 2000);
    }
}
