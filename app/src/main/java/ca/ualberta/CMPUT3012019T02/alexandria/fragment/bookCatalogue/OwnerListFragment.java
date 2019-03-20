package ca.ualberta.CMPUT3012019T02.alexandria.fragment.bookCatalogue;

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
import ca.ualberta.CMPUT3012019T02.alexandria.adapter.OwnerRecyclerViewAdapter;
import ca.ualberta.CMPUT3012019T02.alexandria.model.OwnerListItem;

public class OwnerListFragment extends Fragment {

    private List<OwnerListItem> owner;
    private String author;
    private String isbn;
    private String title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_book_catalogue_owners, null);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.book_catalogue_recycler);
        OwnerRecyclerViewAdapter userAdapter = new OwnerRecyclerViewAdapter(getContext(),owner);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(userAdapter);

        return rootView;
    }

    public void dataGrab(String title, String author, String isbn){
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    //Temp list
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO set up proper back end
        owner = new ArrayList<>();
        Bitmap aBitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888);

        owner.add(new OwnerListItem(aBitmap, "Fake User 4", "owner Id", isbn, "requested", title, author));
        owner.add(new OwnerListItem(aBitmap, "Fake User 4", "owner Id", isbn, "requested", title, author));
        owner.add(new OwnerListItem(aBitmap, "Fake User 4", "owner Id", isbn, "available", title, author));
        owner.add(new OwnerListItem(aBitmap, "Fake User 4", "owner Id", isbn, "available", title, author));

    }
}
