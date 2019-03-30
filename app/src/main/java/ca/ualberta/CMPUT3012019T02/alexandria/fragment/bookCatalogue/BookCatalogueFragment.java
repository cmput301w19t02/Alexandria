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

import java.util.ArrayList;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

public class BookCatalogueFragment extends Fragment {

    private String title;
    private String author;
    private String isbn;
    private ArrayList<String> availableOwners;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_book_catalogue, null);
        extractData();
        setUI(rootView);

        //Needed for Search UI hack
        getActivity().findViewById(R.id.navigation).setVisibility(View.VISIBLE);

        OwnerListFragment frag = new OwnerListFragment();
        frag.dataGrab(title, author, isbn, availableOwners);
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

    private void extractData() {
        Bundle arguments = getArguments();

        title = arguments.getString("title");
        author = arguments.getString("author");
        isbn = arguments.getString("isbn");
        availableOwners = arguments.getStringArrayList("availableOwners");
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
