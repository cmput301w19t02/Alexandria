package ca.ualberta.CMPUT3012019T02.alexandria.fragment.myBook;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ISBNLookup;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ViewUserProfileActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;

import static android.app.Activity.RESULT_OK;

public class MyBookTransactionFragment extends Fragment {

    private String status;
    private String isbn;
    private String borrowerId;

    private BookController bookController = BookController.getInstance();
    private UserController userController = UserController.getInstance();
    private ImageController imageController = ImageController.getInstance();

    private final int RESULT_ISBN = 1;

    private Activity activity;

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

        Bundle arguments = getArguments();
        isbn = arguments.getString("isbn");
        status = arguments.getString("status");

        View rootView = inflater.inflate(R.layout.fragment_my_book_transaction, null);

        //setUI
        setStatusBar(rootView);
        getUserInfo(rootView);

        //onclickListeners
        rootView.findViewById(R.id.my_book_borrower_pic).setOnClickListener(mListener);
        rootView.findViewById(R.id.my_book_borrower).setOnClickListener(mListener);
        rootView.findViewById(R.id.my_book_message).setOnClickListener(mListener);
        rootView.findViewById(R.id.my_book_user_ellipses).setOnClickListener(mListener);


        return rootView;
    }

    //gets the borrower info from firebase
    private void getUserInfo(View v) {
        ImageView ivBorrowerPic = v.findViewById(R.id.my_book_borrower_pic);
        Button btBorrowerName = v.findViewById(R.id.my_book_borrower);


        bookController.getMyOwnedBook(isbn).handleAsync((bookResult, bookError) -> {
            if (bookError == null) {
                this.borrowerId = bookResult.get().getUserBorrowing();
                userController.getUserProfile(borrowerId).handleAsync((userResult, userError) -> {
                    if (userError == null) {
                        String username = userResult.getUsername();
                        String pictureId = userResult.getPicture();

                        if (pictureId != null) {
                            imageController.getImage(pictureId)
                                    .handleAsync((imageResult, imageError) -> {
                                        if (imageError == null) {

                                            Bitmap squareBitmap
                                                    = Bitmap.createBitmap(imageResult, 0, 0,
                                                    Math.min(imageResult.getWidth(),
                                                            imageResult.getHeight()),
                                                    Math.min(imageResult.getWidth(),
                                                            imageResult.getHeight()));

                                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                                                    .create(getResources(), squareBitmap);
                                            drawable.setCornerRadius(Math.min(
                                                    imageResult.getWidth(), imageResult.getHeight()));
                                            drawable.setAntiAlias(true);

                                            activity.runOnUiThread(() -> {
                                                ivBorrowerPic.setImageDrawable(drawable);
                                                ivBorrowerPic.refreshDrawableState();
                                                btBorrowerName.setText(username);
                                            });
                                        } else {
                                            Log.e("Error", "Error getting user image");
                                        }
                                        return null;
                                    });
                        }
                    } else {
                        Log.e("Error", "Error getting user");
                    }
                    return null;
                });
            } else {
                Log.e("Error", "Error getting owned book");
            }
            return null;
        });
    }

    //sets bottom Status bar text and icon
    private void setStatusBar(View v) {
        TextView tvStatus = v.findViewById(R.id.my_book_status);
        ImageView ivStatus = v.findViewById(R.id.my_book_status_icon);
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

    //onClick functions for buttons
    private final View.OnClickListener mListener = (View v) -> {
        switch (v.getId()) {
            case R.id.my_book_borrower_pic:
                onClickUser();
                break;
            case R.id.my_book_borrower:
                onClickUser();
                break;
            case R.id.my_book_message:
                onClickMessageUser();
                break;
            case R.id.my_book_user_ellipses:
                onClickEllipses(v);
                break;
            default:
                throw new RuntimeException("No Button Found");
        }
    };

    //switch to the borrower's profile
    private void onClickUser() {
        Intent intentViewOwner = new Intent(activity, ViewUserProfileActivity.class);
        intentViewOwner.putExtra("USER_ID", borrowerId);
        startActivity(intentViewOwner);
    }

    //TODO navigate to message fragment
    private void onClickMessageUser() {

    }

    //creates a popup menu for user to select options
    private void onClickEllipses(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v.findViewById(R.id.my_book_user_ellipses));
        popup.getMenuInflater().inflate(getStatusMenu(), popup.getMenu());

        popup.setOnMenuItemClickListener((MenuItem item) -> {


            Intent intent = new Intent(activity, ISBNLookup.class);

            switch (item.getItemId()) {
                case R.id.option_view_user:
                    onClickUser();
                    break;
                case R.id.option_set_borrowed:
                    startActivityForResult(intent, RESULT_ISBN);
                    break;
                case R.id.option_return:
                    startActivityForResult(intent, RESULT_ISBN);
                    break;
                case R.id.option_cancel_order:
                    cancelOrder();
                    break;
                default:
                    throw new RuntimeException("No Button Found");
            }
            return true;
        });
        popup.show();
    }

    //switches popup menus depending on status
    private int getStatusMenu() {
        switch (status) {
            case "accepted":
                return R.menu.menu_my_book_accepted;
            case "borrowed":
                return R.menu.menu_my_book_borrowed;
            default:
                throw new RuntimeException("No Button Found");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request we're responding to
        if (requestCode == RESULT_ISBN) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String isbn = extras.getString("isbn");
                switch (status) {
                    case "accepted":
                        setBorrowed(isbn);
                        break;
                    case "borrowed":
                        acceptReturn(isbn);
                        break;
                    default:
                }
            }
        }
    }

    private void setBorrowed(String scannedIsbn) {
//        if (isbn.equals(scannedIsbn)) {
        // TODO: fix ISBN scanning so that it scans properly
        if (true) {
            BookController.getInstance().scanMyOwnedBook(isbn).handleAsync((aVoid, throwable) -> {
                if (throwable == null) {

                    BookController.getInstance().exchangeBook(isbn, userController.getMyId()).handleAsync((aVoid1, throwable1) -> {
                        activity.runOnUiThread(() -> {
                            if (throwable1 == null) {

                                // Completed successfully

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Scan successful. Please wait for the other member to confirm.");
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                        return null;
                    });

                } else {
                    activity.runOnUiThread(() -> {
                        throwable.printStackTrace();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Was not able to scan ISBN. Please try again later.");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    });
                }
                return null;
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Scanned ISBN does not match the book you are returning");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void acceptReturn(String scannedIsbn) {
//        if (isbn.equals(scannedIsbn)) {
        // TODO: fix ISBN scanning so that it scans properly
        if (true) {
            BookController.getInstance().scanMyOwnedBook(isbn).handleAsync((aVoid, throwable) -> {
                if (throwable == null) {

                    BookController.getInstance().returnBook(isbn, userController.getMyId()).handleAsync((aVoid1, throwable1) -> {
                        activity.runOnUiThread(() -> {
                            if (throwable1 == null) {

                                // Completed successfully

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Scan successful. Please wait for the other member to confirm.");
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                        return null;
                    });

                } else {
                    activity.runOnUiThread(() -> {
                        throwable.printStackTrace();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Was not able to scan ISBN. Please try again later.");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    });
                }
                return null;
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Scanned ISBN does not match the book you are returning");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void cancelOrder() {
        bookController.declineRequest(isbn, borrowerId).handleAsync((aVoid, throwable) -> {
            activity.runOnUiThread(() -> {
                if (throwable == null) {

                    // Successfully declined the request
                    // TODO: MyBookFragment.onStatusChange()

                } else {
                    throwable.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Unable to cancel order. Please try again later.");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            return null;
        });
    }

}
