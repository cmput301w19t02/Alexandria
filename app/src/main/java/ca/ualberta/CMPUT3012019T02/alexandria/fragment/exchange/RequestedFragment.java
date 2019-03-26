package ca.ualberta.CMPUT3012019T02.alexandria.fragment.exchange;

import android.app.Activity;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.adapter.BookDataAdapter;
import ca.ualberta.CMPUT3012019T02.alexandria.adapter.BookRecyclerViewAdapter;
import ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.BookListItem;

/**
 * Fragment for filtering book list that has the status of Requested
 */

public class RequestedFragment extends Fragment implements Observer {

    private List<BookListItem> bookListings = new ArrayList<>();
    private BookRecyclerViewAdapter bookAdapter;
    private Activity activity;
    private View view;

    /**
     * Sets up the RecyclerView for the Fragment
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_exchange_requested,null);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.requested_recycler);
        bookAdapter = new BookRecyclerViewAdapter(
                getContext(), bookListings,"UserBookFragment");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(bookAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BookDataAdapter.getInstance().addObserver(this);
        update(null, null);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(() -> {
            bookListings.clear();
            for (BookListItem book : BookDataAdapter.getInstance().getMyBorrowedBooksList()) {
                if (book.getStatus().equals("requested")) {
                    bookListings.add(book);
                }
            }
            bookAdapter.notifyDataSetChanged();

            RecyclerView mRecyclerView = view.findViewById(R.id.requested_recycler);
            TextView emptyView = view.findViewById(R.id.empty_view);
            if (mRecyclerView != null && emptyView != null) {
                if (bookListings.isEmpty() ) {
                    mRecyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }
        });
    }
}


