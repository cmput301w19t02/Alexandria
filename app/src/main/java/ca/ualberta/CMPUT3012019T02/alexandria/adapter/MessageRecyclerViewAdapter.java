package ca.ualberta.CMPUT3012019T02.alexandria.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.holder.ReceivedMessageViewHolder;
import ca.ualberta.CMPUT3012019T02.alexandria.model.holder.SentMessageViewHolder;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.Message;


/**
 * The Message recycler view adapter.
 */
public class MessageRecyclerViewAdapter extends RecyclerView.Adapter {

    private static final int SENT = 1;
    private static final int RECEIVED = 2;

    private Context mContext;
    private List<Message> mMessageList;

    private UserController userController = UserController.getInstance();

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
        //TODO: message type check
        if (myId.equals(senderId)) {
            return SENT;
        } else {
            return RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View mView;

        if (viewType == SENT) {
            mView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_message_sent, viewGroup, false);
            return new SentMessageViewHolder(mView);
        } else {
            mView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_message_received, viewGroup, false);
            return new ReceivedMessageViewHolder(mView);
        }
        //TODO: location message check and onClickListeners

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myViewHolder, int position) {
        Date date = new Date(mMessageList.get(position).getDate());

        if (myViewHolder.getItemViewType() == SENT){
            ((SentMessageViewHolder) myViewHolder).tvTimeStamp.setText(date.toString());
            ((SentMessageViewHolder) myViewHolder).tvContent.setText(mMessageList.get(position).getContent());
        } else if (myViewHolder.getItemViewType() == RECEIVED) {
            ((ReceivedMessageViewHolder) myViewHolder).tvTimeStamp.setText(date.toString());
            ((ReceivedMessageViewHolder) myViewHolder).tvContent.setText(mMessageList.get(position).getContent());
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
