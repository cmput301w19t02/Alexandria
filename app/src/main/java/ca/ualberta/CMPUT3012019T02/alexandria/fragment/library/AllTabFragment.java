package ca.ualberta.CMPUT3012019T02.alexandria.fragment.library;

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
 * Fragment for listing all own books
 */
public class AllTabFragment extends Fragment {

    private List<BookList> allMyBooks;

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
        BookRecyclerViewAdapter bookAdapter = new BookRecyclerViewAdapter(
                getContext(), allMyBooks,"UserBookFragment");
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
        allMyBooks = new ArrayList<>();
        allMyBooks.add(new BookList
                (aBitmap, "Test Title",
                        "Test Author", "Test ISBN", "requested", "ZvLVXXLOoWTZ7o6xmW6fT4PP0Wj1"));
        allMyBooks.add(new BookList
                (aBitmap, "Test Title 2",
                        "Test Author", "Test ISBN", "accepted","ZvLVXXLOoWTZ7o6xmW6fT4PP0Wj1"));
        allMyBooks.add(new BookList
                (aBitmap, "Test Title 3",
                        "Test Author", "Test ISBN", "borrowed", "ZvLVXXLOoWTZ7o6xmW6fT4PP0Wj1"));
        allMyBooks.add(new BookList
                (aBitmap, "Test Title 4",
                        "Test Author", "Test ISBN", "available", "ZvLVXXLOoWTZ7o6xmW6fT4PP0Wj1"));

    }
}
