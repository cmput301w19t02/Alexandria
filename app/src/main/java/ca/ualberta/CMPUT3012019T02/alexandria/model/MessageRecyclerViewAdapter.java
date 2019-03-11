package ca.ualberta.CMPUT3012019T02.alexandria.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.Message;


/**
 *
 */
public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private Context mContext;
    private List<Message> mMessageList;

    public MessageRecyclerViewAdapter(Context mContext, List<Message> mMessageList) {
        this.mContext = mContext;
        this.mMessageList = mMessageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View mView;

        mView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_message, viewGroup, false);
        MessageViewHolder mViewHolder = new MessageViewHolder(mView);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder myViewHolder, int position) {

        myViewHolder.tvSenderUsername.setText(mMessageList.get(position).getSender());
        myViewHolder.tvContent.setText(mMessageList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public void updateMessageList(){

    }
}
