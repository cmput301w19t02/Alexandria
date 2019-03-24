package ca.ualberta.CMPUT3012019T02.alexandria.model.holder;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

/**
 * Sets up item ids for Recycler views, which require attributes to be public.
 */
public class SentMessageViewHolder extends RecyclerView.ViewHolder {

    /**
     * The Item message.
     */
    public ConstraintLayout itemMessage;
    /**
     * The TextView content.
     */
    public TextView tvContent;
    /**
     * The TextView sender username.
     */
    public TextView tvTimeStamp;

    /**
     * Instantiates a new Message view holder.
     *
     * @param itemView the item view
     */
    public SentMessageViewHolder(@NonNull View itemView) {
        super(itemView);

        itemMessage = itemView.findViewById(R.id.item_message);
        tvTimeStamp = itemView.findViewById(R.id.message_time_sent);
        tvContent = itemView.findViewById(R.id.message_content_sent);
    }
}
