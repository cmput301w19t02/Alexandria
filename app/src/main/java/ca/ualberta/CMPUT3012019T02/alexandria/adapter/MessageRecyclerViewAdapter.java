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
import ca.ualberta.CMPUT3012019T02.alexandria.model.holder.MessageViewHolder;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.Message;


/**
 * The Message recycler view adapter.
 */
public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private Context mContext;
    private List<Message> mMessageList;

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

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View mView;

        mView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_message_recieve, viewGroup, false);
        MessageViewHolder mViewHolder = new MessageViewHolder(mView);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder myViewHolder, int position) {
        Date date = new Date(mMessageList.get(position).getDate());
        myViewHolder.tvSendTime.setText(date.toString());
        myViewHolder.tvContent.setText(mMessageList.get(position).getContent());
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
