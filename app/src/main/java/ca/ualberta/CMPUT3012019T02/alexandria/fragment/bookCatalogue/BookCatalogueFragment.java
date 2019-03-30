package ca.ualberta.CMPUT3012019T02.alexandria.fragment.bookCatalogue;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;

public class BookCatalogueFragment extends Fragment {

    private String title;
    private String author;
    private String isbn;

    private ValueEventListener valueEventListener;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_book_catalogue, null);

        Bundle arguments = getArguments();
        isbn = arguments.getString("isbn");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                new Thread(() -> {
                    if (!isAdded()) {
                        return;
                    }
                    Book book = dataSnapshot.getValue(Book.class);
                    if (book != null) {

                        title = book.getTitle();
                        author = book.getAuthor();

                        getActivity().runOnUiThread(() -> {
                            setUI(rootView);
                        });

                    } else {
                        getActivity().runOnUiThread(() -> getActivity().getFragmentManager().popBackStack());
                    }
                }).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };

        databaseReference = FirebaseDatabase.getInstance().getReference("books/" + isbn);
        databaseReference.addValueEventListener(valueEventListener);

        //Needed for Search UI hack
        getActivity().findViewById(R.id.navigation).setVisibility(View.VISIBLE);

        Bundle bundle = new Bundle();
        bundle.putString("isbn", isbn);
        OwnerListFragment frag = new OwnerListFragment();
        frag.setArguments(bundle);
        loadFragment(frag);

        // toolbar
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        // remove default title
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener((View v) -> getFragmentManager().popBackStack());

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        databaseReference.removeEventListener(valueEventListener);
    }

    private void setUI(View v) {
        TextView tvTitle = v.findViewById(R.id.bc_title);
        TextView tvAuthor = v.findViewById(R.id.bc_author);
        TextView tvIsbn = v.findViewById(R.id.bc_isbn);

        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvIsbn.setText(isbn);

    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getChildFragmentManager()
                    .beginTransaction().replace(R.id.bc_container, fragment).commit();
        }
    }
}
