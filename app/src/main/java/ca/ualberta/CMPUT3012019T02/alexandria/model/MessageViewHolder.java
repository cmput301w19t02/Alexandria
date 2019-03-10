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

public class MessageViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout itemMessage;
    public TextView tvContent;
    public TextView tvSenderUsername;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);

        itemMessage = itemView.findViewById(R.id.item_message);
        tvContent = itemView.findViewById(R.id.message_content);
        tvSenderUsername = itemView.findViewById(R.id.message_user);
    }
}
