package ca.ualberta.CMPUT3012019T02.alexandria.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import ca.ualberta.CMPUT3012019T02.alexandria.activity.ISBNLookup;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ViewImageActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ViewUserProfileActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;

import static android.app.Activity.RESULT_OK;

/**
 * Implements UserBookRecyclerView
 */

public class UserBookFragment extends Fragment implements View.OnClickListener {

    private ImageController imageController = ImageController.getInstance();
    private BookController bookController = BookController.getInstance();

    private String coverId;
    private String title;
    private String author;
    private String isbn;
    private String status;
    private String ownerId;
    private final int RESULT_ISBN = 1;
    private Activity activity;
    private AlertDialog scanSuccessfulDialog;
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
        View rootView = inflater.inflate(R.layout.fragment_user_book,null);

        // toolbar
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        // remove default title
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener((View v) -> getFragmentManager().popBackStack());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Scan successful. Please wait for the other member to confirm.");
        scanSuccessfulDialog = builder.create();

        Bundle arguments = getArguments();
        isbn = arguments.getString("isbn");
        ownerId = arguments.getString("ownerId");
        status = null;

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                new Thread(() -> {
                    if (!isAdded()) {
                        return;
                    }
                    OwnedBook ownedBook = dataSnapshot.getValue(OwnedBook.class);
                    if (ownedBook != null) {
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

                        activity.runOnUiThread(() -> {
                            setImage(rootView);
                            setBookInfo(rootView);
                            setStatusBar(rootView);
                            setButtons(rootView);
                            scanSuccessfulDialog.dismiss();
                        });
                    } else {
                        activity.runOnUiThread(() -> getFragmentManager().popBackStack());
                    }
                }).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };

        databaseReference = FirebaseDatabase.getInstance().getReference(bookController.getOwnedBookPath(ownerId, isbn));
        databaseReference.addValueEventListener(valueEventListener);

        rootView.findViewById(R.id.user_book_owner).setOnClickListener(mListener);
        rootView.findViewById(R.id.user_book_owner_pic).setOnClickListener(mListener);
        rootView.findViewById(R.id.user_book_button_temp).setOnClickListener(mListener);
        rootView.findViewById(R.id.user_book_button).setOnClickListener(mListener);

        ImageView bookCover = rootView.findViewById(R.id.user_book_cover);
        bookCover.setOnClickListener(this);

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
        inflater.inflate(R.menu.menu_user_book, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //menu switch
            case R.id.option_view_user:
                onClickUser();
                break;
            case R.id.option_message_user:
                break;
            case R.id.menu_user_book_ellipses:
                break;
            default:
                throw new RuntimeException("Unknown option");
        }
        return super.onOptionsItemSelected(item);
    }

    private void setImage(View v) {
        new Thread(() -> {
            try {
                if (coverId != null) {
                    Bitmap bitmap = imageController.getImage(coverId).get(5, TimeUnit.SECONDS);
                    activity.runOnUiThread(() -> {
                        ImageView ivCover = v.findViewById(R.id.user_book_cover);
                        ivCover.setImageBitmap(bitmap);
                    });
                } else {
                    ImageView ivCover = v.findViewById(R.id.user_book_cover);
                    ivCover.setImageBitmap(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                activity.runOnUiThread(() -> {
                    showError("Failed to get image from server");
                });
            }
        }).start();
    }

    private void setBookInfo(View v) {
        TextView tvTitle = v.findViewById(R.id.user_book_title);
        TextView tvAuthor= v.findViewById(R.id.user_book_author);
        TextView tvIsbn = v.findViewById(R.id.user_book_isbn);
        Button tvOwner = v.findViewById(R.id.user_book_owner);

        ImageView ivOwnerPic = v.findViewById(R.id.user_book_owner_pic);

        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvIsbn.setText(isbn);

        // sets owner name and avatar
        UserController userController = UserController.getInstance();
        userController.getUserProfile(ownerId).handleAsync((result, error) -> {
            if (error == null) {
                // Update ui here
                String name = result.getName();
                String photoId = result.getPicture();
                activity.runOnUiThread(() -> {
                    tvOwner.setText(name);
                });
                // sets owner image if there is one
                ImageController imageController = ImageController.getInstance();
                imageController.getImage(photoId).handleAsync((resultImage, errorImage) -> {
                    if (errorImage == null) {
                        Bitmap bitmap = resultImage;

                        if (bitmap != null) {
                            Bitmap squareBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                                    Math.min(bitmap.getWidth(), bitmap.getHeight()),
                                    Math.min(bitmap.getWidth(), bitmap.getHeight()));

                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                                    .create(getResources(), squareBitmap);

                            drawable.setCornerRadius(Math.min(bitmap.getWidth(), bitmap.getHeight()));
                            drawable.setAntiAlias(true);

                            activity.runOnUiThread(() -> {
                                ivOwnerPic.setImageDrawable(drawable);
                            });
                        }
                    } else {
                        showError(errorImage.getMessage());
                    }
                    return null;
                });
            } else {
                // Show error message
                throw new NullPointerException("user profile not obtained");
            }
            return null;
        });
    }

    /**
     * Shows an error message in toast
     * @param message error message
     */
    private void showError(String message) {
        Toast.makeText(getView().getContext(), "Error: " + message, Toast.LENGTH_LONG).show();
    }

    //sets bottom Status bar text and icon
    private void setStatusBar(View v) {
        TextView tvStatus = v.findViewById(R.id.user_book_status);
        ImageView ivStatus = v.findViewById(R.id.user_book_status_icon);
        //makes the first letter in the status capitalized
        String statusText = status.substring(0, 1).toUpperCase() + status.substring(1);
        tvStatus.setText(statusText);

        switch (status) {
            case "available":
                ivStatus.setImageResource(R.drawable.ic_status_available);
                break;
            case "requested":
                ivStatus.setImageResource(R.drawable.ic_status_requested);
                break;
            case "accepted":
                ivStatus.setImageResource(R.drawable.ic_status_accepted);
                break;
            case "borrowed":
                ivStatus.setImageResource(R.drawable.ic_status_borrowed);
                break;
            default:
                throw new RuntimeException("Status out of bounds");
        }
    }

    //Sets button text dependant on status
    private void setButtons(View v){
        Button button = v.findViewById(R.id.user_book_button);
        Button tempButton = v.findViewById(R.id.user_book_button_temp);
        String availText = "Send a Request";
        String cancelReq = "Cancel Request";
        String borrText = "Process Return";

        //For the button that only appears when status is accepted
        if (!status.equals("accepted")) {
            tempButton.setVisibility(View.GONE);
        } else {
            tempButton.setVisibility(View.VISIBLE);
        }

        //Button text for the main button
        switch (status){
            case "available": button.setText(availText); break;
            case "requested": button.setText(cancelReq); break;
            case "accepted": button.setText(cancelReq); break;
            case "borrowed": button.setText(borrText); break;
            default: throw new RuntimeException("Status out of bounds");
        }

    }

    private final View.OnClickListener mListener = (View v) -> {
        switch(v.getId()){
            case R.id.user_book_owner: onClickUser(); break;
            case R.id.user_book_owner_pic: onClickUser(); break;
            case R.id.user_book_button_temp: onClickTempButton(); break;
            case R.id.user_book_button: onClickButton(); break;
            default: throw new RuntimeException("No Button Found");
        }
    };

    //switch to the book owner's profile
    private void onClickUser() {
        Intent intentViewOwner = new Intent(getActivity(), ViewUserProfileActivity.class);
        intentViewOwner.putExtra("USER_ID", ownerId);
        startActivity(intentViewOwner);
    }

    //TODO navigate to message fragment
    private void onClickMessageUser() {

    }

    //main button with multiple actions
    private void onClickButton() {

        switch (status) {
            case "available":
                sendRequest();
                break;
            case "requested":
                cancelRequest();
                break;
            case "accepted":
                cancelRequest();
                break;
            case "borrowed":
                Intent intent = new Intent(getActivity(), ISBNLookup.class);
                startActivityForResult(intent, RESULT_ISBN);
                break;
            default:
        }

    }

    //2nd button for when status is accepted
    private void onClickTempButton() {
        Intent intent = new Intent(getActivity(), ISBNLookup.class);
        startActivityForResult(intent, RESULT_ISBN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request we're responding to
        if (requestCode == RESULT_ISBN) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String isbn = extras.getString("isbn").trim();
                switch (status) {
                    case "accepted":
                        setStatusBorrowed(isbn);
                        break;
                    case "borrowed":
                        processReturn(isbn);
                        break;
                    default:
                }
            }
        }
    }

    private void sendRequest() {
        BookController.getInstance().requestBook(isbn, ownerId).handleAsync((aVoid, throwable) -> {
            activity.runOnUiThread(() -> {
                if (throwable == null) {
                    Toast.makeText(activity, "Book request sent", Toast.LENGTH_LONG).show();
                } else {
                    throwable.printStackTrace();
                    throwable.printStackTrace();
                    activity.runOnUiThread(() -> {
                        Toast.makeText(activity, "Request failed. Try again later", Toast.LENGTH_LONG).show();
                    });
                }
            });
            return null;
        });
    }

    private void cancelRequest() {
        BookController.getInstance().cancelRequest(isbn, ownerId).handleAsync((aVoid, throwable) -> {
            activity.runOnUiThread(() -> {
                if (throwable == null) {
                    Toast.makeText(activity, "Book request cancelled", Toast.LENGTH_LONG).show();
                } else {
                    throwable.printStackTrace();
                    activity.runOnUiThread(() -> {
                        Toast.makeText(activity, "Request cancel failed. Try again later", Toast.LENGTH_LONG).show();
                    });
                }
            });
            return null;
        });
    }

    private void setStatusBorrowed(String scannedIsbn) {
        if (isbn.equals(scannedIsbn)) {
            BookController.getInstance().scanMyBorrowedBook(isbn).handleAsync((aVoid, throwable) -> {
                if (throwable == null) {

                    BookController.getInstance().exchangeBook(isbn, ownerId).handleAsync((aVoid1, throwable1) -> {
                        activity.runOnUiThread(() -> {
                            if (throwable1 == null) {
                                Toast.makeText(activity, "Book borrowed", Toast.LENGTH_LONG).show();
                            } else {
                                scanSuccessfulDialog.show();
                            }
                        });
                        return null;
                    });

                } else {
                    throwable.printStackTrace();
                    activity.runOnUiThread(() -> {
                        Toast.makeText(activity, "Was not able to scan ISBN", Toast.LENGTH_LONG).show();
                    });
                }
                return null;
            });
        } else {
            Toast.makeText(activity, "Scanned ISBN does not match", Toast.LENGTH_LONG).show();
        }
    }

    private void processReturn(String scannedIsbn) {
        if (isbn.equals(scannedIsbn)) {
            BookController.getInstance().scanMyBorrowedBook(isbn).handleAsync((aVoid, throwable) -> {
                if (throwable == null) {

                    BookController.getInstance().returnBook(isbn, ownerId).handleAsync((aVoid1, throwable1) -> {
                        activity.runOnUiThread(() -> {
                            if (throwable1 == null) {
                                Toast.makeText(activity, "Book returned", Toast.LENGTH_LONG).show();
                            } else {
                                scanSuccessfulDialog.show();
                            }
                        });
                        return null;
                    });

                } else {
                    throwable.printStackTrace();
                    activity.runOnUiThread(() -> {
                        Toast.makeText(activity, "Was not able to scan ISBN", Toast.LENGTH_LONG).show();
                    });
                }
                return null;
            });
        } else {
            Toast.makeText(activity, "Scanned ISBN does not match", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        Intent startExpandImage = new Intent(getActivity(), ViewImageActivity.class);
        startExpandImage.putExtra("IMAGE_ID", coverId);
        startActivity(startExpandImage);
    }
}
