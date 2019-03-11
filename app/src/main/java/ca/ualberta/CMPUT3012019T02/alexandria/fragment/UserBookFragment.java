package ca.ualberta.CMPUT3012019T02.alexandria.fragment;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ViewUserProfileActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;

/**
 * Implements UserBookRecyclerView
 */

public class UserBookFragment extends Fragment {

    private Bitmap cover;
    private String title;
    private String author;
    private String isbn;
    private String status;
    private String owner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_book,null);

        // toolbar
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);    // remove default title

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

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


    private void extractData() {
        Bundle arguments = getArguments();

        cover = arguments.getParcelable("cover");
        title = arguments.getString("title");
        author = arguments.getString("author");
        isbn = arguments.getString("isbn");
        status = arguments.getString("status");
        owner = arguments.getString("owner");
    }

    private void setBookInfo(View v) {
        TextView tvTitle = v.findViewById(R.id.user_book_title);
        TextView tvAuthor= v.findViewById(R.id.user_book_author);
        TextView tvIsbn = v.findViewById(R.id.user_book_isbn);
        Button tvOwner = v.findViewById(R.id.user_book_owner);

        ImageView ivCover = v.findViewById(R.id.user_book_cover);
        ImageButton ivOwnerPic = v.findViewById(R.id.user_book_owner_pic);

        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvIsbn.setText(isbn);
        tvOwner.setText(owner);

        UserController userController = UserController.getInstance();
        userController.getUserProfile(owner).handleAsync((result, error) -> {
            if(error == null) {
                // Update ui here
                String name = result.getName();
                getActivity().runOnUiThread(() -> {
                    tvOwner.setText(name);
                });
            }
            else {
                // Show error message
                throw new NullPointerException("user profile not obtained");
            }
            return null;
        });

        //TODO implement firebase lookup for user profile pic
        ivCover.setImageBitmap(cover);
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

    //To be called when the status changes in order to reload the page
    private void onStatusChange(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    private final View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.user_book_owner: onClickUser(); break;
                case R.id.user_book_owner_pic: onClickUser(); break;
                case R.id.user_book_button_temp: onClickTempButton(); break;
                case R.id.user_book_button: onClickButton(); break;
                default: throw new RuntimeException("No Button Found");
            }
        }
    };

    //TODO implement Activity Switching
    //switch to the book owner's profile
    private void onClickUser() {
        Intent intentViewOwner = new Intent(getActivity(), ViewUserProfileActivity.class);
        intentViewOwner.putExtra("USER_ID", owner);
        startActivity(intentViewOwner);
    }

    //TODO implement firebase status switching
    //main button with multiple actions
    private void onClickButton() {

        switch(status){
            case "available": break;
            case "requested": break;
            case "accepted": break;
            case "borrowed": break;
        }

    }

    //TODO implement firebase status switching
    //2nd button for when status is accepted
    private void onClickTempButton() {

    }
}
