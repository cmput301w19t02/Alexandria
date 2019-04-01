package ca.ualberta.CMPUT3012019T02.alexandria.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.holder.ReceivedLocationMessageViewHolder;
import ca.ualberta.CMPUT3012019T02.alexandria.model.holder.ReceivedMessageViewHolder;
import ca.ualberta.CMPUT3012019T02.alexandria.model.holder.SentLocationMessageViewHolder;
import ca.ualberta.CMPUT3012019T02.alexandria.model.holder.SentMessageViewHolder;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.Message;


/**
 * The Message recycler view adapter.
 */
public class MessageRecyclerViewAdapter extends RecyclerView.Adapter {

    private static final int SENT_TEXT = 1;
    private static final int SENT_LOCATION = 2;
    private static final int RECEIVED_TEXT = 3;
    private static final int RECEIVED_LOCATION = 4;

    private Context mContext;
    private List<Message> mMessageList;

    private UserController userController = UserController.getInstance();
    private ImageController imageController = ImageController.getInstance();

    /**
     * Instantiates a new Message recycler view adapter.
     *
     * @param mContext     the application context
     * @param mMessageList the message list
     */
    public MessageRecyclerViewAdapter(Context mContext, List<Message> mMessageList) {
        this.mContext = mContext;
        this.mMessageList = mMessageList;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = (Message) mMessageList.get(position);
        String myId = userController.getMyId();
        String senderId = message.getSender();
        if (myId.equals(senderId)) {
            if (message.getType().equals("text")) {
                return SENT_TEXT;
            } else {
                return SENT_LOCATION;
            }
        } else {
            if (message.getType().equals("text")){
                return RECEIVED_TEXT;
            } else {
                return RECEIVED_LOCATION;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View mView;

        if (viewType == SENT_TEXT) {
            mView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_message_sent, viewGroup, false);
            return new SentMessageViewHolder(mView);

        } else if (viewType == RECEIVED_TEXT){
            mView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_message_received, viewGroup, false);
            return new ReceivedMessageViewHolder(mView);

        } else if (viewType == SENT_LOCATION){
            mView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_location_message_sent, viewGroup, false);
            return new SentLocationMessageViewHolder(mView);

        } else {
            mView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_location_message_recieved, viewGroup, false);
            return new ReceivedLocationMessageViewHolder(mView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myViewHolder, int position) {
        Date date = new Date(mMessageList.get(position).getDate());

        if (myViewHolder.getItemViewType() == SENT_TEXT){
            ((SentMessageViewHolder) myViewHolder).tvTimeStamp.setText(date.toString());
            ((SentMessageViewHolder) myViewHolder).tvContent.setText(mMessageList.get(position).getContent());

        } else if (myViewHolder.getItemViewType() == RECEIVED_TEXT) {
            ((ReceivedMessageViewHolder) myViewHolder).tvTimeStamp.setText(date.toString());
            ((ReceivedMessageViewHolder) myViewHolder).tvContent.setText(mMessageList.get(position).getContent());

        } else if (myViewHolder.getItemViewType() == SENT_LOCATION) {
             String[] content = mMessageList.get(position).getContent().split(",");
             String imageId = content[0];
             String lat = content[1];
             String lng = content[2];
             imageController.getImage(imageId).handleAsync((result,error) -> {
                 if (error == null) {
                     ((SentLocationMessageViewHolder) myViewHolder).ivLocationImage.setImageBitmap(result);
                     ((SentLocationMessageViewHolder) myViewHolder).ivLocationImage
                             .setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     String zoom = "15";
                                     String geoString = "geo:" + lat + "," + lng + "?z=" + zoom + "&q=" + lat + "," + lng;
                                     Uri intentUri = Uri.parse(geoString);
                                     Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
                                     mapIntent.setPackage("com.google.android.apps.maps");
                                     mContext.startActivity(mapIntent);
                                 }
                             });
                 } else {
                     //handle if there is an error
                 }
                 return null;
             });
        } else {
            String[] content = mMessageList.get(position).getContent().split(",");
            String imageId = content[0];
            String lat = content[1];
            String lng = content[2];
            imageController.getImage(imageId).handleAsync((result,error) -> {
                if (error == null) {
                    ((ReceivedLocationMessageViewHolder) myViewHolder).ivLocationImage.setImageBitmap(result);
                    ((ReceivedLocationMessageViewHolder) myViewHolder).ivLocationImage
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String zoom = "15";
                                    String geoString = "geo:" + lat + "," + lng + "?z=" + zoom;
                                    Uri intentUri = Uri.parse(geoString);
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    mContext.startActivity(mapIntent);
                                }
                            });
                } else {
                    //handle if there is an error
                }
                return null;
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    /**
     * Update message list.
     *
     * @param messageList the new message list
     */
    public void updateMessageList(List<Message> messageList){
        mMessageList = messageList;
    }
}
