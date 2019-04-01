package ca.ualberta.CMPUT3012019T02.alexandria.model.holder;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

/**
 * Sets up item ids for Recycler views, which require attributes to be public.
 */
public class ReceivedLocationMessageViewHolder extends RecyclerView.ViewHolder {

    /**
     * The Item message.
     */
    public ConstraintLayout itemMessage;

    /**
     * The TextView sender username.
     */
    public TextView tvTimeStamp;

    /**
     * The ImageView image
     */
    public ImageView ivLocationImage;

    /**
     * Instantiates a new Message view holder.
     *
     * @param itemView the item view
     */
    public ReceivedLocationMessageViewHolder(@NonNull View itemView) {
        super(itemView);

        itemMessage = itemView.findViewById(R.id.item_location_message_received);
        tvTimeStamp = itemView.findViewById(R.id.message_time_received);
        ivLocationImage = itemView.findViewById(R.id.image_received_location);
    }
}
