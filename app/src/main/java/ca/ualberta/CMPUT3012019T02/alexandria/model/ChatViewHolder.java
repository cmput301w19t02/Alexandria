package ca.ualberta.CMPUT3012019T02.alexandria.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

/**
 * Sets up item ids for Recycler views, which require attributes to be public.
 */

public class ChatViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout itemChat;
    public TextView tvChatRecieverUsername;
    public ImageView ivChatUserPic;
    public ImageView ivReadStatus;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);

        itemChat = itemView.findViewById(R.id.item_chat);
        tvChatRecieverUsername = itemView.findViewById(R.id.chat_reciever_username);
        ivChatUserPic = itemView.findViewById(R.id.chat_user_pic);
        ivReadStatus = itemView.findViewById(R.id.read_status);
    }
}
