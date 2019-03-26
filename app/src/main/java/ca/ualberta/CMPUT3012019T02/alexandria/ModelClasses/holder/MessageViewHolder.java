package ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

/**
 * Sets up item ids for Recycler views, which require attributes to be public.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {

    /**
     * The Item message.
     */
    public LinearLayout itemMessage;
    /**
     * The TextView content.
     */
    public TextView tvContent;
    /**
     * The TextView sender username.
     */
    public TextView tvSenderUsername;

    /**
     * Instantiates a new Message view holder.
     *
     * @param itemView the item view
     */
    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);

        itemMessage = itemView.findViewById(R.id.item_message);
        tvContent = itemView.findViewById(R.id.message_content);
        tvSenderUsername = itemView.findViewById(R.id.message_user);
    }
}
