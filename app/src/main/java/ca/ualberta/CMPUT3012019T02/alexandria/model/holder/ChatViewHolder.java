package ca.ualberta.CMPUT3012019T02.alexandria.model.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

/**
 * The chat view holder
 * Sets up item ids for Recycler views, which require attributes to be public.
 */
public class ChatViewHolder extends RecyclerView.ViewHolder {

    /**
     * The Item chat.
     */
    public LinearLayout itemChat;
    /**
     * The TextView chat reciever username.
     */
    public TextView tvChatRecieverUsername;
    /**
     * The ImageView chat user pic.
     */
    public ImageView ivChatUserPic;
    /**
     * The ImageView read status.
     */
    public ImageView ivReadStatus;

    /**
     * Instantiates a new Chat view holder.
     *
     * @param itemView the item view
     */
    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);

        itemChat = itemView.findViewById(R.id.item_chat);
        tvChatRecieverUsername = itemView.findViewById(R.id.chat_reciever_username);
        ivChatUserPic = itemView.findViewById(R.id.chat_user_pic);
        ivReadStatus = itemView.findViewById(R.id.read_status);
    }
}
