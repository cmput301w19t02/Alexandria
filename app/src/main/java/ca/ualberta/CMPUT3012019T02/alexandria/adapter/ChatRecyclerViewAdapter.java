package ca.ualberta.CMPUT3012019T02.alexandria.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ChatRoomActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ChatController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.chatroom.ChatRoomItem;
import ca.ualberta.CMPUT3012019T02.alexandria.model.holder.ChatViewHolder;

/**
 * Recycler View adapter for the chat room list in MessageFragment. Sets up an on click listener
 * for each list item to go to the appropriate chat room.
 */
public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private static final int MESSAGE_READ = 1;
    private static final int MESSAGE_UNREAD = 2;

    private Context mContext;
    private List<ChatRoomItem> mChatRoomList;
    private Map<String, RoundedBitmapDrawable> mProfileImageMap;

    private UserController userController = UserController.getInstance();
    private ChatController chatController = ChatController.getInstance();
    private ImageController imageController = ImageController.getInstance();

    /**
     * Instantiates a new Chat recycler view adapter.
     *
     * @param mContext      the application context
     * @param mChatRoomList the chat room list
     */
    public ChatRecyclerViewAdapter(Context mContext, List<ChatRoomItem> mChatRoomList,
                                   Map<String, RoundedBitmapDrawable> mProfileImageMap) {
        this.mContext = mContext;
        this.mChatRoomList = mChatRoomList;
        this.mProfileImageMap = mProfileImageMap;
    }

    @Override
    public int getItemViewType(int position) {
        ChatRoomItem chatRoomItem = (ChatRoomItem)mChatRoomList.get(position);
        if (chatRoomItem.getReadStatus()) {
            return MESSAGE_READ;
        } else {
            return MESSAGE_UNREAD;
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View mView;

        if (viewType == MESSAGE_READ) {
            mView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_chat_list_read, viewGroup, false);
        } else {
            mView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_chat_list_unread, viewGroup, false);
        }
        ChatViewHolder mViewHolder = new ChatViewHolder(mView);

        mViewHolder.itemChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chatId = mChatRoomList.get(mViewHolder.getAdapterPosition()).getChatId();
                String myId = userController.getMyId();
                String receiverId = mChatRoomList.get(mViewHolder.getAdapterPosition())
                        .getUser1Id();
                String receiverName = mChatRoomList.get(mViewHolder.getAdapterPosition())
                        .getUser1Name();

                if (receiverId.equals(myId)) {
                    receiverId = mChatRoomList.get(mViewHolder.getAdapterPosition()).getUser2Id();
                    receiverName = mChatRoomList.get(mViewHolder.getAdapterPosition())
                            .getUser2Name();
                }

                Intent intent = new Intent(viewGroup.getContext(), ChatRoomActivity.class);
                intent.putExtra("chatId", chatId);
                intent.putExtra("receiverId", receiverId);
                intent.putExtra("receiverName", receiverName);

                chatController.setUserChatRoomReadStatus(chatId, myId, true);
                mContext.startActivity(intent);
            }
        });

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder myViewHolder, int position) {
        String myId = userController.getMyId();
        String user1Id = mChatRoomList.get(position).getUser1Id();
        String user2Id = mChatRoomList.get(position).getUser2Id();
        if (!myId.equals(user1Id)) {
            myViewHolder.tvChatReceiverUsername.setText(mChatRoomList.get(position).getUser1Name());
            if (mProfileImageMap.get(user1Id) != null) {
                myViewHolder.ivChatUserPic.setBackground(null);
                myViewHolder.ivChatUserPic.setImageDrawable(mProfileImageMap.get(user1Id));
            }
        } else {
            myViewHolder.tvChatReceiverUsername.setText(mChatRoomList.get(position).getUser2Name());
            if (mProfileImageMap.get(user2Id) != null) {
                myViewHolder.ivChatUserPic.setBackground(null);
                myViewHolder.ivChatUserPic.setImageDrawable(mProfileImageMap.get(user2Id));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mChatRoomList.size();
    }

    /**
     * Update chat room list.
     *
     * @param newList the new list
     */
    public void updateChatRoomList(List<ChatRoomItem> newList){
        mChatRoomList = newList;
    }
}
