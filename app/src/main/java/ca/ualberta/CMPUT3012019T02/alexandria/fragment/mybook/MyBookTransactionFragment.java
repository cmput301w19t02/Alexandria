package ca.ualberta.CMPUT3012019T02.alexandria.fragment.mybook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ViewUserProfileActivity;

public class MyBookTransactionFragment extends Fragment {

    private String status;
    private String borrowerId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_book_transaction,null);



        setStatusBar(rootView);
        getUserInfo(rootView);

        //onclickListeners
        rootView.findViewById(R.id.my_book_borrower).setOnClickListener(mListener);
        rootView.findViewById(R.id.my_book_borrower).setOnClickListener(mListener);
        rootView.findViewById(R.id.my_book_message).setOnClickListener(mListener);
        rootView.findViewById(R.id.my_book_user_ellipses).setOnClickListener(mListener);


        return rootView;
    }

    //sets the status via parent fragment
    public void setStatus(String status) {
        this.status = status;
    }

    //gets the borrower info from firebase
    private void getUserInfo(View v){
        this.borrowerId = ""; //TODO implement backend
        ImageView ivBorrowerPic = v.findViewById(R.id.my_book_borrower_pic);
        Button btBorrowerName = v.findViewById(R.id.my_book_borrower);


        //TODO implement Firebase Lookup
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
        switch(v.getId()){
            case R.id.my_book_borrower_pic: onClickUser(); break;
            case R.id.my_book_borrower: onClickUser(); break;
            case R.id.my_book_message: onClickMessageUser(); break;
            case R.id.my_book_user_ellipses: onClickEllipses(v); break;
            default: throw new RuntimeException("No Button Found");
        }
    };

    //switch to the borrower's profile
    private void onClickUser() {
        Intent intentViewOwner = new Intent(getActivity(), ViewUserProfileActivity.class);
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
            switch (item.getItemId()){
                case R.id.option_view_user: onClickUser(); break;
                case R.id.option_set_borrowed: setBorrowed(); break;
                case R.id.option_return: acceptReturn(); break;
                default: throw new RuntimeException("No Button Found");
            }
            return true;
        });
        popup.show();
    }

    //switches popup menus depending on status
    private int getStatusMenu() {
        switch (status){
            case "accepted": return R.menu.menu_my_book_accepted;
            case "borrowed": return R.menu.menu_my_book_borrowed;
            default: throw new RuntimeException("No Button Found");
        }

    }

    //TODO implement
    //opens the camera to change status
    private void setBorrowed(){

    }

    //TODO implement
    //opens camera to process return
    private void acceptReturn(){

    }

}
