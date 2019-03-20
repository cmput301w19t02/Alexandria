package ca.ualberta.CMPUT3012019T02.alexandria.model.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

/**
 * Sets up item_owner ID for RecyclerViews
 */

public class OwnerViewHolder extends RecyclerView.ViewHolder{

    public RelativeLayout itemOwner;
    public TextView ownerUsername;
    public ImageView ownerPic;
    public ImageView statusIcon;

    public OwnerViewHolder(@NonNull View itemView) {
        super(itemView);

        this.itemOwner = itemView.findViewById(R.id.item_owner);
        this.ownerUsername = itemView.findViewById(R.id.item_owner_name);
        this.ownerPic = itemView.findViewById(R.id.item_owner_pic);
        this.statusIcon = itemView.findViewById(R.id.item_owner_status);
    }
}
