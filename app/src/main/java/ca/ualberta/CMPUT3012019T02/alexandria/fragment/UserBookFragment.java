package ca.ualberta.CMPUT3012019T02.alexandria.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import java9.util.concurrent.CompletableFuture;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ISBNLookup;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ViewUserProfileActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.SearchController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;

import static android.app.Activity.RESULT_OK;

/**
 * Implements UserBookRecyclerView
 */

public class UserBookFragment extends Fragment {

    private Bitmap cover;
    private String title;
    private String author;
    private String isbn;
    private String status;
    private String ownerId;
    private String ownerUsername;
    private Bitmap ownerPic;
    private final int RESULT_ISBN = 1;



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

        extractData();
        setBookInfo(rootView);
        setStatusBar(rootView);
        setButtons(rootView);

        rootView.findViewById(R.id.user_book_owner).setOnClickListener(mListener);
        rootView.findViewById(R.id.user_book_owner_pic).setOnClickListener(mListener);
        rootView.findViewById(R.id.user_book_button_temp).setOnClickListener(mListener);
        rootView.findViewById(R.id.user_book_button).setOnClickListener(mListener);

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


    private void extractData() {
        Bundle arguments = getArguments();

        cover = arguments.getParcelable("cover");
        title = arguments.getString("title");
        author = arguments.getString("author");
        isbn = arguments.getString("isbn");
        status = arguments.getString("status");
        ownerId = arguments.getString("ownerId");

        if (arguments.getString("ownerUsername") != null) {
            ownerUsername = arguments.getString("ownerUsername");
            ownerPic = arguments.getParcelable("ownerPic");
        }
    }

    private void setBookInfo(View v) {
        TextView tvTitle = v.findViewById(R.id.user_book_title);
        TextView tvAuthor= v.findViewById(R.id.user_book_author);
        TextView tvIsbn = v.findViewById(R.id.user_book_isbn);
        Button tvOwner = v.findViewById(R.id.user_book_owner);

        ImageView ivCover = v.findViewById(R.id.user_book_cover);
        ImageView ivOwnerPic = v.findViewById(R.id.user_book_owner_pic);

        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvIsbn.setText(isbn);

        //TODO Make if for cover as search will not have one
        ivCover.setImageBitmap(cover);

        //if coming in from search result
        if (ownerUsername != null) {
            tvOwner.setText(ownerUsername);
            ivCover.setImageBitmap(ownerPic);
        }
        //search if coming in from exchange
        else {
            // sets owner name and avatar
            UserController userController = UserController.getInstance();
            userController.getUserProfile(ownerId).handleAsync((result, error) -> {
                if (error == null) {
                    // Update ui here
                    String name = result.getName();
                    String photoId = result.getPicture();
                    getActivity().runOnUiThread(() -> {
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
                                drawable.setCornerRadius(Math.min(
                                        bitmap.getWidth(), bitmap.getHeight()));
                                drawable.setAntiAlias(true);

                                getActivity().runOnUiThread(() -> {
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
    }

    /**
     * throws and error in toast
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

    //TODO Implement
    //To be called when the status changes in order to reload the page
    private void onStatusChange(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
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

    //TODO implement firebase status switching
    //main button with multiple actions
    private void onClickButton() {

        switch(status){
            case "available": break;
            case "requested": break;
            case "accepted": break;
            case "borrowed":
                Intent intent = new Intent(getActivity(), ISBNLookup.class);
                startActivityForResult(intent, RESULT_ISBN);
        }

    }

    //TODO implement firebase status switching
    //2nd button for when status is accepted
    private void onClickTempButton() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request we're responding to
        if (requestCode == RESULT_ISBN) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String isbn = extras.getString("isbn");
                CompletableFuture<Book> resultFuture = SearchController.getInstance().searchIsbn(isbn);
                resultFuture.handleAsync((result,error)->{
                   if(error==null){
                       Book book = result;
                       System.out.print("==============================" + book);
                       //TODO implement
                   }
                   else{
                       //TODO show error message
                   }
                   return null;
                });
            }
        }
    }

}
