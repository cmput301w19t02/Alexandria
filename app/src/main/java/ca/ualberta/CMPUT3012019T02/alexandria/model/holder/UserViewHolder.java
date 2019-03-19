package ca.ualberta.CMPUT3012019T02.alexandria.model.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.adapter.UserRecyclerViewAdapter;

/**
 * Creates UserViewHolder for the UserRecyclerViewAdapter
 * Listener code implemented from
 * https://stackoverflow.com/questions/30284067/handle-button-click-inside-a-row-in-recyclerview
 * on March 17, 2018
 */

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public RelativeLayout itemUser;
    public ImageView ivUserPic;
    public Button btUser;
    public ImageButton ibtMessage;
    public ImageButton ibtEllipses;
    private WeakReference<UserRecyclerViewAdapter.UserRecyclerListener> listenerRef;

    public UserViewHolder(@NonNull View itemView,
                          UserRecyclerViewAdapter.UserRecyclerListener mListener) {
        super(itemView);

        listenerRef = new WeakReference<>(mListener);
        itemUser = itemView.findViewById(R.id.item_user);
        ivUserPic = itemView.findViewById(R.id.item_user_pic);
        btUser = itemView.findViewById(R.id.item_user_name);
        ibtMessage = itemView.findViewById(R.id.item_user_message);
        ibtEllipses = itemView.findViewById(R.id.item_user_ellipses);

        //OnClickListeners
        ivUserPic.setOnClickListener(this);
        btUser.setOnClickListener(this);
        ibtMessage.setOnClickListener(this);
        ibtEllipses.setOnClickListener(this);

    }

    //Onclick functions using the interface
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.item_user_pic:
                listenerRef.get().userClick(getAdapterPosition());
                break;
            case R.id.item_user_name:
                listenerRef.get().userClick(getAdapterPosition());
                break;
            case R.id.item_user_message:
                listenerRef.get().messageClick(getAdapterPosition());
                break;
            case R.id.item_user_ellipses:
                listenerRef.get().ellipsesClick(v, getAdapterPosition());
                break;
        }
    }
}
