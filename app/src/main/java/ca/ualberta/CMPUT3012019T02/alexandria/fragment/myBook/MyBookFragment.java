package ca.ualberta.CMPUT3012019T02.alexandria.fragment.myBook;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

public class MyBookFragment extends Fragment {

    private Bitmap cover;
    private String title;
    private String author;
    private String isbn;
    private String status;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_book,null);

        extractData();
        setUI(rootView);
        loadFragment(fragmentSelector());

        // toolbar
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        // remove default title
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener((View v) -> getFragmentManager().popBackStack());

        return rootView;
    }

    //Instantiate the options menu
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (status.equals("available") || status.equals("requested")) {
            inflater.inflate(R.menu.menu_my_book, menu);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //menu switch
            case R.id.option_edit_book:
                break;
            case R.id.option_delete_book:
                break;
            case R.id.menu_my_book_ellipses:
                break;
            default:
                throw new RuntimeException("Unknown option");
        }
        return super.onOptionsItemSelected(item);
    }

    private void extractData() {
        Bundle arguments = getArguments();

        cover = arguments.getParcelable("cover");
        title = arguments.getString("title");
        author = arguments.getString("author");
        isbn = arguments.getString("isbn");
        status = arguments.getString("status");
    }

    private void setUI(View v) {
        TextView tvTitle = v.findViewById(R.id.my_book_title);
        TextView tvAuthor = v.findViewById(R.id.my_book_author);
        TextView tvIsbn = v.findViewById(R.id.my_book_isbn);
        ImageView ivCover = v.findViewById(R.id.my_book_cover);
        TextView tvContainerTitle = v.findViewById(R.id.my_book_container_title);

        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvIsbn.setText(isbn);
        ivCover.setImageBitmap(cover);

        //This is needed due to the way the UI is designed
        //Will set the title for only the Recycler view
        //Since the transaction page items hover at the bottom it's set in the daughter fragment
        if (status.equals("requested") || status.equals("available")) {
            tvContainerTitle.setText(R.string.my_book_requests);
        }

    }

    private Fragment fragmentSelector() {
        switch (status) {
            case "available":
                return new MyBookUserListFragment();
            case "requested":
                return new MyBookUserListFragment();
            case "accepted":
                MyBookTransactionFragment fragment = new MyBookTransactionFragment();
                fragment.setVariables(status,isbn);
                return fragment;
            case "borrowed":
                fragment = new MyBookTransactionFragment();
                fragment.setVariables(status,isbn);
                return fragment;
            default:
                throw new RuntimeException("Status out of bounds");
        }
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getChildFragmentManager()
                    .beginTransaction().replace(R.id.my_book_fragment_container, fragment).commit();
        }
    }

    //TODO Implement, will probably need data bundled again
    //To be called when the status changes in order to reload the page
    public void onStatusChange(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

}
