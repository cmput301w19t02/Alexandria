package ca.ualberta.CMPUT3012019T02.alexandria.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.model.UserListItem;
import ca.ualberta.CMPUT3012019T02.alexandria.model.holder.UserViewHolder;

/**
 * Set up RecyclerView for user lists
 * Code based on code from https://youtu.be/T_QfRU-A3s4 on 03/04/2019
 */
public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private Context mContext;
    private List<UserListItem> mUserListItem;
    private UserRecyclerListener mListener;

    /**
     * Instantiates a new User recycler view adapter.
     *
     * @param mContext      the application context
     * @param mUserListItem the user list item list
     * @param mListener     the a UserRecyclerListener
     */
    public UserRecyclerViewAdapter(
            Context mContext, List<UserListItem> mUserListItem, UserRecyclerListener mListener) {
        this.mContext = mContext;
        this.mUserListItem = mUserListItem;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView;

        mView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_user, viewGroup, false);

        UserViewHolder mViewHolder = new UserViewHolder(mView, mListener);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int position) {

        if (mUserListItem.get(position).getBorrowerPic() != null) {
            userViewHolder.ivUserPic.setImageDrawable(mUserListItem.get(position).getBorrowerPic());
            userViewHolder.ivUserPic.setBackground(null);
        }
        userViewHolder.btUser.setText(mUserListItem.get(position).getBorrowerUsername());
    }

    @Override
    public int getItemCount() {
        return mUserListItem.size();
    }

    /**
     * The interface User recycler listener.
     */
//sets up click functions for the user item
    public interface UserRecyclerListener {
        /**
         * Message click.
         *
         * @param position the position
         */
        void messageClick(int position);

        /**
         * User click.
         *
         * @param position the position
         */
        void userClick(int position);

        /**
         * Ellipses click.
         *
         * @param v        the v
         * @param position the position
         */
        void ellipsesClick(View v, int position);
    }


}
