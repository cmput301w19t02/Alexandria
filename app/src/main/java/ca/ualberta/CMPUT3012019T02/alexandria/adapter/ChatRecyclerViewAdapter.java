package ca.ualberta.CMPUT3012019T02.alexandria.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ChatRoomActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ChatController;
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

    private UserController userController = UserController.getInstance();
    private ChatController chatController = ChatController.getInstance();

    /**
     * Instantiates a new Chat recycler view adapter.
     *
     * @param mContext      the application context
     * @param mChatRoomList the chat room list
     */
    public ChatRecyclerViewAdapter(Context mContext, List<ChatRoomItem> mChatRoomList) {
        this.mContext = mContext;
        this.mChatRoomList = mChatRoomList;
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
                //TODO: After testing add chat room, uncomment and remove 2nd myId
                //String myId = userController.getMyId();
                String myId = mChatRoomList.get(mViewHolder.getAdapterPosition()).getUser1Id();
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
        if (myId.equals(user1Id)) {
            myViewHolder.tvChatReceiverUsername.setText(mChatRoomList.get(position).getUser1Name());
        } else {
            myViewHolder.tvChatReceiverUsername.setText(mChatRoomList.get(position).getUser2Name());
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
