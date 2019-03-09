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

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private Context mContext;
    private List<ChatRoomItem> mChatRoomList;

    public ChatRecyclerViewAdapter(Context mContext, List<ChatRoomItem> mChatRoomList) {
        this.mContext = mContext;
        this.mChatRoomList = mChatRoomList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View mView;

        mView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_chat_list, viewGroup, false);
        ChatViewHolder mViewHolder = new ChatViewHolder(mView);

        mViewHolder.itemChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //bundles data for fragment switching
                Intent intent = new Intent(viewGroup.getContext(), ChatRoomActivity.class);
                Bundle bundle = new Bundle();

                //bundle.putParcelable("user_pic", mChatRoomList.get(mViewHolder.getAdapterPosition()).getUserPic());
                bundle.putString("chatId", mChatRoomList.get(mViewHolder.getAdapterPosition()).getChatRoomId());
                bundle.putString("recieverId", mChatRoomList.get(mViewHolder.getAdapterPosition()).getUser2Id());

                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder myViewHolder, int position) {

        //myViewHolder.ivChatUserPic.setImageBitmap(mChatRoomList.get(position).getUserPic());
        myViewHolder.tvChatRecieverUsername.setText(mChatRoomList.get(position).getUser2Name());
        // TODO: add image from res folder for read status
        /**
        if (!mChatRoomList.get(position).getReadStatus()){
            //unread image from res folder
            myViewHolder.ivReadStatus.setImageBitmap();
        } else{
            //clear image for read from res folder
            myViewHolder.ivReadStatus.setImageBitmap();
        }
         */
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(24, 24, config);
        myViewHolder.ivReadStatus.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return mChatRoomList.size();
    }
}
