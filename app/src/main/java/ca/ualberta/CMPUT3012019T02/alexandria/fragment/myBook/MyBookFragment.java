package ca.ualberta.CMPUT3012019T02.alexandria.fragment.myBook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.myBook.EditBookActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;

public class MyBookFragment extends Fragment {

    private ImageController imageController = ImageController.getInstance();
    private BookController bookController = BookController.getInstance();
    private UserController userController = UserController.getInstance();

    private String coverId;
    private String title;
    private String author;
    private String isbn;
    private String status;
    private Activity activity;
    private ValueEventListener valueEventListener;
    private DatabaseReference databaseReference;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_book,null);

        Bundle arguments = getArguments();
        isbn = arguments.getString("isbn");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                new Thread(() -> {
                    if (!isAdded()) {
                        return;
                    }
                    OwnedBook ownedBook = dataSnapshot.getValue(OwnedBook.class);
                    if (ownedBook != null) {

                        List<String> statuses = Arrays.asList("available", "requested", "accepted", "borrowed");
                        int oldStatus = statuses.indexOf(status);
                        int newStatus = statuses.indexOf(ownedBook.getStatus());

                        // Accepted and borrowed are technically different, but they both correspond to the same fragment.
                        if (oldStatus == 3) {
                            oldStatus = 2;
                        }
                        if (newStatus == 3) {
                            newStatus = 2;
                        }

                        status = ownedBook.getStatus();
                        coverId = ownedBook.getImageId();
                        try {
                            Book book = bookController.getBook(isbn).get(5, TimeUnit.SECONDS).get();
                            title = book.getTitle();
                            author = book.getAuthor();
                        } catch (Exception e) {
                            e.printStackTrace();
                            activity.runOnUiThread(() -> getFragmentManager().popBackStack());
                        }

                        int finalNewStatus = newStatus;
                        int finalOldStatus = oldStatus;
                        activity.runOnUiThread(() -> {
                            setUI(rootView);
                            setImage(rootView);

                            if (finalOldStatus != finalNewStatus) {
                                loadFragment(fragmentSelector());
                                activity.invalidateOptionsMenu();
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };

        databaseReference = FirebaseDatabase.getInstance().getReference(bookController.getOwnedBookPath(userController.getMyId(), isbn));
        databaseReference.addValueEventListener(valueEventListener);


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

    //Instantiate the options menu
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (status != null) {
            if (status.equals("available") || status.equals("requested")) {
                inflater.inflate(R.menu.menu_my_book, menu);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //menu switch
            case R.id.option_edit_book:
                onClickEditBook();
                break;
            case R.id.option_delete_book:
                deleteWarning();
                break;
            case R.id.menu_my_book_ellipses:
                break;
            default:
                throw new RuntimeException("Unknown option");
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUI(View v) {
        TextView tvTitle = v.findViewById(R.id.my_book_title);
        TextView tvAuthor = v.findViewById(R.id.my_book_author);
        TextView tvIsbn = v.findViewById(R.id.my_book_isbn);
        TextView tvContainerTitle = v.findViewById(R.id.my_book_container_title);

        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvIsbn.setText(isbn);

        //This is needed due to the way the UI is designed
        //Will set the title for only the Recycler view
        //Since the transaction page items hover at the bottom it's set in the daughter fragment
        if (status.equals("requested") || status.equals("available")) {
            tvContainerTitle.setText(R.string.my_book_requests);
        }

    }

    private void setImage(View v) {
        new Thread(() -> {
            try {
                if (coverId != null) {
                    Bitmap bitmap = imageController.getImage(coverId).get(5, TimeUnit.SECONDS);
                    activity.runOnUiThread(() -> {
                        ImageView ivCover = v.findViewById(R.id.my_book_cover);
                        ivCover.setImageBitmap(bitmap);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                activity.runOnUiThread(() -> {
                    showError("Failed to get image from server");
                });
            }
        }).start();
    }

    private Fragment fragmentSelector() {
        Bundle bundle = new Bundle();

        bundle.putString("status", status);
        bundle.putString("isbn", isbn);
        switch (status) {
            case "available":
                return new MyBookAvailableFragment();
            case "requested":
                MyBookUserListFragment requestedListFragment = new MyBookUserListFragment();
                requestedListFragment.setArguments(bundle);
                return requestedListFragment;
            case "accepted":
                MyBookTransactionFragment fragment = new MyBookTransactionFragment();
                fragment.setArguments(bundle);
                return fragment;
            case "borrowed":
                fragment = new MyBookTransactionFragment();
                fragment.setArguments(bundle);
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

    /**
     * Shows an error message in toast
     *
     * @param message error message
     */
    private void showError(String message) {
        Toast.makeText(getView().getContext(), "Error: " + message, Toast.LENGTH_LONG).show();
    }

    private void onClickEditBook() {
        Intent intentEditBook = new Intent(getActivity(), EditBookActivity.class);
        intentEditBook.putExtra("BOOK_ISBN", isbn);
        startActivity(intentEditBook);
    }

    private void deleteWarning() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Delete Book?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Delete",
                (DialogInterface dialog, int which) -> deleteBook());

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel",
                (DialogInterface dialog, int which) -> dialog.dismiss());

        //switch default color
        alertDialog.setOnShowListener( (DialogInterface dialog) -> {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorRed));
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorRed));
        });

        alertDialog.show();
    }

    private void deleteBook() {
        BookController.getInstance().deleteMyOwnedBook(isbn).handleAsync((aVoid, throwable) -> {
            activity.runOnUiThread(() -> {
                if (throwable == null) {

                    getFragmentManager().popBackStack();

                } else {
                    throwable.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Unable to delete book. Please try again later.");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            return null;
        });
    }
}
